package Checkers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Clock {
    Timeline timeline;
    long time;
    long beginTime;
    long endTime;
    int min;
    DateFormat timeFormat = new SimpleDateFormat("mm:ss");
    Label timeLbl;
    long diff;
    Checkers game;

    Clock(int m, Label l, Checkers g){
        min = m;
        timeLbl = l;
        game = g;

        timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                (event)->{
                    diff = System.currentTimeMillis() - beginTime;
                    if(time - diff<0){
                        timeLbl.setText("Time: " + timeFormat.format(0));
                        stop();
                        game.gameEndTime();
                    } else {
                        timeLbl.setText("Time: " + timeFormat.format(time - diff));
                    }
                }
        ));
        time = m * 60 * 1000;
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void start(){
        beginTime = System.currentTimeMillis();
        timeline.play();
    }

    public void stop(){
        time = time - System.currentTimeMillis() + beginTime < 0? 0 : time - System.currentTimeMillis() + beginTime;
        timeline.stop();
    }

    public boolean isEnd(){
        time = time - System.currentTimeMillis() + beginTime < 0? 0 : time - System.currentTimeMillis() + beginTime;
        beginTime = System.currentTimeMillis();
        diff = System.currentTimeMillis() - beginTime;
        if(time == 0){ return true; }
        else return false;
    }
}
