package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class MenuState implements GameObject {
    TextButton startGame;
    TextButton hiScore;
    TextButton helpGuide;
    AssetLoader assetLoader;
    Bitmap background;
    boolean noiseOn;
    Bitmap noise;
    final Bitmap scaledNoise;
    VectorF pos;
    float rotation;
    final Bitmap scaledBackground;
    GameSceneManager gameSceneManager;

    public MenuState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        startGame = new TextButton("Start Game", getScreenWidth() / 3 / 3, getScreenHeight() / 6 * 4, getScreenWidth() / 3, getScreenHeight() / 6 * 5, getScreenWidth() / 11 * 2, getScreenHeight() / 6 * 4 + getScreenHeight() / 10);
        hiScore = new TextButton("Hi-Score", getScreenWidth() / 3 / 3 * 2 + getScreenWidth() / 6 * 1, getScreenHeight() / 6 * 4, (getScreenWidth() / 3 * 2) - getScreenWidth() / 24 * 1, getScreenHeight() / 6 * 5, (getScreenWidth() / 11 * 5) + (getScreenWidth() / 11 / 4), getScreenHeight() / 6 * 4 + getScreenHeight() / 10);
        helpGuide = new TextButton("Help Guide", getScreenWidth() / 3 / 3 * 2 + (getScreenWidth() / 7 * 3 + (getScreenWidth() / 7 / 5)), getScreenHeight() / 6 * 4, (getScreenWidth() / 3 * 3) - ((getScreenWidth() / 3 * 1) / 4 + getScreenWidth() / 60 * 1), getScreenHeight() / 6 * 5, getScreenWidth() / 3 / 3 * 2 + (getScreenWidth() / 11 * 6) - (getScreenWidth() / 11 / 8), getScreenHeight() / 6 * 4 + getScreenHeight() / 10);
        this.assetLoader.AddAssets(new String[]{"tv.png", "start_game.png", "tvnoise.png"});
        noise = assetLoader.getBitmapByName("tvnoise.png");
        background = assetLoader.getBitmapByName("tv.png");
        scaledNoise = noise.createScaledBitmap(noise, getScreenWidth(), getScreenHeight(), false);
        scaledBackground = background.createScaledBitmap(background, getScreenWidth(), getScreenHeight(), false);
        pos = new VectorF(0, 0);
        rotation = 0;
    }

    @Override
    public void update(float lastUpdate) {

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
        helpGuide.draw(view);
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
        this.gameSceneManager.setScene("gameScene");  //This is being removed later, just for testing right now! -AM
        startGame.onTouchEvent(event, 60, 270, 400, 360);
        if (startGame.isTouched() == true) {
            Log.d("OPEN GAME SCREEN", "true");
            setNoiseOn();
            this.gameSceneManager.setScene("gameScene"); //This is the one that should be being used -AM
        }
    }

    public void setNoiseOn() {
        noiseOn = true;
    }

    public boolean getNoiseOn(){
        return noiseOn;
    }
}