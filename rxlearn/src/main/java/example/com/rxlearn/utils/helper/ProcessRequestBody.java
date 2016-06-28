package example.com.rxlearn.utils.helper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Administrator on 2016/6/28.
 */
public class ProcessRequestBody extends RequestBody {
    private ProcessListener mProcessListener;
    private RequestBody mRequestBody;

    private BufferedSink mBufferedSink;

    public ProcessRequestBody(ProcessListener processListener, RequestBody requestBody) {
        mProcessListener = processListener;
        mRequestBody = requestBody;
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(sink(sink));
        }
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long writenBytes = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                writenBytes += byteCount;
                mProcessListener.onProcess(writenBytes, mRequestBody.contentLength(), writenBytes == mRequestBody.contentLength());
            }
        };
    }
}
