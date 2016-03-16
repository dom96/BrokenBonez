package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Game.Levels.LevelInfo;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class implements a Ghost Bike. This Bike tracks the player's fastest time for a specific
 * level and shows them the way they moved.
 */
public class Ghost extends Bike  {
    Context context; // Used for internal storage.
    GhostInfo currentRun; // The information about the current run.
    GhostInfo prevRun; // The information about the last run.
    GameLevel currentLevel; // The level currently being played.

    public Ghost(Context context, AssetLoader assetLoader, GameLevel currentLevel) {
        super(assetLoader, currentLevel, BodyType.Bike, CharacterType.Leslie);
        // Disable gravity of bike.
        leftWheel.setHasGravity(false);
        rightWheel.setHasGravity(false);

        // Set up the ghost.
        this.context = context;
        currentRun = new GhostInfo("Anonymous");
        this.currentLevel = currentLevel;
        try {
            load(LevelInfo.getLevelName(currentLevel.getLevelID()));
        }
        catch (Exception e) {
            // TODO: Show dialog to user?
            Log.e("Ghost", "Failed to load old run: " + e.toString());
        }
    }

    public void reset(GameLevel currentLevel) {
        super.reset();
        currentRun.reset();
        this.currentLevel = currentLevel;

        try {
            load(LevelInfo.getLevelName(currentLevel.getLevelID()));
        }
        catch (Exception e) {
            // TODO: Show dialog to user?
            Log.e("Ghost", "Failed to load old run: " + e.toString());
        }
    }

    private void load(String levelName) throws IOException, ClassNotFoundException {
        String filename = "ghost_" + levelName;
        try {
            FileInputStream stream = context.openFileInput(filename);
            ObjectInputStream deserialiseStream = new ObjectInputStream(stream);
            prevRun = (GhostInfo)deserialiseStream.readObject();
            deserialiseStream.close();
            prevRun.rewind();
        }
        catch (FileNotFoundException exc) {
            Log.d("Ghost", "Could not find Ghost file for previous run, likely no old runs.");
        }
    }

    public void save(String username) throws IOException {
        // Don't save if the last run was faster than the current one.
        if (prevRun != null) {
            Log.w("Ghost", "Time diff: " + (prevRun.getFinishTime() - currentRun.getFinishTime()));
        }
        //Log.w("Ghost", "Prec: " + prevRun.getFinishTime() + " Curr: " + currentRun.getFinishTime());
        if (prevRun != null && prevRun.getFinishTime() < currentRun.getFinishTime()) {
            return;
        }

        currentRun.setUsername(username);
        String filename = "ghost_" + LevelInfo.getLevelName(currentLevel.getLevelID());
        FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream serialiseStream = new ObjectOutputStream(stream);
        serialiseStream.writeObject(currentRun);
        serialiseStream.flush();
        serialiseStream.close();
    }

    public void createSlice(float msPassed, VectorF leftWheelPos, VectorF rightWheelPos,
                            float leftRotation, float rightRotation) {
        if (!currentRun.isFinished()) {
            currentRun.createSlice(msPassed, leftWheelPos, rightWheelPos, leftRotation, rightRotation);
        }

        if (prevRun != null && prevRun.hasSlices()) {
            GhostInfo.TimeSlice slice = prevRun.getSlice(msPassed);
            leftWheel.setPos(slice.leftWheelPos.x, slice.leftWheelPos.y);
            rightWheel.setPos(slice.rightWheelPos.x, slice.rightWheelPos.y);
            leftWheel.setRotation(slice.leftWheelRotation);
            rightWheel.setRotation(slice.rightWheelRotation);
        }
    }

    public void finish() {
        currentRun.finish();
    }

    public boolean isFinished() {
        return currentRun.isFinished();
    }

    /**
     * Returns the time difference between the previous run and the current run. Negative value
     * means that the current run took longer than the previous run. Returns 0 when previous run
     * is null.
     * Will raise an exception when both are not finished!
     */
    public float getTimeDiff() {
        if (prevRun == null) {
            return 0;
        }
        if (!prevRun.isFinished() || !currentRun.isFinished()) {
            throw new RuntimeException("Couldn't get time difference, one of the runs in progress.");
        }

        return prevRun.getFinishTime() - currentRun.getFinishTime();
    }

    public float getCurrentTime() {
        return currentRun.getCurrentTime();
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        Graphics.ghostify(body);
    }

    @Override
    public void setCharacterType(CharacterType characterType) {
        super.setCharacterType(characterType);
        // Need to make a mutable copy of the bitmap in order to change its color.
        character = character.copy(character.getConfig(), true);
        Graphics.ghostify(character);
    }

    @Override
    public void draw(GameView view) {
        if (prevRun != null) {
            super.draw(view);

            view.enableCamera();
            VectorF normal = new VectorF(getRotation()-(float)Math.toRadians(90));
            VectorF pos = getPos().copy();
            pos.multAdd(normal, -50);
            view.drawTextCenter(prevRun.getUsername(), pos.x, pos.y, Color.WHITE, 30,
                    getRotation());
            view.disableCamera();
        }
    }

}
