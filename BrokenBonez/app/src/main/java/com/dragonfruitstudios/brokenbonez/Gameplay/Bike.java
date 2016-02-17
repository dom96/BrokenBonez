package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Circle;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Manifold;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Physics.DynamicBody;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;

public class Bike implements GameObject {
    // Some constants related to the wheels.
    final float wheelSeparation = 74f;
    final float wheelRadius = 20f;
    final float wheelMass = 200f;

    // The current level that this bike is on.
    Level currentLevel;

    // The wheel's of this bike.
    DynamicBody leftWheel;
    DynamicBody rightWheel;

    // The start position of this bike.
    VectorF startPos;

    public Bike(Level currentLevel) {
        this.currentLevel = currentLevel;

        Circle circle = new Circle(new VectorF(0, 0), wheelRadius);
        leftWheel = currentLevel.getPhysicsSimulator().createDynamicBody(circle, wheelMass);
        rightWheel = currentLevel.getPhysicsSimulator().createDynamicBody(circle, wheelMass);

        // Create a constraint between the two wheels so that they are kept together.
        currentLevel.getPhysicsSimulator().createConstraint(leftWheel, rightWheel, wheelSeparation);

        // TODO: Just some small hardcoded tests.
        this.rightWheel.getVelocity().set(20, 0);
        //this.rightWheel.acceleration.set(40, 0);
        this.leftWheel.setAngularVelocity(2);
    }

    public void draw(GameView gameView) {
        gameView.enableCamera();
        // Draw the wheels.
        Bitmap wheel = currentLevel.getAssetLoader().getBitmapByName("bike/wheel_basic.png");
        gameView.drawImage(wheel, leftWheel.getPos(), leftWheel.getRotation(),
                GameView.ImageOrigin.Middle);
        gameView.drawImage(wheel, rightWheel.getPos(), rightWheel.getRotation(),
                GameView.ImageOrigin.Middle);

        // Draw the bike body.
        Bitmap body = currentLevel.getAssetLoader().getBitmapByName("bike/body_one.png");
        // Calculate the vector between the two wheels.
        VectorF leftToRight = rightWheel.getPos().subtracted(leftWheel.getPos());
        float angle = leftToRight.angle();
        leftToRight.normalise();
        VectorF bodyPos = leftWheel.getPos().copy();
        // Move the body so that its positioned between the two wheels.
        bodyPos.multAdd(leftToRight, wheelSeparation / 2);
        // Calculate normal to `leftToRight` vector.
        VectorF ltrNormal = new VectorF(-leftToRight.getY(), leftToRight.getX());
        // Move the body so that its positioned above the wheels.
        bodyPos.multAdd(ltrNormal, -wheelRadius);
        // Draw the body at the specified position and with the specified rotation.
        gameView.drawImage(body, bodyPos, angle, GameView.ImageOrigin.Middle);

        gameView.disableCamera();
        // Draw text on screen with some debug info
        DynamicBody debugWheel = leftWheel; // The wheel to show debug info for.
        String debugInfo = String.format("Bike[%s]", debugWheel.toString());
        gameView.drawText(debugInfo, 20, 60, Color.WHITE);
    }

    /**
     * Called when the size of the GameView changes.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    public void updateSize(int width, int height) {
        updateStartPos(currentLevel.getStartPoint());
    }

    /**
     * Called to update the start position of the bike.
     */
    private void updateStartPos(VectorF startPos) {
        // This is necessary because when the Bike is initialised the GameView may not have
        // initialised properly yet, and so its height has not been calculated yet. This causes
        // the start position to be incorrect.
        this.startPos = startPos;
        Log.d("Bike", "Updated start pos: " + startPos.toString());
        // Calculate positions of the left and right wheels.
        // TODO: This is currently hardcoded.
        leftWheel.setPos(startPos.x + 25, startPos.y);
        //rightWheel.setPos(startPos.x + 200, startPos.y);
        rightWheel.setPos(leftWheel.getPos().x + wheelSeparation, 100);
    }

    public void update(float lastUpdate) {
        // TODO
    }

    /**
     * Sets the torque of the bike. The strength should be a value between 0 and 1. 0 means
     * no acceleration and 1 means maximum acceleration.
     * @param strength
     */
    public void setTorque(float strength) {
        // Left wheel is controlled by the engine, so it gets the acceleration.
        // TODO: Maximum speed of bike is currently hardcoded. Make this customisable, perhaps
        // TODO: allow different bikes with differing acceleration characteristics?
        leftWheel.setTorque(500 * strength);

        Log.d("Bike/Trq", "Torque is now " + 5*strength);
    }

    public VectorF getPos() {
        return new VectorF(leftWheel.getPos().x + (wheelSeparation/2), leftWheel.getPos().y);
    }

    public void setPos(float x, float y) {
        leftWheel.setPos(x, y);
        rightWheel.setPos(x+wheelSeparation, y);
    }
}
