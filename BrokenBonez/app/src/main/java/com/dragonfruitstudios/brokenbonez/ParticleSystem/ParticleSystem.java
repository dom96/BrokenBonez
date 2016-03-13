package com.dragonfruitstudios.brokenbonez.ParticleSystem;

import android.graphics.Bitmap;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

class Particle {
    int partX;
    int partY;
    int partSpeed;
    Bitmap partImage;
    //VectorF pos;
    float rotation;

    public Particle(int startYPos, int yPosRange, int startXPos,
                    int xPosRange, int minSpeed, int speedRange,
                    Bitmap bitmap) {

        partX = startXPos + (int) (Math.random() * xPosRange);
        partY = startYPos + (int) (Math.random() * yPosRange);
        this.partSpeed = (int) (minSpeed + Math.random() * speedRange);
        this.partImage = bitmap;
        //pos = new VectorF(partX, partY);
        rotation = 0;
    }
    public void updatePhysics(int distChange) {
        partY -= distChange * partSpeed;
    }
    public void doDraw(GameView view, VectorF pos) { view.drawImage(partImage, pos, rotation, GameView.ImageOrigin.TopLeft); }
    public boolean outOfSight() { return partY <= -1 * partImage.getHeight(); }
}

public class ParticleSystem {
    Particle particles[];
    private int startYPos;
    private int startXPos;
    private int xPosRange;
    private int yPosRange;
    private int minSpeed;
    private int speedRange;
    private Bitmap bitmap;
    boolean respawnParticle;

    public ParticleSystem(int startYPos, int yPosRange, int startXPos,
                          int xPosRange, int minSpeed, int speedRange,
                          Bitmap bitmap, boolean respawnParticle, int numParticles) {

        particles = new Particle[numParticles];
        this.startYPos = startYPos;
        this.startXPos = startXPos;
        this.yPosRange = yPosRange;
        this.xPosRange = xPosRange;
        this.minSpeed = minSpeed;
        this.bitmap = bitmap;
        this.speedRange = speedRange;
        this.respawnParticle = respawnParticle;

        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(startYPos, yPosRange, startXPos,
                    xPosRange, minSpeed, speedRange,
                    bitmap);
        }
    }

    public void doDraw(GameView view, VectorF pos) {
        for(int i = 0; i < particles.length; i++) {
            Particle particle = particles[i];
            particle.doDraw(view, pos);
        }
    }

    public void updatePhysics(int altDelta) {
        for(int i = 0; i < particles.length; i++) {
            Particle particle = particles[i];
            particle.updatePhysics(altDelta);

            // If this particle is completely out of sight
            // replace it with a new one.
            if(particle.outOfSight()) {
                particles[i] = new Particle(startYPos, startXPos,
                        xPosRange, yPosRange, minSpeed, speedRange,
                        bitmap);
            }
        }
    }
}