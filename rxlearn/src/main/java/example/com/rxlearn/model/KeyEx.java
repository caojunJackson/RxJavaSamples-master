package example.com.rxlearn.model;

import java.util.List;

/**
 * Created by Nevermore on 16/6/26.
 */
public class KeyEx {

    /**
     * ret_code : 0
     * list : ["测试","文字"]
     */

    private int ret_code;
    private List<String> list;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "KeyEx{" +
                "ret_code=" + ret_code +
                ", list=" + list +
                '}';
    }
}
