package com.dragonfruitstudios.brokenbonez.BoundingShapes;

import android.graphics.Color;
import android.graphics.Point;

import com.dragonfruitstudios.brokenbonez.GameView;
import com.dragonfruitstudios.brokenbonez.PointD;
import com.dragonfruitstudios.brokenbonez.Vector;

public class Circle {
    Vector center;
    double radius;

    /**
     * Note: You're better off using the other constructor if you already have a position vector.
     * @param cx
     * @param cy
     * @param radius
     */
    public Circle(double cx, double cy, double radius) {
        this.center = new Vector(cx, cy);
        this.radius = radius;
    }

    public Circle(Vector center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public void set(double cx, double cy) {
        center.set(cx, cy);
    }

    public boolean collidesWith(Rect rect) {
        // Based on answer here: http://stackoverflow.com/a/402019/492186

        // Check whether Circle's centre lies within the rectangle.
        if (rect.containsPoint(center.x, center.y)) { return true; }

        // Check whether either of the sides intersect with the circle.
        Vector TopLeft = new Vector(rect.left, rect.top);
        Vector TopRight = new Vector(rect.right, rect.top);
        Vector BottomLeft = new Vector(rect.left, rect.bottom);
        Vector BottomRight = new Vector(rect.right, rect.bottom);
        if (collidesWith(TopLeft, TopRight) || collidesWith(TopRight, BottomRight) ||
                collidesWith(BottomRight, BottomLeft) || collidesWith(BottomLeft, TopLeft)) {
            return true;
        }
        return false;

    }

    /**
     * Determines whether Line AB intersects with this Circle.
     */
    public boolean collidesWith(Vector a, Vector b) {
        Vector BA = new Vector(b.x - a.x, b.y - a.y);
        Vector CA = new Vector(center.x - a.x, center.y - a.y);
        double l = BA.magnitude();

        BA.normalise();
        double u = CA.dot_product(BA);
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

        double x = center.x - CA.x;
        double y = center.y - CA.y;

        return x * x + y * y <= radius*radius;
    }

    /**
     * This is just for debugging purposes to show where the bounding circle is.
     * @param view
     */
    public void draw(GameView view) {
        view.drawCircle((float)center.x, (float)center.y, (float)radius, Color.parseColor("#4c4a4d"));
    }
}
