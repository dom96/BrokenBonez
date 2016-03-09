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
    VectorF textPos;
    VectorF charNamePos;
    VectorF charImagePos;
    float rotation;
    ImageButton next;
    ImageButton prev;
    ImageButton nextNextNext;
    ImageButton nextNext;
    ImageButton prevPrev;
    ImageButton prevPrevPrev;
    ImageButton select;
    private Camera camera;
    Bike bike;
    BikeSelectionLevel level;
    Bitmap dirtbike;
    Bitmap bmx;
    Bitmap deedeetext, jennytext, leslietext, wanitatext;
    Bitmap deedee, jenny, leslie, wanita;
    int blue, purple, green, red, yellow, white, orange;
    int colorList[];
    Bitmap[] textList;
    Bitmap[] charNameList;
    Bitmap[] charImageList;
    int color;
    Bitmap text;
    Bitmap charName;
    Bitmap charImage;
    int i = 0;
    int j = 0;
    int k = 0;

    public BikeSelectionState(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        this.gameSceneManager = gameSceneManager;
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png"});
        this.physicsSimulator = new Simulator();
        camera = new Camera(0, 0);
        bikeSelectionLevel = new BikeSelectionLevel(this);
        this.assetLoader.AddAssets(new String[]{"nightsky.png", "tvnoise.png", "dirtbike.png", "bmx.png", "bikeselect.png", "deedeetext.png", "jennytext.png", "leslietext.png", "wanitatext.png", "deedee.png", "jenny.png", "leslie.png", "wanita.png"});
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
        deedeetext = assetLoader.getBitmapByName("deedeetext.png");
        jennytext = assetLoader.getBitmapByName("jennytext.png");
        leslietext = assetLoader.getBitmapByName("leslietext.png");
        wanitatext = assetLoader.getBitmapByName("wanitatext.png");
        deedee = assetLoader.getBitmapByName("deedee.png");
        jenny = assetLoader.getBitmapByName("jenny.png");
        leslie = assetLoader.getBitmapByName("leslie.png");
        wanita = assetLoader.getBitmapByName("wanita.png");
        colorList = new int[]{blue, purple, green, red, yellow, white, orange};
        textList = new Bitmap[]{dirtbike, bmx};
        charNameList = new Bitmap[]{deedeetext, jennytext, leslietext, wanitatext};
        charImageList = new Bitmap[]{deedee, jenny, leslie, wanita};
        scaledBackground = background.createScaledBitmap(background, getScreenWidth(), getScreenHeight(), false);
        pos = new VectorF(0, 0);
        textPos = new VectorF((getScreenWidth() / 10) * 2, (getScreenHeight() / 10) * 4 - 60);
        charNamePos = new VectorF((getScreenWidth() / 10) * 2, (getScreenHeight() / 10) * 8 - 60);
        charImagePos = new VectorF((getScreenWidth() / 10) * 7, (getScreenHeight() / 10) * 5 + 20);
        rotation = 0;
        next = new ImageButton("next.png", assetLoader, (getScreenWidth() / 10) * 4 + 80, (getScreenHeight() / 10) * 4 - 60, 75, 60);
        nextNext = new ImageButton("next.png", assetLoader, (getScreenWidth() / 10) * 4 + 80, (getScreenHeight() / 10) * 6 - 60, 75, 60);
        nextNextNext = new ImageButton("next.png", assetLoader, (getScreenWidth() / 10) * 4 + 80, (getScreenHeight() / 10) * 8 - 60, 75, 60);
        prev = new ImageButton("prev.png", assetLoader, (getScreenWidth() / 10), (getScreenHeight() / 10) * 4 - 60, 75, 60);
        prevPrev = new ImageButton("prev.png", assetLoader, (getScreenWidth() / 10), (getScreenHeight() / 10) * 6 - 60, 75, 60);
        prevPrevPrev = new ImageButton("prev.png", assetLoader, (getScreenWidth() / 10), (getScreenHeight() / 10) * 8 - 60, 75, 60);
        select = new ImageButton("select.png", assetLoader, (getScreenWidth() / 10) * 2, (getScreenHeight() / 10) * 9 - 20, 270, 60);
        level = new BikeSelectionLevel(this);
        bike = new Bike(level, Bike.BodyType.Bike);
        bike.setColor(Color.BLUE);
        color = colorList[i];
        text = textList[j];
        charName = charNameList[k];
        charImage = charImageList[k];
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
        view.drawRect((getScreenHeight() / 10) * 3 + 60, 350, (getScreenWidth() / 10) * 4 - 10, 450, color);
        view.drawImage(text, textPos, rotation, GameView.ImageOrigin.TopLeft);
        view.drawImage(charName, charNamePos, rotation, GameView.ImageOrigin.TopLeft);
        view.drawImage(charImage, charImagePos, rotation, GameView.ImageOrigin.TopLeft);
        next.draw(view);
        prev.draw(view);
        nextNext.draw(view);
        nextNextNext.draw(view);
        prevPrev.draw(view);
        prevPrevPrev.draw(view);
        select.draw(view);
        view.setCamera(camera);
        bikeSelectionLevel.draw(view);
        physicsSimulator.draw(view);
        bike.draw(view);
        level.getPhysicsSimulator().draw(view);
    }

    public void onTouchEvent(MotionEvent event) {
        next.onTouchEvent(event, ((getScreenWidth() / 10) * 4 + 80), ((getScreenHeight() / 10) * 4 - 60), (((getScreenWidth() / 10) * 4 + 80) + 74), (((getScreenHeight() / 10) * 4 - 60) + 59));
        nextNext.onTouchEvent(event, ((getScreenWidth() / 10) * 4 + 80), ((getScreenHeight() / 10) * 6 - 60), (((getScreenWidth() / 10) * 4 + 80) + 74), (((getScreenHeight() / 10) * 6 - 60) + 59));
        nextNextNext.onTouchEvent(event, ((getScreenWidth() / 10) * 4 + 80), ((getScreenHeight() / 10) * 8 - 60), (((getScreenWidth() / 10) * 4 + 80) + 74), (((getScreenHeight() / 10) * 8 - 60) + 74));
        prev.onTouchEvent(event, ((getScreenWidth() / 10)), ((getScreenHeight() / 10) * 4 - 60), (((getScreenWidth() / 10)) + 74), (((getScreenHeight() / 10) * 4 - 60) + 59));
        prevPrev.onTouchEvent(event, (getScreenWidth() / 10), ((getScreenHeight() / 10) * 6 - 60), (((getScreenWidth() / 10)) + 74), (((getScreenHeight() / 10) * 6 - 60) + 59));
        prevPrevPrev.onTouchEvent(event, ((getScreenWidth() / 10)), ((getScreenHeight() / 10) * 8 - 60), (((getScreenWidth() / 10)) + 74), (((getScreenHeight() / 10) * 8 - 60) + 59));
        select.onTouchEvent(event, ((getScreenWidth() / 10) * 2), ((getScreenHeight() / 10) * 9 - 20), (((getScreenWidth() / 10) * 2) + 269), (((getScreenHeight() / 10) * 9 - 20) + 59));

        if(select.isTouched() == true){
            select.isTouched = false;
            GameScene gameScene = (GameScene)this.gameSceneManager.getGameSceneByName("gameScene");
            gameScene.newGame(bike.getBodyType(), bike.getColor());
            //this.gameSceneManager.setScene("gameScene");
            this.gameSceneManager.setScene("levelSelectionScene");
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
                case 0:
                    bike.reset();
                    bike.setBodyType(Bike.BodyType.Bike);
                    break;
                case 1:
                    bike.reset();
                    bike.setBodyType(Bike.BodyType.Bicycle);
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
                case 0:
                    bike.reset();
                    bike.setBodyType(Bike.BodyType.Bike);
                    break;
                case 1:
                    bike.reset();
                    bike.setBodyType(Bike.BodyType.Bicycle);
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

        if(nextNextNext.isTouched() == true){
            nextNextNext.isTouched = false;
            k++;
            if(k > 3){
                k = 0;
            }
            setCharName(k);
            setCharImage(k);
            switch (k) {
                case 0:
                    setCharName(0);
                    break;
                case 1:
                    setCharName(1);
                    break;
                case 2:
                    setCharName(2);
                    break;
                case 3:
                    setCharName(3);
                    break;
            }
        }

        if(prevPrevPrev.isTouched() == true) {
            prevPrevPrev.isTouched = false;
            k--;
            if (k < 0) {
                k = 3;
            }
            setCharName(k);
            setCharImage(k);
            switch (k) {
                case 0:
                    setCharName(0);
                    break;
                case 1:
                    setCharName(1);
                    break;
                case 2:
                    setCharName(2);
                    break;
                case 3:
                    setCharName(3);
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
    public void setCharName(int k){charName = charNameList[k];}
    public void setCharImage(int k){charImage = charImageList[k];}
    public void setColor(int i){
        color = colorList[i];
    }
}