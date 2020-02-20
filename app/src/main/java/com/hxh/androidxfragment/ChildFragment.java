package com.hxh.androidxfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hxh.androidxfragment.indicator.IndicatorHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * Created by HXH on 2020/2/20 11:42
 * TODO
 */
public class ChildFragment extends LazyLoadFragment {

    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.view_pager)
    NoPreViewPager mViewPager;

    private List<String> mTabNames = Arrays.asList("天府文化2", "特色街区2", "大咖说2", "天府圈2");
    private SparseArray<Fragment> mFragments = new SparseArray<>();

    public static ChildFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        ChildFragment fragment = new ChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("InflateParams")
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_child, null);
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return IsBuildFragment.newInstance(mTabNames.get(position));
            }

            @Override
            public int getCount() {
                return mTabNames.size();
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }

            ///////////////////////////////////////////////////////////////////////////
            // 应以这样的方式来持有fragment的引用 start
            ///////////////////////////////////////////////////////////////////////////
            @Override
            public @NonNull
            Object instantiateItem(@NonNull ViewGroup container, int position) {
                Fragment f = (Fragment) super.instantiateItem(container, position);
                mFragments.put(position, f);
                return f;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                super.destroyItem(container, position, object);
                mFragments.remove(position);
            }
            ///////////////////////////////////////////////////////////////////////////
            // 应以这样的方式来持有fragment的引用 end
            ///////////////////////////////////////////////////////////////////////////
        });
        mViewPager.setOffscreenPageLimit(mTabNames.size());
        IndicatorHelper.setAdapterAndDelegate(Objects.requireNonNull(getActivity()),
                mMagicIndicator,
                mViewPager,
                mTabNames,
                R.color.black,
                R.color.red,
                false);
        mViewPager.addOnPageChangeListener(new NoPreViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //setBadgeViewCount(position, x + position);
            }
        });

        int index = 1;
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index");
        }
        mViewPager.setCurrentItem(index);
    }

    @Override
    public String toString() {
        return getArguments().getString("name");
    }
}
