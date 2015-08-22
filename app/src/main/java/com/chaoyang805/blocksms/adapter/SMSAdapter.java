package com.chaoyang805.blocksms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaoyang805.blocksms.R;
import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.utils.StringFormatUtil;

import java.util.List;

/**
 * Created by chaoyang805 on 2015/8/21.
 */
public class SMSAdapter extends BaseAdapter {
    /**
     * 保存所有短信的list
     */
    private List<SMS> mList;
    /**
     * 保存context对象
     */
    private Context mContext;

    public SMSAdapter(Context context, List<SMS> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SMS getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 数据库发生变化后来更新list的方法
     * @param newList
     */
    public void updateList(List<SMS> newList) {
        mList = newList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sms_item, null, false);
            holder = new Viewholder();
            holder.mTvSMSInfo = (TextView) convertView.findViewById(R.id.tv_sms_info);
            holder.mTvNum = (TextView) convertView.findViewById(R.id.tv_num);
            holder.mTvReceivedTime = (TextView) convertView.findViewById(R.id.tv_received_time);
            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }
        holder.mTvSMSInfo.setText(getItem(position).getSMSInfo());
        holder.mTvNum.setText(mContext.getString(R.string.from, getItem(position).getPhoneNum()));
        //将milliseconds格式化后显示在控件上
        String receivedTime = StringFormatUtil.formatMilliseconds(getItem(position).getReceivedTime());
        holder.mTvReceivedTime.setText(receivedTime);
        return convertView;
    }

    class Viewholder{
        TextView mTvSMSInfo;
        TextView mTvReceivedTime;
        TextView mTvNum;
    }
}
