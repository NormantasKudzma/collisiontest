package com.nk.bloxmania;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class CustomSensorListener implements SensorEventListener{
	private float value;

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {}

	@Override
	public void onSensorChanged(SensorEvent e) {
		value = e.values[1];
	}

	public float getValue(){
		return value;
	}
}
