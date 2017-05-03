package com.sky.heartbeat;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * 项目名称：com.sky.heartbeat
 * 类描述：
 * 创建人：Sky
 * 创建时间：2017/4/28 14:44
 */
public class AboutUsActivity extends BaseActivity {
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_about_us);
    }

    @Override
    protected void findId() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("关于");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void processLogic() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
