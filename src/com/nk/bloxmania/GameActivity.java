package com.nk.bloxmania;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdView;

public class GameActivity extends CustomActivity {
	WeakReference<GameView> gv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		gv = new WeakReference<GameView>(((GameView)findViewById(R.id.imageView1)));
	}
	
	@Override
	public void onBackPressed() {
		killThread();
		startCustomActivity(LevelSelectionActivity.class);
	}
	
	@Override
	protected void onDestroy() {
		AdView ad = (AdView)findViewById(R.id.adView);
		if (ad != null){
			for (int i = 0; i < ad.getChildCount(); i++) {
				View v = ad.getChildAt(i);
				v.destroyDrawingCache();
			}
			RelativeLayout rl = (RelativeLayout)findViewById(R.id.game_relative_layout);						
			rl.removeView(ad);
			ad.pause();
			ad.destroy();
			ad.destroyDrawingCache();
			ad = null;
		}
		killThread();
		saveLevelSettings();
		super.onDestroy();
	}
	
	protected void saveLevelSettings(){
		GameEngine.DEATH_COUNT = 0;
	}
	
	void killThread(){
		if (gv != null && gv.get() != null){
			gv.get().finish();
			gv.clear();
			gv = null;
		}
	}
}
