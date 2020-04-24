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

public class HighscoreState extends State{

    private TextButton gameBtn,highscoreBtn,exitBtn;
    private Texture background;
    private Texture playBtn;
    private Stage stage;
    private Skin skin;


    public HighscoreState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, CoronaGame.WIDTH/2,CoronaGame.HEIGHT/2);
        background = new Texture("bg_plain169.png");
        playBtn = new Texture("playbtn.png");

        skin = new Skin(Gdx.files.internal("skins/comic/skin/comic-ui.json"));
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


        exitBtn = new TextButton("Main Menu",skin);
        exitBtn.setSize(col_width*8,row_height*2);
        exitBtn.getLabel().setFontScale(2f);

        table.setPosition(0,0);
        table.row().pad(10, 0, 10, 0);
        table.add(exitBtn).width(exitBtn.getWidth()).height(exitBtn.getHeight());


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
