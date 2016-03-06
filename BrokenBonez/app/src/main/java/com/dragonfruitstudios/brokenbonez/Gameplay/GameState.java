package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;
import com.dragonfruitstudios.brokenbonez.R;
import com.plattysoft.leonids.ParticleSystem;


public class GameState {
    GameLevel currentLevel;
    Bike bike;
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private Simulator physicsSimulator;

    private Camera camera;

    public GameState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.gameSceneManager = gameSceneManager;

        // Load assets.
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png",
                "bike/body_two.png"});
        this.assetLoader.AddAssets(new String[]{"bikeEngine.mp3", "bikeEngineRev.mp3",
                "brokenboneztheme.ogg"});
        this.assetLoader.AddAssets(new String[] {"levels/level1/dog.jpg"});

        // Create a new physics simulator.
        this.physicsSimulator = new Simulator();

        camera = new Camera(0, 0);

        currentLevel = new GameLevel(this);
        bike = new Bike(currentLevel, Bike.BodyType.Bike);
    }

    public void newGame(Bike.BodyType bikeBodyType, int bikeColor) {
        bike.setColor(bikeColor);
        bike.setBodyType(bikeBodyType);
        bike.reset();
    }

    //VectorF p = new VectorF(0, 0);
    public void update(float lastUpdate) {
        //camera.update(lastUpdate);
        //bike.update(lastUpdate);
        //physicsSimulator.update(lastUpdate);
        //currentLevel.update(lastUpdate, bike.getPos());
        //camera.centerHorizontally(bike.getPos().x);
        //camera.setPos(camera.getPos().added(new VectorF(6f, 0)));
        //bike.setVelocity(new VectorF(700, 0));
        //p.set(p.x - 3f, 0);
        p -= 300f * (lastUpdate/1000);
    }

    public void updateSize(int w, int h) {
        currentLevel.updateSize(w, h);
        bike.updateSize(w, h);
        camera.updateSize(w, h);
    }
    Bitmap g = null;
    float p = 0;
    public void draw(GameView view) {
        view.setCamera(camera);
        if (g == null) {
            g = assetLoader.getBitmapByName("levels/level1/dog.jpg");
        }
        //Log.d("Pos", "Starting at: " + p);
        Trace.beginSection("DrawImage");
        for (int i = 0; i < 100; i++) {
            //Trace.beginSection("AllocVec");
            //VectorF pa = p.added(new VectorF(g.getWidth() * i, 0));
            //Trace.endSection();

            float pa = p + g.getWidth() * i;
            if (pa >= -100 && pa <= 1500) {
                view.drawImage(g, pa, 0, 0, GameView.ImageOrigin.TopLeft);
            }

        }
        Trace.endSection();
        //currentLevel.draw(view);
        //bike.draw(view);
        //physicsSimulator.draw(view);
    }

    public void setBikeAcceleration(float strength) {
        bike.setTorque(strength);
    }
    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
    public Simulator getPhysicsSimulator() {
        return physicsSimulator;
    }
}

