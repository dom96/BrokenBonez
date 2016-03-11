package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;
import java.util.List;


public class HighScoreScene extends Scene {
    private GameView gameView;
    private List<HighScore.PrefData> currentHighScores;
    private HighScore highScore;

    public HighScoreScene(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        super(assetLoader, gameSceneManager);
        this.gameView = gameSceneManager.gameView;
        this.highScore = new HighScore(this.gameView);
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void activate() {
        this.currentHighScores = highScore.getSortedList();

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void update(float lastUpdate) {

    }

    @Override
    public void updateSize(int width, int height) {

    }

    @Override
    public void draw(GameView view) {
        String[] dataParts;
        gameView.drawText("High scores", (getScreenWidth() / 2) -70, 160, Color.WHITE, 40);
        for (int i = this.currentHighScores.size() -1; i >= 0; i--) {
            dataParts = this.currentHighScores.get(i).key.split("-");
            gameView.drawText(dataParts[0] + "-" + dataParts[1] + "-" + dataParts[2] + "-" + dataParts[3] + " - " + Integer.toString((this.currentHighScores.get(i).value)), (getScreenWidth() / 2) - 70, (200 + (i * 40)), Color.WHITE, 40);

        }
    }
}
