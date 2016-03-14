package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Menu.ImageButton;

public class DeathOverlay {
    boolean enabled;

    ImageButton mainMenuBtn;
    ImageButton restartLevelBtn;

    public DeathOverlay(AssetLoader assetLoader, int w, int h) {
        assetLoader.AddAssets(new String[]{"menu/deathoverlay/main_menu.png",
                "menu/deathoverlay/restart_level.png"});
        Bitmap btnImg = assetLoader.getBitmapByName("menu/deathoverlay/main_menu.png");
        float buttonsX = (w / 2) - (btnImg.getWidth() / 2);
        mainMenuBtn = new ImageButton("menu/deathoverlay/main_menu.png", assetLoader, buttonsX,
                400, btnImg.getWidth(), btnImg.getHeight());
        restartLevelBtn = new ImageButton("menu/deathoverlay/restart_level.png", assetLoader, buttonsX,
                480, btnImg.getWidth(), btnImg.getHeight());
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void draw(GameView view) {
        if (enabled) {
            view.drawRect(0, 0, view.getWidth(), view.getHeight(), Color.argb(200, 0, 0, 0));

            view.drawTextCenter("Game Over!", view.getWidth() / 2, 200, Color.parseColor("#ffffff"),
                    150);

            mainMenuBtn.draw(view);
            restartLevelBtn.draw(view);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        // TODO:
        //mainMenuBtn.onTouchEvent(event, );
    }
}
