package com.coolweather.app.db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.coolweather.app.R;

public class DelActivity extends Activity{

	CoolWeatherOpenHelper help=new CoolWeatherOpenHelper(this,"cool_weather",null,2);
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);
		SQLiteDatabase db=help.getWritableDatabase();
		
	}
}
