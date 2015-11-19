package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends Activity {

    private GameLoop gameLoop;
    GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        gameView = new GameView(this, 480, 320);
        gameLoop = new GameLoop(60, gameView);
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
}