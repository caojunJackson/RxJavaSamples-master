package example.com.rxlearn.utils.helper;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Administrator on 2016/6/28.
 */
public class ProcessResponseBody extends ResponseBody {
    private ProcessListener mProcessListener;
    private ResponseBody mResponseBody;
    private BufferedSource mBufferedSource;

    public ProcessResponseBody(ResponseBody responseBody, ProcessListener processListener) {
        mProcessListener = processListener;
        mResponseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long nowSize = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long readSize = super.read(sink, byteCount);
                nowSize += (readSize != -1 ? readSize : 0);
                mProcessListener.onProcess(nowSize, mResponseBody.contentLength(), readSize == -1);
                return readSize;
            }
        };
    }
}
