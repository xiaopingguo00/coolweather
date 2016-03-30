package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
/*
 * 解析省，市，县的数据（从服务器取来的数据）,应该是在HttpCallbackListener实现类中被调用
 */

public class Utility {

	/*
	 * 解析从服务器取得的省级数据,先将数据解析为Province类再通过coolWeatherDB的save***方法存到数据库
	 */
	public synchronized static  boolean handleProvinceResponse(CoolWeatherDB db,String response){
		if(!TextUtils.isEmpty(response)){
			String[] provinces=response.split(",");
			if(provinces != null && provinces.length>0){
			for(String data :provinces){
				String[] detail=data.split("\\|");
				Province p=new Province();
				p.setProvinceName(detail[1]);
				p.setProvinceCode(detail[0]);
				db.saveProvince(p);
			}
			return true;
			}
		}
		return false;
	}
	
	/*
	 * 处理市级数据
	 */
	public synchronized static boolean handleCityResponse(CoolWeatherDB db,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] cities=response.split(",");
			if(cities!=null&&cities.length>0){
			for(String data:cities){
				String[] detail=data.split("\\|");
				City c=new City();
				c.setCityName(detail[1]);
				c.setCityCode(detail[0]);
				c.setProvinceId(provinceId);
				db.saveCity(c);
			}
			return true;
			}
			
		}
		return false;
	}
	
	/*
	 * 处理县级市数据
	 */
	public synchronized static boolean handleCountyResponse(CoolWeatherDB db,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] counties=response.split(",");
			if(counties != null && counties.length>0){
			for(String data:counties){
				String[] detail=data.split("\\|");
				County county=new County();
				county.setCountyName(detail[1]);
				county.setCountyCode(detail[0]);
				county.setCityId(cityId);
				db.saveCounty(county);
			}
			return true;
			}
		}
		return false;
	}
	
}
