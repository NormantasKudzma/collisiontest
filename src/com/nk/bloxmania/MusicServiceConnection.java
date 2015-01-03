package com.nk.bloxmania;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class MusicServiceConnection implements ServiceConnection{
	MusicManager mm;
	
	@Override
	public void onServiceConnected(ComponentName c, IBinder b) {
		Log.w("nk", "Music service connected");
		mm = ((MusicManager.ServiceBinder)b).getService();
		mm.play(0);
	}

	@Override
	public void onServiceDisconnected(ComponentName c) {
		mm = null;
	}

	public MusicManager getService(){
		return mm;
	}
}
