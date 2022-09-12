package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * Used YouTube Tutorial from Almas Baimagambetov
 */

public class AsteroidsApp extends Application {

    // Initializing the GUI
    private Pane root;
    // Initializing list of game objects
    private List<GameObject> bullets = new ArrayList<>();
    private List<GameObject> enemies = new ArrayList<>();
    // Initializing vital game objects
    private GameObject player;

    boolean shooting = false;
    int score = 0;
    Text tracker;
    static Polygon y = new Polygon(0, 0, 0, 15, 30, 7.5); // create player's shape = triangle
    // static Polygon h = new Polygon(20,10,40,10,50,30,30,45,10,30); --> create asteroid's shape = pentagon

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    private void addBullet(GameObject bullet, double x, double y) {
        bullets.add(bullet);
        addGameObject(bullet, x, y);
    }

    private void addEnemy(GameObject enemy, double x, double y) {
        enemies.add(enemy);
        addGameObject(enemy, x, y);
    }

    private void onUpdate() {
        tracker.setText("Score: " + score);

        player.inBounds(root.getWidth(), root.getHeight());
        if (shooting&&Math.random()<.15){
            Bullet bullet = new Bullet();
            bullet.setVelocity(player.getVelocity().normalize().multiply(5));
            addBullet(bullet, player.getView().getTranslateX(), player.getView().getTranslateY());
        }

        for (GameObject enemy : enemies) {
            if (player.isColliding(enemy)) {
                Platform.setImplicitExit(false);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.exit();
            }
            for (GameObject bullet : bullets) {
                if (bullet.isColliding(enemy)) {
                    score+=100;
                    bullet.setAlive(false);
                    enemy.setAlive(false);

                    root.getChildren().removeAll(bullet.getView(), enemy.getView());
                }
            }
        }

        bullets.removeIf(GameObject::isDead); // removes bullets
        enemies.removeIf(GameObject::isDead); // removes enemies

        bullets.forEach(GameObject::update); // updates the status for all bullets
        enemies.forEach(GameObject::update); // updates the status for all enemies

        player.update(); // if enemy has hit player

        if (Math.random() < 0.02) {
            addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
    }


    private static class Player extends GameObject {
        Player() {
            super(y);
        }
    }

    private static class Enemy extends GameObject {
        Enemy() {
            super(new Polygon(20,10,40,10,50,30,30,45,10,30));
            this.setColor("SILVER");
            // super(new Rectangle(Math.random()*10+15, Math.random()*10+15, Color.SILVER)); // changes shape to rectangle
        }
    }

    private static class Bullet extends GameObject {
        Bullet() {
            super(new Circle(5, 5, 5, Color.RED));
        }
    }

    private Parent createContent() {
        root = new Pane();
        root.setStyle("-fx-background-color: black;");
        root.setPrefSize(600, 600);

        player = new Player();
        player.setVelocity(new Point2D(1, 0));
        addGameObject(player, 300, 300);

        tracker = new Text(20, 40, "Score: 0");
        tracker.setFill(Color.WHITESMOKE); // make text color white
        tracker.setFont(new Font(30));
        root.getChildren().add(tracker);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;
    }

    @Override // by default
    public void start(Stage stage) throws Exception {
        y.setFill((Color.IVORY)); // make player's color white
        stage.setScene(new Scene(createContent()));
        stage.getScene().setOnKeyPressed(e -> { // e=event associated with pressing a button
            if (e.getCode() == KeyCode.LEFT) {
                player.rotateLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                player.rotateRight();
            } else if (e.getCode() == KeyCode.SPACE) {
                shooting = true;
                Bullet bullet = new Bullet();
                bullet.setVelocity(player.getVelocity().normalize().multiply(5));
                addBullet(bullet, player.getView().getTranslateX(), player.getView().getTranslateY());
            }
        });
        stage.getScene().setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                shooting = false;
            }
                });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // starts a new thread (process)
    }
}