package example.com.rxlearn.network.api;

import java.util.Map;

import example.com.rxlearn.model.HttpKeyResult;
import example.com.rxlearn.model.KeyEx;
import example.com.rxlearn.network.Constants;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Nevermore on 16/6/26.
 */
public interface StoreKeyExApi {
    @FormUrlEncoded
    @POST("key_ex")
    @Headers("apikey:" + Constants.API_STORE_KEY)
    Observable<HttpKeyResult<KeyEx>> exKey(@FieldMap(encoded = true) Map<String, String> map);
}
