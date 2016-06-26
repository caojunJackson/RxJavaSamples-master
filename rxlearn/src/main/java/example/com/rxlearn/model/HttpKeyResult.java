package example.com.rxlearn.model;

import java.util.List;

/**
 * Created by Nevermore on 16/6/26.
 */
public class HttpKeyResult<T> {

    /**
     * showapi_res_code : 0
     * showapi_res_error :
     * showapi_res_body : {"ret_code":0,"list":["测试","文字"]}
     */

    private int showapi_res_code;
    private String showapi_res_error;
    /**
     * ret_code : 0
     * list : ["测试","文字"]
     */

    private T showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public T getT() {
        return showapi_res_body;
    }

    public void setT(T t) {
        this.showapi_res_body = t;
    }

    @Override
    public String toString() {
        return "HttpKeyResult{" +
                "showapi_res_code=" + showapi_res_code +
                ", showapi_res_error='" + showapi_res_error + '\'' +
                ", showapi_res_body=" + showapi_res_body +
                '}';
    }
}
