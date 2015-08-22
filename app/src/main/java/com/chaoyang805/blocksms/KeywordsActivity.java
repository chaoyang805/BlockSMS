package com.chaoyang805.blocksms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chaoyang805.blocksms.app.BlockSMSApp;
import com.chaoyang805.blocksms.bean.Keyword;
import com.chaoyang805.blocksms.db.SMSDAOImpl;
import com.chaoyang805.blocksms.utils.LogHelper;

import java.util.List;

/**
 * Created by chaoyang805 on 2015/8/22.
 */
public class KeywordsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = LogHelper.makeLogTag(KeywordsActivity.class);
    private ListView mLvKeywords;
    private List<Keyword> mKeywordsList;
    private ArrayAdapter<Keyword> mAdapter;
    private SMSDAOImpl mSMSDaoImpl;
    private EditText mEtGetNewKeyword, mEtEditKeyword;
    private Keyword mCurClickedKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keywords);
        initViews();
        mSMSDaoImpl = ((BlockSMSApp) getApplication()).getSMSDaoImpl();
        initDatas();
    }


    /**
     * 初始化视图控件
     */
    private void initViews() {
        mLvKeywords = (ListView) findViewById(R.id.lv_blocked_keywords);
        findViewById(R.id.btn_add_keyword).setOnClickListener(this);
        mLvKeywords.setOnItemLongClickListener(this);
    }

    /**
     * 初始化数据，并显示在listview上
     */
    private void initDatas() {
        mKeywordsList = mSMSDaoImpl.getAllKeywords();
        LogHelper.d(TAG, "mKeywordsList.size = " + mKeywordsList.size());
        if (mKeywordsList.size() > 0) {
            getSupportActionBar().setTitle(R.string.blocked_keywords);
        } else {
            getSupportActionBar().setTitle(R.string.no_keywords_was_added);
        }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mKeywordsList);
        mLvKeywords.setAdapter(mAdapter);
    }

    /**
     * 点击添加按钮时的监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        showAddDialog();
    }

    /**
     * 显示一个对话框来添加新的关键字
     */
    private void showAddDialog() {
        mEtGetNewKeyword = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_keyword)
                .setView(mEtGetNewKeyword)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定时，将新的关键字保存到数据库
                        saveToDatabase();
                        //更新界面显示
                        updateUI();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    /**
     * 数据库更新后通过数据库更新UI界面
     */
    private void updateUI() {
        mKeywordsList = mSMSDaoImpl.getAllKeywords();
        if (mKeywordsList.size() > 0) {
            getSupportActionBar().setTitle(R.string.blocked_keywords);
        } else {
            getSupportActionBar().setTitle(R.string.no_keywords_was_added);
        }
        mAdapter.clear();
        mAdapter.addAll(mKeywordsList);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 将输入的关键字保存到数据库
     */
    private void saveToDatabase() {
        String newKeyword = mEtGetNewKeyword.getText().toString();
        //如果字符串为空，不进行插入
        if (TextUtils.isEmpty(newKeyword)) {
            Toast.makeText(this, R.string.you_input_nothing, Toast.LENGTH_SHORT).show();
            return;
        }
        //如果关键字已经在数据库中存在，不进行重复插入
        if (mSMSDaoImpl.isKeywordExists(newKeyword)) {
            Toast.makeText(this, R.string.keyword_already_exists, Toast.LENGTH_SHORT).show();
            return;
        }
        Keyword keyword = new Keyword();
        keyword.setKeywordStr(newKeyword);
        mSMSDaoImpl.insertKeyword(keyword);
        LogHelper.d(TAG, "newKeyword inserted: " + newKeyword);
    }

    /**
     * 长按Item时可以删除或者修改Item
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mCurClickedKeyword = mAdapter.getItem(position);
        showOptionsDialog();
        return true;
    }

    /**
     * 长按listView的item时进行编辑或删除操作
     */
    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.keyword_options_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        LogHelper.d(TAG, "case 0:" + which);
                        //取消当前的dialog显示
                        dialog.dismiss();
                        //显示编辑关键字的dialog
                        showEditDialog();
                        break;
                    case 1:
                        LogHelper.d(TAG, "case 1:" + which);
                        //取消当前的dialog显示
                        dialog.dismiss();
                        //显示删除关键字的警告窗口
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

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_keyword)
                .setMessage(R.string.are_you_sure_you_want_to_delete_this_keyword)
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
     * 从数据库删除长按的关键字
     */
    private void deleteFromDatabase() {
        mSMSDaoImpl.deleteKeyword(mCurClickedKeyword);
        LogHelper.d(TAG, "keyword deleted:" + mCurClickedKeyword.getKeywordStr());
    }

    /**
     * 显示编辑关键词的dialog
     */
    private void showEditDialog() {
        mEtEditKeyword = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_keyword)
                .setView(mEtEditKeyword)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定时，将新编辑的关键词更改到数据库
                        updateDatabase();
                        //更新界面显示
                        updateUI();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    /**
     * 将新编辑的关键词更改到数据库
     */
    private void updateDatabase() {
        String newKeyword = mEtEditKeyword.getText().toString();
        if (TextUtils.isEmpty(newKeyword)) {
            Toast.makeText(this, getString(R.string.failed_to_edit_keyword), Toast.LENGTH_SHORT).show();
            return;
        }
        //如果关键字已经在数据库中存在，不进行更改
        if (mSMSDaoImpl.isKeywordExists(newKeyword)) {
            Toast.makeText(this, R.string.keyword_already_exists, Toast.LENGTH_SHORT).show();
            return;
        }
        mSMSDaoImpl.updateKeyword(mCurClickedKeyword.getKeywordStr(), newKeyword);
        LogHelper.d(TAG, "keyword updated,oldKeyword:" + mCurClickedKeyword.getKeywordStr() + "newKeyword:" + mEtEditKeyword.getText());
    }
}
