package com.sky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.bean.News;
import com.sky.heartbeat.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>
{

	private List<News> mDatas;
	private Context context;

	public interface OnItemClickLitener
	{
		void onItemClick(View view, News news);
	}

	public interface OnItemLongClickListener{
		void onItemLongClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	private OnItemLongClickListener mOnItemLongClickListener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public void setOnItemLongClickLitener(OnItemLongClickListener mOnItemLongClickListener)
	{
		this.mOnItemLongClickListener = mOnItemLongClickListener;
	}


	public NewsAdapter(Context context, List<News> datas)
	{
		this.context = context;
		mDatas = datas;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(
				R.layout.item_news, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position)
	{
		final News news = mDatas.get(position);
		holder.tv_title.setText(news.getTitle());
		Glide.with(context).load(news.getPicture_url()).fitCenter().placeholder(R.mipmap.login_head).into(holder.img);
		holder.tv_date.setText(news.getDate());

		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null)
		{
			holder.itemView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemClick(holder.itemView, news);
				}
			});
			
			holder.itemView.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					int pos = holder.getLayoutPosition();
					mOnItemLongClickListener.onItemLongClick(holder.itemView, pos);
					return false;
				}
			});
		}
	}

	@Override
	public int getItemCount()
	{
		return mDatas.size();
	}


	class MyViewHolder extends ViewHolder
	{

		TextView tv_title;
		ImageView img;
		TextView tv_date;

		public MyViewHolder(View view)
		{
			super(view);
			tv_title = (TextView) view.findViewById(R.id.message_tv_title);
			img = (ImageView) view.findViewById(R.id.message_img);
			tv_date = (TextView) view.findViewById(R.id.message_tv_time);
		
		
		}
	}
}