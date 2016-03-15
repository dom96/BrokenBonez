package com.dragonfruitstudios.brokenbonez.LevelObjects;

import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;


public class LevelCoin extends LevelObject {

    String coinSound = "coin.mp3";
    String coinImage = "coin.png";
    Rect rect;

    public LevelCoin(GameState gameState, float x, float y, float rotation){
        this.v = new VectorF(x, y);
        String[] s = {coinSound, coinImage};
        this.gameState = gameState;
        this.assets = gameState.getAssetLoader();
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
            gameView.drawImage(assets.getBitmapByName(coinImage), this.getVector(), this.rotation, GameView.ImageOrigin.Middle);
        }
    }

    @Override
    public void update(float lastUpdate) {
        if (this.rect.collisionTest(this.gameState.getBikeRect()).hasCollisions()){
            this.onHit();
        }
    }

    @Override
    public void updateSize(int width, int height) {

    }


    @Override
    public void onHit() {
        if (this.getVisible()){
            this.setVisible(false);
            this.playSound();
            this.gameState.score.changeScoreBy(1);
        }
    }

    @Override
    public void playSound() {
        assets.getSoundByName(coinSound).play(false);
    }
}
