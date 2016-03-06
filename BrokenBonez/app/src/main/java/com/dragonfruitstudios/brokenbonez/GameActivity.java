package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Input.Accelerometer;
import com.plattysoft.leonids.ParticleSystem;

/**
 * Game Activity class used for creating a new game view and game loop instance. Also defines some
 * device related features.
 */
public class GameActivity extends Activity {
    private GameLoop gameLoop;
    GameView gameView;
    PowerManager.WakeLock mWakeLock;
    int counterTest = 0;
    Intent intent;

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
        gameLoop = new GameLoop(gameView, new AssetLoader(this, new String[] {}));
        setContentView(gameView);
        intent = new Intent(GameActivity.this, com.dragonfruitstudios.brokenbonez.Input.Accelerometer.class);
        new Thread(gameLoop).start();
    }
    @Override
    public void onBackPressed() {
        gameLoop.assetLoader.pause(); //Stop all sounds when back button pressed
        if(gameLoop.gameSceneManager.getCurrentSceneString() == "menuScene"){
            moveTaskToBack(true);
        } else {
            this.gameLoop.gameSceneManager.setScene("menuScene");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gameLoop.pause(); // Pauses gameLoop.
        this.mWakeLock.release(); // No need to lock anymore. Calling this saves device's battery.
    }

    @Override
    public void onResume() {
        super.onResume();
        gameLoop.resume(); // Resumes gameLoop
        this.mWakeLock.acquire(); // Acquires the wake lock forcing the device to stay on.
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startActivity(intent);
        gameLoop.onGameTouch(event);
        return super.onTouchEvent(event);
        /**new ParticleSystem(this, 400, R.drawable.smoke, 400)
                .setSpeedModuleAndAngleRange(0.2f, 0.4f, 180, 200)
                .emit(gameView, 400);**/
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        gameLoop.onGameKeyUp(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }
}