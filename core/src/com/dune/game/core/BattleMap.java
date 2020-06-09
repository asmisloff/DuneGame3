package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BattleMap {

    private class Cell {

        protected int cellX, cellY;
        protected int resource;
        private float resourceRegenerationRate;
        private float resourceRegenerationTime;

        protected Cell() {}

        public Cell(int cellX, int cellY) {
            this.cellX = cellX;
            this.cellY = cellY;
            if (MathUtils.random() < 0.1f) {
                resource = MathUtils.random(1, 3);
            }
            resourceRegenerationRate = MathUtils.random(5.0f) - 4.5f;
            if (resourceRegenerationRate < 0.0f) {
                resourceRegenerationRate = 0.0f;
            } else {
                resourceRegenerationRate *= 20.0f;
                resourceRegenerationRate += 10.0f;
            }
        }

        protected void update(float dt) {
            if (resourceRegenerationRate > 0.01f) {
                resourceRegenerationTime += dt;
                if (resourceRegenerationTime > resourceRegenerationRate) {
                    resourceRegenerationTime = 0.0f;
                    resource++;
                    if (resource > 5) {
                        resource = 5;
                    }
                }
            }
        }

        protected void render(SpriteBatch batch) {
            if (resource > 0) {
                float scale = 0.5f + resource * 0.2f;
                batch.draw(resourceTexture, cellX * 80, cellY * 80, 40, 40, 80, 80, scale, scale, 0.0f);
            } else {
                if (resourceRegenerationRate > 0.01f) {
                    batch.draw(resourceTexture, cellX * 80, cellY * 80, 40, 40, 80, 80, 0.1f, 0.1f, 0.0f);
                }
            }
        }
    }

    private class Storage extends Cell {
        public Storage(int cellX, int cellY) {
            this.cellX = cellX;
            this.cellY = cellY;
            this.resource = -1;
        }

        @Override
        protected void render(SpriteBatch batch) {
            batch.draw(storageTexture, cellX * 80, cellY * 80, 40, 40, 80, 80, 1, 1, 0);
        }
    }

    public static final int COLUMNS_COUNT = 20;
    public static final int ROWS_COUNT = 12;
    public static final int CELL_SIZE = 80;
    public static final int MAP_WIDTH_PX = COLUMNS_COUNT * CELL_SIZE;
    public static final int MAP_HEIGHT_PX = ROWS_COUNT * CELL_SIZE;

    private TextureRegion grassTexture;
    private TextureRegion resourceTexture;
    private TextureRegion storageTexture;
    private Cell[][] cells;

    public BattleMap() {
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.resourceTexture = Assets.getInstance().getAtlas().findRegion("resource");
        this.storageTexture = Assets.getInstance().getAtlas().findRegion("storage");
        this.cells = new Cell[COLUMNS_COUNT][ROWS_COUNT];
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            for (int j = 0; j < ROWS_COUNT; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
        for (int col = 0; col < 2; ++col) {
            cells[col][0] = new Storage(col, 0);
        }
    }

    public int getResourceCount(Vector2 point) {
        int cx = (int) (point.x / CELL_SIZE);
        int cy = (int) (point.y / CELL_SIZE);
        return cells[cx][cy].resource;
    }

    public int harvestResource(Vector2 point, int power) {
        int value = 0;
        int cx = (int) (point.x / CELL_SIZE);
        int cy = (int) (point.y / CELL_SIZE);
        Cell cell = cells[cx][cy];
        if (cell instanceof Storage) {
            value = -power;
        }
        else if (cell.resource >= power) {
            value = power;
            cell.resource -= power;
        } else {
            value = cell.resource;
            cell.resource = 0;
        }
        return value;
    }



    public void render(SpriteBatch batch) {
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            for (int j = 0; j < ROWS_COUNT; j++) {
                batch.draw(grassTexture, i * 80, j * 80);
                cells[i][j].render(batch);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            for (int j = 0; j < ROWS_COUNT; j++) {
                cells[i][j].update(dt);
            }
        }
    }
}
