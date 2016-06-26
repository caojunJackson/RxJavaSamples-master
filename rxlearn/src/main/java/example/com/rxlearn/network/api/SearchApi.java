package example.com.rxlearn.network.api;

import java.util.List;

import example.com.rxlearn.model.SearchImage;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/23.
 */
public interface SearchApi {
    @GET("search")
    Observable<List<SearchImage>>  search(@Query("q") String qTitle);
}
