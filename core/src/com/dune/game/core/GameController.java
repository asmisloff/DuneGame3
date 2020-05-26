package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;

public class GameController {
    private BattleMap map;
    private ProjectilesController projectilesController;
    private Tank tank;

    public Tank getTank() {
        return tank;
    }

    public ProjectilesController getProjectilesController() {
        return projectilesController;
    }

    public BattleMap getMap() {
        return map;
    }

    // Инициализация игровой логики
    public GameController() {
        Assets.getInstance().loadAssets();
        this.map = new BattleMap();
        this.projectilesController = new ProjectilesController(this);
        this.tank = new Tank(this, 200, 200);
    }

    public void update(float dt) {
        tank.update(dt);
        projectilesController.update(dt);
        checkCollisions(dt);
    }

    public void checkCollisions(float dt) {
    }

    public void onGameObjectPositionChanged(GameObject go) {    }

    public void onGameObjectPositionChanged(Tank tank) {
        int tileW = 1280 / map.W;
        int tileH = 720 / map.H;

        //индексы клетки, в которой находится танк
        int tanksTileX = (int)tank.position.x / tileW;
        int tanksTileY = (int)tank.position.y / tileH;
        //сколько клеток занимает танк
        int tilesInTankX = tank.diameter / tileW + 1;
        int tilesInTankY = tank.diameter / tileH + 1;
        //область карты вокруг танка для поиска спайса
        int i0 = tanksTileX >= 1 ? tanksTileX - tilesInTankX : 0;
        int i1 = tanksTileX < map.W ? tanksTileX + tilesInTankX : map.W;
        int j0 = tanksTileY >= 1 ? tanksTileY - tilesInTankY : 0;
        int j1 = tanksTileY < map.H ? tanksTileY + tilesInTankY : map.H;

        for (int i = i0; i < i1; i++) {
            for (int j = j0; j < j1; j++) {
                if (map.getTileType(i, j) == map.SPICE) {
                    float tileCenterX = i * tileW + tileW / 2f;
                    float tileCenterY = j * tileH + tileH / 2f;
                    if (tank.position.dst(tileCenterX, tileCenterY) < 0.7f * tank.diameter) {
                        map.flipTile(i, j);
                    }
                }
            }
        }
    }

}
