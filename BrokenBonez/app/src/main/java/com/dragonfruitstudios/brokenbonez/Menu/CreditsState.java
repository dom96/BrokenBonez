package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class CreditsState implements GameObject {
    AssetLoader assetLoader;
    GameSceneManager gameSceneManager;
    VectorF pos;
    float rotation;
    Bitmap philPixelArt;

    Camera camera;

    public CreditsState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        this.assetLoader.AddAssets(new String[]{"credits/philpixelart.png"});
        philPixelArt = assetLoader.getBitmapByName("credits/philpixelart.png");
        rotation = 0;

        camera = new Camera(Graphics.getScreenWidth(), Graphics.getScreenHeight());
        gameSceneManager.gameView.setCamera(camera);
    }

    @Override
    public void update(float lastUpdate) {
        camera.scrollY(0.1f*lastUpdate);
        if (camera.getPos().y > 2000) {
            camera.getPos().set(0, 0);
        }
    }

    @Override
    public void updateSize(int width, int height) {

    }

    @Override
    public void draw(GameView view) {
        view.enableCamera();
        float x = Graphics.getScreenWidth() / 2;
        float currentHeight = Graphics.getScreenHeight() / 2;
        view.drawTextCenter("Programmers", x, currentHeight, Color.WHITE, 92);
        currentHeight += 100;
        view.drawTextCenter("Dominik Picheta", x, currentHeight, Color.LTGRAY, 72);
        currentHeight += 100;
        view.drawTextCenter("Andrew Mulholland", x, currentHeight, Color.LTGRAY, 72);
        currentHeight += 100;
        view.drawTextCenter("Nathaniel McParland", x, currentHeight, Color.LTGRAY, 72);
        currentHeight += 200;
        view.drawTextCenter("Artist", x, currentHeight, Color.WHITE, 92);
        currentHeight += 100;
        view.drawTextCenter("Amy-Leigh Shaw", x, currentHeight, Color.LTGRAY, 72);
        currentHeight += 200;
        view.drawTextCenter("With Special Thanks To Phil Hanna,", x,
                currentHeight, Color.WHITE, 92);
        currentHeight += 100;
        view.drawTextCenter("For Supporting Us Throughout Our Project.", x,
                currentHeight, Color.WHITE, 92);
        currentHeight += philPixelArt.getHeight() + 100;
        view.drawImage(philPixelArt, new VectorF(x, currentHeight), rotation,
                GameView.ImageOrigin.Middle);
        view.disableCamera();
    }

    public void onTouchEvent(MotionEvent event) {

    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
}