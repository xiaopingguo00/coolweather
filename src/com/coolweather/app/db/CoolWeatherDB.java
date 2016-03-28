package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class CoolWeatherDB {
//DB_NAME,VERSION, coolWeatherDB,SQLiteDataBase db
//CoolWeatherDB(),将构造方法私有化
	//getInstance();
	//saveProvince();
	//loadProvinces();
	//saveCity();
	//loadCities()
	//saveCounty()
	//loaddCounties()
	/**
	 * 这是数据库的名字
	 */
	public static final String BD_NAME="cool_weather";
	/*
	 * 数据库的版本
	 * 
	 */
	public static final int VERSION=1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	public CoolWeatherDB(Context context){//构造方法主要是进行为db赋值
		CoolWeatherOpenHelper helper=new CoolWeatherOpenHelper(context, BD_NAME, null, 1);
		db=helper.getWritableDatabase();
	}
	
	public CoolWeatherDB getInstance(Context context){//作用是返回一个单例
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/**
	 * 这个方法是将参数中的参数存入数据库
	 * @param province
	 */
	public void saveProvince(Province province){
		if(province!=null){
		ContentValues values=new ContentValues();
		values.put("province_name", province.getProvinceName());
		values.put("province_code", province.getProvinceCode());
		db.insert("province", null, values);
		}
	}
	
	/**
	 * 将数据从数据库取出来然后放到list中取出来
	 */
	public List<Province> loadProvinces(){
		List<Province>provinces=new ArrayList<Province>();
		Cursor cursor=db.query("province", null, null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				provinces.add(province);
			}
		}
		if(cursor != null){
			cursor.close();
		}
		return provinces;
	}
	
	//将city实例存储到数据库
	public void saveCity(City city){
		ContentValues values=new ContentValues();
		values.put("city_name", city.getCityName());
		values.put("city_code", city.getCityCode());
		db.insert("city", null, values);
	}
	
	//取出数据,返回List<City>
	public List<City> loadCity(int provinceId){
		List<City>cities=new ArrayList<City>();
		Cursor cursor=db.query("city", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				cities.add(city);
			}
		}
		if(cursor!=null){
			cursor.close();
		}
		return cities;
	}
	
	public void saveCounty(County county){
		ContentValues values =new ContentValues();
		values.put("city_name", county.getCountyName());
		values.put("county_code", county.getCountyCode());
		db.insert("county", null, values);
	}
	
	public List<County> loadCounties(int cityId){
		List<County>counties=new ArrayList<County>();
		Cursor cursor=db.query("county", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				County county=new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				counties.add(county);
			}
		}
		if(cursor!=null){
			cursor.close();
		}
		return counties;
	}
	
	
	
	
	
}
