package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.BoundingShapes.Circle;
import com.dragonfruitstudios.brokenbonez.BoundingShapes.Manifold;

import java.util.ArrayList;

// TODO: Abstract the physics so that they can be used for other objects.

public class Bike implements GameObject {
    // Just some physics constants.
    final float mass = 200;
    final float g = 9.81f;
    final float gScaled = 10*g;

    class Wheel {
        VectorF pos; // The current wheel's position.
        VectorF velocity; // Measured in px/s
        VectorF acceleration; // Measured in px/s²

        float rotation; // Measured in radians
        float angularVelocity; // Radians per second
        float angularAcceleration; // Radians per second per second

        Circle boundingCircle; // Used for collision detection.

        boolean wasInAir; // Determines whether the wheel was in air.
        float torque; // Determines what engine power to apply to the left wheel.

        // Fields containing data used for debugging.
        ArrayList<Manifold> lastManifolds = new ArrayList<Manifold>(); // Stores the last collisions

        Wheel() {
            pos = new VectorF(0, 0);
            velocity = new VectorF(0, 0);
            acceleration = new VectorF(0, 0);

            rotation = 0;

            boundingCircle = new Circle(pos, 30);

            wasInAir = true;
        }

        public void update(float lastUpdate, Level currentLevel) {
            // Calculate an update factor based on the last time update was called.
            float updateFactor = lastUpdate / 1000;

            // Change position based on velocity.
            pos.multAdd(velocity, updateFactor);

            // Change angular velocity based on angular acceleration.
            angularVelocity += angularAcceleration * updateFactor;
            // Change rotation based on angular velocity.
            rotation += angularVelocity * updateFactor;

            // Check if the wheel intersects with the current level's ground.
            ArrayList<Manifold> manifolds = currentLevel.collisionTest(boundingCircle);
            // Save the collision info so that the normals can be drawn in the next frame.
            // (Just for debugging).
            lastManifolds = manifolds;

            if (manifolds.size() > 0) {
                // The wheel may be colliding with multiple objects. That's why we get multiple
                // manifolds and have to look at all of them.
                for (Manifold manifold : manifolds) {
                    // Correct position
                    pos.multAdd(manifold.getNormal(), -(manifold.getPenetration()));

                    // Calculate the impulses that the normals enact on the wheel.

                    // Calculate relative velocity in terms of the normal direction.
                    float velRelativeToNormal = velocity.dotProduct(manifold.getNormal());

                    // Calculate restitution: ratio of how much energy remains to how much is lost.
                    // https://en.wikipedia.org/wiki/Coefficient_of_restitution
                    // Affects bounciness.
                    float e = 0.1f;

                    // Calculate the impulse scalar.
                    float j = -(1 + e) * velRelativeToNormal;
                    j /= 1 / mass;

                    // Apply the impulse.
                    VectorF impulse = new VectorF(manifold.getNormal());
                    impulse.mult(j);

                    velocity.multAdd(impulse, 1/mass);

                    if (wasInAir) {
                        wasInAir = false;
                        // TODO: Measure amount of time in air, if it's above some threshold then
                        // TODO: calculate angular vel.
                        // Velocity = ω * radius
                        VectorF newVelocity = new VectorF(angularVelocity * boundingCircle.getRadius(), 0);
                        //velocity.add(newVelocity);
                    } else {
                        // Calculate the wheel's angular velocity based on its linear velocity.
                        // Using the equation: ω = velocity / radius
                        float newAngularVelocity = velocity.magnitude() / boundingCircle.getRadius();
                        // Determine the direction of rotation.
                        if (velocity.getX() < 0) {
                            newAngularVelocity = -newAngularVelocity;
                        }
                        angularVelocity = newAngularVelocity;
                    }

                    // Set velocity based on torque.
                    velocity.add(new VectorF(torque, 0));

                    // Update the wheels' velocity based on acceleration.
                    velocity.multAdd(acceleration, updateFactor);
                }
            }
            else {
                wasInAir = true;
                // Resolve forces when wheel is in air.

                // Acceleration due to gravity.
                acceleration.setY(gScaled);
                // Deceleration due to air resistance. It acts in the opposite direction to the
                // velocity.
                VectorF airResistance = new VectorF(-(0.1f * velocity.getX()), -(0.1f * velocity.getY()));
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
         * Sets the torque of this wheel to the specified amount.
         */
        public void setTorque(float amount) {
            torque = amount;
        }

        public void draw(GameView view) {
            view.drawCircle(pos.x, pos.y, boundingCircle.getRadius(), Color.parseColor("#6d6d6d"));
            // Draw a yellow line to show the wheel's rotation.
            // A rotation of `0` will show the line horizontally to the right.
            VectorF rotatedFinish = new VectorF(boundingCircle.getRadius(), 0);
            rotatedFinish.rotate(rotation);
            view.drawLine(pos, pos.added(rotatedFinish), Color.parseColor("#ffe961"));

            boundingCircle.draw(view);

            // Just for testing.
            // Draw a green line to show the normal of each manifold.
            for (Manifold manifold : lastManifolds) {
                VectorF x = new VectorF(boundingCircle.getCenter());
                x.multAdd(manifold.getNormal(), 30f);
                view.drawLine(boundingCircle.getCenter(), x,
                        Color.parseColor("#00c80a"));
            }
        }

    }

    // The current level that this bike is on.
    Level currentLevel;

    // The wheel's of this bike.
    Wheel leftWheel;
    Wheel rightWheel;

    // The start position of this bike.
    VectorF startPos;

    public Bike(Level currentLevel) {
        this.currentLevel = currentLevel;

        this.leftWheel = new Wheel();
        this.rightWheel = new Wheel();


        // TODO: Just some small hardcoded tests.
        this.rightWheel.velocity.set(20, 0);
        //this.rightWheel.acceleration.set(40, 0);
        this.leftWheel.angularVelocity = 2;
    }

    public void draw(GameView gameView) {
        leftWheel.draw(gameView);
        rightWheel.draw(gameView);

        // Draw text on screen with some debug info
        Wheel debugWheel = leftWheel; // The wheel to show debug info for.
        String debugInfo = String.format("Bike[LWV: (%.1f, %.1f), LWA: (%.1f, %.1f), LWP: (%.1f, %.1f)]",
                debugWheel.velocity.getX(), debugWheel.velocity.getY(), debugWheel.acceleration.getX(),
                debugWheel.acceleration.getY(), debugWheel.pos.getX(), debugWheel.pos.getY());
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
        rightWheel.setPos(230, 100);
    }

    public void update(float lastUpdate) {
        leftWheel.update(lastUpdate, currentLevel);
        rightWheel.update(lastUpdate, currentLevel);
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
        leftWheel.setTorque(5 * strength);

        Log.d("Bike/Trq", "Torque is now " + 5*strength);
    }
}
