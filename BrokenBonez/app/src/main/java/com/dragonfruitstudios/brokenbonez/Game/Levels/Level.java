package com.dragonfruitstudios.brokenbonez.Game.Levels;

import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Intersector;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Manifold;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

/**
 * This class specifies the bare minimum methods needed by the Bike. That way a custom level can
 * be created which does not do much but can still draw a bike (as is done in one of the menus).
 */
public abstract class Level {

    abstract public AssetLoader getAssetLoader();

    abstract public Simulator getPhysicsSimulator();

    /**
     * Returns the Bike's starting point.
     */
    abstract public VectorF getStartPoint();

    /**
     * Determines whether any of the physical StaticBodies in the Level collide with
     * the specified shape.
     */
    public boolean collidesWith(Intersector shape) {
        return getPhysicsSimulator().collidesWith(shape);
    }

    /**
     * Called by the Bike class when it flips over and its body collides with something.
     */
    public void onBikeCrash() {
        Log.w("Level", "No implementation for onBikeCrash!");
    }
}
