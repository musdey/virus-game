package cloud.musdey.corona;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cloud.musdey.corona.states.GameStateManager;
import cloud.musdey.corona.states.MenuState;

public class CoronaGame extends ApplicationAdapter {

    public static final int WIDTH = 450;
    public static final int HEIGHT = 800;
    public static final String TITLE = "Corona Game";

    private GameStateManager gsm;
    private SpriteBatch batch;

    private Music music;

    @Override
    public void create() {
        Gdx.gl.glClearColor(1,0,0,1);
        gsm = new GameStateManager();
        batch = new SpriteBatch();
        music = Gdx.audio.newMusic(Gdx.files.internal("bensound-summer.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        //music.play();
        gsm.push(new MenuState(gsm));
    }

    public void render() {
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }

    @Override
    public void dispose() {
        super.dispose();
        music.dispose();
    }
}
