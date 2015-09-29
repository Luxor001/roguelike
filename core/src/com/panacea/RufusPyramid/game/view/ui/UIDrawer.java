package com.panacea.RufusPyramid.game.view.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.view.GameCamera;
import com.panacea.RufusPyramid.game.view.ViewObject;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.screens.GameScreen;
import com.panacea.RufusPyramid.game.view.screens.MenuScreen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gio on 28/07/15.
 */
public class UIDrawer extends ViewObject {

//    private Stage stage;
//    private Skin skin;

    Skin skin;
    Stage stage;
    SpriteBatch batch;
    Actor root;
    ShapeRenderer renderer;

    ArrayList<Label> labels;

    ImageButton quick;
    TextButton button;

    public UIDrawer() {
        this.labels = new ArrayList<Label>(3);
    }

    @Override
    public void create() {
        super.create();

        float w = (float)Gdx.graphics.getWidth();
        float h = (float)Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
//        OrthographicCamera camera = new OrthographicCamera();
//        camera.setToOrtho(false, (w / h) * 350, 350);
//        camera.update();
        OrthographicCamera camera = new OrthographicCamera();

        //VEDI https://github.com/libgdx/libgdx/wiki/Viewports
        //Se si vuole rendere utilizzabile (e ridimensionabile) il gioco bisogna sicuramente cambiare Viewport
        stage = new Stage(new StretchViewport(GameCamera.get().viewportWidth, GameCamera.get().viewportHeight));

        InputManager.get().addProcessor(stage);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        skin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.getFont("default-font").getData().markupEnabled = true;
        float scale = 1;
        skin.getFont("default-font").getData().setScale(scale);

        this.labels.add(0, this.getNewLabel(""));
        this.labels.add(1, this.getNewLabel(""));
        this.labels.add(2, this.getNewLabel(""));

        Table table = new Table();
        table.setPosition(10, 10);

        table.add(this.labels.get(2)).minWidth(Gdx.graphics.getWidth() - 20).fill();
        table.row();
        table.add(this.labels.get(1)).minWidth(Gdx.graphics.getWidth() - 20).fill();
        table.row();
        table.add(this.labels.get(0)).minWidth(Gdx.graphics.getWidth() - 20).fill();
        table.pack();

        button = new TextButton("Menu", skin);
        button.setSize(60, 40);
        button.setPosition(0, stage.getHeight() - button.getHeight());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go

//                ((Game)Gdx.app.getApplicationListener()).setScreen(new SplashScreen());
                Gdx.app.log("", "Clicked");
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
            }
        });

        stage.addActor(button);
        stage.addActor(table);

        quick = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("data/ui/quick2.png")))));
        quick.setPosition(0, 0);
        quick.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("quick", "quickClicked");
            }
        });
        stage.addActor(quick);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        List<String> diaryLastLines = GameModel.get().getDiary().getLastThreeLines();

        for (int i = 0; i < diaryLastLines.size(); i++) {
            this.labels.get(i).setText(diaryLastLines.get(i));
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private Label getNewLabel(String text) {
        Label newLabel = new Label(text, skin);
        newLabel.setWrap(true);
        newLabel.setAlignment(Align.bottom | Align.left);
        return newLabel;
    }

    @Override
    public void dispose () {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Gdx.app.log("", "resize");

        stage.getViewport().update(width, height, true);
//        button.setPosition(10, height - button.getHeight() - 10);
//        stage.getViewport().update(width,height);
//        stage.getCamera().setToOrtho(false,width,height);
//        ((OrthographicCamera)stage.getCamera()).setToOrtho(true, newW, newH);

        //TODO resize degli elementi grafici.. ? Forse no, però si potrebbero riposizionare.
//        textArea.setWidth(Gdx.graphics.getWidth() - 20);
    }
}
