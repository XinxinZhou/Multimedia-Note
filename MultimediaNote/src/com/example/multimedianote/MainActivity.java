package com.example.multimedianote;



import java.util.ArrayList;




import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends ListActivity implements OnScrollListener{
	public static final int ADD_STATE = 0;
	public static final int EDIT_STATE = 1;
	public static final int CHECK_STATE = 2;
	private static final int DELETE_ID = 0;
	public Button addNoteButton,exitButton;
	public ListView noteListView;
	public Cursor cursor;
	DatabaseManage dm=null;
	ListViewAdapter adapter=null;
	
	private int id = -1;//被点击的条目
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);   
        
        addNoteButton=(Button)this.findViewById(R.id.addRecordButton);
        exitButton=(Button)this.findViewById(R.id.exitButton);
        dm=new DatabaseManage(this);
        
        addNoteButton.setOnClickListener(new AddNoteListener());
        
        noteListView=getListView(); 
       
        initAdapter();//初始化
        Log.v("MainAvtivity","initAdapter");
        
        
        
        exitButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
        	
        });
      
        registerForContextMenu(MainActivity.this.getListView());
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {	        	
        	MainActivity.this.finish();
        }
			return false;	        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public class AddNoteListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("state", ADD_STATE);
			intent.setClass(MainActivity.this,NoteEditActivity.class);
			MainActivity.this.startActivity(intent);
		}
		
	}
    
    //Initial data , to show the notesList On MainPage.
   	public void initAdapter(){
       	
        Log.v("MainAvtivity","Before Database Open");
       	dm.open();//打开数据库操作对象
        Log.v("MainAvtivity","Database Open");
       	
       	cursor = dm.selectAll();//获取所有数据
        Log.v("MainAvtivity","Database selectAll");
        
       	cursor.moveToFirst();//将游标移动到第一条数据，使用前必须调用
       	
       	int count = cursor.getCount();//个数
       	
       	Log.v("MainAvtivity","Database return count");
       	
       	ArrayList<String> items = new ArrayList<String>();
       	ArrayList<String> times = new ArrayList<String>();
       	for(int i= 0; i < count; i++){
       		items.add(cursor.getString(cursor.getColumnIndex("title")));
       		Log.v("Maintitle",cursor.getString(cursor.getColumnIndex("title")).toString());
       		times.add(cursor.getString(cursor.getColumnIndex("time")));
       		cursor.moveToNext();//将游标指向下一个
       	}    	    	
       	
       	dm.close();//关闭数据操作对象
       	adapter = new ListViewAdapter(this,items,times);//创建数据源
       	noteListView.setAdapter(adapter);//自动为id为list的ListView设置适配器
        Log.v("MainAvtivity","setAdapter");
   	}  	
  	
  //click notelistItem,turn into NoteCheckActivity
  	protected void onListItemClick(ListView l, View v, int position, long id) {
  		// TODO Auto-generated method stub
  		super.onListItemClick(l, v, position, id);
  		
  		cursor.moveToPosition(position);
  		
  		Intent intent = new Intent();
  		
  		intent.putExtra("state", ADD_STATE);
  		intent.putExtra("id", cursor.getString(cursor.getColumnIndex("_id")));
  		intent.putExtra("title", cursor.getString(cursor.getColumnIndex("title")));
  		intent.putExtra("time", cursor.getString(cursor.getColumnIndex("time")));
  		intent.putExtra("content", cursor.getString(cursor.getColumnIndex("content")));
  		intent.putExtra("imageNum", cursor.getString(cursor.getColumnIndex("imageNum")));
  		intent.putExtra("imageIndex", cursor.getString(cursor.getColumnIndex("imageIndex")));
  		intent.putExtra("imagePath", cursor.getString(cursor.getColumnIndex("imagePath")));
  		intent.putExtra("audioPath", cursor.getString(cursor.getColumnIndex("audioPath")));
  		intent.putExtra("videoPath", cursor.getString(cursor.getColumnIndex("videoPath")));
  		dm.close();
  		
  		intent.setClass(MainActivity.this, NoteCheckActivity.class);
  		MainActivity.this.startActivity(intent);
  		
  	}
  	
  	 public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenuInfo menuInfo) {
         menu.add(Menu.NONE,DELETE_ID,Menu.NONE,"Delete");
         super.onCreateContextMenu(menu, v, menuInfo);
         final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;       
     }
  	
     public boolean onContextItemSelected(MenuItem item) {
         switch(item.getItemId()){
         case DELETE_ID:
        	dm.open();
         	AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
         	cursor.moveToPosition(menuInfo.position);
         	Log.v("cursor",cursor.getString(cursor.getColumnIndex("_id")));
         	dm.delete(cursor.getLong(cursor.getColumnIndex("_id")));
         	dm.close();
         	initAdapter();
         }
		return false;
     }
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
    
}
