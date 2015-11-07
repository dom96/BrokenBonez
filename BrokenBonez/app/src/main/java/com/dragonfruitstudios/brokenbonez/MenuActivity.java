package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MenuActivity extends Activity implements View.OnClickListener {
        Button startGame;
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startGame = new Button(this);
        startGame.setText("Start Game");
        startGame.setOnClickListener(this);
        setContentView(startGame);
}

public void onClick(View v) {
try {
    Class GameActivity = Class.forName("com.dragonfruitstudios.brokenbonez.GameActivity");
    Intent intent = new Intent(this, GameActivity);
    startActivity(intent);
} catch (ClassNotFoundException e) {
    e.printStackTrace();
}
}
}