package example.com.rxlearn.network.api;

import android.support.annotation.Nullable;

import java.util.List;

import example.com.rxlearn.model.HttpWxHotResult;
import example.com.rxlearn.model.WxNews;
import example.com.rxlearn.network.Constants;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/23.
 */
public interface StoreWxHotApi {
    @GET("{path}")
    @Headers("apikey:" + Constants.API_STORE_KEY)
    Observable<HttpWxHotResult<List<WxNews>>> searchTitle(@Path("path") String path, @Query("num") int num, @Nullable @Query("word") String word);
}
