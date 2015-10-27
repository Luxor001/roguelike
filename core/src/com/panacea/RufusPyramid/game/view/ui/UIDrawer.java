
package com.panacea.RufusPyramid.game.view.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.panacea.RufusPyramid.game.GameController;
import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.AttackAction;
import com.panacea.RufusPyramid.game.actions.PassAction;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.items.IItem;
import com.panacea.RufusPyramid.game.items.Item;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;
import com.panacea.RufusPyramid.game.view.GameCamera;
import com.panacea.RufusPyramid.game.view.GameDrawer;
import com.panacea.RufusPyramid.game.view.ItemsDrawer;
import com.panacea.RufusPyramid.game.view.MusicPlayer;
import com.panacea.RufusPyramid.game.view.ViewObject;
import com.panacea.RufusPyramid.game.view.input.HeroInputManager;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.screens.MenuScreen;
import com.panacea.RufusPyramid.save.SaveLoadHelper;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.JsonResult;
import de.tomgrill.gdxfacebook.core.SignInMode;
import de.tomgrill.gdxfacebook.core.SignInResult;

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
    private Image background;
    private Image attackBackground;
    private Image lifeStats;
    private Stack inventory;
    private Table itemsTable;


    private com.badlogic.gdx.scenes.scene2d.ui.Image options;
    private ImageButton resumeButton;
    private ImageButton postButton;
    private ImageButton exitButton;
    private transient HealthBar healthBar;
    private transient HealthBar manaBar;
//    private Sound inventoryOpenSound;
//    private Sound inventoryCloseSound;
//    private Sound pickSound;
    public UIDrawer() {
        this.labels = new ArrayList<Label>(3);
    }

    private BitmapFont goldAmountText;
    private BitmapFont notificationText;
    private  boolean notificationTextVisible;
    private int notificationTextWidth;
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

