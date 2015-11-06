package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 *  Created by Nathaniel on 03/11/15.
 */

public class GameLoop extends Activity {
    private RunGameLoop gameLoop;
    GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameLoop = new RunGameLoop(60);
        gameView = new GameView(this);
        setContentView(gameView);
        new Thread(gameLoop).start();
    }
    @Override
    public void onPause(){
        gameLoop.pause();
        super.onPause();
    }
    @Override
    public void onResume(){
        gameLoop.resume();
        super.onResume();
    }

    private class RunGameLoop implements Runnable {
        long lastTime = System.nanoTime();
        final int targetFPS;
        final long targetTime;
        volatile boolean run = false;

        public RunGameLoop(int inputFPS) {
            targetFPS = inputFPS;
            targetTime = 1000000000 / targetFPS;
        }
        long lastFPSTime;
        int counter;

        // These flags are used to report the current FPS.
        long lastFPSReport = System.currentTimeMillis(); // The time that FPS was reported last.
        long currFrames = 0; // The current amount of frames rendered.
        long currFPS = 0; // The current FPS

        @Override
        public void run(){
            try {
                while (run) {
                    long presentTime = System.nanoTime();
                    long sleepTime;
                    long updateTime = presentTime - lastTime;
                    lastTime = presentTime;
                    lastTime += updateTime;
                    counter++;

                    if (lastFPSTime >= 1000000000) {
                        lastFPSTime = 0;
                        counter = 0;
                    }

                    gameUpdate();
                    gameDraw();
                    currFrames++;

                    lastTime = System.nanoTime();
                    sleepTime = (targetTime + (lastTime - presentTime));
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime / 1000000L);
                    }

                    // Report the amount of frames that have been rendered.
                    if (System.currentTimeMillis() - lastFPSReport >= 1000) {
                        Log.d("FPS", "Current FPS: " + currFrames );
                        currFPS = currFrames;
                        currFrames = 0;
                        lastFPSReport = System.currentTimeMillis();
                    }
                }
            } catch (InterruptedException e) {
                Log.d("InterruptedException", "Caught an interrupted exception");
            }
        }

        protected void gameUpdate() {
            //Log.d("Loop", "Updating" + counter);

        }

        protected void gameDraw() {
            //Log.d("Loop", "Drawing" + counter);
            if (!gameView.isReady()) {
                return;
            }
            gameView.lockCanvas();
            gameView.clear(Color.BLACK);
            gameView.drawText("FPS: " + currFPS, 200, 100, Color.WHITE);
            gameView.unlockCanvas();
        }

        public void pause(){
            run = false;
        }

        public void resume(){
            run = true;
        }
    }
}