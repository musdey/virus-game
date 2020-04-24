package cloud.musdey.corona.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.JViewport;

import cloud.musdey.corona.CoronaGame;

public class Hud {

    private Stage stage;
    private FillViewport stageViewport;
    private Texture playIcon,pauseIcon;
    public ImageButton playButton,pauseButton;

    BitmapFont font;
    FreeTypeFontGenerator generator;

    private int score;
    private Matrix4 normalProjection;

    public Hud(SpriteBatch spriteBatch, Viewport viewport) {
        stage = new Stage(viewport, spriteBatch);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/flightcorps/flightcorpsi.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter); // font size 12
        generator.dispose();
        normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());

        score = 0;

        playIcon = new Texture("play_button.png");
        pauseIcon = new Texture("pause_button.png");
        Drawable pauseDrawable = new TextureRegionDrawable(new TextureRegion(pauseIcon));
        Drawable playDrawable = new TextureRegionDrawable(new TextureRegion(playIcon));

        playButton = new ImageButton(playDrawable);
        pauseButton = new ImageButton(pauseDrawable);



        Table table = new Table();
        table.columnDefaults(2);
        table.bottom().left();
        table.setFillParent(true);
        //table.setDebug(true);
        table.add(pauseButton);
        stage.addActor(table);

    }

    public void setScore(int score){
        this.score = score;
    }

    public Matrix4 getProjection(){
        return normalProjection;
    }
    public Stage getStage() { return stage; }

    public void dispose(){
        stage.dispose();
    }
}
