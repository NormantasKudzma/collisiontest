package com.nk.bloxmania;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity{
	public static final int BIG_FONT_SIZE = 72;
	
	ScrollBackgroundView sbv;
	Typeface font;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		font = Typeface.createFromAsset(getAssets(), "fonts/disposable_droid.ttf");
		setTitleFont();
		customizeBackground();
		TextView tv = (TextView)findViewById(R.id.version);
		tv.setTypeface(font);
	}
	
	void setTitleFont(){
		TextView tv = (TextView) findViewById(R.id.about_label);
		tv.setTextSize(BIG_FONT_SIZE);
		tv.setTypeface(font);
	}
	
	void customizeBackground(){
		sbv = (ScrollBackgroundView)findViewById(R.id.menu_scroll_background);
		sbv.setScrollSpeedX(0.2f);
		sbv.setScrollSpeedY(0.3f);
	}
	
	@Override
	public void onBackPressed() {
		sbv.isDone(true);
		finish();
	}
}
