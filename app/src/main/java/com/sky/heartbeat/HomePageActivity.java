package com.sky.heartbeat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.sky.utils.SPUtil;

import cn.sharesdk.framework.ShareSDK;

import static com.sky.heartbeat.R.id.textView;

/**
 * 项目名称：com.sky.heartbeat
 * 类描述：程序启动页 3秒后跳转
 * 创建人：Sky
 * 创建时间：2016/11/8 21:12
 */

public class HomePageActivity extends BaseActivity {

    private boolean isFirstIn = true;
    private String TAG = "HomePageActivity";
    private FloatingActionButton fab;
    private TextView nameTextView;


    @Override
    protected void setContentView() {
        ShareSDK.initSDK(this);
        setContentView(R.layout.activity_home_page);
    }

    @Override
    protected void findId() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        nameTextView = (TextView) findViewById(textView);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        isFirstIn = SPUtil.isFirstIn(this);
        Log.i(TAG,"isFirstIn"+isFirstIn);
    }

    @Override
    protected void processLogic() {
        //3秒后执行跳转
        fab.postDelayed(new Runnable() {
            @Override
            public void run() {
                //执行动画
                final View parentView = (View) fab.getParent();
                float scale = (float) (Math.sqrt(parentView.getHeight() * parentView.getHeight() + parentView.getWidth() * parentView.getWidth()) / fab.getHeight());
                PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", scale);
                PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", scale);
                ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(fab, scaleX, scaleY).setDuration(1800);
                objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        parentView.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.colorPrimary));
                        fab.setVisibility(View.GONE);
                        nameTextView.setVisibility(View.VISIBLE);
                    }
                });
                PropertyValuesHolder holderA = PropertyValuesHolder.ofFloat("alpha", 0, 1);
                PropertyValuesHolder holderYm = PropertyValuesHolder.ofFloat("translationY", 0, 300);
                ObjectAnimator textAnimator = ObjectAnimator.ofPropertyValuesHolder(textView, holderA, holderYm).setDuration(1200);
                textAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                textAnimator.setStartDelay(800);

                textAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //第一次进入应用要显示引导页
                        if(isFirstIn){
                            startActivity(new Intent(HomePageActivity.this,GuideActivity.class));
                        }else{
                            startActivity(new Intent(HomePageActivity.this,LoginActivity.class));
                        }
                        finish();
                    }
                });
                objectAnimator.start();
                textAnimator.start();

            }
        },300);
    }
}
