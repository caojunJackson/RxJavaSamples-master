package example.com.rxlearn.model;

import java.util.List;

/**
 * Created by Nevermore on 16/6/26.
 */
public class HttpWxHotResult<T> {

    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2016-06-22","title":"修女你别走啊，我们给你唱首歌怎么会有这么可爱的欧洲杯球迷！","description":"何仙姑夫","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6375762.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MjM5MjAxNjU5NA==&idx=6&mid=2652084222&sn=32749814c0c358b65d28966c5063421f"},{"ctime":"2016-06-22","title":"撕袜财经：能看能撸的欧洲杯解读，身体反应5分钟，颅内高潮2小时","description":"冷笑话","picUrl":"http://t1.qpic.cn/mblogpic/f01a972dbcc1060fd456/2000","url":"http://mp.weixin.qq.com/s?__biz=Mjk2NzMwNTY2MA==&idx=2&mid=2653056300&sn=55e139250976cef62af829a6f90c1389"},{"ctime":"2016-06-22","title":"if姐欧洲杯看现场比赛，还花2欧在脸上画了一幅国旗...","description":"IF","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6369273.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MjM5ODY3MzExNw==&idx=3&mid=2651050682&sn=c68bd3d3a3896a6289b49f20c604e8a9"},{"ctime":"2016-06-21","title":"欧洲杯新网红，有点逗比有点萌的爱尔兰球迷","description":"爆笑gif图","picUrl":"http://t1.qpic.cn/mblogpic/34d9dfb75cfceb04a840/2000","url":"http://mp.weixin.qq.com/s?__biz=MjM5MDA0NDI2MA==&idx=3&mid=2653344150&sn=f746eb8e30be4d6adf0fbe69f2667fdc"},{"ctime":"2016-06-19","title":"if姐看欧洲杯|歪果仁看球时都在喝这12款啤酒","description":"IF","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6302205.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MjM5ODY3MzExNw==&idx=2&mid=2651050219&sn=b747a3bd23953bcacb46dfec459a46ce"},{"ctime":"2016-06-18","title":"欧洲杯最强奇兵导演捷克神逆转！队员名字\u201c被冠名\u201d成最新网红","description":"冷兔","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6291458.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&idx=2&mid=2652502201&sn=b5bb19dc1f6c258ae6a411f8170bc4f8"}]
     */

    private int code;
    private String msg;
    /**
     * ctime : 2016-06-22
     * title : 修女你别走啊，我们给你唱首歌怎么会有这么可爱的欧洲杯球迷！
     * description : 何仙姑夫
     * picUrl : http://zxpic.gtimg.com/infonew/0/wechat_pics_-6375762.jpg/640
     * url : http://mp.weixin.qq.com/s?__biz=MjM5MjAxNjU5NA==&idx=6&mid=2652084222&sn=32749814c0c358b65d28966c5063421f
     */

    private T newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getNewslist() {
        return newslist;
    }

    public void setNewslist(T newslist) {
        this.newslist = newslist;
    }


}
