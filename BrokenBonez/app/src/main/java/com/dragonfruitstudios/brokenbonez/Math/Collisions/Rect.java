package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import android.graphics.Color;
import android.util.Log;
import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class Rect implements Drawable, Intersector {
    private float left, top, right, bottom;

    public Rect(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    private void validateRect() {
        // Ensure that rectangle is valid. This has bitten me in the past, so it's nice to have this
        // check.
        if (left > right || top > bottom) {
            throw new IllegalArgumentException("The rectangle is invalid. Left > right or top > bottom.");
        }
    }

    /**
     * Determines whether `point` collides with any of this rectangle's edges.
     */
    public boolean collidesWith(VectorF point) {
        validateRect();

        return containsPoint(point.x, point.y);
    }

    public boolean containsPoint(float x, float y) {
        validateRect();

        if (x >= left && x <= right && y <= top && y >= bottom) {
            return true;
        }

        return false;
    }

    public Manifold collisionTest(VectorF point) {
        if (collidesWith(point)) {
            Log.e("RectCol", "TODO MANIFOLD");
            return new Manifold(null, -1, true);
        }
        return new Manifold(null, -1, true);
    }

    public Line[] getLines() {
        return new Line[] {
                new Line(getTopLeft(), getTopRight()),
                new Line(getTopLeft(), getBottomLeft()),
                new Line(getBottomLeft(), getBottomRight()),
                new Line(getTopRight(), getBottomRight())
            };
    }

    private VectorF getTopLeft() {
        return new VectorF(left, top);
    }

    private VectorF getTopRight() {
        return new VectorF(right, top);
    }

    private VectorF getBottomLeft() {
        return new VectorF(left, bottom);
    }

    private VectorF getBottomRight() {
        return new VectorF(right, bottom);
    }

    public float distanceSquared(VectorF point) {
        float result = Line.distanceSquared(getTopLeft(), getTopRight(), point);
        // TODO
        /*
        float temp = Line.distanceSquared(getTopLeft(), getTopRight(), point);;
        if (result > temp) {
            result = temp;
        }
        temp = getBottomLeft().distSquared(point);
        if (result > temp) {
            result = temp;
        }
        temp = getBottomRight().distSquared(point);
        if (result > temp) {
            result = temp;
        }*/
        //Log.d("DistSq", "Dist " + getTop() + " " + point.getX() + " " + point.getY() + " is " + result);
        return result;
    }

    /**
    This method is just for debugging purposes!
     */
    public void draw(GameView view) {
        view.drawRectFrame(left, top, right, bottom, Color.parseColor("#ff1122"));

    }

    /*
    Getters and setters, a.k.a boilerplate.
     */

    public void setHorizontal(float top, float bottom) {
        this.top = top;
        this.bottom = bottom;
        validateRect();
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
        validateRect();
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
        validateRect();
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
        validateRect();
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
        validateRect();
    }
}
