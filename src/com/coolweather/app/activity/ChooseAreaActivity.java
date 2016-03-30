package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

public class ChooseAreaActivity extends Activity{

	public static final int PROVINCE_LEVEL=0;
	public static final int CITY_LEVEL=1;
	public static final int COUNTY_LEVEL=2;
	
	private ProgressDialog progressDialog;
	private TextView textview;
	private ListView listview;
	ArrayAdapter<String> adapter;
	List<String> dataList=new ArrayList<String>();
	CoolWeatherDB db;
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	
	private Province selectedProvince;
	private City selectedCity;
	private County selectedCounty;
	
	private int currentLevel;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		textview=(TextView)findViewById(R.id.title_text);
		listview =(ListView)findViewById(R.id.list_view);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		listview.setAdapter(adapter);
	    db=new CoolWeatherDB(this);
	    listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				if(currentLevel==PROVINCE_LEVEL){
					selectedProvince = provinceList.get(index);
					queryCities();
				}else if(currentLevel==CITY_LEVEL){
					selectedCity=cityList.get(index);
					queryCounties();
				}
			}
		});
	    	queryProvinces();	
	}
	
	public void queryProvinces(){
		provinceList=db.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province p:provinceList){
				dataList.add(p.getProvinceCode());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
		    textview.setText("中国");
		    currentLevel=PROVINCE_LEVEL;
			
		}else{
			queryFromServer(null,"province");
		}
	}
	
	
	public void queryCities(){
		
		cityList=db.loadCity(selectedProvince.getId());
		if(cityList.size()>0){
			dataList.clear();
			for(City c:cityList){
				dataList.add(c.getCityName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			textview.setText(selectedProvince.getProvinceCode());
			currentLevel=CITY_LEVEL;
		}else{
			queryFromServer(selectedProvince.getProvinceName(),"city");
		}	
	}
	
	public void queryCounties(){
		countyList=db.loadCounties(selectedCity.getId());
		if(countyList.size()>0){
		dataList.clear();
		for(County c:countyList){
			dataList.add(c.getCountyName());
		}
		adapter.notifyDataSetChanged();
		listview.setSelection(0);
		textview.setText(selectedCity.getCityName());
		currentLevel=COUNTY_LEVEL;
		}else{
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}
		
	/*
	 * 根据传入的代号和类型从服务器查询省市县数据
	 */
    public void queryFromServer(final String code,final String type){
    	String address;
    	if(! TextUtils.isEmpty(code)){
    		address="http://www.weather.com.cn/data/list3/city"+code+".xml";
    	}else{
    		address="http://www.weather.com.cn/data/list3/city.xml";
    	}
    	showProgressDialog();
    	HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){
			@Override
			public void onFinsh(String response) {
				boolean result=false;
				if("province".equals(type)){
					result=Utility.handleProvinceResponse(db, response);
				}else if("city".equals(type)){
					result=Utility.handleCityResponse(db, response, selectedProvince.getId());
				}else if("county".equals(type)){
					result=Utility.handleCountyResponse(db, response, selectedCity.getId());
				}
				if(result){
					runOnUiThread(new Runnable(){
						@Override
						public void run() {
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("county".equals(type)){
								queryCounties();
							}
						}
						});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
					}});
			}});
    	
    }
	
    /*
     * 显示进度对话框
     */
	public void showProgressDialog(){
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在加载..");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	/*
	 * 关闭进度对话框
	 */
	public void closeProgressDialog(){
		if(progressDialog !=null){
			progressDialog.dismiss();
		}
	}
	
	/*
	 * 捕获back键，根据当前级别来判断返回省市县级别还是直接退出
	 */
	@Override
	public void onBackPressed(){
		if(currentLevel == COUNTY_LEVEL){
			queryCities();
		}else if(currentLevel == CITY_LEVEL){
			queryProvinces();
		}else{
			finish();
		}
	}
	
}
