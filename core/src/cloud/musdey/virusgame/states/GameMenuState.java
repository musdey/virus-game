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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import cloud.musdey.virusgame.VirusGame;
import cloud.musdey.virusgame.config.Config;

public class GameMenuState extends State{

    private TextButton gameBtn,highscoreBtn,exitBtn;
    private Texture background,pixmapTexture;
    private Pixmap pixmap;
    private Stage stage;
    private Skin skin;
    private BitmapFont font,buttonFont;
    private FreeTypeFontGenerator generator;
    private Matrix4 normalProjection;
    private Preferences prefs;
    private int points;
    private TextButton.TextButtonStyle textButtonStyle;

    public GameMenuState(StateManager gsm){
        super(gsm);
        cam.setToOrtho(false, cloud.musdey.virusgame.config.Config.VISIBLE_WIDTH, cloud.musdey.virusgame.config.Config.VISIBLE_HEIGHT);
        background = new Texture("bg_plain169.png");

        setupText();
        setupButtonSkin();
        setupMenu();

        prefs = Gdx.app.getPreferences("corona");
        points = prefs.getInteger(cloud.musdey.virusgame.config.Config.PREFS_CURRENTSCORE);
        VirusGame.adsController.showBannerAd();
    }

    private void setupText(){

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/orbitron/Orbitron-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = cloud.musdey.virusgame.config.Config.HIGHSCORE_SIZE;
        font = generator.generateFont(parameter); // font size 12
        font.setColor(Color.valueOf(cloud.musdey.virusgame.config.Config.COLOR_FONT));
        generator.dispose();
        normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());

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

    private void setupMenu(){

        int row_height = Gdx.graphics.getWidth() / 14;
        int col_width = Gdx.graphics.getWidth() / 14;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        //table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);

        gameBtn = new TextButton("Try Again",skin);
        gameBtn.setSize(col_width*8,row_height*1.5f);
        highscoreBtn = new TextButton("Highscore",skin);
        highscoreBtn.setSize(col_width*8,row_height*1.5f);
        exitBtn = new TextButton("Main Menu",skin);
        exitBtn.setSize(col_width*8,row_height*1.5f);

        table.setPosition(Gdx.graphics.getWidth()/2,row_height*3.0f);
        table.add(gameBtn).width(gameBtn.getWidth()).height(gameBtn.getHeight());
        table.row().pad(5, 0, 5, 0);
        table.add(highscoreBtn).width(highscoreBtn.getWidth()).height(highscoreBtn.getHeight());
        table.row().pad(0, 0, 5, 0);
        table.add(exitBtn).width(exitBtn.getWidth()).height(exitBtn.getHeight());


        gameBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(VirusGame.GAMECOUNTER == 10){
                    VirusGame.adsController.showInterstitialAd();
                    VirusGame.GAMECOUNTER = 0;
                }else{
                    VirusGame.GAMECOUNTER++;
                    gsm.set(new GameState(gsm));
                }
            }
        });

        highscoreBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new HighscoreState(gsm));
            }
        });

        exitBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new MenuState(gsm));
            }
        });
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(0.97265625f,0.87109375f, 0.515625f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        //sb.draw(background,0,0, ExamplePlayState.VISIBLE_WIDTH,ExamplePlayState.VISIBLE_HEIGHT);
        //sb.draw(playBtn, cam.position.x - playBtn.getWidth()/2,cam.position.y);
        sb.end();


        sb.setProjectionMatrix(normalProjection);
        sb.begin();
        font.draw(sb,"Your score:",0,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/4,Gdx.graphics.getWidth(), Align.center,true);
        font.draw(sb,""+points,0,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/3,Gdx.graphics.getWidth(), Align.center,true);
        sb.end();
        cam.update();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
        stage.dispose();

        VirusGame.adsController.hideBannerAd();
        VirusGame.adsController.hideInterstitialAd();
    }
}