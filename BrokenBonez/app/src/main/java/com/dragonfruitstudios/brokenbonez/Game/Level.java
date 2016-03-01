package com.dragonfruitstudios.brokenbonez.Game;

import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public abstract class Level {

    abstract public AssetLoader getAssetLoader();

    abstract public Simulator getPhysicsSimulator();

    /**
     * Returns the Bike's starting point.
     */
    abstract public VectorF getStartPoint();
}
