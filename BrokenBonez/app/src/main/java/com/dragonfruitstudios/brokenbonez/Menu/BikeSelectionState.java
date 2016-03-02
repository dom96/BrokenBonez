package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.GameScene;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class BikeSelectionState implements GameObject {
    BikeSelectionLevel bikeSelectionLevel;
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private Simulator physicsSimulator;
    Bitmap background;
    final Bitmap scaledBackground;
    VectorF pos;
    VectorF colorPos;
    VectorF textPos;
    float rotation;
    ImageButton next;
    ImageButton prev;
    ImageButton nextNext;
    ImageButton prevPrev;
    ImageButton select;
    private Camera camera;
    Bike bike;
    BikeSelectionLevel level;
    Bitmap dirtbike;
    Bitmap bmx;
    int blue, purple, green, red, yellow, white, orange;
    int colorList[];
    Bitmap[] textList;
    int color;
    Bitmap text;
    int i = 0;
    int j = 0;

    public BikeSelectionState(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        this.gameSceneManager = gameSceneManager;
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png"});
        this.physicsSimulator = new Simulator();
        camera = new Camera(0, 0);
        bikeSelectionLevel = new BikeSelectionLevel(this);
        this.assetLoader.AddAssets(new String[]{"nightsky.png", "tvnoise.png", "dirtbike.png", "bmx.png"});
        background = assetLoader.getBitmapByName("nightsky.png");
        blue = Color.BLUE;
        purple = Color.parseColor("#7b2b80");
        green = Color.parseColor("#008000");
        red = Color.parseColor("#FF0000");
        yellow = Color.parseColor("#FFFF00");
        white = Color.parseColor("#FFFFFF");
        orange = Color.parseColor("#FFA500");
        dirtbike = assetLoader.getBitmapByName("dirtbike.png");
        bmx = assetLoader.getBitmapByName("bmx.png");
        colorList = new int[]{blue, purple, green, red, yellow, white, orange};
        textList = new Bitmap[]{dirtbike, bmx};
        scaledBackground = background.createScaledBitmap(background, getScreenWidth(), getScreenHeight(), false);
        pos = new VectorF(0, 0);
        textPos = new VectorF(getScreenWidth() / 4 + 190, getScreenHeight() / 4 * 2 + 30);
        colorPos = new VectorF(getScreenWidth() / 4 + 260, getScreenHeight() / 4 * 2 + 130);
        rotation = 0;
        next = new ImageButton("next.png", assetLoader, getScreenWidth() / 4 * 3 - 100, getScreenHeight() / 4 * 2 + 20, 270, 60);
        prev = new ImageButton("prev.png", assetLoader, getScreenWidth() / 4 - 150, getScreenHeight() / 4 * 2 + 20, 270, 60);
        nextNext = new ImageButton("next.png", assetLoader, getScreenWidth() / 4 * 3 - 100, getScreenHeight() / 4 * 3 - 40, 270, 60);
        prevPrev = new ImageButton("prev.png", assetLoader, getScreenWidth() / 4 - 150, getScreenHeight() / 4 * 3 - 40, 270, 60);
        select = new ImageButton("select.png", assetLoader, getScreenWidth() / 4 + 175, getScreenHeight() / 4 * 3 + 100, 270, 60);
        level = new BikeSelectionLevel(this);
        bike = new Bike(level, Bike.BodyType.Bike);
        bike.setColor(Color.BLUE);
        color = colorList[i];
        text = textList[j];
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void update(float lastUpdate) {
        physicsSimulator.update(lastUpdate);
        bikeSelectionLevel.update(lastUpdate);
        level.getPhysicsSimulator().update(lastUpdate);
    }

    @Override
    public void updateSize(int w, int h) {
        bikeSelectionLevel.updateSize(w, h);
        camera.updateSize(w, h);
        bike.updateSize(w, h);
    }

    @Override
    public void draw(GameView view) {
        view.drawImage(scaledBackground, pos, rotation, GameView.ImageOrigin.TopLeft);
        view.drawRect(getScreenWidth()/2 + 60 - 100, 500, getScreenWidth()/2 + 60, 600, color);
        view.drawImage(text, textPos, rotation, GameView.ImageOrigin.TopLeft);
        next.draw(view);
        prev.draw(view);
        nextNext.draw(view);
        prevPrev.draw(view);
        select.draw(view);
        view.setCamera(camera);
        bikeSelectionLevel.draw(view);
        physicsSimulator.draw(view);
        bike.draw(view);
        level.getPhysicsSimulator().draw(view);
    }

    public void onTouchEvent(MotionEvent event) {
        select.onTouchEvent(event, (getScreenWidth() / 4 + 175), (getScreenHeight() / 4 * 3 + 100), ((getScreenWidth() / 4 + 175) + 269), ((getScreenHeight() / 4 * 3 + 100) + 59));
        next.onTouchEvent(event, (getScreenWidth() / 4 * 3 - 100), (getScreenHeight() / 4 * 2 + 20), ((getScreenWidth() / 4 * 3 - 100) + 269), ((getScreenHeight() / 4 * 2 + 20) + 59));
        nextNext.onTouchEvent(event, (getScreenWidth() / 4 * 3 - 100), (getScreenHeight() / 4 * 3 - 40), ((getScreenWidth() / 4 * 3 - 100) + 269), ((getScreenHeight() / 4 * 3 - 40) + 59));
        prev.onTouchEvent(event, (getScreenWidth() / 4 - 150), (getScreenHeight() / 4 * 2 + 20), ((getScreenWidth() / 4 - 150) + 269), ((getScreenHeight() / 4 * 2 + 20) + 59));
        prevPrev.onTouchEvent(event, (getScreenWidth() / 4 - 150), (getScreenHeight() / 4 * 3 - 40), ((getScreenWidth() / 4 - 150) + 269), ((getScreenHeight() / 4 * 3 - 40) + 59));
        
        if(select.isTouched() == true){
            select.isTouched = false;
            GameScene gameScene = (GameScene)this.gameSceneManager.getGameSceneByName("gameScene");
            gameScene.newGame(bike.getBodyType(), bike.getColor());
            this.gameSceneManager.setScene("gameScene");
        }
        // bike model
        if(next.isTouched() == true){
            next.isTouched = false;
            j++;
            if(j > 1){
                j = 0;
            }
            setText(j);
            switch(j){
                case 0: bike.setBodyType(Bike.BodyType.Bike);
                    break;
                case 1: bike.setBodyType(Bike.BodyType.Bicycle);
                    break;
            }
        }
        // bike model
        if(prev.isTouched() == true){
            prev.isTouched = false;
            j--;
            if(j < 0){
                j = 1;
            }
            setText(j);
            switch(j){
                case 0: bike.setBodyType(Bike.BodyType.Bike);
                    break;
                case 1: bike.setBodyType(Bike.BodyType.Bicycle);
                    break;
            }
        }
        // color
        if(nextNext.isTouched() == true){
            nextNext.isTouched = false;
            i++;
            if(i > 6){
                i = 0;
            }
            setColor(i);
            switch (i) {
                case 0:  bike.setColor(Color.BLUE);
                    setColor(0);
                    break;
                case 1:  bike.setColor(Color.parseColor("#7b2b80"));
                    setColor(1);
                    break;
                case 2:  bike.setColor(Color.parseColor("#008000"));
                    setColor(2);
                    break;
                case 3:  bike.setColor(Color.parseColor("#FF0000"));
                    setColor(3);
                    break;
                case 4:  bike.setColor(Color.parseColor("#FFFF00"));
                    setColor(4);
                    break;
                case 5:  bike.setColor(Color.parseColor("#FFFFFF"));
                    setColor(5);
                    break;
                case 6: bike.setColor(Color.parseColor("#FFA500"));
                    setColor(6);
                    break;
            }
        }
        // color
        if(prevPrev.isTouched() == true){
            prevPrev.isTouched = false;
            i--;
            if(i < 0){
                i = 6;
            }
            setColor(i);
            switch (i) {
                case 0:
                    bike.setColor(Color.BLUE);
                    setColor(0);
                    break;
                case 1:
                    bike.setColor(Color.parseColor("#800080"));
                    setColor(1);
                    break;
                case 2:
                    bike.setColor(Color.parseColor("#008000"));
                    setColor(2);
                    break;
                case 3:
                    bike.setColor(Color.parseColor("#FF0000"));
                    setColor(3);
                    break;
                case 4:
                    bike.setColor(Color.parseColor("#FFFF00"));
                    setColor(4);
                    break;
                case 5:
                    bike.setColor(Color.parseColor("#FFFFFF"));
                    setColor(5);
                    break;
                case 6:
                    bike.setColor(Color.parseColor("#FFA500"));
                    setColor(6);
                    break;
            }
        }
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
    public Simulator getPhysicsSimulator() {
        return physicsSimulator;
    }

    public void setText(int j){
        text = textList[j];
    }

    public void setColor(int i){
        color = colorList[i];
    }
}