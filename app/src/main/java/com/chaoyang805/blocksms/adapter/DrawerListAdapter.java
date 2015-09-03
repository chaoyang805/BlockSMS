package com.chaoyang805.blocksms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoyang805.blocksms.R;

import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/3.
 */
public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.DrawerListViewHolder> implements View.OnClickListener {

    private List<String> mDatas;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public DrawerListAdapter(List<String> datas) {
        mDatas = datas;
    }

    @Override
    public DrawerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item, parent, false);
        itemView.setOnClickListener(this);
        DrawerListViewHolder viewHolder = new DrawerListViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrawerListViewHolder holder, int position) {
        holder.tvTag.setText(mDatas.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, (int) v.getTag());
        }
    }

    public static class DrawerListViewHolder extends RecyclerView.ViewHolder{

        final TextView tvTag;

        public DrawerListViewHolder(View itemView) {
            super(itemView);
            tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
        }
    }
}
