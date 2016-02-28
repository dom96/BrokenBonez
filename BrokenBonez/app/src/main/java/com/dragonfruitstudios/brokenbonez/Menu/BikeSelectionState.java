package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class BikeSelectionState extends GameState implements GameObject {
    BikeSelectionLevel bikeSelectionLevel;
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private Simulator physicsSimulator;
    Bitmap background;
    final Bitmap scaledBackground;
    VectorF pos;
    float rotation;
    ImageButton next;
    ImageButton prev;
    ImageButton nextNext;
    ImageButton prevPrev;
    ImageButton select;
    private Camera camera;

    public BikeSelectionState(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        super();
        this.gameSceneManager = gameSceneManager;
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png"});
        this.physicsSimulator = new Simulator();
        camera = new Camera(0, 0);
        bikeSelectionLevel = new BikeSelectionLevel(this);
        this.assetLoader.AddAssets(new String[]{"nightsky.png", "tvnoise.png"});
        background = assetLoader.getBitmapByName("nightsky.png");
        scaledBackground = background.createScaledBitmap(background, getScreenWidth(), getScreenHeight(), false);
        pos = new VectorF(0, 0);
        rotation = 0;
        next = new ImageButton(assetLoader, getScreenWidth() / 4 * 3 - 100, getScreenHeight() / 4 * 2 + 20, 270, 60);
        prev = new ImageButton(assetLoader, getScreenWidth() / 4 - 150, getScreenHeight() / 4 * 2 + 20, 270, 60);
        nextNext = new ImageButton(assetLoader, getScreenWidth() / 4 * 3 - 100, getScreenHeight() / 4 * 3 - 40, 270, 60);
        prevPrev = new ImageButton(assetLoader, getScreenWidth() / 4 - 150, getScreenHeight() / 4 * 3 - 40, 270, 60);
        select = new ImageButton(assetLoader, getScreenWidth() / 4 + 175, getScreenHeight() / 4 * 3 + 100, 270, 60);
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
    }

    @Override
    public void updateSize(int w, int h) {
        bikeSelectionLevel.updateSize(w, h);
        camera.updateSize(w, h);
    }

    @Override
    public void draw(GameView view) {
        view.drawImage(scaledBackground, pos, rotation, GameView.ImageOrigin.TopLeft);
        view.drawImage(next.scaledNext, next.pos, next.rotation, GameView.ImageOrigin.TopLeft);
        view.drawImage(prev.scaledPrev, prev.pos, prev.rotation, GameView.ImageOrigin.TopLeft);
        view.drawImage(nextNext.scaledNext, nextNext.pos, nextNext.rotation, GameView.ImageOrigin.TopLeft);
        view.drawImage(prevPrev.scaledPrev, prevPrev.pos, prevPrev.rotation, GameView.ImageOrigin.TopLeft);
        view.drawImage(select.scaledSelect, select.pos, select.rotation, GameView.ImageOrigin.TopLeft);
        view.setCamera(camera);
        bikeSelectionLevel.draw(view);
        physicsSimulator.draw(view);
    }

    public void onTouchEvent(MotionEvent event) {
        select.onTouchEvent(event, (getScreenWidth() / 4 + 175), (getScreenHeight() / 4 * 3 + 100), ((getScreenWidth() / 4 + 175) + 269), ((getScreenHeight() / 4 * 3 + 100) + 59));
        next.onTouchEvent(event, (getScreenWidth() / 4 * 3 - 100), (getScreenHeight() / 4 * 2 + 20), ((getScreenWidth() / 4 * 3 - 100) + 269), ((getScreenHeight() / 4 * 2 + 20) + 59));
        nextNext.onTouchEvent(event, (getScreenWidth() / 4 * 3 - 100), (getScreenHeight() / 4 * 3 - 40), ((getScreenWidth() / 4 * 3 - 100) + 269), ((getScreenHeight() / 4 * 3 - 40) + 59));
        prev.onTouchEvent(event, (getScreenWidth() / 4 - 150), (getScreenHeight() / 4 * 2 + 20), ((getScreenWidth() / 4 - 150) + 269), ((getScreenHeight() / 4 * 2 + 20) + 59));
        prevPrev.onTouchEvent(event, (getScreenWidth() / 4 - 150), (getScreenHeight() / 4 * 3 - 40), ((getScreenWidth() / 4 - 150) + 269), ((getScreenHeight() / 4 * 3 - 40) + 59));

        if(select.isTouched() == true){
            select.isTouched = false;
            this.gameSceneManager.setScene("gameScene");
        }
        if(next.isTouched() == true){
            next.isTouched = false;
            drawSecondBike();
        }
        if(prev.isTouched() == true){
            prev.isTouched = false;

        }
        if(nextNext.isTouched() == true){
            nextNext.isTouched = false;

        }
        if(prevPrev.isTouched() == true){
            prevPrev.isTouched = false;
        }
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
    public Simulator getPhysicsSimulator() {
        return physicsSimulator;
    }
    public boolean drawSecondBike(){
        return true;
    }
}