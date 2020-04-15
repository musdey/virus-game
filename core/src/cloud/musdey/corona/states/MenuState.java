package cloud.musdey.corona.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import cloud.musdey.corona.CoronaGame;


public class MenuState extends State {

    private Texture background;
    private Texture playBtn;
    private TextButton gameBtn,highscoreBtn,preferenceBtn,exitBtn;
    private Stage stage;
    private Skin skin;
    private FreeTypeFontGenerator generator;

    public MenuState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, CoronaGame.WIDTH, CoronaGame.HEIGHT);
        background = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");

        skin = new Skin(Gdx.files.internal("gdx-skins-master/comic/skin/comic-ui.json"));
        setupMenu();
    }

    private void setupMenu(){

        int row_height = Gdx.graphics.getWidth() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        gameBtn = new TextButton("Start Game",skin);
        gameBtn.setSize(col_width*8,row_height*2);
        gameBtn.getLabel().setFontScale(2f);
        highscoreBtn = new TextButton("Highscore",skin);
        highscoreBtn.setSize(col_width*8,row_height*2);
        highscoreBtn.getLabel().setFontScale(2f);
        preferenceBtn = new TextButton("Settings",skin);
        preferenceBtn.setSize(col_width*8,row_height*2);
        preferenceBtn.getLabel().setFontScale(2f);
        exitBtn = new TextButton("Exit",skin);
        exitBtn.setSize(col_width*8,row_height*2);
        exitBtn.getLabel().setFontScale(2f);

        table.setPosition(0,0);
        table.add(gameBtn).width(gameBtn.getWidth()).height(gameBtn.getHeight());
        table.row().pad(10, 0, 10, 0);
        table.add(highscoreBtn).width(highscoreBtn.getWidth()).height(highscoreBtn.getHeight());
        table.row();
        table.add(exitBtn).width(exitBtn.getWidth()).height(exitBtn.getHeight());


        gameBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new PlayState(gsm));
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
        sb.begin();
        sb.draw(background,0,0, CoronaGame.WIDTH,CoronaGame.HEIGHT);
        //sb.draw(playBtn, cam.position.x - playBtn.getWidth()/2,cam.position.y);
        sb.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        playBtn.dispose();
    }
}
