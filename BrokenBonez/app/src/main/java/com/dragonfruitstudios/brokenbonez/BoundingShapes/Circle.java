package com.dragonfruitstudios.brokenbonez.BoundingShapes;

import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.GameView;
import com.dragonfruitstudios.brokenbonez.VectorF;

public class Circle {
    VectorF center;
    float radius;

    /**
     * Note: You're better off using the other constructor if you already have a position vector.
     * @param cx
     * @param cy
     * @param radius
     */
    public Circle(float cx, float cy, float radius) {
        this.center = new VectorF(cx, cy);
        this.radius = radius;
    }

    public Circle(VectorF center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public void set(float cx, float cy) {
        center.set(cx, cy);
    }

    public boolean collidesWith(Rect rect) {
        // Based on answer here: http://stackoverflow.com/a/402019/492186

        // Check whether Circle's centre lies within the rectangle.
        if (rect.containsPoint(center.x, center.y)) { return true; }

        // Check whether either of the sides intersect with the circle.
        VectorF TopLeft = new VectorF(rect.left, rect.top);
        VectorF TopRight = new VectorF(rect.right, rect.top);
        VectorF BottomLeft = new VectorF(rect.left, rect.bottom);
        VectorF BottomRight = new VectorF(rect.right, rect.bottom);
        if (collidesWith(TopLeft, TopRight) || collidesWith(TopRight, BottomRight) ||
                collidesWith(BottomRight, BottomLeft) || collidesWith(BottomLeft, TopLeft)) {
            return true;
        }
        return false;

    }

    /**
     * Determines whether Line AB intersects with this Circle.
     */
    public boolean collidesWith(VectorF a, VectorF b) {
        VectorF BA = new VectorF(b.x - a.x, b.y - a.y);
        VectorF CA = new VectorF(center.x - a.x, center.y - a.y);
        float l = BA.magnitude();

        BA.normalise();
        float u = CA.dot_product(BA);
        if (u <= 0) {
            CA.set(a.x, a.y);
        }
        else if (u >= l) {
            CA.set(b.x, b.y);
        }
        else {
            BA.mult(u);
            CA.set(BA.x + a.x, BA.y + a.y);
        }

        float x = center.x - CA.x;
        float y = center.y - CA.y;

        boolean result = x * x + y * y <= radius*radius;
        //Log.d("Collision", String.format("(%.1f, %.1f) to (%.1f, %.1f) x Circle(%.1f, %.1f, %.1f) = %b",
        //        a.x, a.y, b.x, b.y, center.x, center.y, radius, result));
        return result;
    }

    /**
     * This is just for debugging purposes to show where the bounding circle is.
     * @param view
     */
    public void draw(GameView view) {
        view.drawCircleWithLine(center.x, center.y, radius, Color.parseColor("#4c4a4d"), Color.parseColor("#14ff1d"));
    }
}
