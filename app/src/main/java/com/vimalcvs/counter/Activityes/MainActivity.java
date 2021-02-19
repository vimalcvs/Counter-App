package com.vimalcvs.counter.Activityes;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;

import com.vimalcvs.counter.R;

public class MainActivity extends AppCompatActivity {

    public static final String ON_KEY_DOWN_BROADCAST = "ON_KEY_DOWN_BROADCAST";
    public static final String KEYCODE_EXTRA = "KEYCODE_EXTRA";
    public static final int KEYCODE_VOLUME_UP = 24;
    public static final int KEYCODE_VOLUME_DOWN = 25;
    private boolean mAllowedVolumeButtons;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPreferences);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* depending on pref keeps screen on or let it off*/
        mAllowedVolumeButtons = mSharedPreferences.getBoolean("useVolumeButtons", false);
        if (mSharedPreferences.getBoolean("keepScreenOn", true)) {
            MainActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (!mSharedPreferences.getBoolean("keepScreenOn", true)){
            MainActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /* sends local broadcast to catch the key volume up event*/
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && mAllowedVolumeButtons){
            Intent intent = new Intent(ON_KEY_DOWN_BROADCAST).putExtra(KEYCODE_EXTRA, KEYCODE_VOLUME_UP);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return true;
        }

        /* sends local broadcast to catch the key volume down event*/
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && mAllowedVolumeButtons){
            Intent intent = new Intent(ON_KEY_DOWN_BROADCAST).putExtra(KEYCODE_EXTRA, KEYCODE_VOLUME_DOWN);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /* depending on pref sets theme*/
    private void setTheme(SharedPreferences sharedPreferences) {
        if (sharedPreferences.getBoolean("nightMod", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
