package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class CreditsState implements GameObject {
    AssetLoader assetLoader;
    GameSceneManager gameSceneManager;
    VectorF pos;
    float rotation;
    Bitmap philPixelArt;

    public CreditsState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        this.assetLoader.AddAssets(new String[]{"credits/philpixelart.png"});
        philPixelArt = assetLoader.getBitmapByName("credits/philpixelart.png");
        // set height for image below:
        pos = new VectorF(getScreenWidth() / 2 - 300, 300);
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
    }

    @Override
    public void updateSize(int width, int height) {

    }

    @Override
    public void draw(GameView view) {
        //set height for text below:
        view.drawTextCenter("Programmers:", getScreenWidth() / 2, getScreenHeight() / 2, Color.WHITE, 92);
        view.drawTextCenter("Dominik Picheta", getScreenWidth() / 2, getScreenHeight() / 2, Color.WHITE, 92);
        view.drawTextCenter("Andrew Mulholland", getScreenWidth() / 2, getScreenHeight() / 2, Color.WHITE, 92);
        view.drawTextCenter("Nathaniel McParland", getScreenWidth() / 2, getScreenHeight() / 2, Color.WHITE, 92);
        view.drawTextCenter("Artist:", getScreenWidth() / 2, getScreenHeight() / 2, Color.WHITE, 92);
        view.drawTextCenter("Amy-Leigh Shaw", getScreenWidth() / 2, getScreenHeight() / 2, Color.WHITE, 92);
        view.drawTextCenter("With Special Thanks To Phil Hanna,", getScreenWidth() / 2, getScreenHeight() / 2, Color.WHITE, 92);
        view.drawTextCenter("For Supporting Us Throughout Our Project.", getScreenWidth() / 2, getScreenHeight() / 2, Color.WHITE, 92);
        view.drawImage(philPixelArt, pos, rotation, GameView.ImageOrigin.TopLeft);
    }

    public void onTouchEvent(MotionEvent event) {

    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
}