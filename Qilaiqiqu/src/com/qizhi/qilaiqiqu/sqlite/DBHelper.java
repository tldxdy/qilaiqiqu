package com.qizhi.qilaiqiqu.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
		//库名
		private static final String DATABASE_NAME = "travel_notes.db";  
		//版本
	    private static final int DATABASE_VERSION = 1;
	    
	    public static final String TABLE_NAME1 = "travel_tbl";
	    
	    public static final String TABLE_NAME2 = "char_tbl";
	    
	    
	    private static final String SQL_CREATE_TABLE1 = "create table if not exists " + TABLE_NAME1 
	             + " ( _id integer primary key autoincrement, "
				 + "   json_string varchar(60000) "
				 + "  )";
	    
	    private static final String SQL_CREATE_TABLE2 = "create table if not exists " + TABLE_NAME2 
	             + " ( _id integer primary key autoincrement, "
	             + "   json_name varchar(100),"
				 + "   json_string varchar(60000) "
				 + "  )";
	    

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(SQL_CREATE_TABLE1);
	    db.execSQL(SQL_CREATE_TABLE2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
