package com.sky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.bean.Menu;
import com.sky.heartbeat.R;

import java.util.ArrayList;
import java.util.List;

public class MineAdapter extends RecyclerView.Adapter<MineAdapter.MyViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_NORMAL = 1;

    private List<Menu> mDatas;
    private LayoutInflater mInflater;
    private View headView;

    public View getHeadView() {
        return headView;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
        notifyItemChanged(0);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void addDatas(ArrayList<Menu> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public MineAdapter(Context context, List<Menu> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        if(headView == null){
            return TYPE_NORMAL;
        }else if(position == 0){
            return TYPE_HEADER;
        }else{
            return TYPE_NORMAL;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(headView != null && viewType == TYPE_HEADER){
            return new MyViewHolder(headView);
        }else{
            MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                    R.layout.item_mine, parent, false));
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,int position) {
        if(getItemViewType(position) == TYPE_HEADER){
            return;
        }
        final int pos = headView == null ? position : position - 1;
        Menu menu = mDatas.get(pos);
        holder.img.setBackgroundResource(menu.getResId());
        holder.tv.setText(menu.getName());

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        int size = mDatas.size();
        return headView == null ? size : size + 1;
    }

    class MyViewHolder extends ViewHolder {

        ImageView img;
        TextView tv;


        public MyViewHolder(View view) {
            super(view);
            if(itemView == headView){
                return;
            }
            tv = (TextView) view.findViewById(R.id.item_mine_tv);
            img = (ImageView) view.findViewById(R.id.item_mine_img);

        }
    }

}