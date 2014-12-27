package com.nk.bloxmania;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LevelSelectionActivity extends CustomActivity{
	public static final int ROW_SIZE = 8;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_select_layout);
		setTitleFont();
		setButtonListeners();
		scrollBackgroundResource = R.id.level_select_scroll_background;
		customizeBackground(0, 0.7f);
	}
	
	void setTitleFont(){
		TextView tv = (TextView) findViewById(R.id.level_label);
		tv.setTextSize(BIG_SIZE);
		tv.setTypeface(font);
	}
	
	void setButtonListeners(){
		int w = BIG_SIZE, 
			h = BIG_SIZE;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float scale = metrics.density;
		w = h *= scale;
		TableLayout tl = (TableLayout)findViewById(R.id.level_table);
		TableRow tr = createTableRow();
        int nr = 0;
        
        for (int i = 0; /*doop dee doop*/; i++){
        	final Button button = new Button(this);
        	button.setTextSize(SMALL_SIZE);
    		button.setBackgroundResource(R.drawable.button);
        	final int lvl = nr;
        	if (lvl >= LevelManager.LEVELS_UNLOCKED){
        		button.setText("X" + nr);
        	}
        	else {
        		button.setText("" + nr);
        	}
            button.setLayoutParams(new TableRow.LayoutParams(w, h));
            button.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					GameView.selectedLevel = lvl;
					startCustomActivity(GameActivity.class);
				}
            });

    		button.setTypeface(font);
            tr.addView(button);
        	
        	nr++;
        	if (i == ROW_SIZE - 1){
        		i = 0;
        		tl.addView(tr);
        		tr = createTableRow();
        	}
        	if (nr == LevelManager.NUM_LEVELS){
        		for (; i < ROW_SIZE; i++){
        			tr.addView(createSpace(w, h));
        		}
        		tl.addView(tr);
        		break;
        	}
        }
	}
	
	public Space createSpace(int w, int h){
		Space spc = new Space(this);
		spc.setMinimumHeight(h);
		spc.setMinimumWidth(w);
		return spc;
	}
	
	public TableRow createTableRow(){
		TableRow tr = new TableRow(this);
		tr.setGravity(Gravity.CENTER_HORIZONTAL);
		return tr;
	}

	@Override
	public void onBackPressed() {
		startCustomActivity(MainActivity.class);
	}
}