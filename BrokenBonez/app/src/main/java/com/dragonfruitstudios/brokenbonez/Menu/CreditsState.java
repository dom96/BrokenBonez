package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class CreditsState implements GameObject {
    AssetLoader assetLoader;
    GameSceneManager gameSceneManager;
    boolean creditsOn = false;
    boolean creditsWait = true;
    float waitTime = 0;
    VectorF pos;
    float rotation;
    Bitmap philPixelArt;

    public CreditsState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        this.assetLoader.AddAssets(new String[]{"credits/philpixelart.png"});
        philPixelArt = assetLoader.getBitmapByName("credits/philpixelart.png");
        pos = new VectorF(getScreenWidth() / 2, getScreenHeight() / 2);
        rotation = 0;

    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void update(float lastUpdate) {
        if(getCreditsWait() == true){
            waitTime += lastUpdate;
            if(waitTime > 2000) {
                creditsOn = true;
                creditsWait = false;
                waitTime = 0;
            }
        }
    }

    @Override
    public void updateSize(int width, int height) {

    }

    @Override
    public void draw(GameView view) {
        if(creditsOn == true){
            view.drawImage(philPixelArt, pos, rotation, GameView.ImageOrigin.TopLeft);
        }
    }

    public boolean getCreditsWait(){
        return creditsWait;
    }

    public void onTouchEvent(MotionEvent event) {

    }
}
