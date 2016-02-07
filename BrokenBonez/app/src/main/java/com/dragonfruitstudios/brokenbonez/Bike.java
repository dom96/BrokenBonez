package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.BoundingShapes.Circle;
import com.dragonfruitstudios.brokenbonez.BoundingShapes.Intersector;
import com.dragonfruitstudios.brokenbonez.BoundingShapes.Manifold;
import com.dragonfruitstudios.brokenbonez.BoundingShapes.Rect;

import java.util.ArrayList;

public class Bike implements GameObject {

    class Wheel {
        VectorF pos;
        VectorF velocity; // Measured in px/s
        VectorF acceleration; // Measured in px/s²

        float rotation; // Measured in radians
        float angularVelocity; // Radians per second
        float angularAcceleration; // Radians per second per second

        Circle boundingCircle;

        boolean wasInAir;
        float torque;

        ArrayList<Manifold> lastManifolds = new ArrayList<Manifold>(); // TODO: Just for testing for now.

        Wheel() {
            pos = new VectorF(0, 0);
            velocity = new VectorF(0, 0);
            acceleration = new VectorF(0, 0);

            //angularAcceleration = 3;

            rotation = 0;

            boundingCircle = new Circle(pos, 30);

            wasInAir = true;
        }

        public void update(float lastUpdate, Level currentLevel) {
            final float mass = 200;
            final float g = 9.81f;
            final float gScaled = 10*g;

            float updateFactor = lastUpdate / 1000;

            VectorF oldPos = new VectorF(pos);
            // Change position based on velocity.
            pos.multAdd(velocity, updateFactor);

            // Change angular velocity based on angular acceleration.
            angularVelocity += angularAcceleration * updateFactor;
            // Change rotation based on angular velocity.
            rotation += angularVelocity * updateFactor;

            // Check if the wheel intersects with the current level's ground.
            ArrayList<Manifold> manifolds = currentLevel.collisionTest(boundingCircle);
            lastManifolds = manifolds;

            if (manifolds.size() > 0) {
                //Manifold manifold = manifolds.get(0);
                int count = 0;
                for (Manifold manifold : manifolds) {
                    count++;
                    //Log.d("Air", "Colliding!");
                    //Log.d("Manifold", "Is null? " + (test.getNormal() == null));
                    // We need to move the wheel, so that it just touches what it collided with.
                    float nearest = currentLevel.getNearestSolid(boundingCircle.getCenter());
                    //Log.d("Nearest", nearest + "");
                    //pos.setY(pos.getY() - (boundingCircle.getRadius() - nearest));

                    // Resolving forces
                    // Grab old acceleration and use it to calculate old resultant force
                    // Then use old resultant force to calculate new resultant force.
                    // F = ma
                    VectorF oldResultantForce = new VectorF(acceleration);
                    oldResultantForce.mult(mass);


                    // We need the angle between the normal and the x-plane.
                    float angle = manifold.getNormal().angle();
                    float generalNatural = -mass * gScaled * (float) Math.cos(angle);
                    float forceY = generalNatural * (float) Math.cos(angle) - mass * gScaled;
                    float friction = 0.45f * generalNatural; // TODO: Define coefficient
                    float forceX = -friction * (float) Math.cos(angle) + generalNatural * (float) Math.sin(angle);

                    //Log.d("Manifold" + count, "Angle: " + angle);

                    float accelY = forceY / mass;
                    float accelX = forceX / mass;//(2.0f/3.0f)*gScaled*-(float)Math.cos(angle);
                    //Log.d("Manifold" + count, "AccelX " + accelX + " AccelY " + accelY);

                    // Correct position
                    pos.multAdd(manifold.getNormal(), -(manifold.getPenetration()));

                    //acceleration.setY(accelY);
                    //acceleration.setX(accelX);

                    // Let's try the impulse engine way.

                    float velAlongNormal = velocity.dotProduct(manifold.getNormal());
                    //if (velAlongNormal > 0) {
                    //    return;
                    //}

                    float e = 0.0f;
                    float j = -(1 + e) * velAlongNormal;
                    j /= 1 / mass;

                    VectorF impulse = new VectorF(manifold.getNormal());
                    impulse.mult(j);

                    //Log.d("Vel", "MultAdd " + impulse + " " + 1/mass);
                    velocity.multAdd(impulse, 1/mass);

                    if (wasInAir) {
                        wasInAir = false;

                        //velocity.setY(0);


                        // Velocity = ω * radius
                        VectorF newVelocity = new VectorF(angularVelocity * boundingCircle.getRadius(), 0);
                        //velocity.add(newVelocity);
                    } else {
                        // TODO: Need to consider the angle of the tangent of the point that the
                        // TODO: wheel is touching.
                        float newAngularVelocity = velocity.magnitude() / boundingCircle.getRadius();
                        if (velocity.getX() < 0) {
                            // TODO: This probably won't work on slopes?
                            newAngularVelocity = -newAngularVelocity;
                        }
                        angularVelocity = newAngularVelocity;
                    }

                    // Set acceleration based on torque.
                    // TODO: This may need to be adjusted, especially when slopes come into play.
                    //acceleration.setX(torque);
                    velocity.add(new VectorF(torque, 0));

                    // Update the wheels' velocity based on acceleration.
                    velocity.multAdd(acceleration, updateFactor);
                }
            }
            else {
                wasInAir = true;
                //Log.d("Air", "In Air!");
                // Resolve forces when wheel is in air.

                // Acceleration due to gravity.
                acceleration.setY(gScaled);
                // Deceleration due to air resistance. It acts in the opposite direction to the
                // velocity.
                VectorF airResistance = new VectorF(-(0.1f * velocity.getX()), -(0.1f * velocity.getY()));
                // TODO: Friction
                // Calculate the resultant acceleration.
                VectorF resultantAccel = new VectorF(acceleration);
                resultantAccel.add(airResistance);

                // Update the wheels' velocity based on acceleration.
                velocity.multAdd(resultantAccel, updateFactor);
            }
        }

