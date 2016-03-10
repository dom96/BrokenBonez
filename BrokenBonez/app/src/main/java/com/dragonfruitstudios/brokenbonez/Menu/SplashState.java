package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class SplashState implements GameObject {
    AssetLoader assetLoader;
    GameSceneManager gameSceneManager;
    Bitmap splashScreen;
    final Bitmap scaledSplashScreen;
    boolean splashOn = true;
    boolean splashWait = true;
    float waitTime = 0;
    VectorF pos;
    float rotation;


    public SplashState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        this.assetLoader.AddAssets(new String[]{"menu/dragonfruitstudiossplash.png"});
        splashScreen = assetLoader.getBitmapByName("menu/dragonfruitstudiossplash.png");
        scaledSplashScreen = splashScreen.createScaledBitmap(splashScreen, getScreenWidth(), getScreenHeight(), false);
        pos = new VectorF(0, 0);
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
        if(getSplashWait() == true){
            waitTime += lastUpdate;
            if(waitTime > 5000) {
                splashOn = false;
                splashWait = false;
                waitTime = 0;
                this.gameSceneManager.setScene("menuScene");
            }
        }
    }

    @Override
    public void updateSize(int width, int height) {

    }

    @Override
    public void draw(GameView view) {
        view.drawImage(scaledSplashScreen, pos, rotation, GameView.ImageOrigin.TopLeft);
    }

    public boolean getSplashWait(){
        return splashWait;
    }
}
