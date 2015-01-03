package com.nk.bloxmania;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicManager{
	private final String FILE_PREFIX = "audio";
	private final String FILE_SUFFIX = "";
	
	private float volume = 0.5f;
	ArrayList<Integer> files;
	private int nr = 0;
	private boolean autoplay = true;	
	private MediaPlayer mp;
	private Context c;
	
	public MusicManager(Context c) {
		this.c = c;
		mp = new MediaPlayer();
		mp.setVolume(volume, volume);
		loadAudioFiles(c);
		shufflePlaylist();	
		mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {			
			@Override
			public void onPrepared(MediaPlayer m) {
				m.start();
			}
		});	
		if (autoplay){
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {		
				@Override
				public void onCompletion(MediaPlayer m) {
					nr++;
					if (nr >= files.size()){
						nr = 0;
						shufflePlaylist();
					}
					play(nr);				
				}
			});
		}
		Log.w("nk", "Music manager started");
	}

	private void shufflePlaylist(){
		int last = files.get(files.size() - 1);
		int temp;
		Random rnd = new Random();
		for (int i = 0; i < files.size(); i++){
			 temp = files.set(rnd.nextInt(files.size() - i) + i, files.get(i));
			 files.set(i, temp);
		}
		if (files.get(0) == last){
			files.set(0, files.get(files.size() - 1));
			files.set(files.size() - 1, last);
		}
	}
	
	private void loadAudioFiles(Context c){
		Resources res = c.getResources();
		files = new ArrayList<Integer>();
		int i = 0;
		int id;
		while (true){
			id = res.getIdentifier(FILE_PREFIX + i + FILE_SUFFIX, "raw", c.getPackageName());
			if (id != 0){
				files.add(id);
				i++;
			}
			else {
				break;
			}
		}
		Log.w("nk", "Music manager counted " + i + " audio files.");
	}
	
	public void setVolume(float vol){
		if (vol > 1.0f){
			vol = 1.0f;
		}
		if (vol < 0.0f){
			vol = 0.0f;
			pause();
		}
		else {
			resume();
		}
		volume = vol;
		mp.setVolume(volume, volume);
	}
	
	public float getVolume(){
		return volume;
	}
	
	public void pause(){
		mp.pause();
	}
	
	public void resume(){
		if (!mp.isPlaying()){
			mp.start();
		}
	}
	
	public void start(){
		mp.setVolume(volume, volume);
		play(nr);
	}
	
	public void play(int i){
		AssetFileDescriptor afd = c.getResources().openRawResourceFd(files.get(i));
		
		try {
			mp.reset();
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
			mp.prepare();
			afd.close();
		}
		catch (Exception e){
			Log.d("nk", "Cannot play : " + e.toString());
		}		
	}
	
	public void finish(){
		Log.w("nk", "MusicManager stopped.");
		if (mp != null){
			mp.stop();
			mp.release();
			mp = null;
		}
		c = null;
		files = null;
	}
}
