package com.hxh.androidxfragment.indicator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.hxh.androidxfragment.NoPreViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.abs.AbsDelegate;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import java.util.List;

/**
 * 指示器帮助类
 */
public class IndicatorHelper {
    private IndicatorHelper() {
        throw new UnsupportedOperationException("no instance for you");
    }

    public static void setAdapterAndDelegate(@NonNull Activity activity,
                                             @NonNull MagicIndicator magicIndicator,
                                             @NonNull NoPreViewPager viewPager,
                                             @NonNull final List<String> tabNames,
                                             @ColorRes final int normalColor,
                                             @ColorRes final int selectedColor) {
        setAdapterAndDelegate(activity, magicIndicator, viewPager, tabNames, normalColor, selectedColor, true);
    }

    public static void setAdapterAndDelegate(@NonNull Activity activity,
                                             @NonNull MagicIndicator magicIndicator,
                                             @NonNull NoPreViewPager viewPager,
                                             @NonNull final List<String> tabNames,
                                             @ColorRes final int normalColor,
                                             @ColorRes final int selectedColor,
                                             boolean adjustMode) {
        CommonNavigator navigator = new CommonNavigator(activity);
        navigator.setAdjustMode(adjustMode);
        //navigator.setScrollPivotX(0.8f);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabNames.size();
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
                pagerTitleView.setText(tabNames.get(index));
                pagerTitleView.setTextSize(16);
                pagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                pagerTitleView.setNormalColor(ActivityCompat.getColor(activity, normalColor));
                pagerTitleView.setSelectedColor(ActivityCompat.getColor(activity, selectedColor));
                pagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
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
        magicIndicator.setDelegate(new AbsDelegate() {
            @Override
            public IPagerNavigator delegateMagic(MagicIndicator magicIndicator, IPagerNavigator pagerNavigator) {
                viewPager.addOnPageChangeListener(new NoPreViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }

                    @Override
                    public void onPageSelected(int position) {
                        magicIndicator.onPageSelected(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        magicIndicator.onPageScrollStateChanged(state);
                    }
                });
                return pagerNavigator;
            }
        });
    }

    /**
     * @param badgeIndexs 需要包含角标的索引数组
     * @return 包含角标的索引数组对应的map
     */
    public static SparseArray<BadgePagerTitleView> setAdapterAndDelegateContainBadge(@NonNull Activity activity,
                                                                                     @NonNull MagicIndicator magicIndicator,
                                                                                     @NonNull NoPreViewPager viewPager,
                                                                                     @NonNull final String[] tabNames,
                                                                                     @ColorRes final int normalColor,
                                                                                     @ColorRes final int selectedColor,
                                                                                     @Nullable int[] badgeIndexs) {
        SparseArray<BadgePagerTitleView> map = new SparseArray<>();
        CommonNavigator navigator = new CommonNavigator(activity);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabNames.length;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
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
                pagerTitleView.setNormalColor(ActivityCompat.getColor(activity, normalColor));
                pagerTitleView.setSelectedColor(ActivityCompat.getColor(activity, selectedColor));
                pagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
                // 需要显示角标的tab索引
                BadgePagerTitleView badgePagerTitleView = null;
                if (badgeIndexs != null && badgeIndexs.length > 0) {
                    for (int badgeIndex : badgeIndexs) {
                        if (index == badgeIndex) {//需要显示角标的tab索引
                            badgePagerTitleView = new BadgePagerTitleView(context);
                            badgePagerTitleView.setInnerPagerTitleView(pagerTitleView);
                            badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT,
                                    -UIUtil.dip2px(context, 6)));
                            badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                            map.put(index, badgePagerTitleView);
                            break;
                        }
                    }
                }
                return badgePagerTitleView != null ? badgePagerTitleView : pagerTitleView;
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
        ////////////////////////
        magicIndicator.setNavigator(navigator);
        magicIndicator.setDelegate(new AbsDelegate() {
            @Override
            public IPagerNavigator delegateMagic(MagicIndicator magicIndicator, IPagerNavigator pagerNavigator) {
                viewPager.addOnPageChangeListener(new NoPreViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }

                    @Override
                    public void onPageSelected(int position) {
                        magicIndicator.onPageSelected(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        magicIndicator.onPageScrollStateChanged(state);
                    }
                });
                return pagerNavigator;
            }
        });
        return map;
    }
}
