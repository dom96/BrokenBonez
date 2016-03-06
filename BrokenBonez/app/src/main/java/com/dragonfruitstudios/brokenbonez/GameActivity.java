package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.BikeSelectionScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.GameScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.HighScoreScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.MenuScene;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.plattysoft.leonids.ParticleSystem;

/**
 * Game Activity class used for creating a new game view and game loop instance. Also defines some
 * device related features.
 */
public class GameActivity extends Activity {
    private GameLoop gameLoop;
    GameView gameView;
    PowerManager.WakeLock mWakeLock;

    Thread gameLoopThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Initialise a new AssetLoader.
        AssetLoader assetLoader = new AssetLoader(this, new String[] {});

        gameView = new GameView(this);

        // Initialise GameSceneManager to manage the different scenes for us.
        final GameSceneManager gameSceneManager = new GameSceneManager(gameView); //Setup the GameSceneManager

        // Initialise GameView and GameLoop for drawing.
        gameLoop = new GameLoop(gameSceneManager, gameView, assetLoader);
        gameView.setCallbacks(new GameView.GVCallbacks() {
            @Override
            public void performDraw(GameView gameView) {

            }

            @Override
            public void onSizeChanged(GameView gameView, int w, int h, int oldw, int oldh) {
                gameSceneManager.updateSize(w, h);
            }

            @Override
            public void onSurfaceAvailable(GameView gameView) {
                gameLoopThread = new Thread(gameLoop);
                gameLoopThread.start();
            }

            @Override
            public void onSurfaceDestroyed(GameView gameView) {
                gameLoop.stop();
                try {
                    gameLoopThread.join();
                } catch (InterruptedException e) {
                    Log.e("GameActivity", "InterruptedException caught");
                }
            }
        });
        setContentView(gameView);

        // Initialise the scenes.
        MenuScene menuScene = new MenuScene(assetLoader, gameSceneManager);   //Create the new MenuScene
        GameScene gameScene = new GameScene(assetLoader, gameSceneManager);   //Create the new GameScene
        HighScoreScene highScoreScene = new HighScoreScene(assetLoader, gameSceneManager);
        BikeSelectionScene bikeSelectionScene = new BikeSelectionScene(assetLoader, gameSceneManager); //Create the BikeSelectionScene
        gameSceneManager.addScene("menuScene", menuScene, true);  //Add the MenuScene just created to the GameSceneManager, then sets it as the active scene
        gameSceneManager.addScene("gameScene", gameScene, false); //Add the Gamescene just created to the GameSceneManager, then makes sure it isn't set as active
        gameSceneManager.addScene("bikeSelectionScene", bikeSelectionScene, false);
        gameSceneManager.addScene("highScoreScene", highScoreScene, false);
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