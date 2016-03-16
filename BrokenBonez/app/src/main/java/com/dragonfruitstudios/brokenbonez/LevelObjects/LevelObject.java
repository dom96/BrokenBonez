package com.dragonfruitstudios.brokenbonez.LevelObjects;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public abstract class LevelObject {

    AssetLoader assets;
    private boolean visible = true;
    protected VectorF v;
    float rotation;

    public abstract void draw(GameView view);

    public abstract void update(float lastUpdate, Bike bike, HighScore score);

    public abstract void updateSize(int width, int height);

    public abstract void onHit(Bike bike, HighScore score);

    public abstract void playSound();

    public float getX(){
        return this.v.getX();
    }

    public float getY(){
        return v.getY();
    }

    public void setX(float x){
        this.v.setX(x);
    }

    public void setY(float y){
        this.v.setY(y);
    }

    public VectorF getVector(){
        return this.v;
    }

    public boolean getVisible(){
        return this.visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }


}
