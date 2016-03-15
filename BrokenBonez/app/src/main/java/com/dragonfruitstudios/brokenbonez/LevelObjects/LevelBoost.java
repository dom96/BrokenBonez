package com.dragonfruitstudios.brokenbonez.LevelObjects;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;


public class LevelBoost extends LevelObject {

    private final String boostSound = "coin.mp3";
    private final String boostImage = "boostgameicon.png";
    private Rect rect;

    public LevelBoost(AssetLoader assets, float x, float y, float rotation){
        this.v = new VectorF(x, y);
        String[] s = {boostSound, boostImage};
        this.assets = assets;
        this.assets.AddAssets(s);
        this.rotation = rotation;
        float width = assets.getBitmapByName(boostImage).getWidth();
        float height = assets.getBitmapByName(boostImage).getHeight();
        VectorF vector = this.getVector();
        this.rect = new Rect(vector, width, height);
        this.rect.recalculateBounds();
    }

    @Override
    public void draw(GameView gameView) {
        this.rect.draw(gameView);
        if (this.getVisible()) {
            gameView.drawImage(assets.getBitmapByName(boostImage), this.getVector(), this.rotation, GameView.ImageOrigin.Middle);
        }
    }

    @Override
    public void update(float lastUpdate, Bike bike, HighScore score) {
        if (this.rect.collisionTest(bike.getBodyRect()).hasCollisions()){
            this.onHit(bike, score);
        }
    }

    @Override
    public void updateSize(int width, int height) {

    }


    @Override
    public void onHit(Bike bike, HighScore score) {
        if (this.getVisible()) {
            this.setVisible(false);
            //this.playSound();
            bike.getLeftWheel().getVelocity().set(1000, 0);
        }
    }

    @Override
    public void playSound() {
        assets.getSoundByName(boostSound).play(false);
    }
}
