package example.com.rxlearn.utils.helper;

/**
 * Created by Administrator on 2016/6/28.
 */
public interface ProcessListener {
    /**
     * @param current     已完成字节数
     * @param total       总字节数
     * @param isCompleted 是否已经完成
     */
    void onProcess(long current, long total, boolean isCompleted);
}
