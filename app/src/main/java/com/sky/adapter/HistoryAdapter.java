package com.sky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sky.bean.MedicineMenu;
import com.sky.heartbeat.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>
{

	private List<MedicineMenu> mDatas;
	private LayoutInflater mInflater;

	public interface OnItemClickLitener
	{
		void onItemClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
	

	public HistoryAdapter(Context context, List<MedicineMenu> datas)
	{
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(
				R.layout.item_history, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position)
	{
		holder.tv.setText(mDatas.get(position).getName());

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
			tv = (TextView) view.findViewById(R.id.history_name);
		
		
		}
	}
}