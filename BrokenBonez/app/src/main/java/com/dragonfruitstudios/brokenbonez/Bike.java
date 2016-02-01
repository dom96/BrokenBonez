package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.BoundingShapes.Circle;
import com.dragonfruitstudios.brokenbonez.BoundingShapes.Rect;

public class Bike {

    class Wheel {
        VectorF pos;
        VectorF velocity; // Measured in px/s
        VectorF acceleration; // Measured in px/s²

        Circle boundingCircle;

        Wheel() {
            pos = new VectorF(0, 0);
            velocity = new VectorF(0, 0);
            acceleration = new VectorF(0, 0);

            boundingCircle = new Circle(pos, 15);
        }

        public void update(float lastUpdate, Level currentLevel) {
            // Acceleration due to gravity.
            acceleration.setY(9.81f);
            // Deceleration due to air resistance. It acts in the opposite direction to the
            // velocity.
            VectorF airResistance = new VectorF(-(0.1f * velocity.getX()), -(0.1f * velocity.getY()));
            // TODO: Friction
            // Calculate the resultant acceleration.
            VectorF resultantAccel = new VectorF(acceleration);
            resultantAccel.add(airResistance);

            // Update the wheels' velocity based on acceleration.
            float updateFactor = lastUpdate / 1000;
            velocity.multAdd(resultantAccel, updateFactor);

            // Change position based on velocity.
            pos.multAdd(velocity, updateFactor);
            // Check if the wheel intersects with the current level's ground.
            if (currentLevel.intersectsGround(boundingCircle)) {
                // We need to move the wheel, so that it just touches what it collided with.
                Rect nearest = currentLevel.getNearestSolid(boundingCircle.getCenter());
                pos.setY(nearest.getTop() - boundingCircle.getRadius());
                velocity.setY(0);
            }
        }

        public void setPos(float x, float y) {
            pos.set(x, y);

            boundingCircle.setCenter(x, y);
        }

        public void setAcceleration(float x, float y) {
            acceleration.set(x, y);
        }

    }

    Wheel leftWheel;
    Wheel rightWheel;

    PointF pos; // The x,y coords of the bottom left of the bike.
    PointF startPos;

    VectorF velocity;

    public Bike(PointF startPos) {
        this.leftWheel = new Wheel();
        this.rightWheel = new Wheel();
        // TODO: Just a small test to see if giving the wheel an initial x-axis velocity works.
        this.leftWheel.velocity.set(20, 0);
        this.rightWheel.velocity.set(40, 0);
        updateStartPos(startPos);

        // TODO: For testing.
        velocity = new VectorF(5, 0);
    }

    public void draw(GameView gameView) {
        gameView.drawRect(pos.x, pos.y - 30, pos.x + 60, pos.y, Color.parseColor("#87000B"));
        leftWheel.boundingCircle.draw(gameView);
        rightWheel.boundingCircle.draw(gameView);

        // Draw text on screen with some debug info
        String debugInfo = String.format("Bike[LWV: (%.1f, %.1f), LWA: (%.1f, %.1f), LWP: (%.1f, %.1f)]",
                leftWheel.velocity.getX(), leftWheel.velocity.getY(), leftWheel.acceleration.getX(),
                leftWheel.acceleration.getY(), leftWheel.pos.getX(), leftWheel.pos.getY());
        gameView.drawText(debugInfo, 20, 60, Color.WHITE);
    }

    public void updateStartPos(PointF startPos) {
        // This is necessary because when the Bike is initialised the GameView may not have
        // initialised properly yet, and so its height has not been calculated yet. This causes
        // the start position to be incorrect.
        pos = new PointF(startPos.x, startPos.y - 300);
        this.startPos = new PointF(pos.x, pos.y);
        Log.d("Bike", "Updated start pos: " + pos.toString());
        // Calculate positions of the left and right wheels.
        leftWheel.setPos(pos.x + 25, pos.y);
        rightWheel.setPos(pos.x + 75, pos.y);
    }

    public void update(float lastUpdate, Level currentLevel) {
        pos.x += velocity.x;
        pos.y += velocity.y;

        if (pos.x >= 380) {
            velocity.rotate((float)Math.toRadians(180));
        }

        if (pos.x <= startPos.x) {
            velocity.rotate((float)Math.toRadians(180));
        }


        leftWheel.update(lastUpdate, currentLevel);
        rightWheel.update(lastUpdate, currentLevel);
    }

    /**
     * Sets the acceleration of the bike. The strength should be a value between 0 and 1. 0 means
     * no acceleration and 1 means maximum acceleration.
     * @param strength
     */
    public void setAcceleration(float strength) {
        // Left wheel is controlled by the engine, so it gets the acceleration.
        // TODO: Maximum speed of bike is currently hardcoded. Make this customisable, perhaps
        // TODO: allow different bikes with differing acceleration characteristics?
        leftWheel.setAcceleration(50*strength, 0);

        Log.d("Bike/Acc", "Acceleration is now " + 50*strength);
    }
}
