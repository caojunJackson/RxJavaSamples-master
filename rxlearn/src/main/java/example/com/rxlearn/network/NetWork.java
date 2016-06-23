package example.com.rxlearn.network;

import example.com.rxlearn.model.SearchImage;
import example.com.rxlearn.network.api.SearchApi;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/6/23.
 */
public class NetWork {
    private static SearchApi sSearchApi;
    private static OkHttpClient sOkHttpClient = new OkHttpClient();
    ;
    private static GsonConverterFactory sGsonConverterFactory = GsonConverterFactory.create();
    ;
    private static CallAdapter.Factory sRxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();


    public static SearchApi getSearchApi() {
        if (sSearchApi == null) {
            sSearchApi = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl("http://zhuangbi.info/")
                    .addConverterFactory(sGsonConverterFactory)
                    .addCallAdapterFactory(sRxJavaCallAdapterFactory)
                    .build()
                    .create(SearchApi.class);
        }
        return sSearchApi;
    }
}
