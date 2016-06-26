package example.com.rxlearn.model;

/**
 * Created by Nevermore on 16/6/26.
 */
public class WxNews {

    /**
     * ctime : 2016-06-22
     * title : 修女你别走啊，我们给你唱首歌怎么会有这么可爱的欧洲杯球迷！
     * description : 何仙姑夫
     * picUrl : http://zxpic.gtimg.com/infonew/0/wechat_pics_-6375762.jpg/640
     * url : http://mp.weixin.qq.com/s?__biz=MjM5MjAxNjU5NA==&idx=6&mid=2652084222&sn=32749814c0c358b65d28966c5063421f
     */

    private String ctime;
    private String title;
    private String description;
    private String picUrl;
    private String url;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WxNews{" +
                "ctime='" + ctime + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
