package com.hxh.androidxfragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

/**
 * Created by HXH on 2020/2/20 11:10
 * TODO
 */
public abstract class LazyLoadFragment extends Fragment {
    protected View mRootView;
    private boolean isFirstLoad = true; // 是否第一次加载

    public LazyLoadFragment() {
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            return mRootView;
        }
        mRootView = createView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, mRootView);
        initParams();
        initViews(savedInstanceState);
        initDatas();
        initListeners();
        log("onCreateView");
        return mRootView;
    }

    protected void log(String log) {
        Log.e("TAG", log + ":" + this.toString());
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected void initListeners() {
    }

    protected void initDatas() {
    }

    protected void initViews(@Nullable Bundle savedInstanceState) {
    }

    protected void initParams() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        log("onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        log("onStart");
    }

    @Override
    public final void onResume() {
        super.onResume();
        log("onResume");
        if (isFirstLoad) {
            isFirstLoad = false;
            onFirst();
        }
        onVisible();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        log("onSaveInstanceState");
    }

    @Override
    public final void onPause() {
        super.onPause();
        log("onPause");
        onInVisible();
    }

    @Override
    public void onStop() {
        super.onStop();
        log("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        log("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        log("onDetach");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 懒加载相关方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 第一次加载数据，只执行一次{@link #onVisible()}前执行
     */
    protected void onFirst() {
        log("onFirst");
    }

    /**
     * 可见时执行
     */
    protected void onVisible() {
        log("onVisible");
    }

    /**
     * 不可见时执行
     */
    protected void onInVisible() {
        log("onInVisible");
    }
}
