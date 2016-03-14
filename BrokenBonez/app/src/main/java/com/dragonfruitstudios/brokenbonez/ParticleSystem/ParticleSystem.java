package com.dragonfruitstudios.brokenbonez.ParticleSystem;

import android.graphics.Bitmap;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

class Particle {
    int partPosX;
    int partPosY;
    int partSpeed;
    Bitmap partImage;
    float partImagePosX;
    float partImagePosY;
    VectorF partImagePos;
    float partImageRotation;

    public Particle(int startYPos, int yPosRange, int startXPos,
                    int xPosRange, int minSpeed, int speedRange,
                    Bitmap bitmap) {

        partPosX = startXPos + (int) (Math.random() * xPosRange);
        partPosY = startYPos + (int) (Math.random() * yPosRange);

        partImagePosY = yPosRange;
        partImagePosX = xPosRange;

        this.partSpeed = (int) (minSpeed + Math.random() * speedRange);
        this.partImage = bitmap;
        partImagePos = new VectorF(partImagePosX, partImagePosY);
        partImageRotation = 0;
    }
    public void doDraw(GameView view) {
        view.drawImage(partImage, partImagePos, partImageRotation, GameView.ImageOrigin.TopLeft);
    }

    public void updatePhysics(int distChange) {
        partPosX -= distChange * partSpeed;
        partPosY -= distChange * partSpeed;
    }
    public boolean outOfSight() { return partPosY <= -1 * partImage.getHeight(); }
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

    public ParticleSystem(int startYPos, int yPosRange, int startXPos,
                          int xPosRange, int minSpeed, int speedRange,
                          Bitmap bitmap, int numParticles) {

        particles = new Particle[numParticles];
        this.startYPos = startYPos;
        this.startXPos = startXPos;
        this.yPosRange = yPosRange;
        this.xPosRange = xPosRange;
        this.minSpeed = minSpeed;
        this.bitmap = bitmap;
        this.speedRange = speedRange;

        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(startYPos, yPosRange, startXPos,
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