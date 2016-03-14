package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Menu.ImageButton;

public class DeathOverlay {
    AssetLoader assetLoader;

    private boolean enabled;

    ImageButton mainMenuBtn;
    ImageButton restartLevelBtn;

    public DeathOverlay(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
        assetLoader.AddAssets(new String[]{"menu/deathoverlay/main_menu.png",
                "menu/deathoverlay/restart_level.png"});
    }

    public void enable() {
        if (!enabled) {
            enabled = true;

            // Create new buttons to reset their state. The ImageButton's onTouchEvent method should
            // do this really, so this is a workaround (TODO).
            Bitmap btnImg = assetLoader.getBitmapByName("menu/deathoverlay/main_menu.png");
            float buttonsX = (Graphics.getScreenWidth() / 2) - (btnImg.getWidth() / 2);
            mainMenuBtn = new ImageButton("menu/deathoverlay/main_menu.png", assetLoader, buttonsX,
                    400, btnImg.getWidth(), btnImg.getHeight());
            restartLevelBtn = new ImageButton("menu/deathoverlay/restart_level.png", assetLoader, buttonsX,
                    480, btnImg.getWidth(), btnImg.getHeight());
        }
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void draw(GameView view) {
        if (enabled) {
            // Draw a transparent overlay over the GameScene.
            view.drawRect(0, 0, view.getWidth(), view.getHeight(), Color.argb(150, 0, 0, 0));

            // Draw the "Game Over" text in the center of the screen.
            view.drawTextCenter("Game Over!", view.getWidth() / 2, 200, Color.parseColor("#ffffff"),
                    150);

            // Draw the buttons.
            mainMenuBtn.draw(view);
            restartLevelBtn.draw(view);
        }
    }

    public enum OverlayResult {
        ShowMainMenu, RestartLevel, None
    }

    public OverlayResult onTouchEvent(MotionEvent event) {
        // Only test for touch events if the overlay is enabled.
        if (enabled) {
            mainMenuBtn.onTouchEvent(event);
            restartLevelBtn.onTouchEvent(event);

            if (mainMenuBtn.isTouched()) {
                return OverlayResult.ShowMainMenu;
            } else if (restartLevelBtn.isTouched()) {
                return OverlayResult.RestartLevel;
            }
        }
        // Return `None` when no touch events occurred.
        return OverlayResult.None;
    }
}
