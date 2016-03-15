package com.dragonfruitstudios.brokenbonez.LevelObjects;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public abstract class LevelObject implements GameObject {

    AssetLoader assets;
    boolean visible = true;
    protected VectorF v;
    float rotation;
    GameState gameState;

    public abstract void onHit();

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
