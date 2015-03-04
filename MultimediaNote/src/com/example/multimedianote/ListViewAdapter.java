package com.example.multimedianote;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	
	
	private List<String> listItems=null;
	private List<String> listItemTimes=null;
	//private HashMap<String,String> listItems;
	
	private LayoutInflater inflater;
	public ListViewAdapter(Context context, List<String> listItemsT, List<String> timesT){
		this.listItems = listItemsT;
		this.listItemTimes = timesT;
		inflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	
	/**
	 * 往列表添加条目
	 * @param item
	 */
	public void addListItem(String item, String time){
		listItems.add(item);
		listItemTimes.add(time);
		
	}
	
	/**
	 * 删除指定位置的数据
	 * @param position
	 */
	public void removeListItem(int position){
		listItems.remove(position);
		listItemTimes.remove(position);
	}

	
	/**
	 * 获取列表的数量
	 */
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	/**
	 * 根据索引获取列表对应索引的内容
	 */
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 通过该函数显示数据
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub	
		if(convertView == null){
			convertView = inflater.inflate(R.layout.notelistitem ,null);// Initial noteListItemView, added into MainView.
		}	
		TextView text = (TextView)convertView.findViewById(R.id.listItem);
		Log.v("LVA","textCreate");
		text.setText(this.listItems.get(position));
		//text.setText("titleHello");
		Log.v("LVA","listItems create");
		TextView time = (TextView)convertView.findViewById(R.id.listItemTime);
		String datetime = DateFormat.format("yyyy-MM-dd kk:mm:ss", 
				Long.parseLong(this.listItemTimes.get(position))).toString();
		time.setText(datetime);
		//time.setText("timeHello");
		
		return convertView;
	}

	
}
