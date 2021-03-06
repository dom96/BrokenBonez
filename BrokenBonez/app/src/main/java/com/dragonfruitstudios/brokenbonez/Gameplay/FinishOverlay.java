package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Menu.ImageButton;

public class FinishOverlay {
    private AssetLoader assetLoader;

    private boolean shown;
    // Information which determines what is displayed to the user.
    private boolean crashed; // Whether the bike crashed or finished successfully.
    private float finishTime; // How long the bike rode for (in seconds).
    private float ghostTimeDiff; // The difference between the fastest bike ride and this ride.

    // Buttons
    private ImageButton continueBtn;
    private ImageButton mainMenuBtn;
    private ImageButton restartLevelBtn;

    public FinishOverlay(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
        // Load assets for the buttons.
        assetLoader.AddAssets(new String[]{"menu/deathoverlay/continue.png",
                "menu/deathoverlay/main_menu.png",
                "menu/deathoverlay/restart_level.png"});

        // Grab one of the assets to calculate the button x position.
        Bitmap btnImg = assetLoader.getBitmapByName("menu/deathoverlay/main_menu.png");
        float buttonsX = (Graphics.getScreenWidth() / 2) - (btnImg.getWidth() / 2);
        continueBtn = new ImageButton("menu/deathoverlay/continue.png", assetLoader, buttonsX,
                400, btnImg.getWidth(), btnImg.getHeight());
        mainMenuBtn = new ImageButton("menu/deathoverlay/main_menu.png", assetLoader, buttonsX,
                480, btnImg.getWidth(), btnImg.getHeight());
        restartLevelBtn = new ImageButton("menu/deathoverlay/restart_level.png", assetLoader, buttonsX,
                560, btnImg.getWidth(), btnImg.getHeight());
    }

    public void show(boolean crashed, float finishTime, float ghostTimeDiff) {
        this.crashed = crashed;
        this.finishTime = finishTime / 1000;
        this.ghostTimeDiff = ghostTimeDiff / 1000;
        shown = true;
    }

    public void hide() {
        shown = false;
    }

    public boolean isShown() {
        return shown;
    }

    public void draw(GameView view) {
        if (shown) {
            // Draw a transparent overlay over the GameScene.
            view.drawRect(0, 0, view.getWidth(), view.getHeight(), Color.argb(150, 0, 0, 0));

            // Draw a message in the center of the screen.
            String text = crashed ? "Game Over!" : "Level Finished!";
            view.drawTextCenter(text, view.getWidth() / 2, 200, Color.parseColor("#ffffff"),
                    150);

            // Draw extra information.
            String smallText = "";
            if (crashed) {
                smallText = String.format("You have crashed after %.1f seconds!", finishTime);
            }
            else {
                smallText = String.format("You have finished in %.1f seconds. ", finishTime);
                if (ghostTimeDiff < 0) {
                    smallText += String.format("But lost against the Ghost by %.1f seconds.",
                        Math.abs(ghostTimeDiff));
                }
                else if (ghostTimeDiff > 0) {
                    smallText += String.format("And won against the Ghost by %.1f seconds!",
                        ghostTimeDiff);
                }
            }
            view.drawTextCenter(smallText, view.getWidth() / 2, 300, Color.WHITE,
                    (int)Graphics.scaleX(30, Graphics.getScreenWidth()));

            // Draw the buttons.
            if (!crashed) {
                continueBtn.draw(view);
            }
            mainMenuBtn.draw(view);
            restartLevelBtn.draw(view);
        }
    }

    /**
     * The action that the user choose in the overlay.
     */
    public enum OverlayResult {
        Continue, ShowMainMenu, RestartLevel, None
    }

    public OverlayResult onTouchEvent(MotionEvent event) {
        // Only test for touch events if the overlay is shown.
        if (shown) {
            if (!crashed) {
                if (continueBtn.onTouchEvent(event)) {
                    return OverlayResult.Continue;
                }
            }
            if (mainMenuBtn.onTouchEvent(event)) {
                return OverlayResult.ShowMainMenu;
            } else if (restartLevelBtn.onTouchEvent(event)) {
                return OverlayResult.RestartLevel;
            }
        }
        // Return `None` when no touch events occurred.
        return OverlayResult.None;
    }
}
