package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class BattleMap {
    private TextureRegion grassTexture;
    private TextureRegion spiceTexture;
    private final float SPICE_PROB = 0.1f;
    private final int[][] map;
    public final int SPICE = 1;
    public final int GRASS = 0;
    public final int W = 16;
    public final int H = 9;

    public BattleMap() {
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.spiceTexture = Assets.getInstance().getAtlas().findRegion("spice");
        this.map = new int[W][H];
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                if (MathUtils.random() < SPICE_PROB) {
                    map[i][j] = SPICE;
                } else {
                    map[i][j] = GRASS;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                batch.draw(map[i][j] == GRASS ? grassTexture : spiceTexture, i * 80, j * 80);
            }
        }
    }

    public void flipTile(int i, int j) {
        map[i][j] = map[i][j] == SPICE ? GRASS : SPICE;
    }

    public int getTileType(int i, int j) {
        return map[i][j];
    }
}