//
//        SoundsProvider.get().loadSound(SoundsProvider.Sounds.INVENTORY);
//        SoundsProvider.get().loadSound(SoundsProvider.Sounds.CLICK);
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



        Texture imageTexture = new Texture(Gdx.files.internal("data/ui/quickback3.png"));
        background = new com.badlogic.gdx.scenes.scene2d.ui.Image(imageTexture);
        background.setPosition(maxX / 2 - 50, 0);
        background.setSize(maxX / 2 + 50, background.getHeight());

        imageTexture = new Texture(Gdx.files.internal("data/ui/life3.png"));
        lifeStats = new com.badlogic.gdx.scenes.scene2d.ui.Image(imageTexture);
        lifeStats.setPosition(0, maxY - imageTexture.getHeight());
        lifeStats.setSize(imageTexture.getWidth(), imageTexture.getHeight());

        goldAmountText = new BitmapFont();
        goldAmountText.setColor(Color.YELLOW);
        goldAmountText.getData().setScale(Gdx.graphics.getHeight() / 640f);


        notificationText = new BitmapFont();
        notificationText.setColor(Color.WHITE);
        notificationText.getData().setScale(2.5f);

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
        manaBar.setPosition(113, maxY - imageTexture.getHeight() + 37);

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
//                if (pickSound == null)
//                    pickSound = SoundsProvider.get().getSound(SoundsProvider.Sounds.CLICK)[0];
                MusicPlayer.playSound(MusicPlayer.SoundType.ITEM_PICK);
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


                return true;
            }
        });

        currX +=imageTexture.getWidth()+5;
        imageTexture = new Texture(Gdx.files.internal("data/ui/inventory2.png"));
        selectedTexture= new Texture(Gdx.files.internal("data/ui/inventory2_sel.png"));
        inventoryButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        inventoryButton.setPosition(currX, 10);
        inventoryButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        inventoryButton.getStyle().imageDown = new SpriteDrawable(new Sprite(selectedTexture));
        inventoryButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!GameController.gameInUI) {
                    GameController.gameInUI = !GameController.gameInUI;
                    inventory.setVisible(!inventory.isVisible());
                    InputManager.get().getHeroProcessor().setPaused(true);
                    MusicPlayer.playSound(MusicPlayer.SoundType.INVENTORY_OPEN);
                } else {
                    GameController.gameInUI = false;
                    inventory.setVisible(false);
                    InputManager.get().getHeroProcessor().setPaused(false);
                    MusicPlayer.playSound(MusicPlayer.SoundType.INVENTORY_CLOSE);
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

                GameController.gameInUI = !GameController.gameInUI;
                InputManager.get().getHeroProcessor().setPaused(GameController.gameInUI);
                options.setVisible(GameController.gameInUI);
                resumeButton.setVisible(GameController.gameInUI);
                postButton.setVisible(GameController.gameInUI);
                exitButton.setVisible(GameController.gameInUI);
                return true;
            }
        });

        inventory = new Stack();

        imageTexture = new Texture(Gdx.files.internal("data/ui/inventory.png"));
        Image inventoryBackground = new Image(imageTexture);
        inventoryBackground.setFillParent(true);

        itemsTable = new Table();
        itemsTable.setFillParent(true);
        itemsTable.align(Align.topLeft);
        itemsTable.padTop(76);
        itemsTable.padLeft(13);

        inventory.setPosition((maxX / 2) - (imageTexture.getWidth() / 2), (maxY / 2) - (imageTexture.getHeight() / 2) + 200);
        inventory.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        inventory.add(inventoryBackground);
        inventory.add(itemsTable);
        inventory.setVisible(false);

        Texture optionsTexture = new Texture(Gdx.files.internal("data/ui/options_menu.png"));
        options = new com.badlogic.gdx.scenes.scene2d.ui.Image(optionsTexture);
        options.setPosition((maxX / 2) - optionsTexture.getWidth() / 2, (maxY / 2) - optionsTexture.getHeight() / 2);
        options.setSize(optionsTexture.getWidth(), optionsTexture.getHeight());
        options.setVisible(false);

        imageTexture = new Texture(Gdx.files.internal("data/ui/resume.png"));
        selectedTexture= new Texture(Gdx.files.internal("data/ui/resume_sel.png"));
        resumeButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        resumeButton.setPosition(maxX / 2 - imageTexture.getWidth() / 2, maxY / 2 + 50);
        resumeButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        resumeButton.getStyle().imageDown = new SpriteDrawable(new Sprite(selectedTexture));
        resumeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Timer.schedule(new Timer.Task() {  //    delay per fare vedere la selezione utente..
                                   @Override
                                   public void run() {
                                       GameController.gameInUI = false;
                                       InputManager.get().getHeroProcessor().setPaused(false);
                                       options.setVisible(false);
                                       resumeButton.setVisible(false);
                                       postButton.setVisible(false);
                                       exitButton.setVisible(false);
                                   }
                               }, 0.2f
                );
                return true;
            }
        });
        resumeButton.setVisible(false);

        imageTexture = new Texture(Gdx.files.internal("data/ui/post.png"));
        selectedTexture= new Texture(Gdx.files.internal("data/ui/post_sel.png"));
        postButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        postButton.setPosition(maxX / 2 - imageTexture.getWidth() / 2, maxY / 2 - 50);
        postButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        postButton.getStyle().imageDown = new SpriteDrawable(new Sprite(selectedTexture));
        postButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (Gdx.app.getType() == Application.ApplicationType.Android) {
                    GDXFacebook facebook = GameController.facebook;
                    if (!facebook.isSignedIn()) {
                        facebook.signIn(SignInMode.PUBLISH, GameController.permissionsPublish, null);
                    }
                    GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me/feed").useCurrentAccessToken();
                    request.setMethod(Net.HttpMethods.POST);
                    request.putField("message", "Hey, i just scored " + GameModel.get().getHero().getGoldAmount() + " on Rufus Quest!");
                    request.putField("link", "http://postimg.org/image/ni32aiugb/7647c725/");
                    request.putField("caption", "Rufus Quest");

                    facebook.newGraphRequest(request, new GDXFacebookCallback<JsonResult>() {
                        @Override
                        public void onSuccess(JsonResult result) {
                            // Success
                        }

                        @Override
                        public void onError(GDXFacebookError error) {
                            // Error
                        }

                        @Override
                        public void onFail(Throwable t) {
                            // Fail
                        }

                        @Override
                        public void onCancel() {
                            // Cancel
                        }
                    });

                    try {
                        facebook.signIn(SignInMode.PUBLISH, GameController.permissionsPublish, new GDXFacebookCallback<SignInResult>() {
                            @Override
                            public void onSuccess(SignInResult result) {
                                notificationTextVisible = true;
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            notificationTextVisible = false;
                                        }
                                    }, 2f);
                            }

                            @Override
                            public void onCancel() {
                                Gdx.app.debug("rufus", "SIGN IN (read permissions): User canceled login process");
                            }

                            @Override
                            public void onFail(Throwable t) {
                            }

                            @Override
                            public void onError(GDXFacebookError error) {
                            }

                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
        postButton.setVisible(false);

        imageTexture = new Texture(Gdx.files.internal("data/ui/exit.png"));
        selectedTexture= new Texture(Gdx.files.internal("data/ui/exit_sel.png"));
        exitButton = new ImageButton(new SpriteDrawable(new Sprite(imageTexture)));
        exitButton.setPosition(maxX / 2 - imageTexture.getWidth() / 2, maxY / 2 - 150);
        exitButton.setSize(imageTexture.getWidth(), imageTexture.getHeight());
        exitButton.getStyle().imageDown = new SpriteDrawable(new Sprite(selectedTexture));
        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if(GameModel.get() != null){
                            SaveLoadHelper sl = SaveLoadHelper.getIstance();
                            sl.saveGame();
                        }
                        ((Game) Gdx.app.getApplicationListener()).getScreen().dispose(); //basterà questo?
                        if (GameModel.get() != null) {  //FIXME dopo il caricamento non viene impostato?

                            GameModel.get().disposeAll();
                        }
                        GameController.gameInUI = false;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());//TODO: fare dispose!
                    }
                }, 0.5f        //    delay per fare vedere la selezione utente..
                );
                return true;
            }
        });
        exitButton.setVisible(false);

