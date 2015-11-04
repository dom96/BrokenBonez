package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by nathaniel on 03/11/15.
 */
public class GameLoop extends Activity{
    private RunGameLoop mGameLoop;
    StringBuilder builder = new StringBuilder();
    TextView textView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mGameLoop = new RunGameLoop(60);
        textView = new TextView(this);
        textView.setText(builder.toString());
        setContentView(textView);
    }
    @Override
    public void onPause(){
        mGameLoop.pause();
        super.onPause();
    }
    @Override
    public void onResume(){
        mGameLoop.resume();
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

                    lastTime = System.nanoTime();
                    sleepTime = (targetTime + (lastTime - presentTime));
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime / 1000000L);
                    }
                }
            } catch (InterruptedException e) {
            }
        }
        protected void gameUpdate(){
            Log.d("Loop", "Updating" + counter); }
        protected void gameDraw(){
            Log.d("Loop", "Drawing" + counter);
        }

        public void pause(){
            run = false;
        }

        public void resume(){
            run = true;
        }
    }
}