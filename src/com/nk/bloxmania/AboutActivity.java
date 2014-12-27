package com.nk.bloxmania;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends CustomActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		setTitleFont();
		scrollBackgroundResource = R.id.menu_scroll_background;
		customizeBackground(0.2f, 0.3f);
		TextView tv = (TextView)findViewById(R.id.version);
		tv.setTypeface(font);
	}
	
	void setTitleFont(){
		TextView tv = (TextView) findViewById(R.id.about_label);
		tv.setTextSize(BIG_SIZE);
		tv.setTypeface(font);
	}
	
	@Override
	public void onBackPressed() {
		startCustomActivity(MainActivity.class);
	}
}
