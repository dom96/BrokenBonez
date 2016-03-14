package com.dragonfruitstudios.brokenbonez.HighScores;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class HighScoreData {
    public String name;
    public int score;
    public float time;
    public DateFormat dateTime = new SimpleDateFormat("dd-MM-yyyy-kk-mm-ss");
    public String dateString;


    public HighScoreData(String name, int score, float time, String dateTime){
        setData(name, score, time, dateTime);
    }

    public HighScoreData(String data, int score){
        String[] dataParts;
        dataParts = data.split("〰");
        setData(dataParts[0], score,  Integer.parseInt(dataParts[1]), dataParts[2]);
    }

    private void setData(String name, int score, float time, String dateTime){
        this.name = name;
        this.score = score;
        this.time = time;
        this.dateString = dateTime;
        try {
            this.dateTime.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d("Hello", dateTime);
    }
    //public String getDateString(){
        //return date
    //}



}
