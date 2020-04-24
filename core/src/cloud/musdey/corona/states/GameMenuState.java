package cloud.musdey.corona.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import cloud.musdey.corona.CoronaGame;

public class GameMenuState extends State{

    private TextButton gameBtn,highscoreBtn,exitBtn;
    private Texture background;
    private Texture playBtn;
    private Stage stage;
    private Skin skin;

    public GameMenuState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false,ExamplePlayState.VISIBLE_WIDTH,ExamplePlayState.VISIBLE_HEIGHT);
        background = new Texture("bg_plain169.png");
        playBtn = new Texture("playbtn.png");

        skin = new Skin(Gdx.files.internal("skins/comic/skin/comic-ui.json"));
        setupMenu();
        CoronaGame.adsController.showBannerAd();
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
        gameBtn.getLabel().setFontScale(1.5f);
        highscoreBtn = new TextButton("Highscore",skin);
        highscoreBtn.setSize(col_width*8,row_height*1.5f);
        highscoreBtn.getLabel().setFontScale(1.5f);
        exitBtn = new TextButton("Main Menu",skin);
        exitBtn.setSize(col_width*8,row_height*1.5f);
        exitBtn.getLabel().setFontScale(1.5f);

        table.setPosition(Gdx.graphics.getWidth()/2,row_height*3.0f);
        table.add(gameBtn).width(gameBtn.getWidth()).height(gameBtn.getHeight());
        table.row().pad(5, 0, 5, 0);
        table.add(highscoreBtn).width(highscoreBtn.getWidth()).height(highscoreBtn.getHeight());
        table.row().pad(0, 0, 5, 0);
        table.add(exitBtn).width(exitBtn.getWidth()).height(exitBtn.getHeight());


        gameBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(CoronaGame.GAMECOUNTER == 10){
                    CoronaGame.adsController.showInterstitialAd();
                    CoronaGame.GAMECOUNTER = 0;
                }else{
                    CoronaGame.GAMECOUNTER++;
                    gsm.set(new ExamplePlayState(gsm));
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
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background,0,0, ExamplePlayState.VISIBLE_WIDTH,ExamplePlayState.VISIBLE_HEIGHT);
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
        CoronaGame.adsController.hideBannerAd();
        CoronaGame.adsController.hideInterstitialAd();
    }
}
