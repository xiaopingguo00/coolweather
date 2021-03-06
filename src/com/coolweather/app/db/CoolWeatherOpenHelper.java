package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
	public static final String CREATE_PROVINCE="create table province("
			+"id integer primary key autoincrement,"
			+"province_name text," +
			"province_code text)";
	
	public static final String CREATE_CITY="create table city(" +
			"id integer primary key autoincrement," +
			"city_name tex," +
			"city_code text," +
			"province_id integer)";
	
	public static final String CREATE_COUNTY="create table county(" +
			"id integer primary key autoincrement," +
			"county_name text," +
			"county_code," +
			"city_id integer)";
	
	public CoolWeatherOpenHelper(Context context,String name,CursorFactory factory,int version){
		super(context,name,factory,version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion) {
		db.execSQL("drop table if exixts province");
		db.execSQL("drop table if exixts city");
		db.execSQL("drop table if exixts county");
		onCreate(db);
	}

}
