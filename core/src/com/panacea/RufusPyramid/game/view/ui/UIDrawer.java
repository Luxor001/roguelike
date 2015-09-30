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
import com.badlogic.gdx.scenes.scene2d.InputListener;
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

import java.awt.Image;
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
    ImageButton passButton;
    ImageButton spellButton;
    ImageButton inventoryButton;
    ImageButton optionsButton;
    com.badlogic.gdx.scenes.scene2d.ui.Image bar;
    com.badlogic.gdx.scenes.scene2d.ui.Image background;
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

        float maxX = GameCamera.getMaxX();
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

        Texture imageTexture = new Texture(Gdx.files.internal("data/ui/background.png"));
        background = new com.badlogic.gdx.scenes.scene2d.ui.Image(imageTexture);
        background.setPosition(maxX / 2 - 40, 0);
        background.setSize(maxX/2  + 40, 100);

        float currX = (maxX/2 - 40)+10;
        imageTexture = new Texture(Gdx.files.internal("data/ui/pass2.png"));
        passButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        passButton.setPosition(currX, 15);
        passButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());

        passButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("pass", "Clicked");
                return true;
            }
        });

        currX +=imageTexture.getWidth()+5;
        imageTexture = new Texture(Gdx.files.internal("data/ui/spell2.png"));
        spellButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        spellButton.setPosition(currX, 15);
        spellButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        spellButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("spell", "Clicked");
                return true;
            }
        });

        currX +=imageTexture.getWidth()+5;
        imageTexture = new Texture(Gdx.files.internal("data/ui/inventory2.png"));
        inventoryButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        inventoryButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        inventoryButton.setPosition(currX, 15);
        inventoryButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        inventoryButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("inventory", "Clicked");
                return true;
            }
        });

        currX +=imageTexture.getWidth() + 5;
        imageTexture = new Texture(Gdx.files.internal("data/ui/options2.png"));
        optionsButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        optionsButton= new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        optionsButton.setPosition(currX, 15);
        optionsButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        optionsButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("options", "Clicked");
                return true;
            }
        });

        imageTexture = new Texture(Gdx.files.internal("data/ui/bar.png"));
        bar = new com.badlogic.gdx.scenes.scene2d.ui.Image(imageTexture);
        bar.setPosition(maxX/2  - 40, 100);
        bar.setSize(maxX/2 + 40, imageTexture.getHeight());

        stage.addActor(background);
        stage.addActor(passButton);
        stage.addActor(spellButton);
        stage.addActor(inventoryButton);
        stage.addActor(optionsButton);
        stage.addActor(bar);
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
