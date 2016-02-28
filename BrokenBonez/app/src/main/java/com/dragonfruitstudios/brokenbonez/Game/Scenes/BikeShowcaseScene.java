package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Level;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class BikeShowcaseScene extends Scene {
    private class ShowcaseLevel extends Level {
        Simulator sim;
        AssetLoader loader;
        public ShowcaseLevel(AssetLoader loader, Simulator sim) {
            this.loader = loader;
            this.sim = sim;

            sim.createStaticBody(new Rect(new VectorF(100, 300), 800, 100));
        }

        @Override
        public AssetLoader getAssetLoader() {
            return loader;
        }

        @Override
        public Simulator getPhysicsSimulator() {
            return sim;
        }

        @Override
        public VectorF getStartPoint() {
            return new VectorF(400, 100);
        }
    }

    ShowcaseLevel level;
    Bike bike;
    public BikeShowcaseScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        level = new ShowcaseLevel(assetLoader, new Simulator());
        bike = new Bike(level, Bike.BodyType.Bike);
    }

    public void draw(GameView view) {
        bike.draw(view);
        level.getPhysicsSimulator().draw(view);
    }

    public void update(float lastUpdate) {
        level.getPhysicsSimulator().update(lastUpdate);
    }

    public void updateSize(int w, int h) {
        bike.updateSize(w, h);
    }

    public void onTouchEvent(MotionEvent event) {
        bike.setBodyType(Bike.BodyType.Bicycle);
    }

}
