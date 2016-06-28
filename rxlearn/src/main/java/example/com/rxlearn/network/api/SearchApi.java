package example.com.rxlearn.network.api;

import java.util.List;

import example.com.rxlearn.model.SearchImage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/23.
 */
public interface SearchApi {
    @GET("search")
    Observable<List<SearchImage>> search(@Query("q") String qTitle);

    @GET("search")
    Call<ResponseBody> basicSearch(@Query("q") String qTitle);

    @GET("search")
    Call<List<SearchImage>> transSearch(@Query("q") String qTitle);
}
