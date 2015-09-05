package com.chaoyang805.blocksms.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaoyang805.blocksms.R;

/**
 * Created by chaoyang805 on 2015/9/5.
 */
public class KeywordsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_keywords,container,false);

        return rootView;
    }
}
