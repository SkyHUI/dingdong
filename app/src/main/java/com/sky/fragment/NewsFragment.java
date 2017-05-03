package com.sky.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.sky.adapter.NewsAdapter;
import com.sky.bean.News;
import com.sky.heartbeat.BaseActivity;
import com.sky.heartbeat.DetailActivity;
import com.sky.heartbeat.R;
import com.sky.utils.CommandOrder;
import com.sky.utils.GsonUtils;
import com.sky.utils.RequestConstant;
import com.sky.view.DividerGridItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.sky.helloworld
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/10/11 17:12
 */
public class NewsFragment extends BaseFragment {

    private RecyclerView mRecycler;
    private List<News> newsList;
    private NewsAdapter adapter;
    /*private int[] resIds  = {R.mipmap.new1,R.mipmap.new2,R.mipmap.new3};

    private String[] titles = {"注意！这么做是在天天“喂养癌细胞”",
            "骨性关节炎的运动疗法有哪些",
            "眼部浮肿多见肾病，盘点身上6肿预示什么病"};

    private String[] dates = {"2017-1-1","2017-1-2","2017-1-3"};*/

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.message_layout,null,false);
    }

    @Override
    protected void initFindViewById(View view) {
        mRecycler = (RecyclerView) view.findViewById(R.id.message_recycler);
    }

    @Override
    protected void initData() {
        newsList = new ArrayList<>();
        JSONObject json = new JSONObject();
        try {
            json.put("command", CommandOrder.COMMAND_NEWS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestServer(RequestConstant.REQUEST_NEWS, json, new BaseActivity.DataCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(getClass().getName(),"新闻列表----->"+ t);
                try {
                    JSONArray array = new JSONArray(t);
                    for(int i = 0;i<array.length();i++){
                        JSONObject json = array.getJSONObject(i);
                        News news = new GsonUtils().toBean(json.toString(),News.class);
                        newsList.add(news);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new NewsAdapter(getActivity(),newsList);
                mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecycler.addItemDecoration(new DividerGridItemDecoration(getActivity()));
                mRecycler.setAdapter(adapter);

                adapter.setOnItemClickLitener(new NewsAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, News news) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("news",news);
                        startActivity(intent);
                    }
                });

            }
        });
    }

    @Override
    protected void initEvent() {

    }
}
