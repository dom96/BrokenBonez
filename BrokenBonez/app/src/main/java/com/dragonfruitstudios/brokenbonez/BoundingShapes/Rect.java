package com.dragonfruitstudios.brokenbonez.BoundingShapes;

public class Rect {
    float left, top, right, bottom;

    public Rect(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean containsPoint(float x, float y) {
        if (x >= left && x <= right && y <= top && y >= bottom) {
            return true;
        }

        return false;
    }

}
