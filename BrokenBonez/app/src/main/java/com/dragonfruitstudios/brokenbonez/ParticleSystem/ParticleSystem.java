package com.dragonfruitstudios.brokenbonez.ParticleSystem;

import android.graphics.Bitmap;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;
import com.dragonfruitstudios.brokenbonez.Menu.Settings;
import com.dragonfruitstudios.brokenbonez.Menu.SettingsState;

public class ParticleSystem {
    private Particle particles[];
    GameSceneManager gameSceneManager;

    private int startYPos;
    private int startXPos;
    private int xPosRange;
    private int minSpeed;
    private int speedRange;
    private Bitmap bitmap;

    public ParticleSystem(int startYPos, int startXPos,
                          int xPosRange, int minSpeed, int speedRange,
                          Bitmap bitmap, int numParticles, GameSceneManager gameSceneManager) {
        this.gameSceneManager = gameSceneManager;
        this.startYPos = startYPos;
        this.startXPos = startXPos;
        this.xPosRange = xPosRange;
        this.minSpeed = minSpeed;
        this.bitmap = bitmap;
        this.speedRange = speedRange;
        particles = new Particle[numParticles];

        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(startYPos, startXPos,
                    xPosRange, minSpeed, speedRange,
                    bitmap);
        }
    }

    public void doDraw(GameView view) {
        for(int i = 0; i < particles.length; i++) {
            Particle particle = particles[i];
                particle.doDraw(view);
            }
    }

    public void updatePhysics(int altDelta) {
        for(int i = 0; i < particles.length; i++) {
            Particle particle = particles[i];
            particle.updatePhysics(altDelta);
        }
    }
}

class Particle {
    private int xpos;
    private int ypos;
    private int speed;
    private Bitmap bitmap;
    private VectorF pos;
    private float rotation;

    public Particle(int startYPos, int startXPos,
                    int xPosRange, int minSpeed, int speedRange,
                    Bitmap bitmap) {
        xpos = startXPos + (int) (Math.random() * xPosRange);
        ypos = (startYPos);

        this.speed = (int) (minSpeed + Math.random() * speedRange);
        this.bitmap = bitmap;
        pos = new VectorF(xpos, ypos);
        this.rotation = 0;
    }

    public void updatePhysics(int distChange) {
        ypos -= distChange * speed;
    }

    public void doDraw(GameView view) {
        view.drawImage(bitmap, pos, rotation, GameView.ImageOrigin.TopLeft);
    }


}