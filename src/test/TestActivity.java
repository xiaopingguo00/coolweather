package test;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherOpenHelper;

public class TestActivity extends Activity {

	private CoolWeatherOpenHelper helper;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_del);
		helper=new CoolWeatherOpenHelper(this, "cool_weather", null, 1);
		Button del=(Button)findViewById(R.id.del);
		del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SQLiteDatabase db=helper.getWritableDatabase();
				db.delete("city", "id>=1", new String[]{"1"});
			}
		});
		
	}
}
