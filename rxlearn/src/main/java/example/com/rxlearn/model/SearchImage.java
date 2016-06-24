package example.com.rxlearn.model;

/**
 * Created by Administrator on 2016/6/23.
 */
public class SearchImage {
    public String image_url;
    public String description;

    @Override
    public String toString() {
        return "SearchImage{" +
                "image_url='" + image_url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
