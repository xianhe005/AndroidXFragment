package com.hxh.androidxfragment.indicator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * Created by HXH at 2019/5/29
 * 指示器帮助类-无viewpager
 */
public class NoVPIndicatorHelper {
    private NoVPIndicatorHelper() {
        throw new UnsupportedOperationException("no instance for you");
    }

    public static void setIndicator(@NonNull Activity activity,
                                    @NonNull MagicIndicator magicIndicator,
                                    @NonNull final String[] tabNames,
                                    @ColorRes final int normalColor,
                                    @ColorRes final int selectedColor,
                                    @Nullable OnIndicatorChangeListener listener) {
        CommonNavigator navigator = new CommonNavigator(activity);
        navigator.setAdjustMode(true);
        //navigator.setScrollPivotX(0.8f);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabNames.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                SimplePagerTitleView pagerTitleView = new ColorTransitionPagerTitleView(context) {
                    private float mMinScale = 0.9f;

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                        super.onEnter(index, totalCount, enterPercent, leftToRight);    // 实现颜色渐变
                        setScaleX(mMinScale + (1.0f - mMinScale) * enterPercent);
                        setScaleY(mMinScale + (1.0f - mMinScale) * enterPercent);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        super.onLeave(index, totalCount, leavePercent, leftToRight);    // 实现颜色渐变
                        setScaleX(1.0f + (mMinScale - 1.0f) * leavePercent);
                        setScaleY(1.0f + (mMinScale - 1.0f) * leavePercent);
                    }
                };
                pagerTitleView.setText(tabNames[index]);
                pagerTitleView.setTextSize(16);
                pagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                pagerTitleView.setNormalColor(ActivityCompat.getColor(activity, normalColor));
                pagerTitleView.setSelectedColor(ActivityCompat.getColor(activity, selectedColor));
                pagerTitleView.setOnClickListener(v -> {
                    magicIndicator.onPageSelected(index);
                    magicIndicator.onPageScrolled(index, 0.0F, 0);
                    if (listener != null) {
                        listener.onChange(index);
                    }
                });
                return pagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //indicator.setStartInterpolator(new AccelerateInterpolator());
                //indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                //indicator.setLineHeight(4 * scale);
                indicator.setColors(ActivityCompat.getColor(context, selectedColor));
                return indicator;
            }
        });
        magicIndicator.setNavigator(navigator);
    }

    public interface OnIndicatorChangeListener {
        void onChange(int index);
    }
}
