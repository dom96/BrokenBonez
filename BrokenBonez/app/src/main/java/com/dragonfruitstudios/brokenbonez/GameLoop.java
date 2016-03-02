package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.BikeSelectionScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.GameScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.MenuScene;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Core game loop class which handles drawing and updating of the game.
 */
public class GameLoop implements Runnable {
    public static int targetFPS = 60; // Mutable so that we can slow down simulation -DP
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
        this.gameView = gameView;
        this.assetLoader = assetLoader;

        this.gameSceneManager = new GameSceneManager(gameView); //Setup the GameSceneManager

        MenuScene menuScene = new MenuScene(assetLoader, gameSceneManager);   //Create the new MenuScene
        GameScene gameScene = new GameScene(assetLoader, gameSceneManager);   //Create the new GameScene
        BikeSelectionScene bikeSelectionScene = new BikeSelectionScene(assetLoader, gameSceneManager);
        this.gameSceneManager.addScene("menuScene", menuScene, true);  //Add the MenuScene just created to the GameSceneManager, then sets it as the active scene
        this.gameSceneManager.addScene("gameScene", gameScene, false); //Add the Gamescene just created to the GameSceneManager, then makes sure it isn't set as active
        this.gameSceneManager.addScene("bikeSelectionScene", bikeSelectionScene, false);

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
    long lastUpdate = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

    // Fields used to store debugging information.
    // These flags are used to report the current FPS.
    long lastFPSReport = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()); // The time that FPS was reported last.
    long currFrames = 0; // The current amount of frames rendered.
    long currFPS = 0; // The current FPS
    // Debugging flag to determine whether to run the game loop slowly.
    boolean slowMotion = false;
    boolean step = false;

    public final int FRAMES_PER_SECOND = 60;
    public final int WAIT_TIME = 1000 / FRAMES_PER_SECOND;
    public final int MAX_FRAME_SKIP = 5;
    long NEXT_UPDATE = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    public final int MAX_FRAMES_PER_SECOND = 60;
    public final int MIN_WAIT_TIME = 1000 / MAX_FRAMES_PER_SECOND;
    long LAST_UPDATE = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    int GAME_LOOPS;

    @Override
    public void run(){
        while (true) {
            while (System.currentTimeMillis() < LAST_UPDATE + MIN_WAIT_TIME){
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LAST_UPDATE = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

            GAME_LOOPS = 0;
            while(TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) > NEXT_UPDATE && GAME_LOOPS < MAX_FRAME_SKIP){
                gameUpdate();
                NEXT_UPDATE += WAIT_TIME;
                GAME_LOOPS++;
                currFrames++;
                counter++;
            }

            gameView.postInvalidate();

            // If statement for checking if the last fps time was over 1 million.
            // If it was then setCenter the lastFPSTime to 0
            if (lastFPSTime >= 1000000000) {
                lastFPSTime = 0;
                counter = 0;
            }
            // Report the amount of frames that have been rendered.
            if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFPSReport >= 1000) {
                Log.d("FPS", "Current FPS: " + currFPS );
                currFPS = currFrames;
                currFrames = 0;
                lastFPSReport = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
            }
        }
    }

    protected void gameUpdate() {
        updateLock.lock();

        // Only update when the game is not paused.
        if (run) {
            // Calculate the number of milliseconds since the last update
            float msSinceLastUpdate = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastUpdate;
            if (slowMotion) {
                msSinceLastUpdate = 1;
            }
            // Pass it to GameState's update method.
            gameSceneManager.update(msSinceLastUpdate);
        }

        // Update the `lastUpdate` variable with the current time.
        lastUpdate = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

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
}
