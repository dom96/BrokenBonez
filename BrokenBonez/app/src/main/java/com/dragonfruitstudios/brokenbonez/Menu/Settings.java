package com.dragonfruitstudios.brokenbonez.Menu;

import java.io.Serializable;

public class Settings implements Serializable {
    public boolean boolSoundEnabled;
    public boolean boolAccelEnabled;
    public boolean boolParticlesEnabled;

    public Settings(boolean boolSoundEnabled, boolean boolAccelEnabled, boolean boolParticlesEnabled){
        this.boolSoundEnabled = boolSoundEnabled;
        this.boolAccelEnabled = boolAccelEnabled;
        this.boolParticlesEnabled = boolParticlesEnabled;
    }
}