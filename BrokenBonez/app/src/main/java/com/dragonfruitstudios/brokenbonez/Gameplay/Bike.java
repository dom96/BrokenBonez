package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Game.Level;
import com.dragonfruitstudios.brokenbonez.GameLoop;
import com.dragonfruitstudios.brokenbonez.Input.Accelerometer;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Circle;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Line;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Constraint;
import com.dragonfruitstudios.brokenbonez.Math.Physics.DynamicBody;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.Physics.StaticBody;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class Bike implements GameObject {
    // Some constants related to the wheels.
    final float wheelSeparation = 74f; // TODO: Change this depending on body type.
    final float wheelRadius = 20f;
    final float wheelMass = 200f;
    final float tiltSensitivity = 2f; // Affects the rate of bike tilting.

    // The current level that this bike is on.
    Level currentLevel;

    // The wheel's of this bike.
    DynamicBody leftWheel;
    DynamicBody rightWheel;
    // The constraint between the two wheels.
    Constraint wheelConstraint;
    // The body of this bike.
    Rect bodyRect;

    // The start position of this bike.
    VectorF startPos;

    // Customisation of the bike.
    Bitmap body;
    BodyType bodyType;
    Bitmap character;
    CharacterType characterType;
    int color; // The bike color.

    // Specifies how much the bike should be tilting per update.
    float currentTiltForce = 0;

    // Has this bike crashed?
    boolean dead;

    public enum BodyType {
        Bike, Bicycle
    }

    public enum CharacterType {
        DeeDee, Jenny, Leslie, Wanita
    }

    public Bike(AssetLoader assetLoader, Level currentLevel, BodyType bodyType,
                CharacterType characterType) {
        // Store the current level.
        this.currentLevel = currentLevel;

        // Load the bike assets
        assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png",
                "bike/body_two.png", "bike/deedee.png", "bike/jenny.png", "bike/leslie.png",
                "bike/wanita.png"});

        // Define physical bodies to model the Bike.
        Circle circle = new Circle(new VectorF(0, 0), wheelRadius);
        leftWheel = currentLevel.getPhysicsSimulator().createDynamicBody(circle, wheelMass);
        rightWheel = currentLevel.getPhysicsSimulator().createDynamicBody(circle, wheelMass);

        // Create a constraint between the two wheels so that they are kept together.
        wheelConstraint = currentLevel.getPhysicsSimulator().createConstraint(leftWheel,
                rightWheel, wheelSeparation);

        // Store and compute variables holding customisation data (bike color, shape etc.)
        this.bodyType = bodyType;
        setCharacterType(characterType);
        // The `setColor` setter computes the necessary `body` bitmap.
        setColor(Color.parseColor("#4d27f6"));

        // Create bounding rectangle around Bike body.
        bodyRect = new Rect(new VectorF(0, 0), 1, 1);
    }

    private void updateBodyRect(VectorF bodyRotation, VectorF bodyNormal) {
        // Use the left wheel's position as the position for the bottom left of the body rectangle.
        VectorF bottomLeft = leftWheel.getPos().copy();

        // Project the bottomLeft vector in the direction of the right wheel.
        VectorF bottomRight = bottomLeft.copy();
        bottomRight.multAdd(bodyRotation, wheelSeparation);

        // Project the bottomLeft vector up in the direction of the normal to the body's angle.
        VectorF topLeft = bottomLeft.copy();
        topLeft.multAdd(bodyNormal, -43);

        // Project the bottomRight vector up in the direction of the normal to the body's angle.
        VectorF topRight = bottomRight.copy();
        topRight.multAdd(bodyNormal, -43);

        // Update the bodyRect with the new positions.
        bodyRect.setLines(new Line(topLeft, topRight), new Line(topLeft, bottomLeft),
                new Line(bottomLeft, bottomRight), new Line(bottomRight, topRight));
    }

    public void draw(GameView gameView) {
        gameView.enableCamera();
        // Draw the wheels.
        Bitmap wheel = currentLevel.getAssetLoader().getBitmapByName("bike/wheel_basic.png");
        gameView.drawImage(wheel, leftWheel.getPos(), leftWheel.getRotation(),
                GameView.ImageOrigin.Middle);
        gameView.drawImage(wheel, rightWheel.getPos(), rightWheel.getRotation(),
                GameView.ImageOrigin.Middle);

        // Draw the bike body as long as the bike hasn't crashed.
        if (!dead) {
            // Calculate the vector between the two wheels.
            VectorF leftToRight = rightWheel.getPos().subtracted(leftWheel.getPos());
            // Check if the left wheel is in the same position as the right wheel.
            if (!leftWheel.getPos().equals(rightWheel.getPos())) {
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
                // Draw the character at/with the specified position and rotation.
                bodyPos.multAdd(ltrNormal, -10); // Move the character up a bit.
                gameView.drawImage(character, bodyPos, angle, GameView.ImageOrigin.Middle);

                // Set the `bodyRect` so that it overlays the bike body.
                updateBodyRect(leftToRight, ltrNormal);
            } else {
                // Handle the rare case when the wheels are in the same position.
                gameView.drawImage(body, leftWheel.getPos(), 0, GameView.ImageOrigin.Middle);
            }
        }

        // Draw the body rect for debugging purposes.
        bodyRect.draw(gameView);

        gameView.disableCamera();

        if (Graphics.drawDebugInfo) {
            // Draw text on screen with some debug info
            DynamicBody debugWheel = leftWheel; // The wheel to show debug info for.
            String debugInfo = String.format(
                    "Bike[%s, OnGrnd: %s %s, A: %.1f°, Tilt: %.1f]",
                    debugWheel.toString(),
                    leftWheel.isOnGround() ? "✓" : "✘", rightWheel.isOnGround() ? "✓" : "✘",
                    (Math.toDegrees(new Line(leftWheel.getPos(), rightWheel.getPos()).calcRotation())),
                    currentTiltForce);
            gameView.drawText(debugInfo, 20, 60, Color.WHITE);
        }
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
        Log.d("Bike", "Updated start pos: " + startPos.toString());
        this.startPos = startPos;

        // Calculate positions of the left and right wheels.
        leftWheel.setPos(startPos.x, startPos.y);
        rightWheel.setPos(leftWheel.getPos().x + wheelSeparation, startPos.y);
    }

    public void update(float lastUpdate) {
        float updateFactor = Simulator.calcUpdateFactor(lastUpdate);

        // Handle tilting of the bike depending on the `currentTiltForce`.
        boolean rotate = false;
        if (Math.abs(currentTiltForce) > 0.08) {
            // This code is a tad magical. The Line constructor does not copy the wheels'
            // position vectors, so when the Line is rotated the position vectors belonging to the
            // wheel's are rotated directly.
            Line leftToRight = new Line(leftWheel.getPos(), rightWheel.getPos());

            rotate = true;
            // Check if bike is on ground.
            if (leftWheel.isOnGround()) {
                // Check if bike reached max tilt.
                float angle = (float) Math.toDegrees(leftToRight.calcRotation());
                if (angle < -30) {
                    // The bike may be above the threshold, in that case we want it to tilt if
                    // the tilting direction is away from its threshold (towards ground).
                    rotate = Math.abs(angle + tiltSensitivity * currentTiltForce) < Math.abs(angle);
                }
            }

            if ((leftWheel.isOnGround() || rightWheel.isOnGround()) && currentTiltForce > 0) {
                // Disallow lifting left wheel to prevent issues with no traction.
                rotate = false;
            }

            if (rotate) {
                Log.w("Bike", "Tilt: " + currentTiltForce);
                // Rotate the bike based on the currentTiltForce.
                leftToRight.rotate(-tiltSensitivity * currentTiltForce * updateFactor, leftToRight.getCenter());
                // Disable the gravity of the wheel that is in the air while tilting (to prevent it
                // from dropping to ground).
                rightWheel.setHasGravity(!leftWheel.isOnGround());
                leftWheel.setHasGravity(!rightWheel.isOnGround());
            }
        }

        if (!rotate) {
            rightWheel.setHasGravity(true);
            leftWheel.setHasGravity(true);
        }

        // Determine if Bike body collided with anything.
        if (currentLevel.collidesWith(bodyRect) && !dead) {
            // The bike collided with something, so let's remove the constraint between the wheels
            // and make it fall apart.
            currentLevel.getPhysicsSimulator().removeConstraint(wheelConstraint);

            dead = true;

            // Call the current level's onBikeCrash method. This method will take the necessary
            // steps to end the game if necessary.
            currentLevel.onBikeCrash();
        }
    }

    /**
     * Resets the bike to its original position and state.
     */
    public void reset() {
        updateStartPos(currentLevel.getStartPoint());
        leftWheel.reset();
        rightWheel.reset();

        currentLevel.getPhysicsSimulator().addConstraint(wheelConstraint);
        dead = false;

        bodyRect = new Rect(new VectorF(0, 0), 1, 1);
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

        Log.d("Bike/Trq", "Torque is now " + 5 * strength);
    }

    public void setTilt(float value) {
        currentTiltForce = value;
    }

    public VectorF getPos() {
        return new VectorF(leftWheel.getPos().x + (wheelSeparation/2), leftWheel.getPos().y);
    }

    public int getColor() {
        return color;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public CharacterType getCharacterType() {
        return characterType;
    }

    public float getRotation() {
        VectorF leftToRight = rightWheel.getPos().subtracted(leftWheel.getPos());
        return leftToRight.angle();
    }

    public void setPos(float x, float y) {
        leftWheel.setPos(x, y);
        rightWheel.setPos(x + wheelSeparation, y);
    }

    public void setColor(int color) {
        this.color = color;

        String filename = "bike/body_one.png";
        String bodyColor = "#4d27f6";
        switch (bodyType) {
            case Bike:
                filename = "bike/body_one.png";
                bodyColor = "#4d27f6";
                break;
            case Bicycle:
                filename = "bike/body_two.png";
                bodyColor = "#2caf04";
                break;
        }
        body = currentLevel.getAssetLoader().getBitmapByName(filename);
        // Need to make a mutable copy of the bitmap in order to change its color.
        body = body.copy(body.getConfig(), true);
        // Change the bike body color.
        Graphics.replaceColor(body, Color.parseColor(bodyColor), color);
    }

    public void setBodyType(BodyType bodyType) {
        // No need to set the body when it hasn't changed.
        if (this.bodyType != bodyType) {
            this.bodyType = bodyType;
            setColor(color); // Refresh body image.
        }
    }

    public void setCharacterType(CharacterType characterType) {
        this.characterType = characterType;
        String characterKey = "bike/deedee.png";
        switch (characterType) {
            case Leslie:
                characterKey = "bike/leslie.png";
                break;
            case DeeDee:
                characterKey = "bike/deedee.png";
                break;
            case Wanita:
                characterKey = "bike/wanita.png";
                break;
            case Jenny:
                characterKey = "bike/jenny.png";
                break;
        }
        character = currentLevel.getAssetLoader().getBitmapByName(characterKey);
    }

    public DynamicBody getLeftWheel() {
        return leftWheel;
    }

    public DynamicBody getRightWheel() {
        return rightWheel;
    }

    public Rect getBodyRect(){
        return bodyRect;
    }
}
