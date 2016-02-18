package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import android.graphics.Color;
import android.graphics.Paint;

import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class Circle extends Intersector implements Drawable {
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
    public boolean collidesWith(Polygon shape) {
        // Based on answer here: http://stackoverflow.com/a/402019/492186

        // Check whether Circle's centre lies within the rectangle.
        if (shape.collisionTest(center).hasCollided()) { return true; }

        // Check whether either of the sides intersect with the circle.
        for (Line line : shape.getLines()) {
            if (collidesWith(line.getStart(), line.getFinish())) {
                return true;
            }
        }
        return false;

    }

    /**
     * Checks if the specified shape collides with this Circle.
     * @return A Manifold containing information about the collision.
     */
    @Override
    public Manifold collisionTest(Intersector shape) {
        if (shape instanceof Polygon) {
            return collisionTestWithPolygon((Polygon)shape);
        }
        else if (shape instanceof Line) {
            return collisionTestWithLine((Line)shape);
        }

        return collisionNotImplemented(shape);
    }


    private Manifold collisionTestWithPolygon(Polygon shape) {
        Manifold pointResult = shape.collisionTest(center);
        if (pointResult.hasCollided()) {
            pointResult.setPenetration(pointResult.getPenetration() + radius);
            return pointResult;
        }

        for (Line line : shape.getLines()) {
            Manifold res = collisionTest(line);
            if (res.hasCollided()) {
                return res;
            }
        }
        return Manifold.noCollision();
    }

    /**
     * Checks if the specified line collides with this circle.
     * @return A Manifold instance containing information about the collision.
     */
    private Manifold collisionTestWithLine(Line line) {
        // Diagram at the following link explains this algorithm:
        // http://stackoverflow.com/a/1079478/492186
        VectorF a = line.getStart();
        VectorF b = line.getFinish();

        // Get the vector which represents the line segment.
        VectorF BA = new VectorF(b.x - a.x, b.y - a.y);
        // Get the vector between the line start and the circle center.
        VectorF CA = new VectorF(center.x - a.x, center.y - a.y);

        // Calculate the length of the line segment.
        float l = BA.magnitude();

        // Make sure that CA is on the line segment and that it is the point closest to center.
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

        float x = CA.x - center.x;
        float y = CA.y - center.y;

        // Check if length of vector from point on line to center is less than the radius of
        // the circle. If so, then we have a collision.
        boolean collided = x * x + y * y <= radius*radius;

        if (collided) {
            // Set a flag for debugging.
            line.setTimeOfLastCollision(System.nanoTime());

            // Calculate how far the circle penetrated the line.
            float depth = radius - (float)Math.sqrt(x * x + y * y);

            // The normal points in the direction of the point on the line that collides with
            // the circle.
            VectorF normal = new VectorF(x, y);
            normal.normalise();

            return new Manifold(normal, depth, collided);
        }
        else {
            return Manifold.noCollision();
        }
    }

    /**
     * Determines whether Line AB intersects with this Circle.
     */
    private boolean collidesWith(VectorF a, VectorF b) {
        return collisionTest(new Line(a, b)).hasCollided();
    }

    public Circle copy() {
        return new Circle(center.copy(), radius);
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
