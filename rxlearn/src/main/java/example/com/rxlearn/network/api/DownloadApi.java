package example.com.rxlearn.network.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2016/6/29.
 */
public interface DownloadApi {
    @GET("pic/normal/31a95c937b5144d1f62a02746b88b64a.jpg")
    Call<ResponseBody> download();
}
