package com.chaoyang805.blocksms.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chaoyang805.blocksms.MainActivity;
import com.chaoyang805.blocksms.R;
import com.chaoyang805.blocksms.app.BlockSMSApp;
import com.chaoyang805.blocksms.bean.BlockedPhoneNum;
import com.chaoyang805.blocksms.db.SMSDAOImpl;
import com.chaoyang805.blocksms.utils.LogHelper;

import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/5.
 */
public class PhoneListFragment extends Fragment implements AdapterView.OnItemLongClickListener, View.OnClickListener {


    private static final String TAG = LogHelper.makeLogTag(PhoneListFragment.class);

    private View mRootView;
    private ListView mLvBlockedPhone;
    private List<BlockedPhoneNum> mPhoneList;
    private ArrayAdapter<BlockedPhoneNum> mAdapter;
    private SMSDAOImpl mSMSDaoImpl;
    private EditText mEtGetNewPhone, mEtEditPhone;
    private BlockedPhoneNum mCurClickedPhone;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_blocked_phone, container, false);
        initViews();
        mSMSDaoImpl = ((BlockSMSApp) getActivity().getApplication()).getSMSDaoImpl();
        initDatas();
        return mRootView;
    }

    /**
     * 初始化视图控件
     */
    private void initViews() {
        mLvBlockedPhone = (ListView) mRootView.findViewById(R.id.lv_blocked_phone);
        mRootView.findViewById(R.id.btn_add_phone).setOnClickListener(this);
        mLvBlockedPhone.setOnItemLongClickListener(this);
    }

    /**
     * 初始化数据，并显示在listview上
     */
    private void initDatas() {
        mContext = getActivity();
        mPhoneList = mSMSDaoImpl.getAllPhoneNums();
        LogHelper.d(TAG, "mPhoneList.size = " + mPhoneList.size());
        if (mPhoneList.size() > 0) {
            getActionBar().setTitle(R.string.blocked_phone);
        } else {
            getActionBar().setTitle(R.string.no_phone_num_was_added);
        }
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mPhoneList);
        mLvBlockedPhone.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {
        showAddDialog();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mCurClickedPhone = mAdapter.getItem(position);
        showOptionsDialog();
        return false;
    }

    private ActionBar getActionBar() {
        return ((MainActivity) getActivity()).getSupportActionBar();
    }

    /**
     * 长按listView的item时进行编辑或删除操作
     */
    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(R.array.keyword_options_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        LogHelper.d(TAG, "case 0:" + which);
                        //取消当前的dialog显示
                        dialog.dismiss();
                        //显示编辑号码的dialog
                        showEditDialog();
                        break;
                    case 1:
                        LogHelper.d(TAG, "case 1:" + which);
                        //取消当前的dialog显示
                        dialog.dismiss();
                        //显示删除号码的警告窗口
                        showDeleteDialog();
                        break;
                    default:
                        LogHelper.d(TAG, "default which:" + which);
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * 显示编辑号码的dialog
     */
    private void showEditDialog() {
        mEtEditPhone = new EditText(mContext);
        //设置输入类型为数字
        mEtEditPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.edit_phone)
                .setView(mEtEditPhone)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定时，将修改后的号码更改到数据库
                        updateDatabase();
                        //更新界面显示
                        updateUI();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    /**
     * 显示删除号码的警告窗口
     */
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.delete_phone)
                .setMessage(R.string.are_you_sure_you_want_to_delete_this_num)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //从数据库删除长按的关键字
                        deleteFromDatabase();
                        //更新界面显示
                        updateUI();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    /**
     * 从数据库删除长按的号码
     */
    private void deleteFromDatabase() {
        mSMSDaoImpl.deletePhoneNum(mCurClickedPhone);
        LogHelper.d(TAG, "phone num deleted:" + mCurClickedPhone.getPhoneNumStr());
    }

    /**
     * 显示一个对话框来添加新的关键字
     */
    private void showAddDialog() {
        mEtGetNewPhone = new EditText(mContext);
        mEtGetNewPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.add_phone_to_black_list)
                .setView(mEtGetNewPhone)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定时，将新的号码保存到数据库
                        saveToDatabase();
                        //更新界面显示
                        updateUI();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    /**
     * 将输入的关键字保存到数据库
     */
    private void saveToDatabase() {
        String newPhoneStr = mEtGetNewPhone.getText().toString().trim();
        //如果字符串为空，不进行插入
        if (TextUtils.isEmpty(newPhoneStr)) {
            Toast.makeText(mContext, R.string.you_input_nothing, Toast.LENGTH_SHORT).show();
            return;
        }
        //如果号码已经在数据库中存在，不进行重复插入
        if (mSMSDaoImpl.isPhoneNumExists(newPhoneStr)) {
            Toast.makeText(mContext, R.string.phone_num_already_exists, Toast.LENGTH_SHORT).show();
            return;
        }
        BlockedPhoneNum phoneNum = new BlockedPhoneNum();
        phoneNum.setPhoneNumStr(newPhoneStr);
        mSMSDaoImpl.insertPhoneNum(phoneNum);
        LogHelper.d(TAG, "newPhoneNum inserted: " + newPhoneStr);
    }

    /**
     * 数据库更新后通过数据库更新UI界面
     */
    private void updateUI() {
        mPhoneList = mSMSDaoImpl.getAllPhoneNums();
        if (mPhoneList.size() > 0) {
            getActionBar().setTitle(R.string.blocked_phone);
        } else {
            getActionBar().setTitle(R.string.no_phone_num_was_added);
        }
        mAdapter.clear();
        mAdapter.addAll(mPhoneList);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 将修改后的号码更新到数据库
     */
    private void updateDatabase() {
        String newPhoneStr = mEtEditPhone.getText().toString().trim();
        if (TextUtils.isEmpty(newPhoneStr)) {
            Toast.makeText(mContext, getString(R.string.failed_to_edit_phone), Toast.LENGTH_SHORT).show();
            return;
        }
        //如果号码已经在数据库中存在，不进行修改
        if (mSMSDaoImpl.isPhoneNumExists(newPhoneStr)) {
            Toast.makeText(mContext, R.string.phone_num_already_exists, Toast.LENGTH_SHORT).show();
            return;
        }
        mSMSDaoImpl.updateKeyword(mCurClickedPhone.getPhoneNumStr(), newPhoneStr);
        LogHelper.d(TAG, "phone num updated,oldNum:" + mCurClickedPhone.getPhoneNumStr() + "new num:" + mEtEditPhone.getText());
    }

}
