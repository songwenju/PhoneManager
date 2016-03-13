package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 欢迎页
 * 作者：songwenju on 2016/1/31 15:13
 * 邮箱：songwenju01@163.com
 */
public class WelcomeGuideActivity extends BaseActivity {
    ViewPager mVpGuide;

    //引入页面图片资源
    public static final int[] pics = {
            R.layout.guid_view1, R.layout.guid_view2, R.layout.guid_view3, R.layout.guid_view4
    };
    LinearLayout mLlDots;
    // 底部小点图片
    private ImageView[] mDots;
    //记录当前选中位置
    private int currentIndex;
    private ArrayList<View> mViews;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_guid;
    }

    @Override
    protected void onInitView() {
        mVpGuide = (ViewPager) findViewById(R.id.vp_guide);
    }

    @Override
    protected void onInitListener() {

    }

    @Override
    protected void onInitData() {
        mViews = new ArrayList<>();
        //初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);

            if (i == pics.length - 1) {
                Button startBtn = (Button) view.findViewById(R.id.btn_enter);
                startBtn.setOnClickListener(this);
            }

            mViews.add(view);
        }
    }

    @Override
    protected void onSetViewData() {
        PagerAdapter adapter = new GuideViewPagerAdapter(mViews);
        mVpGuide.setAdapter(adapter);
        mVpGuide.addOnPageChangeListener(new PageChangeListener());
        initDos();
    }


    @Override
    protected void processClick(View v) {
        if(v.getId() == R.id.btn_enter){
            enterMainActivity();
            return;
        }
        int position = (int) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    /**
     * 初始化小圆点
     */
    private void initDos() {
        mDots = new ImageView[pics.length];
        mLlDots = (LinearLayout) findViewById(R.id.ll_dots);
        for (int i = 0; i < pics.length; i++) {
            //得到一个LinearLayout下面的每一个子元素
            mDots[i] = (ImageView) mLlDots.getChildAt(i);
            mDots[i].setEnabled(false);  //都设置为灰色
            mDots[i].setOnClickListener(this);
            mDots[i].setTag(i);//设置位置tag,方便取出与当前的位置对应
        }
        currentIndex = 0;
        mDots[currentIndex].setEnabled(true); //设置为白色,即选中状态
    }

    /**
     * 设置当前的指示点
     * @param position 位置
     */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position){
            return;
        }
        LogUtil.e(this,"position:"+position);
        mDots[position].setEnabled(true);
        mDots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    /**
     * 设置当前的view
     * @param position 位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length){
            return;
        }
        mVpGuide.setCurrentItem(position);
    }

    /**
     * 进入MainActivity
     */
    private void enterMainActivity() {
        Intent intent = new Intent(this,SplashActivity.class);
        startActivity(intent);
        SpUtil.putBoolean(AppConstants.FIRST_OPEN,false);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //如果切换到后台,就设置下次不进入功能引导页
        SpUtil.putBoolean(AppConstants.FIRST_OPEN, false);
        finish();
    }

    /**
     * viewPagerAdapter
     */
    private class GuideViewPagerAdapter extends PagerAdapter {
        private List<View> views;
        public GuideViewPagerAdapter(ArrayList<View> views) {
            super();
            this.views = views;
        }

        @Override
        public int getCount() {
            if (views != null){
                return views.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        //当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // position :当前页面，及你点击滑动的页面
            // positionOffset:当前页面偏移的百分比
            // positionOffsetPixels:当前页面偏移的像素位置
        }

        //当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurDot(position);
        }

        //当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int state) {
            // state ==1的时,表示正在滑动
            // state==2的时,表示滑动完毕了
            // state==0的时,表示什么都没做。
        }
    }

}
