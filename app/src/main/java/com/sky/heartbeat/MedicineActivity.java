package com.sky.heartbeat;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.sky.adapter.HistoryAdapter;
import com.sky.bean.MedicineMenu;
import com.sky.utils.CommandOrder;
import com.sky.utils.GsonUtils;
import com.sky.utils.RequestConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.sky.heartbeat
 * 类描述：
 * 创建人：Sky
 * 创建时间：2017/3/28 16:35
 */
public class MedicineActivity extends BaseActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout cLayout;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<MedicineMenu> mDatas = new ArrayList<>();

    /**
     * 1 - 疾病库
     * 2 - 用药指导
     * 3 - 检查结果
     * 页面类型
     *
     */
    private int type = 1;

    /**
     * 当前菜单级数
     */
    private int currentMenu = 1;

    private String title = "";

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_medicine);
        Intent intent = getIntent();
        type = intent.getIntExtra("type",1);
        title = intent.getStringExtra("title");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void findId() {
        recyclerView = (RecyclerView) findViewById(R.id.disease_list);
        cLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    }

    @Override
    protected void setListener() {
        adapter.setOnItemClickLitener(new HistoryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                currentMenu ++ ;
                if(currentMenu == 2){
                    cLayout.setTitle(mDatas.get(position).getName());
                    queryMenu(CommandOrder.COMMAND_MEDICINE_CHILD,mDatas.get(position).getId());
                }else if(currentMenu >= 3){
                    currentMenu = 2;
                    //进入详情页面
                }
            }
        });
    }

    @Override
    protected void initData() {
        adapter = new HistoryAdapter(MedicineActivity.this,mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(MedicineActivity.this));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        queryMenu(CommandOrder.COMMAND_MEDICINE_PARENT,type);

    }

    @Override
    protected void processLogic() {

    }

    /**
     * 查询医药菜单
     * @param type
     */
    private void queryMenu(int command,int type){
        JSONObject json = new JSONObject();
        try {
            json.put("command", command);
            json .put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestServer(RequestConstant.REQUEST_MEDICINE, json, new DataCallback() {
            @Override
            public void onSuccess(String t) {
                Log.e(getClass().getName(),"医药请求结果------->" + t);
                try {
                    mDatas.clear();
                    JSONArray array = new JSONArray(t);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject json = array.getJSONObject(i);
                        MedicineMenu menu = new GsonUtils().toBean(json.toString(),MedicineMenu.class);
                        mDatas.add(menu);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 顶部返回键
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           goBack();
        }
        return true;
    }

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * 返回操作
     */
    private void goBack(){
        if(currentMenu == 1){
            finish();
        }else if(currentMenu == 2){
            queryMenu(CommandOrder.COMMAND_MEDICINE_PARENT,type);
            currentMenu = 1;
            cLayout.setTitle(title);
        }
    }

}
