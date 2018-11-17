package com.mrhuman.levi9.ageofarmies;

import android.os.Build;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;

import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Renderable;
import com.mrhuman.levi9.ageofarmies.gamecore.Building;
import com.mrhuman.levi9.ageofarmies.gamecore.GameModel;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


class BuildingNode extends Node {

    private Building building;
    private Node buildingVisual;
    private Node buildingOptions;
    private Node buildingHealth;

    public BuildingNode(Building building, Renderable renderable, float scale) {
        super();
        this.building = building;

        buildingVisual = new Node();
        buildingVisual.setParent(this);
        buildingVisual.setRenderable(renderable);
        buildingVisual.setLocalScale(new Vector3(scale, scale, scale));

        buildingVisual.setOnTapListener(new OnTapListener() {
            @Override
            public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {

            }
        });
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}

public class GUIAdapter extends Node implements Runnable {

    private GameActivity gameActivity;

    private GameModel gameModel;
    private BuildingNode board[][];
    private int width;
    private int height;
    private int scale;
    private boolean running;

    public GUIAdapter(int width, int height, int scale, GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        this.width = width;
        this.height = height;
        this.scale = scale;
        gameModel = new GameModel(); // width, height

        board = new BuildingNode[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = new BuildingNode(null, gameActivity.tileRenderable, 1.0f);
                board[i][j].setParent(this);
                board[i][j].setLocalPosition(getPositionFromXY(i, j));
            }
        }

        //gameModel.start();
        running = true;
        //new Thread(this).start();
    }

    private Vector3 getPositionFromXY(int i, int j) {
        return new Vector3(
                (i - width / 2.0f) / 5.0f,
                (0.0f),
                (j - height / 2.0f) / 5.0f
        );
    }

    public void finish() {
        running = false;
        notifyAll();
    }

    @Override
    public void run() {
        while (running) {
            synchronized (this) {

            }
            try {
                sleep(1000/30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
