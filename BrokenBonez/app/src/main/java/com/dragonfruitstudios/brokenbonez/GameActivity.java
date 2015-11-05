package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends Activity {

    private GameLoop gameLoop;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        gameLoop = new GameLoop(60);
        textView = new TextView(this);
        textView.setText("Foobar");
        setContentView(textView);
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