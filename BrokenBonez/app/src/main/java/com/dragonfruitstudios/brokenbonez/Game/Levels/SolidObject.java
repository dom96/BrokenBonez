package com.dragonfruitstudios.brokenbonez.Game.Levels;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class SolidObject {
    public VectorF pos;
    public String theClass;

    public SolidObject(VectorF pos, String theClass) {
        this.pos = pos;
        this.theClass = theClass;
    }
}