package com.nk.bloxmania;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends CustomActivity{	
	protected MusicManager musicManager = null;
	protected MusicServiceConnection msc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		setUpInterface();
		scrollBackgroundResource = R.id.menu_scroll_background;
		customizeBackground(0.5f, 0.5f);
		LevelManager.countLevels(this);
		LevelManager.countBackgrounds(this);
		
		Intent i = new Intent(this, MusicManager.class);
		if (startService(i) != null){
			if (msc == null){
				msc = new MusicServiceConnection();
				bindService(i, msc, Context.BIND_AUTO_CREATE);
				musicManager = msc.getService();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		if (msc != null){
			unbindService(msc);
		}
		if (musicManager != null){
			musicManager.pause();
			musicManager = null;
		}
		super.onDestroy();
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
