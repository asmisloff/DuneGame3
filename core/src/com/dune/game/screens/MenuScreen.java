package com.dune.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dune.game.core.Assets;

public class MenuScreen extends AbstractScreen {

    private final Stage stage;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        BitmapFont font14 = Assets.getInstance().getAssetManager().get("fonts/font14.ttf");
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                skin.getDrawable("smButton"), null, null, font14);

        final TextButton exitBtn = new TextButton("Exit", textButtonStyle);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        final TextButton newGameBtn = new TextButton("New Game", textButtonStyle);
        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        Group menuGroup = new Group();
        exitBtn.setPosition(0, 0);
        newGameBtn.setPosition(130, 0);
        menuGroup.addActor(exitBtn);
        menuGroup.addActor(newGameBtn);
        menuGroup.setPosition(900, 680);
        stage.addActor(menuGroup);

        skin.dispose();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    public void update(float dt) {
//        if (Gdx.input.justTouched()) {
//            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
//        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}