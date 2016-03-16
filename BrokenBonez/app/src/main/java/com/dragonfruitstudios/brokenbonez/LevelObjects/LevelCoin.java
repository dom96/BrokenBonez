package com.dragonfruitstudios.brokenbonez.LevelObjects;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;


public class LevelCoin extends LevelObject {

    String coinSound = "coin.mp3";
    String coinImage = "coin.png";
    Rect rect;

    public LevelCoin(AssetLoader assets, float x, float y, float rotation){
        this.v = new VectorF(x, y);
        String[] s = {coinSound, coinImage};
        this.assets = assets;
        this.assets.AddAssets(s);
        this.rotation = rotation;
        float width = assets.getBitmapByName(coinImage).getWidth();
        float height = assets.getBitmapByName(coinImage).getHeight();
        VectorF vector = this.getVector();
        this.rect = new Rect(vector, width, height);
        this.rect.recalculateBounds();
    }

    @Override
    public void draw(GameView gameView) {
        if (this.getVisible()) {
            gameView.drawImage(assets.getBitmapByName(coinImage), this.getVector(), this.rotation, GameView.ImageOrigin.TopLeft);
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
        if (this.getVisible()){
            this.setVisible(false);
            this.playSound();
            score.changeScoreBy(1);
        }
    }

    @Override
    public void playSound() {
        assets.getSoundByName(coinSound).play(false);
    }
}
