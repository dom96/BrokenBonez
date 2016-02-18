package com.dragonfruitstudios.brokenbonez;
        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;

/** Launches BrokenBonez Main Menu **/
public class MenuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class GameActivity = Class.forName("com.dragonfruitstudios.brokenbonez.GameActivity");
            Intent intent = new Intent(this, GameActivity);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}