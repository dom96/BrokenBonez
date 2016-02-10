package com.dragonfruitstudios.brokenbonez.LevelObjects;

import android.app.Activity;

import com.dragonfruitstudios.brokenbonez.Game.GameView;


public class LevelCoin extends LevelObject {

    String coinSound = "coin.mp3";
    String coinImage = "coin.png";

    public LevelCoin(Activity activity, float x, float y, float rotation){
        this.activity = activity;
        String[] s = {coinSound, coinImage};
        setupAssets(s);
        this.setX(x);
        this.setY(y);
        this.rotation = rotation;
    }

    @Override
    public void draw(GameView gameView) {
        if (visible) {
            gameView.drawImage(assets.getBitmapByName(coinImage), this.getVector(), this.rotation, GameView.ImageOrigin.Middle);
        }
    }

    @Override
    public void update(float lastUpdate) {

    }

    @Override
    public void updateSize(int width, int height) {

    }


    @Override
    public void onHit() {
        this.setVisible(false);
        this.playSound();
        //Update player score here
    }

    @Override
    public void playSound() {
        assets.getSoundByName(coinSound).play(false);
    }
}
