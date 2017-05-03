package com.sky.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sky.adapter.CollectionAdapter;
import com.sky.bean.ADInfo;
import com.sky.bean.MedicineMenu;
import com.sky.bean.Menu;
import com.sky.heartbeat.HistoryActivity;
import com.sky.heartbeat.MedicineActivity;
import com.sky.heartbeat.R;
import com.sky.hkg.DeviceListActivity;
import com.sky.view.DividerGridItemDecoration;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.sky.fragment
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/12/13 11:04
 */
public class CollectionFragment extends Fragment {

    private List<ImageView> views;
    private List<ADInfo> infos;
    private CycleViewPager cycleViewPager;
    private RecyclerView collectionRecycler;
    private String[] imageUrls = {"http://bpic.588ku.com/back_pic/00/05/11/005625e8534fef8.jpg",
            "http://bpic.588ku.com/back_pic/00/03/14/44561d1b0adfa64.jpg",
            "http://bpic.588ku.com/back_pic/00/09/91/35563099f3ba81b.jpg",
            "http://bpic.588ku.com/back_pic/00/03/46/20561e74d584317.jpg"
    };

    private CollectionAdapter adapter;
    private List<Menu> menus;
    private int[] resIds = {R.mipmap.collection,R.mipmap.history,R.mipmap.collection,R.mipmap.history,R.mipmap.collection,};
    private String[] names = {"采集","历史记录","疾病库","用药指导","检查结果"};

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_layout,container,false);
        cycleViewPager = (CycleViewPager) getActivity().getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        collectionRecycler = (RecyclerView) view.findViewById(R.id.collection_recycler);
        initialize();
        initRecycler();
        return view;
    }

    private void initRecycler() {
        menus = new ArrayList<>();
        for(int i = 0;i<resIds.length;i++){
            Menu menu = new Menu();
            menu.setResId(resIds[i]);
            menu.setName(names[i]);
            menus.add(menu);
        }
        adapter = new CollectionAdapter(getActivity(),menus);
        collectionRecycler.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        collectionRecycler.setAdapter(adapter);
        collectionRecycler.addItemDecoration(new DividerGridItemDecoration(getActivity()) );

        adapter.setOnItemClickLitener(new CollectionAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0://采集
                        startActivity(new Intent(getActivity(), DeviceListActivity.class));
                        break;
                    case 1://历史记录
                        startActivity(new Intent(getActivity(), HistoryActivity.class));
                        break;
                    case 2:
                        Intent intent = new Intent(getActivity(), MedicineActivity.class);
                        intent.putExtra("title",names[position]);
                        intent.putExtra("type", MedicineMenu.TYPE_DESEASE);
                        startActivity(intent);
                        break;
                    case 3:
                        Intent intent1 = new Intent(getActivity(), MedicineActivity.class);
                        intent1.putExtra("title",names[position]);
                        intent1.putExtra("type", MedicineMenu.TYPE_DRUG);
                        startActivity(intent1);
                        break;
                    case 4:
                        Intent intent2 = new Intent(getActivity(), MedicineActivity.class);
                        intent2.putExtra("title",names[position]);
                        intent2.putExtra("type", MedicineMenu.TYPE_INSPECTION);
                        startActivity(intent2);
                        break;
                    default:
                        Toast.makeText(getActivity(),names[position],Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @SuppressLint("NewApi")
    private void initialize() {

        views = new ArrayList<>();
        infos = new ArrayList<>();

        for(int i = 0; i < imageUrls.length; i ++){
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("图片-->" + i );
            infos.add(info);
        }

        //如果多于一张轮播图， 将最后一个ImageView添加进来
        if(infos.size()>1){
            ImageView img = new ImageView(getActivity());
            Glide.with(this).load(infos.get(infos.size() - 1).getUrl()).centerCrop().error(R.mipmap.icon_error).placeholder(R.mipmap.login_head).into(img);
            views.add(img);
        }

        for (int i = 0; i < infos.size(); i++) {
            ImageView img = new ImageView(getActivity());
            Glide.with(this).load(infos.get(i).getUrl()).centerCrop().error(R.mipmap.icon_error).placeholder(R.mipmap.login_head).into(img);
            views.add(img);
        }
        // 如果多于一张轮播图，将第一个ImageView添加进来
        if(infos.size()>1){
            ImageView img = new ImageView(getActivity());
            Glide.with(this).load(infos.get(0).getUrl()).centerCrop().error(R.mipmap.icon_error).placeholder(R.mipmap.login_head).into(img);
            views.add(img);
            // 设置循环，在调用setData方法前调用
            cycleViewPager.setCycle(true);
            //设置轮播
            cycleViewPager.setWheel(true);
        }

        // 在加载数据前设置是否循环
        Log.i("main", "VIEW:"+views.size());
        cycleViewPager.setData(views, infos, mAdCycleViewListener);

        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
                Toast.makeText(getActivity(),
                        "position-->" + info.getContent(), Toast.LENGTH_SHORT)
                        .show();
            }

        }

    };


}
