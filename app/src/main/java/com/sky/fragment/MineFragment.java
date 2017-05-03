package com.sky.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sky.adapter.MineAdapter;
import com.sky.bean.Menu;
import com.sky.download.DownLoaderTask;
import com.sky.heartbeat.AboutUsActivity;
import com.sky.heartbeat.FeedBackActivity;
import com.sky.heartbeat.GuideActivity;
import com.sky.heartbeat.LoginActivity;
import com.sky.heartbeat.R;
import com.sky.utils.ActivityCollector;
import com.sky.utils.FileUtils;
import com.sky.utils.RequestConstant;
import com.sky.view.MustDoThingDailog;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.sky.fragment
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/12/13 11:06
 */
public class MineFragment extends BaseFragment{

    private TextView tv_name;

    private RecyclerView recyclerView;

    private MineAdapter adapter;

    private int[] resIds = new int[]{R.mipmap.download,R.mipmap.welcome_page,R.mipmap.about,R.mipmap.question,R.mipmap.logout};

    private String[] names = new String[]{"数据模型下载","欢迎页", "关于叮咚","问题反馈","退出登录"};

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.mine_layout,null,false);
        return view;
    }

    @Override
    protected void initFindViewById(View view) {
        tv_name = (TextView) view.findViewById(R.id.mine_tv_name);
        recyclerView = (RecyclerView) view.findViewById(R.id.mine_rv);
    }

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Menu> menuList = new ArrayList<>();
        for(int i = 0;i < names.length;i++){
            Menu menu = new Menu();
            menu.setResId(resIds[i]);
            menu.setName(names[i]);
            menuList.add(menu);
        }
        adapter = new MineAdapter(getActivity(),menuList);
        recyclerView.setAdapter(adapter);
        //设置RecyclerView的头部
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.item_mine_head,recyclerView,false);
        adapter.setHeadView(headView);

        adapter.setOnItemClickLitener(new MineAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        //数据模型下载
                        alert("确定下载数据模型？", new MustDoThingDailog.NeedDoThing() {
                            @Override
                            public void mustDoThings() {

                                DownLoaderTask task = new DownLoaderTask(RequestConstant.REQUEST_MODEL, FileUtils.PATH_ROOT, getContext());
                                task.execute();
                            }
                        });
                        break;
                    case 1:
                        //欢迎页
                        Intent intent = new Intent(getActivity(), GuideActivity.class);
                        intent.putExtra("switch",false);
                        startActivity(intent);
                        break;
                    case 2:
                        //关于叮咚
                        startActivity(new Intent(getActivity(), AboutUsActivity.class));

                        break;
                    case 3:
                        //问题反馈
                        startActivity(new Intent(getActivity(), FeedBackActivity.class));
                        break;
                    case 4:
                        //退出登录
                        ActivityCollector.finishAll();
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                        break;
                }
            }
        });

    }
}
