package com.panacea.RufusPyramid.game.view.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.AttackAction;
import com.panacea.RufusPyramid.game.actions.PassAction;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.view.GameCamera;
import com.panacea.RufusPyramid.game.view.ViewObject;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.screens.MenuScreen;

import java.util.ArrayList;
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
    ShapeRenderer renderer;

    ArrayList<Label> labels;

    ImageButton passButton;
    ImageButton spellButton;
    ImageButton inventoryButton;
    ImageButton optionsButton;
    public ImageButton attackButton;
    com.badlogic.gdx.scenes.scene2d.ui.Image background;
    com.badlogic.gdx.scenes.scene2d.ui.Image attackBackground;
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

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        skin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.getFont("default-font").getData().markupEnabled = true;
        float scale = 1.2f;
        skin.getFont("default-font").getData().setScale(scale);

        this.labels.add(0, this.getNewLabel(""));
        this.labels.add(1, this.getNewLabel(""));
        this.labels.add(2, this.getNewLabel(""));

        float maxX = GameCamera.get().viewportWidth;
        Table table = new Table();
        table.setPosition(maxX / 2 - 50, 105);

        table.add(this.labels.get(2)).minWidth(Gdx.graphics.getWidth() - 20).fill();
        table.row();
        table.add(this.labels.get(1)).minWidth(Gdx.graphics.getWidth() - 20).fill();
        table.row();
        table.add(this.labels.get(0)).minWidth(Gdx.graphics.getWidth() - 20).fill();
        table.pack();


        stage.addActor(table);

        Texture imageTexture = new Texture(Gdx.files.internal("data/ui/quickback3.png"));
        background = new com.badlogic.gdx.scenes.scene2d.ui.Image(imageTexture);
        background.setPosition(maxX / 2 - 50, 0);
        background.setSize(maxX / 2 + 50, background.getHeight());

        imageTexture = new Texture(Gdx.files.internal("data/ui/backAttack.png"));
        attackBackground= new com.badlogic.gdx.scenes.scene2d.ui.Image(imageTexture);
        attackBackground.setPosition(0, 0);
        attackBackground.setSize(imageTexture.getWidth(), imageTexture.getHeight());

        imageTexture = new Texture(Gdx.files.internal("data/ui/attackButton.png"));
        Texture selectedTexture= new Texture(Gdx.files.internal("data/ui/attackButton_deact.png"));
        attackButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        attackButton.setPosition(8,8);
        attackButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        attackButton.getStyle().imageDisabled = new SpriteDrawable(new Sprite(selectedTexture));
        attackButton.setDisabled(true);
        attackButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                DefaultHero hero = GameModel.get().getHero();
                if(hero.getFirstTarget() != null)
                    hero.fireActionChosenEvent(new AttackAction(hero,hero.getFirstTarget()));
                return true;
            }
        });

        float currX = (maxX/2 - 40)+10;
        imageTexture = new Texture(Gdx.files.internal("data/ui/pass2.png"));
        selectedTexture= new Texture(Gdx.files.internal("data/ui/pass2_sel.png"));
        passButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        passButton.setPosition(currX, 10);
        passButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        passButton.getStyle().imageDown = new SpriteDrawable(new Sprite(selectedTexture));
        passButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameModel.get().getHero().fireActionChosenEvent(new PassAction());
                return true;
            }
        });


        currX +=imageTexture.getWidth()+5;
        imageTexture = new Texture(Gdx.files.internal("data/ui/spell2.png"));
        selectedTexture= new Texture(Gdx.files.internal("data/ui/spell2_sel.png"));
        spellButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        spellButton.setPosition(currX, 10);
        spellButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        spellButton.getStyle().imageDown = new SpriteDrawable(new Sprite(selectedTexture));
        spellButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("spell", "Clicked");

                return true;
            }
        });


        currX +=imageTexture.getWidth()+5;
        imageTexture = new Texture(Gdx.files.internal("data/ui/inventory2.png"));
        selectedTexture= new Texture(Gdx.files.internal("data/ui/inventory2_sel.png"));
        inventoryButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        inventoryButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        inventoryButton.setPosition(currX, 10);
        inventoryButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        inventoryButton.getStyle().imageDown = new SpriteDrawable(new Sprite(selectedTexture));
        inventoryButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("inventory", "Clicked");
                return true;
            }
        });

        currX +=imageTexture.getWidth() + 5;
        imageTexture = new Texture(Gdx.files.internal("data/ui/options2.png"));
        selectedTexture= new Texture(Gdx.files.internal("data/ui/options2_sel.png"));
        optionsButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        optionsButton= new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        optionsButton.setPosition(currX, 10);
        optionsButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        optionsButton.getStyle().imageDown = new SpriteDrawable(new Sprite(selectedTexture));
        optionsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("options", "Clicked");
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                return true;
            }
        });

        stage.addActor(attackBackground);
        stage.addActor(attackButton);
        stage.addActor(background);
        stage.addActor(passButton);
        stage.addActor(spellButton);
        stage.addActor(inventoryButton);
        stage.addActor(optionsButton);
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

    public Stage getStage(){
        return stage;
    }
}
