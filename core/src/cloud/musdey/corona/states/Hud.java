package cloud.musdey.corona.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import cloud.musdey.corona.CoronaGame;

public class Hud {

    private Stage stage;
    private FitViewport stageViewport;
    private Texture pauseIcon;
    public ImageButton playButton;
    private Label scoreLabel;
    private Skin skin;

    public Hud(SpriteBatch spriteBatch) {
        stageViewport = new FitViewport(CoronaGame.WIDTH,CoronaGame.HEIGHT);
        stage = new Stage(stageViewport, spriteBatch); //create stage with the stageViewport and the SpriteBatch given in Constructor
        skin = new Skin(Gdx.files.internal("gdx-skins-master/comic/skin/comic-ui.json"));

        scoreLabel = new Label("Score: 0",skin);
        scoreLabel.setFontScale(1.5f);
        pauseIcon = new Texture("playbtn.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(pauseIcon));
        playButton = new ImageButton(drawable);

        Table table = new Table();
        table.columnDefaults(2);
        table.bottom().left();
        table.setFillParent(true);
        //table.setDebug(true);
        table.add(scoreLabel).padLeft(10).width(CoronaGame.WIDTH-playButton.getWidth()-10);
        table.add(playButton);
        stage.addActor(table);
    }

    public void setScore(int score){
        scoreLabel.setText("Score: "+score);
    }

    public Stage getStage() { return stage; }

    public void dispose(){
        stage.dispose();
    }
}
