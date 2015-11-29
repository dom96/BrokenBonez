package com.dragonfruitstudios.brokenbonez;

import java.util.List;

import android.view.View;
import android.view.View.OnTouchListener;
import com.dragonfruitstudios.brokenbonez.Input.TouchEvent;

/**
 * Created by rmsfan on 16/11/2015.
 */
//corresponds to input class methods
public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
    public List<Input.TouchEvent> getTouchEvents();
}
