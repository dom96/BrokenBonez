package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class MenuState implements GameObject {
    ImageButton startGame;
    ImageButton hiScore;
    ImageButton credits;
    ImageButton settings;
    AssetLoader assetLoader;
    Bitmap background;
    boolean noiseOn;
    boolean noiseWait;
    float waitTime = 0;
    Bitmap noise;
    final Bitmap scaledNoise;
    VectorF pos;
    float rotation;
    final Bitmap scaledBackground;
    GameSceneManager gameSceneManager;

    public MenuState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        startGame = new ImageButton("menu/start.png", assetLoader, getScreenWidth() / 2 - getScreenWidth() / 4, getScreenHeight() / 4 + 60, 930, 270);
        hiScore = new ImageButton("menu/hiscore.png", assetLoader, getScreenWidth() / 2 - getScreenWidth() / 4, getScreenHeight() / 4 * 2 + 120, 425, 120);
        credits = new ImageButton("menu/credits.png", assetLoader,  getScreenWidth() / 4 * 2 + 25, getScreenHeight() / 4 * 2 + 120, 425, 120);
        settings = new ImageButton("menu/settings.png", assetLoader, (getScreenWidth() / 10) * 9 + 70, (getScreenHeight() / 10) * 9 + 15, 120, 100);
        this.assetLoader.AddAssets(new String[]{"menu/tv.png", "menu/tvnoise.png"});
        noise = assetLoader.getBitmapByName("menu/tvnoise.png");
        background = assetLoader.getBitmapByName("menu/tv.png");
        scaledNoise = noise.createScaledBitmap(noise, getScreenWidth(), getScreenHeight(), false);
        scaledBackground = background.createScaledBitmap(background, getScreenWidth(), getScreenHeight(), false);
        pos = new VectorF(0, 0);
        rotation = 0;
    }

    @Override
    public void update(float lastUpdate) {
        if(getNoiseWait() == true){
            waitTime += lastUpdate;
                if(waitTime > 1000) {
                    noiseOn = false;
                    noiseWait = false;
                    waitTime = 0;
                    startBikeSelectionScreen();
            }
        }
    }

    @Override
    public void updateSize(int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void draw(GameView view) {
        view.drawImage(scaledBackground, pos, rotation, GameView.ImageOrigin.TopLeft);
        startGame.draw(view);
        hiScore.draw(view);
        credits.draw(view);
        settings.draw(view);
        if(getNoiseOn() == true){
            view.drawImage(scaledNoise, pos, rotation, GameView.ImageOrigin.TopLeft);
        }
    }

    /**TODO:
     * @param event
     * Create a timer so the white noise appears for a short duration before transitioning to the
     * screen linked to the button the user pressed(this could be our loading screen here.)
     * Add a way to launch the game-play screen. I can launch it through GameLoop by
     * uncommenting line 36 but unsure how to get it implemented to launch here. Andrew
     * I need a quick explanation on that.
     */
    public void onTouchEvent(MotionEvent event) {
        startGame.onTouchEvent(event, (getScreenWidth() / 2 - getScreenWidth() / 4), (getScreenHeight() / 4 + 60), ((getScreenWidth() / 2 - getScreenWidth() / 4) + 929), ((getScreenHeight() / 4 + 60) + 269));
        hiScore.onTouchEvent(event, (getScreenWidth() / 2 - getScreenWidth() / 4), (getScreenHeight() / 4 * 2 + 120), ((getScreenWidth() / 2 - getScreenWidth() / 4) + 424), ((getScreenHeight() / 4 * 2 + 120) + 119));
        credits.onTouchEvent(event, (getScreenWidth() / 4 * 2 + 25), (getScreenHeight() / 4 * 2 + 120), ((getScreenWidth() / 4 * 2 + 25) + 424), ((getScreenHeight() / 4 * 2 + 120) + 119));
        settings.onTouchEvent(event, (getScreenWidth() / 10) * 9 + 70, (getScreenHeight() / 10) * 9 + 15, (((getScreenWidth() / 10) * 9 + 70) + 119), (((getScreenHeight() / 10) * 9 + 15) + 99));

        if(startGame.isTouched() == true){
            startGame.isTouched = false;
            setNoiseOn();
            setNoiseWait();
        }
        if(hiScore.isTouched == true){
            hiScore.isTouched = false;
            Log.d("HISCORE", "isTouched");
            gameSceneManager.setScene("highScoreScene");
        }
        if(credits.isTouched == true){
            credits.isTouched = false;
            gameSceneManager.setScene("creditsScene");
        }
        if(settings.isTouched == true){
            settings.isTouched = false;
            gameSceneManager.setScene("settingsScene");
        }

    }

    public void startBikeSelectionScreen(){this.gameSceneManager.setScene("bikeSelectionScene");}
    public boolean getNoiseOn(){
        return noiseOn;
    }
    public boolean getNoiseWait(){
        return noiseWait;
    }
    public void setNoiseOn() {
        noiseOn = true;
    }
    public void setNoiseWait(){
        noiseWait = true;
    }
}