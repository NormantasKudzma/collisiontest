package com.nk.collisiontest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.collisiontest.R;

public class LevelSelectionActivity extends Activity{
	public static final int BASE_BUTTON_SIZE = 72;
	public static final int FONT_SIZE = BASE_BUTTON_SIZE / 2;
	
	private Typeface font;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_select_layout);
		font = Typeface.createFromAsset(getAssets(), "fonts/disposable_droid.ttf");
		setTitleFont();
		setButtonListeners();
	}
	
	void setTitleFont(){
		TextView tv = (TextView) findViewById(R.id.level_label);
		tv.setTextSize(FONT_SIZE * 2);
		tv.setTypeface(font);
	}
	
	void setButtonListeners(){
		int w = BASE_BUTTON_SIZE, 
			h = BASE_BUTTON_SIZE;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float scale = metrics.density;
		w *= scale;
		h *= scale;
		TableLayout tl = (TableLayout)findViewById(R.id.level_table);
		TableRow tr = new TableRow(this);
        int nr = 0;
        
        for (int i = 0; /*doop beep doop*/; i++){
        	final Button button = new Button(this);
        	button.setTextSize(FONT_SIZE);
    		button.setBackgroundResource(R.drawable.button);
        	final int lvl = nr;
        	if (lvl >= LevelManager.LEVELS_UNLOCKED){
        		button.setText("X");
        	}
        	else {
        		button.setText("" + nr);
        	}
            button.setLayoutParams(new TableRow.LayoutParams(w, h));
            button.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					GameView.selectedLevel = lvl;
					Intent i = new Intent(LevelSelectionActivity.this, GameActivity.class);
					startActivity(i);
				}
            	
            });

    		button.setTypeface(font);
            tr.addView(button);
        	
        	nr++;
        	if (i == 8){
        		i %= 8;
        		tl.addView(tr);
        		tr = new TableRow(this);
        	}
        	if (nr == LevelManager.NUM_LEVELS){
        		tl.addView(tr);
        		break;
        	}
        }
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(LevelSelectionActivity.this, MainActivity.class);
		startActivity(i);
	}
}
