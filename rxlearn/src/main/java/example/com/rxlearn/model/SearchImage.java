package example.com.rxlearn.model;

/**
 * Created by Administrator on 2016/6/23.
 */
public class SearchImage {

    /**
     * id : 187
     * description : ????????????????????????????
     * path : i/2015-07-16-77b07ee973ffd5ab39015b927b04b3c3.jpg
     * size : 11007
     * width : 220
     * height : 94
     * created_at : 2015-07-16 16:03:06
     * updated_at : 2016-02-26 22:26:41
     * user_id : 1
     * permitted_at : 2016-02-26 22:26:41
     * disk : qiniu
     * hotpoint : 101
     * channel : null
     * upload_id : 167
     * image_url : http://zhuangbi.idagou.com/i/2016-02-26-b56183130c20ca2f8b6ec9c6616c1f95.jpg
     * file_size : 18.78 KB
     */

    private int id;
    private String description;
    private String path;
    private int size;
    private int width;
    private int height;
    private String created_at;
    private String updated_at;
    private int user_id;
    private String permitted_at;
    private String disk;
    private int hotpoint;
    private Object channel;
    private int upload_id;
    private String image_url;
    private String file_size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPermitted_at() {
        return permitted_at;
    }

    public void setPermitted_at(String permitted_at) {
        this.permitted_at = permitted_at;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public int getHotpoint() {
        return hotpoint;
    }

    public void setHotpoint(int hotpoint) {
        this.hotpoint = hotpoint;
    }

    public Object getChannel() {
        return channel;
    }

    public void setChannel(Object channel) {
        this.channel = channel;
    }

    public int getUpload_id() {
        return upload_id;
    }

    public void setUpload_id(int upload_id) {
        this.upload_id = upload_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    @Override
    public String toString() {
        return "SearchImage{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", width=" + width +
                ", height=" + height +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", user_id=" + user_id +
                ", permitted_at='" + permitted_at + '\'' +
                ", disk='" + disk + '\'' +
                ", hotpoint=" + hotpoint +
                ", channel=" + channel +
                ", upload_id=" + upload_id +
                ", image_url='" + image_url + '\'' +
                ", file_size='" + file_size + '\'' +
                '}';
    }
}
