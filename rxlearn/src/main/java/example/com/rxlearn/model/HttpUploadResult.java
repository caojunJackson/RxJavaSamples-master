package example.com.rxlearn.model;

/**
 * Created by Administrator on 2016/6/27.
 */
public class HttpUploadResult {

    /**
     * retCode : 0
     * url : http://testfileservice.4sonline.net:88/fileService/weixin/pic/normal/3ac27cbcb4ce011632cee0a045cb27c6.
     * packageName :
     */

    private int retCode;
    private String url;
    private String packageName;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "HttpUploadResult{" +
                "retCode=" + retCode +
                ", url='" + url + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
