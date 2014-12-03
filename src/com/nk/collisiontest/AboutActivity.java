package com.nk.collisiontest;

import com.example.collisiontest.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity{
	Typeface font;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		font = Typeface.createFromAsset(getAssets(), "fonts/disposable_droid.ttf");
		setTitleFont();
	}
	
	void setTitleFont(){
		TextView tv = (TextView) findViewById(R.id.about_label);
		tv.setTypeface(font);
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(AboutActivity.this, MainActivity.class);
		startActivity(i);
	}
}
