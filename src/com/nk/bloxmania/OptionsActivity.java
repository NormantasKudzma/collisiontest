package com.nk.bloxmania;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class OptionsActivity extends MainActivity{
	private final int MAX_VOL = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_layout);
		setTitleFont();
		loadSettings();
		initListeners();
	}
	
	void setTitleFont(){
		TextView tv = (TextView) findViewById(R.id.options_title);
		tv.setTextSize(BIG_SIZE);
		tv.setTypeface(font);
	}
	
	@Override
	public void onBackPressed() {
		saveSettings();
		startCustomActivity(MainActivity.class);
	}

	protected void initListeners(){
		SeekBar volume = (SeekBar)findViewById(R.id.volume_slider);
		volume.setMax(MAX_VOL);
		volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar s) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar s) {
			}
			
			@Override
			public void onProgressChanged(SeekBar s, int val, boolean user) {
				float vol = 1.0f * val / MAX_VOL;
				CustomActivity.musicManager.setVolume(vol);
				updateValue("volume", vol + "");
			}
		});
		volume.setProgress((int)(CustomActivity.musicManager.getVolume() * MAX_VOL));
	}
}
