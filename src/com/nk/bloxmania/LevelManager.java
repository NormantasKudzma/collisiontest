package com.nk.bloxmania;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

public class LevelManager {
	enum SetVar{
		unlocked {
			@Override
			void set(String s) {
				int num;
				try {
					num = Integer.parseInt(s);
				}
				catch (Exception e){
					num = 1;
				}
				LEVELS_UNLOCKED = num;
			}
		},
		level {
			@Override
			void set(String s) {
				String st = s.substring(0, s.indexOf("="));
				String end = s.substring(s.indexOf("=") + 1);
				int lvl, num;
				try {
					lvl = Integer.parseInt(st);
					num = Integer.parseInt(end);
					if (DEATHS != null){
						DEATHS[lvl] = num;
					}
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
		};
		abstract void set(String s);
	}
	
	public final static String DEF_PATH = Environment.getExternalStorageDirectory().toString() + "/nk/";
	public final static String FILE_NAME = "LevelSettings.dat";
	public static final String LVLS_PATH = "levels/level";
	
	public static int LEVELS_UNLOCKED = 0;
	public static int NUM_LEVELS = 0;
	public static int NUM_BACKGROUNDS = 0;
	public static int [] DEATHS;
	
	// Loads death/level settings from default file
	private static void loadSettings(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(DEF_PATH + FILE_NAME)));
			String line;
			String st, end;
			while ((line = br.readLine()) != null){
				st = line.substring(0, line.indexOf("="));
				end = line.substring(line.indexOf("=") + 1);
				SetVar.valueOf(st).set(end);
			}
			br.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
		
	public static void updateDeaths(int numLvl){
		if (DEATHS != null && numLvl < DEATHS.length){
			DEATHS[numLvl]++;
		}
		saveSettings();
	}
	
	public static void saveSettings(){
		try {
			File f = new File(DEF_PATH);
			if (!f.isDirectory()){
				f.mkdir();
			}
			PrintWriter pw = new PrintWriter(new File(DEF_PATH + FILE_NAME));
			pw.println("unlocked=" + LEVELS_UNLOCKED);
			if (DEATHS != null){
				for (int i = 0; i < DEATHS.length; i++){
					pw.println("level=" + i + "=" + DEATHS[i]);
				}
			}
			pw.close();
			Log.w("nk", "Successfully written to level settings file : " + DEATHS.length + " entries");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void countLevels(Context c){
		AssetManager m = c.getResources().getAssets();
		while (true){
			try {
				m.open(LVLS_PATH + NUM_LEVELS);
				NUM_LEVELS++;
			}
			catch (Exception e){
				break;
			}
		}
		DEATHS = new int[NUM_LEVELS];
		Log.w("nk", "LevelManager counted " + NUM_LEVELS + " levels in total.");
		loadSettings();

		if (LEVELS_UNLOCKED == 0){
			LEVELS_UNLOCKED++;
		}
	}

	public static void countBackgrounds(Context c){
		Resources res = c.getResources();
		while (true){
			int resId = res.getIdentifier("bg" + NUM_BACKGROUNDS, "drawable", c.getPackageName());
			if (resId != 0){
				NUM_BACKGROUNDS++;
			}
			else {
				break;
			}
		}
		Log.w("nk", "LevelManager counted " + NUM_BACKGROUNDS + " backgrounds in total.");
	}
	
	public static int getDeaths(int numLvl){
		if (DEATHS != null && numLvl < DEATHS.length){
			return DEATHS[numLvl];
		}
		else {
			return 0;
		}
	}
}
