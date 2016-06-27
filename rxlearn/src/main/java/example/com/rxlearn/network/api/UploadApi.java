package example.com.rxlearn.network.api;

import java.util.List;
import java.util.Map;

import example.com.rxlearn.model.HttpKeyResult;
import example.com.rxlearn.model.HttpUploadResult;
import example.com.rxlearn.model.KeyEx;
import example.com.rxlearn.network.Constants;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by Nevermore on 16/6/26.
 */
public interface UploadApi {
    @Multipart
    @POST("fileService/upload.action")
    Observable<HttpUploadResult> upload(@Part List<MultipartBody.Part> parts);

    @POST("fileService/upload.action")
    Observable<HttpUploadResult> upload(@Body MultipartBody multipartBody);

//    @POST("fileService/upload.action")
//    Call<HttpUploadResult> upload(@Body MultipartBody multipartBody);

    MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
}
