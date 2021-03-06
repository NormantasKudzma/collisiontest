package com.nk.bloxmania;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class GameEngine {
	private enum SetVar{
		gravity {
			@Override
			void set(GameEngine instance, String s){
				float num;
				try {
					num = Float.parseFloat(s);
					instance.setScaledGravity(num);
				}
				catch (Exception e){}
			}
		},
		viewportspeed {
			@Override
			void set(GameEngine instance, String s) {
				float num;
				try {
					num = Float.parseFloat(s);
					instance.setScaledViewPortDeltaX(num);
				}
				catch (Exception e){}
			}
		},
		levelmodtext {
			@Override
			void set(GameEngine instance, String s){
				instance.gameView.setInfoText(s);
			}
		},
		verticalspeed {
			@Override
			void set(GameEngine instance, String s) {
				float num;
				try {
					num = Float.parseFloat(s);
					instance.setScaledVerticalSpeed(num);
				}
				catch (Exception e){}
			}
		},
		horizontalspeed {
			@Override
			void set(GameEngine instance, String s) {
				float num;
				try {
					num = Float.parseFloat(s);
					instance.setScaledHorizontalSpeed(num);
				}
				catch (Exception e){}
			}
		};
		abstract void set(GameEngine instance, String s);
	}
	
	public static final String LVLS_PATH = "levels/level";
	public static int DEATH_COUNT = 0;
	
	public final float MAX_ROTATION = 3.7f;
	public final float MIN_ROTATION = -3.7f;
	
	int x, y, w, h;					// Current player's position and size
	Bitmap bitmap;					//
	int screenWidth, screenHeight;	// Screen dimensions
	GameView gameView;
	
	private float scaleW, scaleH;
	
	private float horizontalSpeed = 7;				// Determines the maximum you can move per frame (H)
	private float verticalSpeed = 40;					// Determines the maximum you can move per frame (V)
	private float deltaVerticalDirection = 0.07f;	// Shows how many frames the jump up takes
	private float verticalBreakPoint = 0.82f;
	private float gravity = 32;					// Shows how fast you go down
	private double verticalDirection = 0;						// Current jump up frame
	private static int playerColor = Color.MAGENTA;		// Current player's color
	private boolean isJumping = false;					// Is player moving up?
	
	private ArrayList<GameBlock> level;					// Array containing level data
	private int currentLevel = 0;						// Current level number
	private float viewPortX = -800;						// Current minimum X position to be drawn on screen
	private float viewPortDeltaX = 4;		// X position change per frame (moves to the right side)	
	
	public GameEngine(int scrW, int scrH, Bitmap b, GameView gv){
		scaleW = Scale.getWidthScale(scrW);
		scaleH = Scale.getHeightScale(scrH);
		Log.w("nk", "Screen scale is : " + scaleH);
		// Scaling part
		horizontalSpeed *= scaleW;
		verticalSpeed *= scaleH;
		gravity *= scaleH;
		viewPortX *= scaleW;
		viewPortDeltaX *= scaleW;
		
		w = h = (int)(35 * scaleH);
		x = (int)(scaleW * 150);
		y = (int)(scaleH * 150);
		screenWidth = scrW;
		screenHeight = scrH;
		bitmap = b;
		gameView = gv;
	}
	


	public static int getPlayerColor(){
		return playerColor;
	}
	
	public static void setPlayerColor(int clr){
		playerColor = clr;
	}
	
	public Rect getPlayerRect(){
		return new Rect(x, y, x + w, y + h);
	}
	
	public float getViewPortX(){
		return viewPortX;
	}
	
	public void moveViewPort(){
		viewPortX += viewPortDeltaX;
	}
	
	public ArrayList<GameBlock> getLevel(){
		return level;
	}
	
	public int getCurrentLevel(){
		return currentLevel;
	}
	
	public void setCurrentLevel(int lvl){
		currentLevel = lvl;
	}
	
	public float getGravity(){
		return gravity;
	}
	
	public void setScaledGravity(float grav){
		gravity = grav * scaleH;
	}
	
	public float getHorizontalSpeed(){
		return horizontalSpeed;
	}
	
	public void setScaledHorizontalSpeed(float hs){
		horizontalSpeed = scaleH * hs;
	}
	
	public float getVerticalSpeed(){
		return verticalSpeed;
	}
	
	public void setScaledVerticalSpeed(float vs) {
		verticalSpeed = scaleH * vs;
	}
	
	public float getViewPortDeltaX(){
		return viewPortDeltaX;
	}
	
	public void setScaledViewPortDeltaX(float speed){
		viewPortDeltaX = speed * scaleH;
	}
	
	public void movePlayerHorizontal(float val){
		// clamp Value
		if (val > MAX_ROTATION){
			val = MAX_ROTATION;
		}
		else {
			if (val < MIN_ROTATION){
				val = MIN_ROTATION;
			}
		}
		
		float movementValue = val * horizontalSpeed;
		x += movementValue;
		
		// clamp X
		if (x < 1){
			x = 1;
		}
		else {
			if (x+w >= screenWidth){
				x = screenWidth - w - 1;
			}
		}
		
		int maxDepth = Math.abs((int)movementValue) + 1;
		if (raycastHorizontal(x, y + 1, true, maxDepth) == 0 || raycastHorizontal(x, y + h - 1, true, maxDepth) == 0 ||
			raycastHorizontal(x + w, y, false, maxDepth) == 0 || raycastHorizontal(x+w, y + h - 1, false, maxDepth) == 0){
			Log.w("nk", "Dead, touched wall plrx " + (x+viewPortX) + " lastblock right " + level.get(level.size() - 1).r.right);
			gameView.setGameOver();
			gameView.isDone(true);
			GameEngine.DEATH_COUNT++;
		}
	}
	
	public void movePlayerVertical(){
		int maxDepth;
		if (isJumping){
			double acceleration = Math.cos(2 * verticalDirection) * verticalSpeed;
			maxDepth = (int)acceleration + 1;
			double delta = Math.min(raycastVertical(x, y, true, maxDepth), raycastVertical(x+w, y, true, maxDepth));
			
			if (delta < acceleration){
				y -= delta - 1;
				verticalDirection = 0;
				isJumping = false;
			}
			else {
				y -= acceleration;
				
				verticalDirection += deltaVerticalDirection;
				if (verticalDirection > verticalBreakPoint){
					isJumping = false;
					verticalDirection = 0;
				}
			}
		}
		else {
			maxDepth = (int)gravity + 1;
			double delta = Math.min(raycastVertical(x, y+h, false, maxDepth), raycastVertical(x+w, y+h, false, maxDepth));
			if (delta <= gravity){
				y += delta - 1;
				isJumping = true;
			}
			else {
				y += gravity;
			}
		}
		
		// Clamp y
		if (y < 1){
			y = 1;
		}
		else {
			if (y+h >= screenHeight){
				y = screenHeight - h - 1;
			}
		}
	}
	
	public int raycastHorizontal(int xPos, int yPos, boolean left){
		return raycastHorizontal(xPos, yPos, left, screenWidth);
	}
	
	public int raycastHorizontal(int xPos, int yPos, boolean left, int maxDepth){
		int result = 0;
		int delta = left ? -1 : 1;
		int wall = Color.BLACK;
		while (xPos > 0 && xPos < screenWidth && result < maxDepth){
			if (bitmap.getPixel(xPos, yPos) != wall){
				result++;
				xPos += delta;
			}
			else {
				break;
			}
		}
		return result;
	}
	
	public int raycastVertical(int xPos, int yPos, boolean up){
		return raycastVertical(xPos, yPos, up, screenHeight);
	}
	
	// Simple raycast methods to determine distances
	public int raycastVertical(int xPos, int yPos, boolean up, int maxDepth){
		int result = 0;
		int delta = up ? -1 : 1;
		int wall = Color.BLACK;

		while (yPos > 0 && yPos < screenHeight && result < maxDepth){
			if (bitmap.getPixel(xPos, yPos) != wall){
				result++;
				yPos += delta;
			}
			else {
				break;
			}
		}
		return result;
	}
	
	public void loadLevel(Resources res){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(res.getAssets().open(LVLS_PATH + currentLevel)));
			String line;
			level = new ArrayList<GameBlock>();
			while ((line = br.readLine()) != null){
				if (!Character.isDigit(line.charAt(0)) && !(line.charAt(0) == '-')){
					String st = line.substring(0, line.indexOf('='));
					String end = line.substring(line.indexOf('=') + 1);
					SetVar.valueOf(st).set(this, end);
				}
				else {
					RectF r = new RectF();
					String [] arr = line.split("\t");
					r.left = scaleW * Integer.parseInt(arr[0]);
					r.top = scaleH * Integer.parseInt(arr[1]);
					r.right = scaleW * Integer.parseInt(arr[2]);
					r.bottom = scaleH * Integer.parseInt(arr[3]);
					GameBlock gb = new GameBlock(this);
					gb.setBlockRect(r);
					if (arr.length > 4){
						gb.isMoving = true;
						gb.xDir = scaleW * Float.parseFloat(arr[4]);
						gb.yDir = scaleH * Float.parseFloat(arr[5]);
					}
					if (arr.length > 6){
						// get params, currently only collider
						gb.hasCollider = true;
					}
					level.add(gb);
				}
			}
			br.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
