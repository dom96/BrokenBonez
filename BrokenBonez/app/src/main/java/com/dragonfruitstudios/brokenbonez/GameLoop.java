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
    GameState gameState;

    public GameLoop(int inputFPS, GameView gameView) {
        targetFPS = inputFPS;
        targetTime = 1000000000 / targetFPS;
        this.gameView = gameView;

        // TODO: The following would be so much simpler and nicer if Android Studio would support
        // Java 8 :(
        // Could we use retrolambda? https://github.com/evant/gradle-retrolambda
        // If so the code would just be something like:
        // this.gameView.setDrawingFunction(this::gameDraw);
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
        this.gameState = new GameState(gameView);
    }

    long lastFPSTime;
    int counter;

    // These flags are used to report the current FPS.
    long lastFPSReport = System.currentTimeMillis(); // The time that FPS was reported last.
    long currFrames = 0; // The current amount of frames rendered.
    long currFPS = 0; // The current FPS

    @Override
    public void run(){
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
            gameView.postInvalidate();
            currFrames++;


            lastTime = System.nanoTime();
            sleepTime = (targetTime + (lastTime - presentTime));
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000L);
                }
                catch (InterruptedException exc) {
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
        //Log.d("Loop", "Updating" + counter);
        gameState.update();
    }

    protected void gameUpdateSize(int w, int h) {
        gameState.updateSize(w, h);
    }

    protected void gameDraw(GameView gameView) {
        gameView.clear(Color.BLACK);

        gameState.draw();

        gameView.drawText("FPS: " + currFPS, 20, 30, Color.WHITE);
    }


    public void pause(){
        run = false;
    }

    public void resume(){
        run = true;
    }
}