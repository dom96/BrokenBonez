package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class HighScoreScene extends Scene {
    private SharedPreferences gamePrefs;
    private GameView gameView;
    private List<PrefData> currentHighScores;

    public HighScoreScene(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        this.gameView = gameSceneManager.gameView;
        this.gamePrefs = gameView.getContext().getSharedPreferences("BrokenScores", Context.MODE_PRIVATE);
        getSortedList();
    }
    public final class PrefData {
        public String key;
        public int value;
    }

    public void AddHighScore(int newHighScore){

        DateFormat dateForm = new SimpleDateFormat("dd-MM-yyyy-kk-mm-ss");
        String highScore = "HighScore-" + dateForm.format(new Date());
        if (this.gamePrefs.getAll().size() > 4){
            int lowestScore = 0;
            String lowestID = "";
            List<PrefData> highScoresList = buildArrayListOfSharedPreferences(gamePrefs.getAll());

            for (int count = 0; count < highScoresList.size(); count++) {

                if (highScoresList.get(count).value == newHighScore){
                    lowestScore = 1000;
                    break;
                }
                if (lowestScore == 0){
                    lowestScore = highScoresList.get(count).value;
                    lowestID = highScoresList.get(count).key;
                } else {
                    if (highScoresList.get(count).value < lowestScore) {
                        lowestScore = highScoresList.get(count).value;
                        lowestID = highScoresList.get(count).key;
                    }
                }
            }
            if (lowestScore < newHighScore){
                SharedPreferences.Editor editor = gamePrefs.edit();
                editor.remove(lowestID);
                editor.putInt(highScore, newHighScore);
                editor.commit();
                List<PrefData> highScoresList2 = buildArrayListOfSharedPreferences(gamePrefs.getAll());
            }
        }else{
            SharedPreferences.Editor editor = gamePrefs.edit();
            editor.putInt(highScore, newHighScore);
            editor.commit();
        }
    }

    private List<PrefData> buildArrayListOfSharedPreferences(Map<String,?> highscores){
        List<PrefData> highScoresList = new ArrayList<>();
        for(Map.Entry<String,?> singleHighscore : highscores.entrySet()) {
            PrefData prefData = new PrefData();
            prefData.key = singleHighscore.getKey();
            prefData.value = Integer.parseInt(singleHighscore.getValue().toString());
            highScoresList.add(prefData);
        }
        return highScoresList;
    }

    public List<PrefData> getSortedList(){
        List<PrefData> list = buildArrayListOfSharedPreferences(gamePrefs.getAll());
        list = sortList(list);
        return list;
    }

    private List<PrefData> sortList(List<PrefData> list){
        Collections.sort(list, new Comparator<PrefData>() {
            @Override
            public int compare(PrefData z1, PrefData z2) {
                if (z1.value > z2.value)
                    return 1;
                if (z1.value < z2.value)
                    return -1;
                return 0;
            }
        });
        return list;
    }

    public void clearAllHighScores(){
        SharedPreferences.Editor editor = gamePrefs.edit();
        editor.clear();
        editor.commit();
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
        this.currentHighScores = getSortedList();
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
        String[] dataparts;
        gameView.drawText("High scores", (getScreenWidth() / 2) -70, 160, Color.WHITE, 40);
        for (int i = this.currentHighScores.size() -1; i >= 0; i--) {
            dataparts = null;
            dataparts = this.currentHighScores.get(i).key.split("-");
            gameView.drawText(dataparts[1] + "-" + dataparts[2] + "-" + dataparts[3] + " - " + Integer.toString((this.currentHighScores.get(i).value)), (getScreenWidth() / 2) - 70, (200 + (i * 40)), Color.WHITE, 40);

        }
    }
}
