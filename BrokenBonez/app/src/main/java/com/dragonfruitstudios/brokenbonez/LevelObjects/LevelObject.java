package com.dragonfruitstudios.brokenbonez.LevelObjects;


import android.app.Activity;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.GameObject;
import com.dragonfruitstudios.brokenbonez.GameView;
import com.dragonfruitstudios.brokenbonez.VectorF;

public abstract class LevelObject implements GameObject {

    AssetLoader assets;
    boolean visible = true;
    boolean solid;
    GameView gameView;
    Activity activity;
    protected VectorF v = new VectorF(this.getX(), this.getY());
    float rotation;


    public abstract void onHit();

    public abstract void playSound();

    public boolean isSolid(){
        return this.solid;
    }

    public float getX(){
        return v.getX();
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

    protected void setupAssets(String[] s){
        Activity activity = (Activity) gameView.getContext();
        this.assets = new AssetLoader(activity, s);
    }


}
