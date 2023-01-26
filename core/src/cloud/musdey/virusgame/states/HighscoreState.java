package cloud.musdey.virusgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import cloud.musdey.virusgame.VirusGame;
import cloud.musdey.virusgame.config.Config;

public class HighscoreState extends State {

    private TextButton backBtn,shareBtn,exitBtn;
    private Stage menuStage;
    private Skin skin;
    private Viewport bgViewport;

    private BitmapFont font,buttonFont;
    float imageRatio;
    private FreeTypeFontGenerator generator;
    private  int row_height,col_width,highScore;
    private Matrix4 normalProjection;
    private Preferences prefs;
    private Pixmap pixmap;
    private Texture pixmapTexture;
    private TextButton.TextButtonStyle textButtonStyle;

    public HighscoreState(StateManager gsm){
        super(gsm);

        row_height = Gdx.graphics.getWidth() / 14;
        col_width = Gdx.graphics.getWidth() / 14;

        float aspectRatio =(float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth(); // 1,77

        cam.setToOrtho(false, cloud.musdey.virusgame.config.Config.VISIBLE_WIDTH, cloud.musdey.virusgame.config.Config.VISIBLE_WIDTH*aspectRatio);
        cam.position.set(cloud.musdey.virusgame.config.Config.VISIBLE_HEIGHT*imageRatio/2, cloud.musdey.virusgame.config.Config.VISIBLE_HEIGHT/2,0);

        bgViewport = new FillViewport(cloud.musdey.virusgame.config.Config.VISIBLE_HEIGHT/aspectRatio, cloud.musdey.virusgame.config.Config.VISIBLE_HEIGHT,cam);
        bgViewport.setScreenBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        bgViewport.apply();

        cam.update();

        setupTitle();
        setupButtonSkin();
        setupMenu();
        setupPreferences();

        VirusGame.adsController.showBannerAd();
    }

    private void setupPreferences(){
        prefs = Gdx.app.getPreferences(cloud.musdey.virusgame.config.Config.PREFS_NAME);
        if(!prefs.contains(cloud.musdey.virusgame.config.Config.PREFS_HIGHSCORE)){
            prefs.putInteger(cloud.musdey.virusgame.config.Config.PREFS_HIGHSCORE,0);
            prefs.flush();
        };
        highScore = prefs.getInteger(cloud.musdey.virusgame.config.Config.PREFS_HIGHSCORE);
    }

    private void setupTitle(){
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/orbitron/Orbitron-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        font = generator.generateFont(parameter); // font size 12
        font.setColor(Color.valueOf(cloud.musdey.virusgame.config.Config.COLOR_FONT));
        generator.dispose();
        normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
    }

    private void setupMenu(){

        menuStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(menuStage);

        Table table = new Table();
        menuStage.addActor(table);

        shareBtn = new TextButton("Share with friends",skin);
        shareBtn.setSize(col_width*8,row_height*1.5f);
        backBtn = new TextButton("Main Menu",skin);
        backBtn.setSize(col_width*8,row_height*1.5f);
        exitBtn = new TextButton("Exit",skin);
        exitBtn.setSize(col_width*8,row_height*1.5f);

        table.setPosition(Gdx.graphics.getWidth()/2,row_height*3.0f);
        table.add(shareBtn).width(shareBtn.getWidth()).height(shareBtn.getHeight());
        table.row().pad(5, 0, 5, 0);
        table.add(backBtn).width(backBtn.getWidth()).height(backBtn.getHeight());
        table.row().pad(0, 0, 5, 0);
        table.add(exitBtn).width(exitBtn.getWidth()).height(exitBtn.getHeight());

        shareBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                VirusGame.shareController.shareWithFriends(
                        "I just set up the highscore of "+highScore+". Can you beat that? Go to https://musdey.cloud/thevirusgame and download the game."
                );
            }
        });

        backBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new MenuState(gsm));
            }
        });

        exitBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }
    private void setupButtonSkin(){
        skin = new Skin();

        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixmapTexture = new Texture(pixmap);
        skin.add("white", pixmapTexture);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/orbitron/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter paramButton = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramButton.size = cloud.musdey.virusgame.config.Config.BUTTON_SIZE;
        buttonFont = generator.generateFont(paramButton);
        buttonFont.setColor(Color.valueOf(cloud.musdey.virusgame.config.Config.COLOR_FONT));
        generator.dispose();
        skin.add("default", buttonFont);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.valueOf(cloud.musdey.virusgame.config.Config.COLOR_BLUE_WORLD));
        textButtonStyle.down = skin.newDrawable("white", Color.valueOf(cloud.musdey.virusgame.config.Config.COLOR_BLUE_WORLD2));
        textButtonStyle.checked = skin.newDrawable("white", Color.valueOf(cloud.musdey.virusgame.config.Config.COLOR_BLUE_WORLD2));
        textButtonStyle.over = skin.newDrawable("white", Color.valueOf(Config.COLOR_BLUE_WORLD2));
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
    }

    @Override
    public void handleInput() {}

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(0.97265625f,0.87109375f, 0.515625f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);
        cam.update();
        sb.begin();
        sb.end();

        sb.setProjectionMatrix(normalProjection);
        sb.begin();
        font.draw(sb,"Your highscore:",0,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/4,Gdx.graphics.getWidth(), Align.center,true);
        font.draw(sb,""+highScore,0,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/3,Gdx.graphics.getWidth(), Align.center,true);
        //font.draw(sb,"200",0,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight()/16)*2,Gdx.graphics.getWidth(), Align.center,true);
        sb.end();
        cam.update();
        menuStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        menuStage.draw();
    }

    @Override
    public void dispose() {
        font.dispose();
        menuStage.dispose();
        VirusGame.adsController.hideBannerAd();
    }
}
