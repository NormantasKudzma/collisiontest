package com.nk.bloxmania;

import com.example.collisiontest.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity{
	public static final int BIG_FONT_SIZE = 72;
	
	Typeface font;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		font = Typeface.createFromAsset(getAssets(), "fonts/disposable_droid.ttf");
		setTitleFont();
		customizeBackground();
	}
	
	void setTitleFont(){
		TextView tv = (TextView) findViewById(R.id.about_label);
		tv.setTextSize(BIG_FONT_SIZE);
		tv.setTypeface(font);
	}
	
	void customizeBackground(){
		ScrollBackgroundView sbv = (ScrollBackgroundView)findViewById(R.id.menu_scroll_background);
		sbv.setScrollSpeedX(0.2f);
		sbv.setScrollSpeedY(0.3f);
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(AboutActivity.this, MainActivity.class);
		startActivity(i);
	}
}
