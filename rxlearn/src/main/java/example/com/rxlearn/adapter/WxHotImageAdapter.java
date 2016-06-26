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
import example.com.rxlearn.model.WxNews;

/**
 * Created by Nevermore on 16/6/24.
 */
public class WxHotImageAdapter extends RecyclerView.Adapter {
    private List<WxNews> mDatas;

    public void setDatas(List<WxNews> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_fragment_wxhot, parent, false);
        return new WxHotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WxHotViewHolder viewHolder = (WxHotViewHolder) holder;
        WxNews wxNews = mDatas.get(position);
        Glide.clear(viewHolder.mIvCover);
        Glide.with(holder.itemView.getContext()).load(wxNews.getPicUrl()).into(viewHolder.mIvCover);
        viewHolder.mTvTitle.setText(wxNews.getTitle());
        viewHolder.mTvDes.setText(wxNews.getDescription());
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    static class WxHotViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivCover)
        ImageView mIvCover;
        @Bind(R.id.tvTitle)
        TextView mTvTitle;
        @Bind(R.id.tvDes)
        TextView mTvDes;

        public WxHotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
