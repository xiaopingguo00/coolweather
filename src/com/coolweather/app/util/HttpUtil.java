package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * sendHttpRequest(address,HttpCallbackListener a){}
 * 这个方法根据参数address来取得远程服务器的数据并调用回调方法将数据存到数据库，再取出来显示
 * @author Administrator
 */
public class HttpUtil {
public static void sendHttpRequest(final String address,final HttpCallbackListener listener){//这里面的参数为什么一定要final
	new Thread(new Runnable(){

		@Override
		public void run() {
			HttpURLConnection conn=null;
			try {
				URL url=new URL(address);
				conn=(HttpURLConnection)url.openConnection();
				conn.setRequestMethod("GET");
				conn.setReadTimeout(8000);
				conn.setConnectTimeout(8000);
				InputStream in=conn.getInputStream();
				BufferedReader reader=new BufferedReader(new InputStreamReader(in));
				StringBuilder sb=new StringBuilder();
				String line="";
				while((line=reader.readLine())!=null){
					sb.append(line);
				}
				if(listener != null){
					listener.onFinsh(sb.toString());
				}
				
			}  catch (Exception e) {
				
				if(listener!=null){
					listener.onError(e);
				}
				e.printStackTrace();
			}finally{
				if(conn != null){
					conn.disconnect();
				}
			}
		}
	}).start();
	
}
}
