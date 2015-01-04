package com.nk.bloxmania;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends CustomActivity{
	protected enum SetVar {
		volume {
			@Override
			void set(String s) {
				float vol;
				try {
					vol = Float.parseFloat(s);
				}
				catch (Exception e){
					vol = 0.5f;
				}
				CustomActivity.musicManager.setVolume(vol);
			}
		};
		abstract void set(String s);
	}
	
	private final String DEF_PATH = Environment.getExternalStorageDirectory().toString() + "/nk/";
	private final String FILE_NAME = "GameSettings.dat";
	private static boolean settingsLoaded = false;
	
	protected ArrayList<String> settings = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		setUpInterface();
		scrollBackgroundResource = R.id.menu_scroll_background;
		customizeBackground(0.5f, 0.5f);
		if (!settingsLoaded){
			settingsLoaded = !settingsLoaded;
			LevelManager.countLevels(this);
			LevelManager.countBackgrounds(this);
			loadSettings();
		}
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
				startCustomActivity(OptionsActivity.class);
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
	
	protected void loadSettings(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(DEF_PATH + FILE_NAME)));
			String line;
			while ((line = br.readLine()) != null){
				String st = line.substring(0, line.indexOf('='));
				String end = line.substring(line.indexOf('=') + 1);
				try {
					SetVar.valueOf(st).set(end);
					settings.add(line);
				}
				catch (Exception e){}
			}
			br.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected void saveSettings() {
		try {
			PrintWriter pw = new PrintWriter(new File(DEF_PATH + FILE_NAME));
			for (String i : settings){
				pw.println(i);
			}
			pw.close();
			Log.w("nk", "Successfully written to options file : " + settings.size() + " entries");
		}
		catch (Exception e){
			e.printStackTrace();
		}
		super.saveSettings();
	}
	
	protected void updateValue(String key, String value){
		int index = -1;
		for (int i = 0; i < settings.size(); i++){
			if (settings.get(i).contains(key)){
				index = i;
			}
		}
		if (index != -1){
			settings.set(index, key + "=" + value);
		}
		else {
			settings.add(key + "=" + value);
		}
	}
}
