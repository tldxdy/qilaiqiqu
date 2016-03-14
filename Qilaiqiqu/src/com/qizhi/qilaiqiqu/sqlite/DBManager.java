package com.qizhi.qilaiqiqu.sqlite;


import java.util.ArrayList;
import java.util.List;

import com.qizhi.qilaiqiqu.model.RidingDraftModel;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	 private DBHelper helper;  
	    private SQLiteDatabase db;  
		
		private static final String TABLE_NAME = "travel_tbl";
		
		//private static final String SQL_DROP_TABLE = "drop table if exists " + TABLE_NAME;

		private static final String SQL_INSERT = "insert into " + TABLE_NAME + " values (NULL, ?)";
		
		private static final String SQL_QUERY = "select * from " + TABLE_NAME;
		
		private static final String SQL_UPDATE = "update " + TABLE_NAME + " set _id = ?, json_string = ?  where _id = ?";
	    
		private static final String SQL_DELETE = "delete from " + TABLE_NAME +" where _id = ?";
	      
	    public DBManager(Context context) {  
	        helper = new DBHelper(context);  
	        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
	        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
	        db = helper.getWritableDatabase();  
	    }
	 public boolean add(RidingDraftModel ridingDraftModel){
		 db.execSQL(SQL_INSERT,new String[]{ridingDraftModel.getJsonString()});
		return true;  
	 }
	 /*public TravelsinformationModel query(TravelsinformationModel praise){
		 Cursor c =db.rawQuery("select * from praise_tbl where userID=? and postID=?", new String[]{praise.getUserID()+"",praise.getPostID()+""});
		 while(c.moveToNext()){
			 int userID=c.getInt(c.getColumnIndex("userID"));
			 int postID=c.getInt(c.getColumnIndex("postID"));
			 int isPraise=c.getInt(c.getColumnIndex("isPraise"));
			 TravelsinformationModel reModel1=new TravelsinformationModel();
			 return reModel1;
		 }
		return null;
	 }*/
	 public List<RidingDraftModel> queryAll(){
		 List<RidingDraftModel> list = new ArrayList<RidingDraftModel>();
		 Cursor cursor = db.rawQuery(SQL_QUERY,null);
		 while(cursor.moveToNext()){
			 int _id=cursor.getInt(cursor.getColumnIndex("_id"));
			 String jsonString=cursor.getString(cursor.getColumnIndex("json_string"));
			 RidingDraftModel reModel1=new RidingDraftModel(_id, jsonString);
			 list.add(reModel1);
		 }
		return list;
		 
	 }
	 
	 public boolean update(RidingDraftModel ridingDraftModel){
		 db.execSQL(SQL_UPDATE, new String[]{ridingDraftModel.get_id()+"",ridingDraftModel.getJsonString() ,ridingDraftModel.get_id() +"" });
		return true;
	 }
	 
	 public boolean delete(RidingDraftModel ridingDraftModel){
		 db.execSQL(SQL_DELETE, new String[]{ridingDraftModel.get_id()+""});
		return true;
	 }
	 
}
