package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler.*;

/**
 * Core game loop class which handles drawing and updating of the game.
 */
public class GameLoop implements Runnable {
    long lastTime = System.nanoTime();
    final int targetFPS;
    final long targetTime;
    volatile boolean run = false;
    GameView gameView;
    GameState gameState;

    /**
     * A method for taking the input fps i.e. fps entered when declaring a new game loop in
     * GameActivity class or the fps we want our game loop to constantly run at.
     */
    public GameLoop(int inputFPS, GameView gameView) {
        targetFPS = inputFPS;
        targetTime = 1000000000 / targetFPS;

        this.gameView = gameView;

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

        this.gameState = new GameState();
    }

    long lastFPSTime;
    int counter;

    // These flags are used to report the current FPS.
    long lastFPSReport = System.currentTimeMillis(); // The time that FPS was reported last.
    long currFrames = 0; // The current amount of frames rendered.
    long currFPS = 0; // The current FPS

    // These flags are used for timing the `update` method
    long lastUpdate = System.currentTimeMillis();

    @Override
    public void run(){
        while (true) { //
            long presentTime = System.nanoTime(); // Sets the present time to the current system time.
            long sleepTime; // Variable for the amount of time the thread needs to sleep for.
            // Setting the update time to the present time subtracted from the last time
            // in which the game loop was run.
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
            sleepTime = (targetTime + (lastTime - presentTime));
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000L);
                }
                catch (InterruptedException exc) {
                    // TODO: Dom: Look up what to do in this case properly.
                    // TODO: I recall that there is some method that should be called in the
                    // TODO: event of this.
                    Log.d("Error", "Interrupted exception was caught.");
                }
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
        // Calculate the number of milliseconds since the last update, pass it to
        // GameState's update method.
        gameState.update(System.currentTimeMillis() - lastUpdate);

        // Update the `lastUpdate` variable with the current time.
        lastUpdate = System.currentTimeMillis();
    }

    protected void gameUpdateSize(int w, int h) {
        gameState.updateSize(w, h);
    }

    protected void gameDraw(GameView gameView) {
        gameView.clear(Color.BLACK);

        gameState.draw(gameView);

        gameView.drawText("FPS: " + currFPS, 20, 30, Color.WHITE);
    }

    public void onGameTouch(MotionEvent event) {
        // Determine what action the user performed.
        ControlIsActive action = TouchHandler.determineAction(event, 320);
        Log.d("GameActivity/Touch", action.toString());
        switch (action) {
            case ACTION_GAS_DOWN:
                gameState.setBikeAcceleration(0.5f);
                break;
            case ACTION_GAS_UP:
                gameState.setBikeAcceleration(0f);
                break;
            case ACTION_BRAKE_DOWN:
                // TODO: This should brake not move the wheel backward.
                gameState.setBikeAcceleration(-0.5f);
                break;
            case ACTION_BRAKE_UP:
                gameState.setBikeAcceleration(0f);
                break;
        }
    }

    /**
     * Just for debugging purposes, the game does not use keyboard.
     */
    public void onGameKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_P:
                if (gameState.isPaused()) {
                    gameState.resume();
                }
                else {
                    gameState.pause();
                }
                break;
            case KeyEvent.KEYCODE_SPACE:
                if (gameState.getDebugStep() > 0) {
                    gameState.setDebugStep(0);
                }
                else {
                    gameState.setDebugStep(1);

                }
                break;
            case KeyEvent.KEYCODE_S:
                gameState.step(1);
                break;
        }

    }

    // Called when the user minimizes the game.
    public void pause() {
        run = false;
    }

    // Called when the user resumes the game from the android menu.
    public void resume() {
        run = true;
    }
}
