package com.nk.bloxmania;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
	
		RadioGroup rg = (RadioGroup)findViewById(R.id.color_chooser_buttons);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {		
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int newColor = Color.MAGENTA;
				if (checkedId != -1){
					RadioButton rb = (RadioButton)findViewById(checkedId);
					ColorDrawable d = (ColorDrawable)rb.getBackground();
					newColor = d.getColor();
				}
				GameEngine.setPlayerColor(newColor);
				updateValue("plrcolor", newColor + "");
			}
		});
		View bv = rg.findViewWithTag("" + GameEngine.getPlayerColor());
		if (bv != null){
			rg.check(bv.getId());
		}
	}
}
