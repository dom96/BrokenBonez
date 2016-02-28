package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class BikeSelectionState extends GameState implements GameObject {
    BikeSelectionLevel bikeSelectionLevel;
    Bike selectedBike;
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private Simulator physicsSimulator;
    Bitmap background;
    final Bitmap scaledBackground;
    VectorF pos;
    float rotation;
    ImageButton next;
    ImageButton prev;
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
        selectedBike = new Bike(bikeSelectionLevel);
        this.assetLoader.AddAssets(new String[]{"blanktv.png", "tvnoise.png"});
        background = assetLoader.getBitmapByName("blanktv.png");
        scaledBackground = background.createScaledBitmap(background, getScreenWidth(), getScreenHeight(), false);
        pos = new VectorF(0, 0);
        rotation = 0;
        next = new ImageButton(assetLoader, getScreenWidth() / 4 * 3 - 100, getScreenHeight() / 4 * 2 + 40, 270, 60);
        prev = new ImageButton(assetLoader, getScreenWidth() / 4 - 150, getScreenHeight() / 4 * 2 + 40, 270, 60);
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void update(float lastUpdate) {
        selectedBike.update(lastUpdate);
        physicsSimulator.update(lastUpdate);
        bikeSelectionLevel.update(lastUpdate, selectedBike.getPos());
    }

    @Override
    public void updateSize(int w, int h) {
        bikeSelectionLevel.updateSize(w, h);
        selectedBike.updateSize(w, h);
        camera.updateSize(w, h);
    }

    @Override
    public void draw(GameView view) {
        //view.drawImage(scaledBackground, pos, rotation, GameView.ImageOrigin.TopLeft);
        view.drawImage(next.scaledNext, next.pos, next.rotation, GameView.ImageOrigin.TopLeft);
        view.drawImage(prev.scaledPrev, prev.pos, prev.rotation, GameView.ImageOrigin.TopLeft);
        view.setCamera(camera);
        bikeSelectionLevel.draw(view);
        selectedBike.draw(view);
        physicsSimulator.draw(view);
    }

    public void onTouchEvent(MotionEvent event) {

    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
    public Simulator getPhysicsSimulator() {
        return physicsSimulator;
    }
}
