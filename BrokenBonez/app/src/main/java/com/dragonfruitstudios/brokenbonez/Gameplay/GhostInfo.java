package com.dragonfruitstudios.brokenbonez.Gameplay;


import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.io.Serializable;
import java.util.ArrayList;

public class GhostInfo implements Serializable {
    // Increase this to save space. I set it to 1 for best quality.
    // TODO: Another option to improve quality without sacrificing space is to implement
    // TODO: interpolation between time slices.
    private final int sliceEvery = 1;

    private String username;

    protected class TimeSlice implements Serializable {
        float time;
        VectorF leftWheelPos;
        VectorF rightWheelPos;
        float leftWheelRotation;
        float rightWheelRotation;

        protected TimeSlice(float time, VectorF leftWheelPos, VectorF rightWheelPos,
                            float leftWheelRotation, float rightWheelRotation) {
            this.time = time;
            this.leftWheelPos = leftWheelPos.copy();
            this.rightWheelPos = rightWheelPos.copy();
            this.leftWheelRotation = leftWheelRotation;
            this.rightWheelRotation = rightWheelRotation;
        }
    }

    private float currentTime;
    private float finishTime; // Stores the time when the Ghost finished the level. By default -1.
    private ArrayList<TimeSlice> slices;

    // Only used when reading slices. Holds the index of the last returned slice.
    private int lastSliceIndex;

    // Only used when writing slices. Holds the time when the last slice was stored.
    private float lastSliceCreation;

    public GhostInfo(String username) {
        this.username = username;
        this.slices = new ArrayList<TimeSlice>();
        this.finishTime = -1;
    }

    public void createSlice(float msPassed, VectorF leftWheelPos, VectorF rightWheelPos,
                            float leftWheelRotation, float rightWheelRotation) {
        // Create a slice every specified number of miliseconds, not each frame to save space.
        if (currentTime - lastSliceCreation > sliceEvery) {
            slices.add(new TimeSlice(currentTime + msPassed, leftWheelPos, rightWheelPos,
                    leftWheelRotation, rightWheelRotation));
            lastSliceCreation = currentTime;
        }
        currentTime += msPassed;
    }

    public TimeSlice getSlice(float msPassed) {
        // Find the next slice.
        currentTime += msPassed;
        TimeSlice result = slices.get(lastSliceIndex);
        for (int i = lastSliceIndex; i < slices.size()-1; i++) {
            if (slices.get(i).time > currentTime) {
                break;
            }
            else {
                result = slices.get(i);
                lastSliceIndex = i;
            }
        }
        return result;
    }

    /**
     * Restarts the GhostInfo to its starting TimeSlice. This is useful when wanting to read the
     * TimeSlices from the beginning.
     */
    public void rewind() {
        currentTime = 0;
        lastSliceIndex = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void finish() {
        if (isFinished()) {
            throw new RuntimeException("GhostInfo already has been finished.");
        }
        this.finishTime = currentTime;
    }

    public float getFinishTime() {
        if (finishTime == -1) {
            throw new RuntimeException("Finish time not set.");
        }

        return finishTime;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public boolean isFinished() {
        return finishTime != -1;
    }

    public boolean hasSlices() {
        return slices.size() != 0;
    }

    public void reset() {
        this.username = "Anonymous";
        this.slices.clear();
        this.currentTime = 0;
        this.finishTime = -1;
        this.lastSliceCreation = 0;
    }
}