package com.example.multimedianote;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;



public class DatabaseManage {
	
	private Context mContext = null;
	private SQLiteDatabase mSQLiteDatabase = null;// SQLiteDatabase Object, for recording operation
	private DatabaseHelper dh = null;// to create database
	private String dbName = "note_1.db";
	private int dbVersion = 1;
	
	public DatabaseManage(Context context){
		mContext = context;
	}
	
	/**
	 * Open Database
	 */
	public void open(){
		
		try{
			dh = new DatabaseHelper(mContext, dbName, null, dbVersion);
			if(dh == null){
				Log.v("msg", "is null");
				return ;
			}
			mSQLiteDatabase = dh.getWritableDatabase();
	
		}catch(SQLiteException se){
			se.printStackTrace();
		}
	}
	
	/**
	 * close database
	 */
	public void close(){
		mSQLiteDatabase.close();
		dh.close();	
	}
	
	/**
	 * get all records
	 */
		public Cursor selectAll(){
			Cursor cursor = null;
			try{
				String sql = "select * from notes_1";
				cursor = mSQLiteDatabase.rawQuery(sql, null);
			}catch(Exception ex){
				ex.printStackTrace();
				Log.v("DatabaseManage","Error in selectAll");
				cursor = null;
			}
			return cursor;
		}
		
		/**
		 * get record by id
		 */
		public Cursor selectById(int id){
			
			//String result[] = {};
			Cursor cursor = null;
			try{
				String sql = "select * from notes_1 where _id='" + id +"'";
				cursor = mSQLiteDatabase.rawQuery(sql, null);
			}catch(Exception ex){
				ex.printStackTrace();
				cursor = null;
			}
			
			return cursor;
		}
		
		
		//插入数据
		public long insert(String title, String content,String imageNum, String imageIndex, String imagePath, String audioPath, String videoPath){
			
			long datetime = System.currentTimeMillis();
			long l = -1;
			try{
				ContentValues cv = new ContentValues();
				cv.put("title", title);
				cv.put("content", content);
				cv.put("imageNum", imageNum);
				cv.put("imageIndex", imageIndex);
				cv.put("imagePath", imagePath);
				cv.put("time", datetime);
				cv.put("audioPath", audioPath);
				cv.put("videoPath", videoPath);
				l = mSQLiteDatabase.insert("notes_1", null, cv);
	
			}catch(Exception ex){
				ex.printStackTrace();
				l = -1;
			}
			return l;
			
		}
		
		//删除数据
		public int delete(long id){
			int affect = 0;
			try{
				affect = mSQLiteDatabase.delete("notes_1", "_id=?", new String[]{id+""});
			}catch(Exception ex){
				ex.printStackTrace();
				affect = -1;
			}
			
			return affect;
		}
		

		//修改数据
		public int update(int id, String title, String content,String imageNum, String imageIndex, String imagePath, String audioPath, String videoPath){
			int affect = 0;
			long datetime = System.currentTimeMillis();		
			try{
				ContentValues cv = new ContentValues();
				
				cv.put("title", title);
				cv.put("content", content);
				cv.put("imageNum", imageNum);
				cv.put("imageIndex", imageIndex);
				cv.put("imagePath", imagePath);
				cv.put("time", datetime);
				cv.put("audioPath", audioPath);
				cv.put("videoPath", videoPath);
				String w[] = {id+""};
				affect = mSQLiteDatabase.update("notes_1", cv, "_id=?", w);
			}catch(Exception ex){
				ex.printStackTrace();
				affect = -1;
			}
			return affect;
		}
		
}
