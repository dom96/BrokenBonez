package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.hardware.SensorEvent;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.BikeSelectionScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.GameScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.HighScoreScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.LevelSelectionScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.MenuScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.SettingsScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.SplashScene;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Core game loop class which handles drawing and updating of the game.
 */
public class GameLoop implements Runnable {
    public static int targetFPS = 60; // Mutable so that we can slow down simulation -DP
    final long targetTime;
    volatile boolean run = false;
    GameView gameView;
    GameSceneManager gameSceneManager;
    AssetLoader assetLoader;

    // This lock prevents the drawing of objects while they are being updated.
    // Without it there were bugs like for example the bike "teleporting" forward for a split
    // second when it was moving very fast.
    private Lock updateLock;

    /**
     * A method for taking the input fps i.e. fps entered when declaring a new game loop in
     * GameActivity class or the fps we want our game loop to constantly run at.
     */
    public GameLoop(GameView gameView, AssetLoader assetLoader) {
        targetTime = 1000000000 / targetFPS;
        this.gameView = gameView;
        this.assetLoader = assetLoader;

        this.gameSceneManager = new GameSceneManager(gameView); //Setup the GameSceneManager
        MenuScene menuScene = new MenuScene(assetLoader, gameSceneManager);   //Create the new MenuScene
        GameScene gameScene = new GameScene(assetLoader, gameSceneManager);   //Create the new GameScene
        HighScoreScene highScoreScene = new HighScoreScene(assetLoader, gameSceneManager);
        BikeSelectionScene bikeSelectionScene = new BikeSelectionScene(assetLoader, gameSceneManager);
        LevelSelectionScene levelSelectionScene = new LevelSelectionScene(assetLoader, gameSceneManager);
        SettingsScene settingsScene = new SettingsScene(assetLoader, gameSceneManager);
        SplashScene splashScene = new SplashScene(assetLoader, gameSceneManager);
        this.gameSceneManager.addScene("splashScene", splashScene, true);
        this.gameSceneManager.addScene("menuScene", menuScene, false);  //Add the MenuScene just created to the GameSceneManager, then sets it as the active scene
        this.gameSceneManager.addScene("gameScene", gameScene, false); //Add the Gamescene just created to the GameSceneManager, then makes sure it isn't set as active
        this.gameSceneManager.addScene("bikeSelectionScene", bikeSelectionScene, false);
        this.gameSceneManager.addScene("levelSelectionScene", levelSelectionScene, false);
        this.gameSceneManager.addScene("highScoreScene", highScoreScene, false);
        this.gameSceneManager.addScene("settingsScene", settingsScene, false);

        updateLock = new ReentrantLock();

        // Set the methods which should be called when certain events occur in the GameView.
        // Unfortunately no lambda support in Java 8, so no beautiful callbacks for us.
        this.gameView.setCallbacks(new GameView.GVCallbacks() {
            @Override
            public void performDraw(GameView gameView) {
                gameDraw(gameView);
            }

            @Override
            public void onSizeChanged(GameView gameView, int w, int h, int oldw, int oldh) {
                gameUpdateSize(w, h);
            }
        });
    }

    long lastFPSTime;
    int counter;

    // These flags are used for timing the `update` method
    long lastUpdate = System.currentTimeMillis();

    // Fields used to store debugging information.
    // These flags are used to report the current FPS.
    long lastFPSReport = System.currentTimeMillis(); // The time that FPS was reported last.
    long currFrames = 0; // The current amount of frames rendered.
    long currFPS = 0; // The current FPS
    // Debugging flag to determine whether to run the game loop slowly.
    boolean slowMotion = false;
    boolean step = false;

    long lastTime;



