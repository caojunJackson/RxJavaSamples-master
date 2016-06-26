package example.com.rxlearn.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.com.rxlearn.R;
import example.com.rxlearn.model.SearchImage;

/**
 * Created by Nevermore on 16/6/24.
 */
public class SearchImageAdapter extends RecyclerView.Adapter {
    private List<SearchImage> mDatas;

    public void setDatas(List<SearchImage> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_search_image, parent, false);
        return new SearchImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchImageViewHolder viewHolder = (SearchImageViewHolder) holder;
        SearchImage searchImage = mDatas.get(position);
        Glide.clear(viewHolder.mIvShow);
        Glide.with(holder.itemView.getContext())
                .load(searchImage.image_url)
                .into(viewHolder.mIvShow);
        viewHolder.mTvDes.setText(searchImage.description);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    static class SearchImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivShow)
        ImageView mIvShow;
        @Bind(R.id.tvDes)
        TextView mTvDes;

        SearchImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
