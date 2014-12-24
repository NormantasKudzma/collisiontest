package com.nk.bloxmania;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

public class LevelManager {
	public final static String DEF_PATH = Environment.getExternalStorageDirectory().toString() + "/nk/";
	public final static String FILE_NAME = "LevelSettings.dat";
	public static final String LVLS_PATH = "levels/level";
	
	public static int LEVELS_UNLOCKED = 0;
	public static int NUM_LEVELS = 0;
	public static int NUM_BACKGROUNDS = 0;
	public static ArrayList<Integer> DEATH_DATA = new ArrayList<Integer>();
	
	static {
		// Load death data
		Log.w("nk", "LevelManager started");
		ArrayList<String> arr = readSettings();
		if (arr != null){
			for (String i : arr){
				try {
					DEATH_DATA.add(Integer.parseInt(i));
				}
				catch (Exception e){}
			}
		}

		if (LEVELS_UNLOCKED == 0){
			LEVELS_UNLOCKED++;
		}
	}
	
	// Loads death/level settings from default file
	private static ArrayList<String> readSettings(){
		try {
			ArrayList<String> arr = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(new File(DEF_PATH + FILE_NAME)));
			String line;
			boolean first = true;
			while ((line = br.readLine()) != null){
				if (first){
					LEVELS_UNLOCKED = Integer.parseInt(line);
					first = !first;
				}
				else {
					arr.add(line);
				}
			}
			br.close();
			return arr;
		}
		catch (Exception e){
			saveSettings();
			e.printStackTrace();
			return null;
		}
	}
	
	// Adds deathCount to level's death count
	public static void updateDeaths(int numLvl, int deathCount) {
		if (numLvl < DEATH_DATA.size()){
			DEATH_DATA.set(numLvl, DEATH_DATA.get(numLvl) + deathCount);
		}
		else {
			DEATH_DATA.add(deathCount);
		}
		saveSettings();
	}
	
	public static void saveSettings(){
		try {
			File f = new File(DEF_PATH);
			f.mkdir();
			PrintWriter pw = new PrintWriter(new File(DEF_PATH + FILE_NAME));
			pw.println(LEVELS_UNLOCKED);
			if (DEATH_DATA != null){
				for (Integer i : DEATH_DATA){
					pw.println(i);
				}
			}
			pw.close();
			Log.w("nk", "Successfully written to settings file : " + DEATH_DATA.size() + " entries");
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
		Log.w("nk", "LevelManager counted " + NUM_LEVELS + " levels in total.");
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
		if (numLvl < DEATH_DATA.size()){
			return DEATH_DATA.get(numLvl);
		}
		else {
			return 0;
		}
	}
}
