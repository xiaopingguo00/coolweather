package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * sendHttpRequest(address,HttpCallbackListener a){}
 * ����������ݲ���address��ȡ��Զ�̷����������ݲ����ûص����������ݴ浽���ݿ⣬��ȡ������ʾ
 * @author Administrator
 */
public class HttpUtil {
public static void sendHttpRequest(final String address,final HttpCallbackListener listener){//������Ĳ���Ϊʲôһ��Ҫfinal
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
