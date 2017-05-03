package com.sky.heartbeat;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sky.adapter.HistoryAdapter;
import com.sky.bean.MedicineMenu;
import com.sky.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.sky.helloworld
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/10/9 12:49
 */
public class HistoryActivity extends BaseActivity{

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<MedicineMenu> mDatas = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.history_layout);
    }

    @Override
    protected void findId() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.history_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.history_list);
    }

    @Override
    protected void setListener() {
        initEvent();
    }

    @Override
    protected void initData() {
        initData();

        adapter = new HistoryAdapter(this,mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this) );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void init(){
        for(String name : FileUtils.getFileNames()){
            MedicineMenu menu = new MedicineMenu();
            menu.setName(name);
            mDatas.add(menu);
        }
    }

    @Override
    protected void processLogic() {

    }

    private void initEvent(){
        adapter.setOnItemClickLitener(new HistoryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HistoryActivity.this,LeafChartActivity.class);
                intent.putExtra("fileName",mDatas.get(position).getName());
                startActivity(intent);
            }

        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
