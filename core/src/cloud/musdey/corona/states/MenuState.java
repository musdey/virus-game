package cloud.musdey.corona.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

import cloud.musdey.corona.config.Config;

public class MenuState extends State {

    private Texture background,pixmapTexture;
    private TextButton gameBtn,highscoreBtn,preferenceBtn,exitBtn;
    private Stage menuStage,textStage;
    private Skin skin;
    private Viewport bgViewport;
    private Pixmap pixmap;

    private BitmapFont font,buttonFont;
    float imageRatio;
    private FreeTypeFontGenerator generator;
    private int row_height,col_width;

    private Matrix4 normalProjection;

    private TextButton.TextButtonStyle textButtonStyle;

    public MenuState(StateManager gsm){
        super(gsm);

        row_height = Gdx.graphics.getWidth() / 14;
        col_width = Gdx.graphics.getWidth() / 14;

        float aspectRatio =(float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth(); // 1,77

        background = new Texture("virus_original.png");
        imageRatio = (float)background.getWidth()/(float)background.getHeight();

        cam.setToOrtho(false, Config.VISIBLE_WIDTH,Config.VISIBLE_WIDTH*aspectRatio);
        cam.position.set(Config.VISIBLE_HEIGHT*imageRatio/2, Config.VISIBLE_HEIGHT/2,0);

        bgViewport = new FillViewport(Config.VISIBLE_HEIGHT/aspectRatio,Config.VISIBLE_HEIGHT,cam);
        bgViewport.setScreenBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        bgViewport.apply();

        cam.update();

        setupText();
        setupButtonSkin();
        setupMenu();
    }

    private void setupText(){

        textStage = new Stage(new ScreenViewport());
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/orbitron/Orbitron-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Config.TITLE_SIZE;
        font = generator.generateFont(parameter);
        font.setColor(Color.valueOf(Config.COLOR_FONT));

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
        paramButton.size = Config.BUTTON_SIZE;
        buttonFont = generator.generateFont(paramButton);
        buttonFont.setColor(Color.valueOf(Config.COLOR_FONT));
        generator.dispose();
        skin.add("default", buttonFont);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.valueOf(Config.COLOR_BLUE_WORLD));
        textButtonStyle.down = skin.newDrawable("white", Color.valueOf(Config.COLOR_BLUE_WORLD2));
        textButtonStyle.checked = skin.newDrawable("white", Color.valueOf(Config.COLOR_BLUE_WORLD2));
        textButtonStyle.over = skin.newDrawable("white", Color.valueOf(Config.COLOR_BLUE_WORLD2));
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
    }

    private void setupMenu(){

        menuStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(menuStage);

        Table table = new Table();
        //table.setFillParent(true);
        //table.setDebug(true);
        menuStage.addActor(table);

        gameBtn = new TextButton("Start Game",skin);
        gameBtn.setSize(col_width*8,row_height*1.5f);
        highscoreBtn = new TextButton("Highscore",skin);
        highscoreBtn.setSize(col_width*8,row_height*1.5f);
//        preferenceBtn = new TextButton("Settings",skin);
//        preferenceBtn.setSize(col_width*6,row_height*1.5f);
        exitBtn = new TextButton("Exit",skin);
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
                gsm.set(new GameState(gsm));
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
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void handleInput() {}

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);

        cam.update();

        sb.begin();
        sb.draw(background, 0,0, Config.VISIBLE_HEIGHT*imageRatio, Config.VISIBLE_HEIGHT);
        sb.end();

        sb.setProjectionMatrix(normalProjection);
        sb.begin();
        font.draw(sb,Config.TITLE,0,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/16,Gdx.graphics.getWidth(), Align.center,true);
        sb.end();

        cam.update();

        menuStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        menuStage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        menuStage.dispose();
    }
}
