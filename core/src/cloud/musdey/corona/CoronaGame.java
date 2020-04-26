package cloud.musdey.corona;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cloud.musdey.corona.config.Config;
import cloud.musdey.corona.mobile.AdsController;
import cloud.musdey.corona.mobile.ShareController;
import cloud.musdey.corona.states.StateManager;
import cloud.musdey.corona.states.MenuState;

public class CoronaGame extends ApplicationAdapter {

    public static final String TITLE = Config.TITLE;
    public static int GAMECOUNTER;

    private StateManager gsm;
    private SpriteBatch batch;

    private Music music;
    public static AdsController adsController;
    public static ShareController shareController;

    public CoronaGame(AdsController adsController, ShareController shareController){
        this.shareController = shareController;
        this.adsController = adsController;
    }

    @Override
    public void create() {
        //Gdx.gl.glClearColor(1,0,0,1);
        gsm = new StateManager();
        batch = new SpriteBatch();
        //music = Gdx.audio.newMusic(Gdx.files.internal("bensound-summer.mp3"));
        //music.setLooping(true);
        //music.setVolume(0.5f);
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
        //music.dispose();
        batch.dispose();
    }
}
