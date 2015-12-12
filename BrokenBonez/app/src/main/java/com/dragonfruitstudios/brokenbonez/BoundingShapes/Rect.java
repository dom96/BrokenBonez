package com.dragonfruitstudios.brokenbonez.BoundingShapes;

import com.dragonfruitstudios.brokenbonez.PointD;

public class Rect {
    double left, top, right, bottom;

    public Rect(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean containsPoint(double x, double y) {
        if (x >= left && x <= right && y <= top && y >= bottom) {
            return true;
        }

        return false;
    }

}
