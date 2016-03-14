package com.qizhi.qilaiqiqu.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
		//库名
		private static final String DATABASE_NAME = "travel_notes.db";  
		//版本
	    private static final int DATABASE_VERSION = 1;
	    
	    public static final String TABLE_NAME = "travel_tbl";
	    
	    
	    private static final String SQL_CREATE_TABLE = "create table if not exists " + TABLE_NAME 
	             + " ( _id integer primary key autoincrement, "
				 + "   json_string varchar(60000) "
				 + "  )";
	    

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
