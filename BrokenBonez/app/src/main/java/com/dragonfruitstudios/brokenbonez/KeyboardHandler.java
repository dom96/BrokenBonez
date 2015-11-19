package com.dragonfruitstudios.brokenbonez;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.View.OnKeyListener;
import com.dragonfruitstudios.brokenbonez.Input.KeyEvent; //imports keyEvent class to read keyEvents 1 or 0
import com.dragonfruitstudios.brokenbonez.Pool.PoolObjectFactory; //used to create pool/poolfactoryobject
/**
 * Created by rmsfan on 16/11/2015.
 */
public class KeyboardHandler {
    boolean[] pressedKeys = new boolean[128]; //range of 1-127 keys
    Pool <KeyEvent> keyEventPool;
    List <KeyEvent> keyEventsBuffer = new ArrayList <KeyEvent> ();
    List <KeyEvent> keyEvents = new ArrayList <KeyEvent> ();

    public KeyboardHandler(View view) {
        PoolObjectFactory <KeyEvent> factory = new PoolObjectFactory <KeyEvent> () {
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        };
        keyEventPool = new Pool < KeyEvent > (factory, 100);
        view.setOnKeyListener((OnKeyListener) this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
            return false;
        synchronized (this) {
            KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar(); //retrieves char set
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = true; //1
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = false; //0
            }
            keyEventsBuffer.add(keyEvent);
        }
        return false;
    }
    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode > 127) //return keyCode i.e. unicode charset
            return false;
        return pressedKeys[keyCode];
    }
    public List <KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();
            for (int i = 0; i < len; i++) {
                keyEventPool.free(keyEvents.get(i));
            }
            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
        }
    }
}