        /**
         * Sets the position of this wheel to the specified x and y coordinates.
         */
        public void setPos(float x, float y) {
            pos.set(x, y);

            boundingCircle.setCenter(x, y);
        }

        /**
         * Sets the acceleration of this wheel to the specified vector.
         */
        public void setAcceleration(float amount) {
            torque = amount;
        }

        public void draw(GameView view) {
            view.drawCircle(pos.x, pos.y, boundingCircle.getRadius(), Color.parseColor("#6d6d6d"));
            // A rotation of `0` will show the line horizontally to the right.
            VectorF rotatedFinish = new VectorF(boundingCircle.getRadius(), 0);
            rotatedFinish.rotate(rotation);
            view.drawLine(pos, pos.added(rotatedFinish), Color.parseColor("#ffe961"));

            boundingCircle.draw(view);

            // Just for testing.
            for (Manifold manifold : lastManifolds) {
                VectorF x = new VectorF(boundingCircle.getCenter());
                x.multAdd(manifold.getNormal(), 30f);
                view.drawLine(boundingCircle.getCenter(), x,
                        Color.parseColor("#00c80a"));
            }
        }

    }

    Level currentLevel;

    Wheel leftWheel;
    Wheel rightWheel;

    VectorF startPos;

    public Bike(Level currentLevel) {
        this.currentLevel = currentLevel;

        this.leftWheel = new Wheel();
        this.rightWheel = new Wheel();
        // TODO: Just a small test to see if giving the wheel an initial x-axis velocity works.
        this.rightWheel.velocity.set(20, 0);
        //this.rightWheel.acceleration.set(40, 0);
        this.leftWheel.angularVelocity = 2;
    }

    public void draw(GameView gameView) {
        leftWheel.draw(gameView);
        rightWheel.draw(gameView);

        // Draw text on screen with some debug info
        Wheel debugWheel = leftWheel;
        String debugInfo = String.format("Bike[LWV: (%.1f, %.1f), LWA: (%.1f, %.1f), LWP: (%.1f, %.1f)]",
                debugWheel.velocity.getX(), debugWheel.velocity.getY(), debugWheel.acceleration.getX(),
                debugWheel.acceleration.getY(), debugWheel.pos.getX(), debugWheel.pos.getY());
        gameView.drawText(debugInfo, 20, 60, Color.WHITE);
    }

    public void updateSize(int width, int height) {
        updateStartPos(currentLevel.getStartPoint());
    }

    private void updateStartPos(VectorF startPos) {
        // This is necessary because when the Bike is initialised the GameView may not have
        // initialised properly yet, and so its height has not been calculated yet. This causes
        // the start position to be incorrect.
        this.startPos = startPos;
        Log.d("Bike", "Updated start pos: " + startPos.toString());
        // Calculate positions of the left and right wheels.
        leftWheel.setPos(startPos.x + 25, startPos.y);
        //rightWheel.setPos(startPos.x + 200, startPos.y);
        rightWheel.setPos(230, 100);
    }

    public void update(float lastUpdate) {
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
        leftWheel.setAcceleration(5*strength);

        Log.d("Bike/Acc", "Torque is now " + 5*strength);
    }
}
