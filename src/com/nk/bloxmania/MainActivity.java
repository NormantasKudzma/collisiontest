package com.nk.bloxmania;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends CustomActivity{	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		setUpInterface();
		scrollBackgroundResource = R.id.menu_scroll_background;
		customizeBackground(0.5f, 0.5f);
		LevelManager.countLevels(this);
		LevelManager.countBackgrounds(this);
	}
	
	void setUpInterface(){
		TextView tv = (TextView)findViewById(R.id.version);
		tv.setTypeface(font);
		
		Button b1 = (Button)findViewById(R.id.button_play);
		b1.setTypeface(font);
		b1.setTextSize(SMALL_SIZE);
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startCustomActivity(LevelSelectionActivity.class);
			}
		});
		
		Button b2 = (Button)findViewById(R.id.button_options);
		b2.setTypeface(font);
		b2.setTextSize(SMALL_SIZE);
		b2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// CALL OPTIONS
			}
		});
		
		Button b3 = (Button)findViewById(R.id.button_about);
		b3.setTypeface(font);
		b3.setTextSize(SMALL_SIZE);
		b3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startCustomActivity(AboutActivity.class);
			}
		});
	}
}
