package cloud.musdey.corona.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import cloud.musdey.corona.CoronaGame;
import cloud.musdey.corona.sprites.Player;
import cloud.musdey.corona.sprites.Tube;

public class PlayState extends State {

    private static final float ASPECT_RATIO =
            (float)CoronaGame.WIDTH/(float)CoronaGame.HEIGHT;

    private enum GameState{
        Running,Paused
    }
    private int points;

    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    GameState state = GameState.Running;

    private Player player;
    private Texture background,ground;
    private Tube tube;

    private Array<Tube> tubes;
    private Vector2 groundPos1,groundPos2;

    private Hud hud;
    private SpriteBatch spriteBatch;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        player = new Player(50,300);
        tube = new Tube(100);
        cam.setToOrtho(false, CoronaGame.WIDTH/2,CoronaGame.HEIGHT/2);

        background = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth/2,GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth/2)+ground.getWidth(),GROUND_Y_OFFSET);

        tubes = new Array<Tube>();
        for(int i = 1; i <= TUBE_COUNT;i++){
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }

        points = 0;
        setupHUD();
    }

    private void setupHUD(){
        spriteBatch = new SpriteBatch();
        hud = new Hud(spriteBatch);
        Gdx.input.setInputProcessor(hud.getStage());
        hud.playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(state == GameState.Running){
                    state = GameState.Paused;
                }else{
                    state = GameState.Running;
                }
            }
        });
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            // TODO: check if pause was clicked to avoid jumping when pausing
            if(state==GameState.Running){
                player.jump();
            }
        }
    }
    boolean counted = false;
    @Override
    public void update(float dt) {
        if(state == GameState.Running) {
            handleInput();
            updateGround();
            player.update(dt);

            cam.position.x = player.getPosition().x + 80;

            for (int i = 0; i < tubes.size; i++) {
                Tube tube = tubes.get(i);
                if (cam.position.x - cam.viewportWidth / 2 > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                    tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                    counted = false;
                }
                if(player.getPosition().x > tube.getPosTopTube().x){
                    if(!counted){
                        points++;
                        counted = true;
                        hud.setScore(points);
                    }
                }
                if (tube.collides(player.getBounds())) {
                    endGame();
                }
            }
            if (player.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
                endGame();
            }
            cam.update();
        }else{
            // don't update
        }
    }

    private void endGame(){
        gsm.set(new GameMenuState(gsm));
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background,cam.position.x - (cam.viewportWidth/2),0);
        sb.draw(player.getPlayer(),player.getPosition().x,player.getPosition().y);
        for(Tube tube : tubes){
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
        }
        sb.draw(ground,groundPos1.x,groundPos1.y);
        sb.draw(ground,groundPos2.x,groundPos2.y);
        sb.end();

        //Secondly draw the Hud
        spriteBatch.setProjectionMatrix(hud.getStage().getCamera().combined); //set the spriteBatch to draw what our stageViewport sees
        hud.getStage().act(); //act the Hud
        hud.getStage().draw(); //draw the Hud
    }

    @Override
    public void dispose() {
        background.dispose();
        player.dispose();
        ground.dispose();
        hud.dispose();
        for(Tube tube: tubes){
            tube.dispose();
        }
    }

    private void updateGround(){
        if(cam.position.x - (cam.viewportWidth/2)>groundPos1.x+ground.getWidth()){
            groundPos1.add(ground.getWidth()*2,0);
        }
        if(cam.position.x - (cam.viewportWidth/2)>groundPos2.x+ground.getWidth()){
            groundPos2.add(ground.getWidth()*2,0);
        }
    }
}
