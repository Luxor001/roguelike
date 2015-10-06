package com.panacea.RufusPyramid.game.view.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.panacea.RufusPyramid.game.GameController;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.AttackAction;
import com.panacea.RufusPyramid.game.actions.PassAction;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.view.GameCamera;
import com.panacea.RufusPyramid.game.view.SoundsProvider;
import com.panacea.RufusPyramid.game.view.ViewObject;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.screens.MenuScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gio on 28/07/15.
 */
public class UIDrawer extends ViewObject {

    private Skin skin;
    private Stage stage;
    private SpriteBatch batch;
    private ShapeRenderer renderer;

    private ArrayList<Label> labels;

    private ImageButton passButton;
    private ImageButton spellButton;
    private ImageButton inventoryButton;
    private ImageButton optionsButton;
    private ImageButton attackButton;
    private com.badlogic.gdx.scenes.scene2d.ui.Image background;
    private com.badlogic.gdx.scenes.scene2d.ui.Image attackBackground;
    private com.badlogic.gdx.scenes.scene2d.ui.Image lifeStats;
    private com.badlogic.gdx.scenes.scene2d.ui.Image inventory;
    private TextButton button;
    private HealthBar healthBar;
    private HealthBar manaBar;
    private Sound inventoryOpenSound;
    private Sound pickSound;
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


        SoundsProvider.get().loadSound(SoundsProvider.Sounds.INVENTORY_OPEN);
        SoundsProvider.get().loadSound(SoundsProvider.Sounds.CLICK);
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
        float maxY = GameCamera.get().viewportHeight;
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

        imageTexture = new Texture(Gdx.files.internal("data/ui/life3.png"));
        lifeStats = new com.badlogic.gdx.scenes.scene2d.ui.Image(imageTexture);
        lifeStats.setPosition(0, maxY - imageTexture.getHeight());
        lifeStats.setSize(imageTexture.getWidth(), imageTexture.getHeight());

        SpriteDrawable healthForeGround = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("data/ui/health.png"))));
        SpriteDrawable healthBackgroundd = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("data/ui/healthBack.png"))));
        healthBar = new HealthBar(0,100,1,false,new HealthBar.ProgressBarStyle(healthBackgroundd,healthForeGround));
        healthBar.setValue(100);
        healthBar.setHeight(healthForeGround.getSprite().getHeight());
        healthBar.setWidth(healthForeGround.getSprite().getWidth());
        healthBar.setPosition(113, maxY - imageTexture.getHeight() + 64);

        SpriteDrawable manaForeGround = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("data/ui/mana.png"))));
        manaBar = new HealthBar(0,100,1,false,new HealthBar.ProgressBarStyle(healthBackgroundd,manaForeGround));
        manaBar.setValue(100);
        manaBar.setHeight(manaForeGround.getSprite().getHeight());
        manaBar.setWidth(manaForeGround.getSprite().getWidth());
        manaBar.setPosition(113, maxY - imageTexture.getHeight()+37);

        imageTexture = new Texture(Gdx.files.internal("data/ui/backAttack.png"));
        attackBackground= new com.badlogic.gdx.scenes.scene2d.ui.Image(imageTexture);
        attackBackground.setPosition(0, 0);
        attackBackground.setSize(imageTexture.getWidth(), imageTexture.getHeight());

        imageTexture = new Texture(Gdx.files.internal("data/ui/attackButton.png"));
        Texture selectedTexture= new Texture(Gdx.files.internal("data/ui/attackButton_deact.png"));
        attackButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        attackButton.setPosition(8, 8);
        attackButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        attackButton.getStyle().imageDisabled = new SpriteDrawable(new Sprite(selectedTexture));
        attackButton.setDisabled(true);
        attackButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                DefaultHero hero = GameModel.get().getHero();
                if (hero.getFirstTarget() != null)
                    hero.fireActionChosenEvent(new AttackAction(hero, hero.getFirstTarget()));
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
                GameModel.get().getHero().fireActionChosenEvent(new PassAction(GameModel.get().getHero()));
                if (pickSound == null)
                    pickSound = SoundsProvider.get().getSound(SoundsProvider.Sounds.CLICK)[0];
                pickSound.play(0.6f);
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
                if(!GameController.gameInUI) {
                    GameController.gameInUI=true;
                    inventory.setVisible(true);
                    if (inventoryOpenSound == null)
                        inventoryOpenSound = SoundsProvider.get().getSound(SoundsProvider.Sounds.INVENTORY_OPEN)[0];
                    InputManager.get().getHeroProcessor().setPaused(true);
                    inventoryOpenSound.play(1f);
                }else{
                    GameController.gameInUI = false;
                    inventory.setVisible(false);
                    InputManager.get().getHeroProcessor().setPaused(false);
                }
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

                ((Game) Gdx.app.getApplicationListener()).getScreen().dispose(); //basterà questo?
                GameModel.get().disposeAll();
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());//TODO: Fare dispose!
                return true;
            }
        });


        imageTexture = new Texture(Gdx.files.internal("data/ui/inventory.png"));
        inventory = new com.badlogic.gdx.scenes.scene2d.ui.Image(imageTexture);
        inventory.setPosition((maxX/2)-(imageTexture.getWidth()/2), (maxY/2 )- (imageTexture.getHeight()/2)+200);
        inventory.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        inventory.setVisible(false);

        stage.addActor(lifeStats);
        stage.addActor(inventory);
        stage.addActor(healthBar);
        stage.addActor(manaBar);
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

    public ImageButton getAttackButton(){
        return this.attackButton;
    }
    public void setHealthBarValue(float value){
        this.healthBar.setValue(value);
    }
    public void setManaBarValue(float value){
        this.manaBar.setValue(value);
    }
}
