package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScoreData;

import java.util.ArrayList;
import java.util.List;


public class HighScoreScene extends Scene {
    private GameView gameView;
    protected ArrayList<HighScoreData> currentHighScores = new ArrayList<HighScoreData>();
    private HighScore highScore;

    public HighScoreScene(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        super(assetLoader, gameSceneManager);
        this.gameView = gameSceneManager.gameView;
        this.highScore = new HighScore(this.gameView);
    }

    public void updateCurrentHighScoresList(){
        List<HighScore.PrefData> highScoreList = highScore.getSortedList();
        currentHighScores.clear();
        for (int count = 0; count < highScoreList.size(); count++) {
            HighScoreData h = new HighScoreData(highScoreList.get(count).key, highScoreList.get(count).value);
            this.currentHighScores.add(h);
        }
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
        updateCurrentHighScoresList();

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

        HighScoreData highScore;
        gameView.drawText("High scores", (getScreenWidth() / 2) -70, 160, Color.WHITE, 40);

        for (int i = this.currentHighScores.size() -1; i >= 0; i--) {
            highScore = currentHighScores.get(i);
            gameView.drawText(highScore.name + "-" + highScore.dateString + " - " + highScore.time + " " + highScore.score, (getScreenWidth() / 2) - 70, (200 + (i * 40)), Color.WHITE, 40);

        }
    }
}
