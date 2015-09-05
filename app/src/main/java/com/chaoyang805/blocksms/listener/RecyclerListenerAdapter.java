package com.chaoyang805.blocksms.listener;

import android.view.View;

import com.chaoyang805.blocksms.adapter.SMSRecyclerAdapter;
import com.chaoyang805.blocksms.bean.SMS;

/**
 * Created by chaoyang805 on 2015/9/4.
 */
public abstract class RecyclerListenerAdapter implements SMSRecyclerAdapter.RecyclerListener {

    @Override
    public void onItemClick(View v, SMS sms) {

    }

    public void onItemClick(View v,int position){

    }

    @Override
    public void onItemLongClick(View v, SMS sms) {

    }
}
