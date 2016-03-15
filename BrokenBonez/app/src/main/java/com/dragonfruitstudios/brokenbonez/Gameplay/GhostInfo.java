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

    private float time;
    private ArrayList<TimeSlice> slices;

    // Only used when reading slices. Holds the index of the last returned slice.
    private int lastSliceIndex;

    // Only used when writing slices. Holds the time when the last slice was stored.
    private float lastSliceCreation;

    /**
     * Returns the total time passed in miliseconds.
     */
    public float getTotalTime() {
        if (slices.size() > 0) {
            return slices.get(slices.size()-1).time;
        }
        else {
            return 0;
        }
    }

    public GhostInfo(String username) {
        this.username = username;
        this.slices = new ArrayList<TimeSlice>();
    }

    public void createSlice(float msPassed, VectorF leftWheelPos, VectorF rightWheelPos,
                            float leftWheelRotation, float rightWheelRotation) {
        // Create a slice every specified number of miliseconds, not each frame to save space.
        if (time - lastSliceCreation > sliceEvery) {
            slices.add(new TimeSlice(time + msPassed, leftWheelPos, rightWheelPos,
                    leftWheelRotation, rightWheelRotation));
            lastSliceCreation = time;
        }
        time += msPassed;
    }

    public TimeSlice getSlice(float msPassed) {
        // Find the next slice.
        time += msPassed;
        TimeSlice result = slices.get(lastSliceIndex);
        for (int i = lastSliceIndex; i < slices.size()-1; i++) {
            if (slices.get(i).time > time) {
                break;
            }
            else {
                result = slices.get(i);
            }
        }
        return result;
    }

    public void enableReading() {
        time = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void reset() {
        this.username = "Anonymous";
        this.slices.clear();
    }
}