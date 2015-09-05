package com.chaoyang805.blocksms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaoyang805.blocksms.R;
import com.chaoyang805.blocksms.RecyclerListenerAdapter;
import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.utils.StringFormatUtil;

import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/4.
 */
public class SMSRecyclerAdapter extends RecyclerView.Adapter<SMSRecyclerAdapter.SMSViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<SMS> mSMSes;
    private Context mContext;

    public SMSRecyclerAdapter(Context context, List<SMS> smses) {
        mContext = context;
        mSMSes = smses;
    }

    public interface RecyclerListener {
        void onItemClick(View itemView, SMS sms);

        void onItemLongClick(View itemView, SMS sms);
    }

    private RecyclerListenerAdapter mListenerAdapter;

    public void addListener(RecyclerListenerAdapter listenerAdapter) {
        mListenerAdapter = listenerAdapter;
    }


    @Override
    public SMSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_item, parent, false);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        SMSViewHolder viewHolder = new SMSViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SMSViewHolder holder, int position) {
        SMS sms = mSMSes.get(position);
        holder.tvReceivedTime.setText(StringFormatUtil.formatMilliseconds(sms.getReceivedTime()));
        holder.tvSMSInfo.setText(sms.getSMSInfo());
        holder.tvNum.setText(mContext.getString(R.string.from, sms.getPhoneNum()));
        holder.itemView.setTag(sms);
    }

    @Override
    public int getItemCount() {
        return mSMSes.size();
    }

    @Override
    public void onClick(View itemView) {
        if (mListenerAdapter != null) {
            mListenerAdapter.onItemClick(itemView, (SMS) itemView.getTag());
        }
    }

    @Override
    public boolean onLongClick(View itemView) {
        if (mListenerAdapter != null) {
            mListenerAdapter.onItemLongClick(itemView, (SMS) itemView.getTag());
            return true;
        }
        return false;
    }

    public static class SMSViewHolder extends RecyclerView.ViewHolder {
        TextView tvSMSInfo;
        TextView tvNum;
        TextView tvReceivedTime;


        public SMSViewHolder(View itemView) {
            super(itemView);

            tvSMSInfo = (TextView) itemView.findViewById(R.id.tv_sms_info);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);
            tvReceivedTime = (TextView) itemView.findViewById(R.id.tv_received_time);
        }
    }
}