//Some stuff like setBounds, set the content with .add(actor), and so on

        GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
        layout.setText(notificationText, "Score on facebook posted!");
        notificationTextWidth = (int) layout.width;// contains the width of the current set text

        stage.addActor(table);
        stage.addActor(options);
        stage.addActor(resumeButton);
        stage.addActor(postButton);
        stage.addActor(exitButton);
        stage.addActor(lifeStats);
        stage.addActor(inventory);
//        stage.addActor(itemsTable);
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

        if (itemsTable.getCells().size != GameModel.get().getHero().getEquipment().getStorage().size()) {
            itemsTable.clearChildren();
            int i = 0;
            for (final UsableItem item : GameModel.get().getHero().getEquipment().getStorage()) {
                Image itemImage = new Image(GameDrawer.get().getItemsDrawer().getTexture(item));
                itemImage.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        InputManager.get().getHeroProcessor().useItem(item);
                    }
                });
                itemsTable.add(itemImage).size(80,80).align(Align.topLeft).padBottom(10).padRight(8);
                i++;
                if (i % 6 == 0)
                    itemsTable.row();
            }
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        float maxY =  Gdx.graphics.getHeight();
        batch.begin();
        goldAmountText.draw(batch, GameModel.get().getHero().getGoldAmount() + "", 160, maxY - 7);
        if(notificationTextVisible)
            notificationText.draw(batch, "Score on facebook posted!",GameCamera.get().viewportWidth/2-notificationTextWidth/3, 250 );
        batch.end();
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
