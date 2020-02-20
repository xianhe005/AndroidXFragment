package com.hxh.androidxfragment;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hxh.androidxfragment.indicator.IndicatorHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.view_pager)
    NoPreViewPager mViewPager;

    private List<String> mTabNames = Arrays.asList("天府文化", "特色街区", "大咖说", "天府圈");
    private SparseArray<Fragment> mFragments = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position > 1) {
                    return IsBuildFragment.newInstance(mTabNames.get(position));
                }
                return ChildFragment.newInstance(mTabNames.get(position));
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
        IndicatorHelper.setAdapterAndDelegate(this,
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

        int index = 0;
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index");
        }
        mViewPager.setCurrentItem(index);
    }
}
