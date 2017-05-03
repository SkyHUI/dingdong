package com.sky.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sky.bean.Menu;
import com.sky.heartbeat.R;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder>
{

	private List<Menu> mDatas;
	private LayoutInflater mInflater;
	private Context context;

	public interface OnItemClickLitener
	{
		void onItemClick(View view, int position);
		void onItemLongClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}


	public CollectionAdapter(Context context, List<Menu> datas)
	{
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(
				R.layout.item_collection, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position)
	{
		Menu menu = mDatas.get(position);
		Drawable top = context.getResources().getDrawable(menu.getResId());
		holder.tv.setText(menu.getName());
		holder.tv.setCompoundDrawablesWithIntrinsicBounds(null,top,null,null);

		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null)
		{
			holder.itemView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemClick(holder.itemView, pos);
				}
			});
			
			holder.itemView.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
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

		TextView tv;

		public MyViewHolder(View view)
		{
			super(view);
			tv = (TextView) view.findViewById(R.id.item_collection_tv);
		
		
		}
	}
}