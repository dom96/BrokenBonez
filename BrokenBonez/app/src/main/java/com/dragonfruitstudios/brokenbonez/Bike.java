package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

// TODO: Rename Vector to VectorF, clashes with java.utils class.
import com.dragonfruitstudios.brokenbonez.BoundingShapes.Circle;
import com.dragonfruitstudios.brokenbonez.Vector;

public class Bike {

    class Wheel {
        Vector pos;
        Vector velocity;
        Vector acceleration;

        Circle boundingCircle;

        Wheel() {
            pos = new Vector(0, 0);
            velocity = new Vector(0, 0);
            acceleration = new Vector(0, 0);

            boundingCircle = new Circle(pos, 15);
        }

        public void update(double lastUpdate, Level currentLevel) {
            // Acceleration due to gravity.
            acceleration.setY(9.81);

            // Update the wheels' velocity based on acceleration.
            double updateFactor = lastUpdate / 1000;
            velocity.multAdd(acceleration, updateFactor);
            //System.out.println("Velocity: " + velocity.toString());

            // Change position based on velocity.
            pos.multAdd(velocity, updateFactor);
            if (currentLevel.intersectsGround(boundingCircle)) {
                //System.out.println("Left wheel intersects current level.");
                pos.multAdd(velocity, -updateFactor);
                velocity.set(0, 0);
            }

        }

        public void setPos(double x, double y) {
            pos.set(x, y);

            boundingCircle.set(x, y);
        }

    }

    Wheel leftWheel;
    Wheel rightWheel;

    PointF pos; // The x,y coords of the bottom left of the bike.
    PointF startPos;

    Vector velocity;

    public Bike(PointF startPos) {
        this.leftWheel = new Wheel();
        this.rightWheel = new Wheel();
        updateStartPos(startPos);

        // TODO: For testing.
        velocity = new Vector(5, 0);
    }

    public void draw(GameView gameView) {
        gameView.drawRect(pos.x, pos.y - 30, pos.x + 60, pos.y, Color.parseColor("#87000B"));
        leftWheel.boundingCircle.draw(gameView);
        rightWheel.boundingCircle.draw(gameView);
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

    public void update(double lastUpdate, Level currentLevel) {
        pos.x += velocity.x;
        pos.y += velocity.y;

        if (pos.x >= 380) {
            velocity.rotate(Math.toRadians(180));
        }

        if (pos.x <= startPos.x) {
            velocity.rotate(Math.toRadians(180));
        }


        leftWheel.update(lastUpdate, currentLevel);
        rightWheel.update(lastUpdate, currentLevel);
    }
}
