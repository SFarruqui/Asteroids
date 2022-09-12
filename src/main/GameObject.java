package main;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Used YouTube Tutorial from Almas Baimagambetov
 */
public class GameObject {

    private Node view;
    private Point2D velocity = new Point2D(0, 0);

    private boolean alive = true;

    public GameObject(Node view) {
        this.view = view;
    }

    public void update() {
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public Node getView() {
        return view;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDead() {
        return !alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getRotate() {
        return view.getRotate();
    }

    public void rotateRight() {
        view.setRotate(view.getRotate() + 20);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
    }

    public void rotateLeft() {
        view.setRotate(view.getRotate() - 20);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
    }

    public boolean isColliding(GameObject other) {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }

    public void inBounds(double width, double height) {
        if (getView().getBoundsInParent().getMaxX() >= width) {
            getView().setTranslateX(getView().getTranslateX() - width);
        }
        if (getView().getBoundsInParent().getMinX() <= 0) {
            getView().setTranslateX(getView().getTranslateX() + width);
        }
        if (getView().getBoundsInParent().getMaxY() >= height) {
            getView().setTranslateY(getView().getTranslateY() - height);
        }
        if (getView().getBoundsInParent().getMinY() <= 0) {
            getView().setTranslateY(getView().getTranslateY() + height);
        }
    }
        public void setColor(String color){
            view.setStyle(("-fx-fill:"+color+";"));
        }
}