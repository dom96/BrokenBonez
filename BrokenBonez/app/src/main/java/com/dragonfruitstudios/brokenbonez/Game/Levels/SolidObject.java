package com.dragonfruitstudios.brokenbonez.Game.Levels;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

/**
 * A solid object to place on the Level. Usually represents the position of a LevelObject.
 */
public class SolidObject {
    public VectorF pos;
    public String theClass;

    public SolidObject(VectorF pos, String theClass) {
        this.pos = pos;
        this.theClass = theClass;
    }
}