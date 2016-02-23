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
    ImageButton startGame;
    AssetLoader assetLoader;
    Bitmap background;
    boolean noiseOn;
    boolean noiseWait;
    Bitmap noise;
    final Bitmap scaledNoise;
    VectorF pos;
    float rotation;
    final Bitmap scaledBackground;
    GameSceneManager gameSceneManager;

    public MenuState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        startGame = new ImageButton(assetLoader);
        this.assetLoader.AddAssets(new String[]{"tv.png", "tvnoise.png"});
        noise = assetLoader.getBitmapByName("tvnoise.png");
        background = assetLoader.getBitmapByName("tv.png");
        scaledNoise = noise.createScaledBitmap(noise, getScreenWidth(), getScreenHeight(), false);
        scaledBackground = background.createScaledBitmap(background, getScreenWidth(), getScreenHeight(), false);
        pos = new VectorF(0, 0);
        rotation = 0;
    }

    @Override
    public void update(float lastUpdate) {
        float waitTime;
        if(getNoiseWait() == true){
            waitTime = lastUpdate;
            while(lastUpdate == waitTime){
                Log.d("START","Start");
                startGameScreen();
                break;
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
        view.drawImage(startGame.startGame, pos, rotation, GameView.ImageOrigin.TopLeft);
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
        setNoiseOn();
        setNoiseWait();
    }

    public void startGameScreen() {
        this.gameSceneManager.setScene("gameScene");
    }
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