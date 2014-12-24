package com.nk.bloxmania;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

public class AboutTextView extends TextView{
	public static final String ABOUT_TXT = "AboutContent";
	
	public AboutTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadTextFromFile(context.getAssets());
	}

	void loadTextFromFile(AssetManager a){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(a.open(ABOUT_TXT)));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null){
				sb.append(line);
			}
			setText(Html.fromHtml(sb.toString()));
			br.close();
		}
		catch (Exception e){
			e.printStackTrace();
			setText("Something went wrong: " + e.getLocalizedMessage());
		}
	}
}
