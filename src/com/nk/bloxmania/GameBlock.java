package com.nk.bloxmania;

import android.graphics.RectF;

public class GameBlock {
	RectF r;
	float w, h;
	boolean isMoving = false;
	boolean hasCollider = false;
	float xDir = 0;
	float yDir = 0;
	GameEngine e;
	
	public GameBlock(GameEngine eng){
		e = eng;
	}
	
	public void frame(float left){
		if (xDir != 0){
			if (hasCollider){
				int x = (int) (xDir < 0 ? left - 1 : left + w + 1);
				int y = (int) (r.top + h / 2);
				int dist = e.raycastHorizontal(x, y, (xDir < 0), (int)xDir);
				if (Math.abs(xDir) > dist){
					float signDist = Math.signum(dist);
					r.left += signDist;
					r.right += signDist;
					xDir = -xDir;
				}
				else {
					r.left += xDir;
					r.right += xDir;
				}
			}
			else {
				r.left += xDir;
				r.right += xDir;
			}
		}
		if (yDir != 0){
			if (hasCollider){
				int x = (int) left;
				int y = (int) (yDir < 0 ? r.top - 1 : r.bottom + 1);
				int dist = e.raycastVertical(x, y, (yDir < 0), (int)yDir);
				if (dist < Math.abs(yDir) + 1){
					float signDist = (dist - 1) * Math.signum(yDir);
					r.top += signDist;
					r.bottom += signDist;
					yDir = -yDir;
				}
				else {
					r.top += yDir;
					r.bottom += yDir;
				}
			}
			else {
				r.top += yDir;
				r.bottom += yDir;
			}
		}
	}
	
	public void setBlockRect(RectF r){
		this.r = r;
		w = r.right - r.left;
		h = r.bottom - r.top;
	}
	
	public boolean isMoving(){
		return isMoving;
	}
}
