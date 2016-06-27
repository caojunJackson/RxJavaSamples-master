package example.com.rxlearn.network;

import example.com.rxlearn.network.api.SearchApi;
import example.com.rxlearn.network.api.StoreKeyExApi;
import example.com.rxlearn.network.api.StoreWxHotApi;
import example.com.rxlearn.network.api.UploadApi;
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
    private static StoreWxHotApi sStoreWxHotApi;
    private static StoreKeyExApi sStoreKeyExApi;
    private static UploadApi sUploadApi;
    private static OkHttpClient sOkHttpClient = new OkHttpClient();


    private static GsonConverterFactory sGsonConverterFactory = GsonConverterFactory.create();
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

    public static StoreWxHotApi getStoreWxHotApi() {
        if (sStoreWxHotApi == null) {
            sStoreWxHotApi = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl("http://apis.baidu.com/txapi/weixin/")
                    .addConverterFactory(sGsonConverterFactory)
                    .addCallAdapterFactory(sRxJavaCallAdapterFactory)
                    .build()
                    .create(StoreWxHotApi.class);
        }
        return sStoreWxHotApi;
    }

    public static StoreKeyExApi getStoreKeyExApi() {
        if (sStoreKeyExApi == null) {
            sStoreKeyExApi = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl("http://apis.baidu.com/showapi_open_bus/key_extra/")
                    .addConverterFactory(sGsonConverterFactory)
                    .addCallAdapterFactory(sRxJavaCallAdapterFactory)
                    .build()
                    .create(StoreKeyExApi.class);
        }
        return sStoreKeyExApi;
    }

    public static UploadApi getUploadApi() {
        if (sUploadApi == null) {
            sUploadApi = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl("http://testfileservice.4sonline.net:88/")
                    .addConverterFactory(sGsonConverterFactory)
                    .addCallAdapterFactory(sRxJavaCallAdapterFactory)
                    .build()
                    .create(UploadApi.class);
        }
        return sUploadApi;
    }
}
