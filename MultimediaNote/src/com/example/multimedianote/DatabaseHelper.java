package com.example.multimedianote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper  extends SQLiteOpenHelper{

	private String tableName = "notes_1";
	private String sql = "create table if not exists " + tableName +
			"(_id integer primary key autoincrement, " +
			"title varchar," +
			"content text," +
			"imageNum text," +
			"imageIndex text," +
			"imagePath text," +
			"audioPath text,"+
			"videoPath text,"+
			"time varchar)";
	
	
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
