package com.chaoyang805.blocksms.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaoyang805.blocksms.MainActivity;
import com.chaoyang805.blocksms.R;
import com.chaoyang805.blocksms.SMSDetailActivity;
import com.chaoyang805.blocksms.adapter.SMSRecyclerAdapter;
import com.chaoyang805.blocksms.app.BlockSMSApp;
import com.chaoyang805.blocksms.bean.SMS;
import com.chaoyang805.blocksms.db.SMSDAOImpl;
import com.chaoyang805.blocksms.listener.RecyclerListenerAdapter;
import com.chaoyang805.blocksms.utils.Constants;

import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/4.
 */
public class SMSFragment extends Fragment {


    private View rootView;

    private RecyclerView mSMSList;

    private SMSRecyclerAdapter mAdapter;

    private SMSDAOImpl mSMSDAO;

    private List<SMS> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sms,container,false);
        mSMSList = (RecyclerView) rootView.findViewById(R.id.recycler_sms);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mSMSList.setLayoutManager(linearLayoutManager);
        mSMSDAO = ((BlockSMSApp) getActivity().getApplication()).getSMSDaoImpl();
        mList = mSMSDAO.getAllSMS();
        mAdapter = new SMSRecyclerAdapter(getActivity(),mList);
        mAdapter.addListener(new RecyclerListenerAdapter() {
            @Override
            public void onItemLongClick(View itemView, SMS sms) {
                showDeleteDialog(sms);
            }

            @Override
            public void onItemClick(View itemView, SMS sms) {
                int currentPosId = sms.getId();
                Intent intent = new Intent(getActivity(), SMSDetailActivity.class);
                intent.putExtra(Constants.EXTRA_ID, currentPosId);
                startActivity(intent);
            }
        });
        mSMSList.setAdapter(mAdapter);
        return rootView;
    }

    /**
     * 显示删除的dialog
     * @param sms
     */
    private void showDeleteDialog(final SMS sms) {
        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(getActivity());
        deleteDialogBuilder
                .setTitle(R.string.delete_sms)
                .setMessage(R.string.are_you_sure_you_want_delete_this_sms)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //将按下的item的对应信息从数据库删除
                        mSMSDAO.deleteSMS(sms);
                        //更新界面的数据
                        updateUI(sms);
                    }
                })
                .setNegativeButton(R.string.cancel, null).show();
    }

    private void updateUI(SMS sms) {
        int position = mList.indexOf(sms);
        mList.remove(position);
//        mList = mSMSDAO.getAllSMS();
        mAdapter.notifyItemRemoved(position);
        if (mList.size() > 0){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.blocked_sms);
        }else {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.no_sms_was_blocked);
        }
    }

//    private void generateDatas() {
//        mList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            mList.add(new SMS(System.currentTimeMillis(), "测试短信" + i, "1378874707" + i));
//        }
//    }
}
