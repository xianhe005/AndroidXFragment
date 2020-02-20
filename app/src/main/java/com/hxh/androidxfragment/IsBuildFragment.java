package com.hxh.androidxfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;

/**
 * Created by HXH on 2019/11/23 14:58
 * 正在建设中
 */
public class IsBuildFragment extends LazyLoadFragment {

    @BindView(R.id.tv)
    TextView mTv;

    public static IsBuildFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        IsBuildFragment fragment = new IsBuildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("InflateParams")
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_is_building, null);
    }

    @Override
    protected void onFirst() {
        super.onFirst();
        log("setText");
        assert getArguments() != null;
        mTv.setText(getArguments().getString("name"));
    }

    @Override
    public String toString() {
        return getArguments().getString("name");
    }
}
