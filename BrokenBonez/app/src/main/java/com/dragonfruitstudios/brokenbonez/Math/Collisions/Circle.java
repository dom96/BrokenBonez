package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import android.graphics.Color;
import android.graphics.Paint;

import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class Circle implements Drawable {
    VectorF center;
    float radius;

    /**
     * Creates a new Circle bounding shape with the specified center x and y position as well as
     * radius.
     *
     * Note: You're better off using the other constructor if you already have a position vector.
     */
    public Circle(float cx, float cy, float radius) {
        this.center = new VectorF(cx, cy);
        this.radius = radius;
    }

    /**
     * Constructs a new Circle bounding shape with the specified center vector and radius.
     */
    public Circle(VectorF center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Determines whether this Circle collides with the specified shape.
     * @param shape An object which implements the Intersector interface.
     * @return Whether this Circle collides with the specified shape.
     */
    public boolean collidesWith(Intersector shape) {
        // Based on answer here: http://stackoverflow.com/a/402019/492186

        // Check whether Circle's centre lies within the rectangle.
        if (shape.collidesWith(center)) { return true; }

        // Check whether either of the sides intersect with the circle.
        for (Line line : shape.getLines()) {
            if (collidesWith(line.getStart(), line.getFinish())) {
                return true;
            }
        }
        return false;

    }

    /**
     * Checks if the specified shape collides with this circle.
     * @return A Manifold containing information about the collision.
     */
    public Manifold collisionTest(Intersector shape) {
        // TODO: For now there is no chance of the circle's center being on a Shape's edge.
        //Manifold pointResult = shape.collisionTest(center);
        //if (pointResult.isCollided()) {
        //    pointResult.setPenetration(pointResult.getPenetration() + radius);
        //    return pointResult;
        //}

        for (Line line : shape.getLines()) {
            Manifold res = collisionTest(line);
            if (res.isCollided()) {
                return res;
            }
        }
        return new Manifold(null, -1, false);
    }

    /**
     * Checks if the specified line collides with this circle.
     * @return A Manifold instance containing information about the collision.
     */
    private Manifold collisionTest(Line line) {
        VectorF a = line.getStart();
        VectorF b = line.getFinish();

        VectorF BA = new VectorF(b.x - a.x, b.y - a.y);
        VectorF CA = new VectorF(center.x - a.x, center.y - a.y);
        float l = BA.magnitude();

        BA.normalise();
        float u = CA.dotProduct(BA);
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

        boolean collided = x * x + y * y <= radius*radius;
        if (collided) {
            // Calculate how far the circle penetrated the line.
            float depth = radius - (float)Math.sqrt(x * x + y * y);

            // Calculate the normal.
            VectorF startToFinish = b.subtracted(a);
            VectorF normal = new VectorF(-startToFinish.getY(), startToFinish.getX());

            if (startToFinish.angle() > center.angle()) {
                normal = new VectorF(startToFinish.getY(), -startToFinish.getX());
            }
            normal.normalise();

            // Make sure that the normal meets the line.
            VectorF projectionToLine = normal.clone();
            projectionToLine.mult(radius);
            projectionToLine.add(center);

            // Can't check whether `projectToLine` collides with `line` because collision detection
            // is too strict.
            if (!line.isNear(projectionToLine)) {
                float distanceToA = a.distSquared(center);
                float distanceToB = b.distSquared(center);
                // Choose the normal depending on which edge of the line is nearest the
                // circle's center.
                if (distanceToA > distanceToB) {
                    normal = b.clone();
                    normal.normalise();
                }
                else {
                    normal = a.clone();
                    normal.normalise();
                }
            }

            return new Manifold(normal, depth, collided);
        }
        else {
            return new Manifold(null, -1, false);
        }
    }

    /**
     * Determines whether Line AB intersects with this Circle.
     */
    private boolean collidesWith(VectorF a, VectorF b) {
        return collisionTest(new Line(a, b)).isCollided();
    }

    // <editor-fold desc="Getters/Setters">

    public void setCenter(float cx, float cy) {
        center.set(cx, cy);
    }

    public void setCenter(VectorF center) {
        this.center = center;
    }

    public VectorF getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    // </editor-fold>

    /**
     * This draw method is used for debugging to show where the bounding circle is.
     */
    public void draw(GameView view) {
        view.drawCircle(center.x, center.y, radius, Color.parseColor("#ff279c"), Paint.Style.STROKE);
    }
}
