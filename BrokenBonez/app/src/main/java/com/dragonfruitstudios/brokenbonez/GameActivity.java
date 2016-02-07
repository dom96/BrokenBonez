package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;

/**
 * Game Activity class used for creating a new game view and game loop instance. Also defines some
 * device related features.
 */
public class GameActivity extends Activity {
    private GameLoop gameLoop;
    GameView gameView;
    PowerManager.WakeLock mWakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        // Power manager gives control over the power state of the android device.
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // CPU will run regardless of display timeouts or the state of the screen after
        // the user presses the power button.
        this.mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wake Lock");
        this.mWakeLock.acquire(); // Acquires the wake lock forcing the device to stay on.
        // Enables full screen mode.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameLoop = new GameLoop(60, gameView, new AssetLoader(this, new String[] {}));
        setContentView(gameView);
        new Thread(gameLoop).start();
    }

    @Override
    public void onPause() {
        gameLoop.pause(); // Pauses gameLoop.
        super.onPause();
        this.mWakeLock.release(); // No need to lock anymore. Calling this saves device's battery.
    }

    @Override
    public void onResume() {
        gameLoop.resume(); // Resumes gameLoop
        super.onResume();
        this.mWakeLock.acquire(); // Acquires the wake lock forcing the device to stay on.
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gameLoop.onGameTouch(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        gameLoop.onGameKeyUp(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }
}