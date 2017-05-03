package com.sky.heartbeat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sky.fragment.CollectionFragment;
import com.sky.fragment.NewsFragment;
import com.sky.fragment.MineFragment;
import com.sky.hkg.DeviceListActivity;
import com.sky.utils.ActivityCollector;

import org.loader.mdtab.MDTab;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.sky.heartbeat
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/11/8 21:19
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static MainActivity mInstance;
    private FloatingActionButton floatingActionButton, floatingActionButton2, floatingActionButton3;
    private float currentY;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ViewPager mviewPager;
    private MDTab mdTab;
    private String[] mMenus = new String[]{"资讯", "采集", "我的"};
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private TextView tv_name;
    private Toolbar toolbar;
    private FrameLayout frameLayout;

    private Resources resources = null;

    @Override
    public void setListener() {
        floatingActionButton.setOnClickListener(this);
        floatingActionButton2.setOnClickListener(this);
        floatingActionButton3.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.item1:
                        frameLayout.setVisibility(View.GONE);
                        toolbar.setTitle(resources.getString(R.string.app_name));
                        break;
                    case R.id.item2:
                        frameLayout.setVisibility(View.VISIBLE);
                        toolbar.setTitle(resources.getString(R.string.collection));
                        break;
                    case R.id.item3:
                        frameLayout.setVisibility(View.VISIBLE);
                        toolbar.setTitle(resources.getString(R.string.week));
                        break;
                    case R.id.item4:
                        frameLayout.setVisibility(View.VISIBLE);
                        toolbar.setTitle(resources.getString(R.string.count));
                        break;
                    case R.id.item5:    //退出登录
                        ActivityCollector.finishAll();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        break;
                    case R.id.setting:
                        frameLayout.setVisibility(View.VISIBLE);
                        toolbar.setTitle(resources.getString(R.string.setting));
                        break;
                    case R.id.about:
                        frameLayout.setVisibility(View.VISIBLE);
                        toolbar.setTitle(resources.getString(R.string.about));
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingbt:
                showAnimation();
                break;
            case R.id.floatingbt2:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case R.id.floatingbt3:
                startActivity(new Intent(this, DeviceListActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void findId() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        frameLayout = (FrameLayout) findViewById(R.id.main_frame);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingbt);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.floatingbt2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.floatingbt3);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        /**
         * 注意NavigationView中的Head部分
         * 必须要通过  navigationView.getHeaderView(0)来获得其自View
         * 否则会报错
         */
        tv_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_head_name);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mviewPager = (ViewPager) findViewById(R.id.viewPager);
        mdTab = (MDTab) findViewById(R.id.mdtab);
    }

    @Override
    protected void setContentView() {
        mInstance = this;
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {
        initToolBar();
        currentY = floatingActionButton.getTranslationY();
        initViewPager();
        resources = this.getResources();
        String username = getIntent().getStringExtra("username");
        if(!TextUtils.isEmpty(username)){
            tv_name.setText(username);
        }
    }

    @Override
    protected void processLogic() {
        disableNavigationViewScrollbars(navigationView);
    }

    private void initViewPager() {
        //初始化fragments
        fragments.add(new NewsFragment());
        fragments.add(new CollectionFragment());
        fragments.add(new MineFragment());
        //为ViewPager设置Adapter
        mviewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

        });
        //为MDTab初始化设置信息
        mdTab.setAdapter(new MDAdapter());
        mdTab.itemChecked(0);
        mdTab.setOnItemCheckedListener(new MDTab.OnItemCheckedListener() {
            @Override
            public void onItemChecked(int position, View view) {
                mviewPager.setCurrentItem(position);
            }
        });

        mviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){

                    case 1:
//                        Log.i("TAG","Id:"+getSupportFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content));
                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mdTab.setupWithViewPager(mviewPager);
    }

    /**
     * FloatingActionButton 动画控制
     */
    private void showAnimation() {
        floatingActionButton2.setVisibility(View.VISIBLE);
        floatingActionButton3.setVisibility(View.VISIBLE);
        float y2 = floatingActionButton2.getTranslationY();
        float y3 = floatingActionButton3.getTranslationY();
        if (y2 == currentY && y3 == currentY) {
            ObjectAnimator translation2 = ObjectAnimator.ofFloat(floatingActionButton2, "translationY", currentY, currentY - 200);
            translation2.setDuration(1000);
            translation2.start();
            ObjectAnimator translation3 = ObjectAnimator.ofFloat(floatingActionButton3, "translationY", currentY, currentY - 400);
            translation3.setDuration(1000);
            translation3.start();
        } else {
            ObjectAnimator translation2 = ObjectAnimator.ofFloat(floatingActionButton2, "translationY", y2, currentY);
            translation2.setDuration(1000);
            translation2.start();
            ObjectAnimator translation3 = ObjectAnimator.ofFloat(floatingActionButton3, "translationY", y3, currentY);
            translation3.setDuration(1000);
            translation3.start();
        }

    }

    class MDAdapter extends MDTab.TabAdapter {

        @Override
        public int getItemCount() {
            return mMenus.length;
        }

        @Override
        public Drawable getDrawable(int position) {
            int res = getResources().getIdentifier("icon_" + position, "mipmap", getPackageName());
            return getResources().getDrawable(res);
        }

        @Override
        public CharSequence getText(int position) {
            return mMenus[position];
        }
    }

    private void initToolBar(){
        toolbar.setNavigationIcon(R.mipmap.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 去掉NegationView中的Scrollbar
     * @param navigationView
     */
    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstance = null;
    }
}
