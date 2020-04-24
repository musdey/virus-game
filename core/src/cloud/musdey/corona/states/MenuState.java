package cloud.musdey.corona.states;

import com.badlogic.gdx.Gdx;
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

import static cloud.musdey.corona.states.ExamplePlayState.VISIBLE_HEIGHT;
import static cloud.musdey.corona.states.ExamplePlayState.VISIBLE_WIDTH;

public class MenuState extends State {

    private Texture background;
    private TextButton gameBtn,highscoreBtn,preferenceBtn,exitBtn;
    private Stage menuStage,textStage;
    private Skin skin;
    private Viewport bgViewport;

    BitmapFont font;
    float imageRatio,realImageWidth;
    FreeTypeFontGenerator generator;
    private  int row_height,col_width;

    private Matrix4 normalProjection;

    public MenuState(GameStateManager gsm){
        super(gsm);

        row_height = Gdx.graphics.getWidth() / 14;
        col_width = Gdx.graphics.getWidth() / 14;

        float aspectRatio =(float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth(); // 1,77

        background = new Texture("virus_original.png");
        imageRatio = (float)background.getWidth()/(float)background.getHeight();
//        realImageWidth = VISIBLE_HEIGHT * imageRatio;

        cam.setToOrtho(false,VISIBLE_WIDTH,VISIBLE_WIDTH*aspectRatio);
        cam.position.set(VISIBLE_HEIGHT*imageRatio/2, VISIBLE_HEIGHT/2,0);

        bgViewport = new FillViewport(VISIBLE_HEIGHT/aspectRatio,VISIBLE_HEIGHT,cam);
        bgViewport.setScreenBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        bgViewport.apply();

        cam.update();

        skin = new Skin(Gdx.files.internal("skins/flat-earth/skin/flat-earth-ui.json"));

        setupTitle();
        setupMenu();
    }

    private void setupTitle(){

        textStage = new Stage(new ScreenViewport());
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/flightcorps/flightcorps3d.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        font = generator.generateFont(parameter); // font size 12
        generator.dispose();
        normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());

    }

    private void setupMenu(){

        menuStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(menuStage);

        Table table = new Table();
        //table.setFillParent(true);
        //table.setDebug(true);
        menuStage.addActor(table);

        gameBtn = new TextButton("Start Game",skin);
        gameBtn.setSize(col_width*6,row_height*1.5f);
        gameBtn.getLabel().setFontScale(1.0f);
        highscoreBtn = new TextButton("Highscore",skin);
        highscoreBtn.setSize(col_width*6,row_height*1.5f);
        highscoreBtn.getLabel().setFontScale(1.0f);
        preferenceBtn = new TextButton("Settings",skin);
        preferenceBtn.setSize(col_width*6,row_height*1.5f);
        preferenceBtn.getLabel().setFontScale(1.0f);
        exitBtn = new TextButton("Exit",skin);
        exitBtn.setSize(col_width*6,row_height*1.5f);
        exitBtn.getLabel().setFontScale(1.0f);

        table.setPosition(Gdx.graphics.getWidth()/2,row_height*3.0f);
        table.add(gameBtn).width(gameBtn.getWidth()).height(gameBtn.getHeight());
        table.row().pad(5, 0, 5, 0);
        table.add(highscoreBtn).width(highscoreBtn.getWidth()).height(highscoreBtn.getHeight());
        table.row().pad(0, 0, 5, 0);
        table.add(exitBtn).width(exitBtn.getWidth()).height(exitBtn.getHeight());

        gameBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new ExamplePlayState(gsm));
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
        sb.draw(background, 0,0, VISIBLE_HEIGHT*imageRatio, VISIBLE_HEIGHT);
        sb.end();

        sb.setProjectionMatrix(normalProjection);
        sb.begin();
        font.draw(sb,"The Virus Game",0,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/16,Gdx.graphics.getWidth(), Align.center,true);
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