    @Override
    public void run(){
        while (true) { //
            long presentTime = System.nanoTime(); // Sets the present time to the current system time.
            long sleepTime; // Variable for the amount of time the thread needs to sleep for.
            // Setting the update time to the present time subtracted from the last time
            // in which the game loop was run.
            long extraSleepTime = 0L;
            long updateTime = presentTime - lastTime;
            lastTime = presentTime; // Setting the last time to the present time.
            // Adding the update time to the last time and setting the result to last time.
            lastTime += updateTime;
            counter++;

            // If statement for checking if the last fps time was over 1 million.
            // If it was then setCenter the lastFPSTime to 0
            if (lastFPSTime >= 1000000000) {
                lastFPSTime = 0;
                counter = 0;
            }

            gameUpdate();
            gameView.postInvalidate();
            currFrames++;

            // Sets the last time the loop was run to the present time.
            lastTime = System.nanoTime();
            // Sleep time will be equal to the last time subtracted from the current time added
            // to the target time. If the sleep time is more than 0 it will try to sleep
            // for the sleep time divided by 1 million in long type.
            sleepTime = (targetTime - (lastTime - presentTime) - extraSleepTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000L);
                    extraSleepTime = (System.nanoTime() - lastTime) - sleepTime;
                }
                catch (InterruptedException exc) {
                    // TODO: Dom: Look up what to do in this case properly.
                    // TODO: I recall that there is some method that should be called in the
                    // TODO: event of this.
                    Log.d("Error", "Interrupted exception was caught.");
                }
            } else {
                extraSleepTime = 0L;
            }
            // Report the amount of frames that have been rendered.
            if (System.currentTimeMillis() - lastFPSReport >= 1000) {
                Log.d("FPS", "Current FPS: " + currFPS );
                currFPS = currFrames;
                currFrames = 0;
                lastFPSReport = System.currentTimeMillis();
            }
        }
    }

    protected void gameUpdate() {
        updateLock.lock();

        // Only update when the game is not paused.
        if (run) {
            // Calculate the number of milliseconds since the last update
            float msSinceLastUpdate = System.currentTimeMillis() - lastUpdate;
            if (slowMotion) {
                msSinceLastUpdate = 1;
            }
            // Pass it to GameState's update method.
            gameSceneManager.update(msSinceLastUpdate);
        }

        // Update the `lastUpdate` variable with the current time.
        lastUpdate = System.currentTimeMillis();

        // Check to see if a simple 1ms step is wanted.
        if (step) {
            gameSceneManager.update(1);
            step = !step;
        }
        updateLock.unlock();
    }

    protected void gameUpdateSize(int w, int h) {
        updateLock.lock();
        gameSceneManager.updateSize(w, h);
        updateLock.unlock();
    }

    protected void gameDraw(GameView gameView) {
        updateLock.lock();
        gameView.clear(Color.BLACK);

        gameSceneManager.draw();

        gameView.drawText("FPS: " + currFPS, 20, 30, Color.WHITE);
        updateLock.unlock();
    }

    public void onGameTouch(MotionEvent event) {
        gameSceneManager.getCurrentSceneObject().onTouchEvent(event);
    }

    public void onGameSensorChanged(SensorEvent event) {
        gameSceneManager.getCurrentSceneObject().onSensorChanged(event);
    }

    /**
     * Just for debugging purposes, the game does not use keyboard.
     */
    public void onGameKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_P:
                if (run) {
                    pause();
                }
                else {
                    resume();
                }
                break;
            case KeyEvent.KEYCODE_SPACE:
                slowMotion = !slowMotion;
                if (slowMotion) {
                    targetFPS = 1000;
                }
                else {
                    targetFPS = 60;
                }
                break;
            case KeyEvent.KEYCODE_S:
                step = true;
                break;
            case KeyEvent.KEYCODE_D:
                gameSceneManager.setScene("gameScene");
                break;
        }

    }

    // Called when the user minimizes the game.
    // or when the 'P' key is pressed (when debugging in an emulator).
    public void pause() {
        run = false;
        this.assetLoader.pause();
        gameSceneManager.pause();
    }

    // Called when the user resumes the game from the android menu.
    public void resume() {
        run = true;
        this.assetLoader.resume();
        gameSceneManager.resume();
    }

    public static float calcUpdateFactor(float lastUpdate) {
        return 1f/targetFPS;
    }
}