package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

public class GameActivity extends Activity {

private GameLoop gameLoop;
        GameView gameView;
        PowerManager.WakeLock mWakeLock;

@Override
public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    this.mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wake Lock");
    this.mWakeLock.acquire();
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    gameView = new GameView(this, 480, 320);
    gameLoop = new GameLoop(60, gameView);
    setContentView(gameView);
    new Thread(gameLoop).start();
}

@Override
public void onPause(){
    gameLoop.pause();
    super.onPause();
    this.mWakeLock.release();
}
@Override
public void onResume(){
    gameLoop.resume();
    super.onResume();
    this.mWakeLock.acquire();
}
}