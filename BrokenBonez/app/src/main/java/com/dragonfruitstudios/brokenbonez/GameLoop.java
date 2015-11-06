package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class GameLoop implements Runnable {

    long lastTime = System.nanoTime();
    final int targetFPS;
    final long targetTime;
    volatile boolean run = false;
    GameView gameView;

    public GameLoop(int inputFPS, GameView gameView) {
        targetFPS = inputFPS;
        targetTime = 1000000000 / targetFPS;
        this.gameView = gameView;
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
                    Log.d("FPS", "Current FPS: " + currFPS );
                    currFPS = currFrames;
                    currFrames = 0;
                    lastFPSReport = System.currentTimeMillis();
                }
            }
        } catch (InterruptedException e) {

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