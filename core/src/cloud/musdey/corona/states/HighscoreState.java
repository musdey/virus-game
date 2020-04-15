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

import java.awt.Menu;

import cloud.musdey.corona.CoronaGame;

public class HighscoreState extends State{

    private TextButton gameBtn,highscoreBtn,exitBtn;
    private Texture background;
    private Texture playBtn;
    private Stage stage;
    private Skin skin;


    public HighscoreState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, CoronaGame.WIDTH/2,CoronaGame.HEIGHT/2);
        background = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");

        skin = new Skin(Gdx.files.internal("gdx-skins-master/comic/skin/comic-ui.json"));
        setupMenu();
    }

    private void setupMenu(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        //table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);


        exitBtn = new TextButton("Main menu",skin,"default");

        table.setPosition(CoronaGame.WIDTH/2,CoronaGame.HEIGHT/4);
        table.add(gameBtn).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(highscoreBtn).fillX().uniformX();
        table.row();
        table.add(exitBtn).fillX().uniformX();


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
        sb.draw(background,0,0, CoronaGame.WIDTH,CoronaGame.HEIGHT);
        sb.draw(playBtn, cam.position.x - playBtn.getWidth()/2,cam.position.y);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        playBtn.dispose();
    }
}
