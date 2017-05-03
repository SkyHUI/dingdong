package com.sky.heartbeat;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sky.adapter.ViewPagerAdapter;
import com.sky.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.sky.heartbeat
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/12/11 11:00
 */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private String TAG = "GuideActivity";
    private int lastValue = -1;
    private boolean sw = true;

    //引导图片资源
    private static final int[] pics =  { R.mipmap.run,
            R.mipmap.ride, R.mipmap.sport};

    //底部小点图片
    private ImageView[] dots ;

    //记录当前选中位置
    private int currentIndex;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void findId() {
        vp = (ViewPager) findViewById(R.id.activity_welcome_viewPager);
    }

    @Override
    protected void setListener() {
        vp.setOnPageChangeListener(this);
    }

    @Override
    protected void initData() {
        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        //初始化引导图片列表
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            views.add(iv);
        }
        //初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        //初始化底部小点
        initDots();
    }

    @Override
    protected void processLogic() {
        sw = getIntent().getBooleanExtra("switch",true);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态

    }

    /**
     *设置当前的引导页
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }

    /**
     *这只当前引导小点的选中
     */
    private void setCurDot(int positon)
    {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {


    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        Log.i(TAG,"arg0:"+arg0+"lastValue:"+lastValue+":"+arg2);
        if(arg0 == pics.length-1){
            //在引导页的最后一页向右滑动时 直接进入登录页面
            if( lastValue == arg2){
                SPUtil.setFirstIn(this);
                finish();
                if(sw){
                    startActivity(new Intent(GuideActivity.this,LoginActivity.class));
                }
            }
        }
        lastValue = arg2;
    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
        setCurDot(arg0);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        setCurView(position);
        setCurDot(position);
    }

}
