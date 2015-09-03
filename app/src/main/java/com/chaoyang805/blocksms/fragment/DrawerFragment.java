package com.chaoyang805.blocksms.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chaoyang805.blocksms.MainActivity;
import com.chaoyang805.blocksms.R;
import com.chaoyang805.blocksms.adapter.DrawerListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/3.
 */
public class DrawerFragment extends Fragment {

    private View mRootView;
    private RecyclerView mList;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private View mDrawerFragmentContainer;
    private DrawerListAdapter mAdapter;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.drawer_layout_fragment, container, false);
        mList = (RecyclerView) mRootView.findViewById(R.id.drawer_list);
        mContext = getActivity();
        final List<String> datas = new ArrayList<>();
        datas.add("屏蔽的短信");
        datas.add("已屏蔽的号码");
        datas.add("已屏蔽的关键字");
        mAdapter = new DrawerListAdapter(datas);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mList.setLayoutManager(manager);
        mAdapter.setOnItemClickListener(new DrawerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                datas.get(position);
                Toast.makeText(mContext,"position = "+position,Toast.LENGTH_SHORT).show();
            }
        });
        mList.setAdapter(mAdapter);
        return mRootView;
    }

    /**
     * 初始化drawer
     * @param drawerFragmentContainerId 承载drawer布局的framelayout的id
     * @param drawerLayout DrawerLayout对象
     * @param toolbar   Toolbar对象
     */
    public void setupDrawer(int drawerFragmentContainerId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerFragmentContainer = getActivity().findViewById(drawerFragmentContainerId);
        mDrawerLayout = drawerLayout;
        mToolbar = toolbar;

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //重新加载optionsMenu
                ((MainActivity) getActivity()).supportInvalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //重新加载optionsMenu
                ((MainActivity) getActivity()).supportInvalidateOptionsMenu();
            }
        };

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }
}
