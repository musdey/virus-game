package cloud.musdey.corona.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import cloud.musdey.corona.config.Config;
import cloud.musdey.corona.sprites.Hands;
import cloud.musdey.corona.sprites.Santizer;
import cloud.musdey.corona.sprites.Virus;

public class GameState extends State {

    private enum State {
        Running,Paused
    }

    private State state = State.Running;

    // Game
    private int points, iconSize;
    private ShapeRenderer sr;
    private Array<Virus> virusArray;
    private Hands player;
    private Texture backGround,playButton,pauseButton,currentPauseTexture;
    private Vector2 bgPos1,bgPos2;
    private Viewport gamePort;
    private Santizer sanitizer;
    private Preferences prefs;
    private Sound cardiB;

    // Text
    private SpriteBatch hudBatch;
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private Matrix4 normalProjection;

    public GameState(StateManager gsm){
        super(gsm);

        float aspectRatio =(float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth(); // 1,77

        cam.setToOrtho(false,Config.VISIBLE_WIDTH,Config.VISIBLE_WIDTH*aspectRatio);
        cam.position.set(Config.GAME_WORLD_WIDTH/2,Config.GAME_WORLD_HEIGHT/2,0);

        gamePort = new FillViewport(Config.VISIBLE_HEIGHT/aspectRatio,Config.VISIBLE_HEIGHT,cam);
        gamePort.setScreenBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        gamePort.apply();

        player = new Hands(18,9);
        sanitizer = new Santizer();
        points = 0;

        playButton = new Texture(Gdx.files.internal("play_button.png"));
        pauseButton = new Texture(Gdx.files.internal("pause_button.png"));
        currentPauseTexture = pauseButton;
        iconSize = Gdx.graphics.getWidth()/8;

        backGround = new Texture(Gdx.files.internal("city.jpg"));
        bgPos1 = new Vector2(cam.position.x - cam.viewportWidth/2,0);
        bgPos2 = new Vector2((cam.position.x - cam.viewportWidth/2)+Config.GAME_WORLD_WIDTH,0);

        virusArray = new Array<Virus>();
        for(int i = 1; i <= Config.VIRUS_COUNT;i++){
            virusArray.add(new Virus(18+(Config.VIRUS_SPACING*i) + Virus.VIRUS_SIZE));
        }
        cam.update();

        cardiB = Gdx.audio.newSound(Gdx.files.internal("cardi-b.mp3"));

        prefs = Gdx.app.getPreferences(Config.PREFS_NAME);

        sr = new ShapeRenderer();
        setupHUD();
    }

    private void setupHUD(){

        hudBatch = new SpriteBatch();
        hudBatch.setColor(Color.BLACK);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/orbitron/Orbitron-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Config.SCORE_SIZE;
        font = generator.generateFont(parameter); // font size 12
        font.setColor(Color.BLACK);
        generator.dispose();
        normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            if(isPauseClicked()){
                if(state == State.Running){
                    currentPauseTexture = pauseButton;
                    state = State.Paused;
                }else{
                    currentPauseTexture = playButton;
                    state = State.Running;
                }
            }else{
                player.jump();
            }
        }
    }

    private boolean isPauseClicked(){
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();

        if(x > Gdx.graphics.getWidth()-iconSize && y < iconSize){
            System.out.println("pause pressed !!!");
            return true;
        }
        return false;
    }

    boolean counted = false;

    @Override
    public void update(float dt) {
        handleInput();

        if(state == State.Running) {
            updateBackGround();
            cam.position.x = player.getPosition().x + 2.5f;
            player.update(dt);

            for (int i = 0; i < virusArray.size; i++) {
                Virus virus = virusArray.get(i);

                if (cam.position.x - cam.viewportWidth / 2 > virus.getX() + 2) {
                    virus.reposition(virus.getX() + Config.VIRUS_SPACING * 4);
                    counted = false;
                }
                if (player.getPosition().x > virus.getX()) {
                    if (!counted) {
                        points++;
                        counted = true;
                    }
                }
                if (virus.collides(player.getBounds())) {
                    endGame();
                }
            }
            if(sanitizer.collides(player.getBounds())){
                points = points+ 5;
                sanitizer.reposition(cam.position.x);
            }
            if(sanitizer.getPosition().x +4 < player.getPosition().x ){
                sanitizer.reposition(cam.position.x);
            }

            if (player.getPosition().y <= 0 || player.getPosition().y >= 16 - 0.9f) {
                endGame();
            }
            cam.update();
        }else{
            // pause
        }
    }

    private void endGame(){
        if(!prefs.contains(Config.PREFS_HIGHSCORE)) {
            prefs.putInteger(Config.PREFS_HIGHSCORE, points);
        }else{
            int val = prefs.getInteger(Config.PREFS_HIGHSCORE);
            if(points > val){
                prefs.putInteger(Config.PREFS_HIGHSCORE,points);
            }
        };
        prefs.putInteger(Config.PREFS_CURRENTSCORE,points);
        prefs.flush();
        cardiB.play(0.5f);
        gsm.set(new GameMenuState(gsm));
    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(0,0,0,01);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);
        cam.update();

        sb.begin();
        sb.draw(backGround,bgPos1.x,0,Config.GAME_WORLD_WIDTH,Config.GAME_WORLD_HEIGHT);
        sb.draw(backGround,bgPos2.x,0,Config.GAME_WORLD_WIDTH,Config.GAME_WORLD_HEIGHT);
        for(Virus virus : virusArray){
            for(Vector2 vector2 : virus.getVirusPositions()){
                sb.draw(virus.getVirusTexture(),vector2.x,vector2.y,2,2);
            }
        }

        sb.draw(sanitizer.getTexture(), sanitizer.getPosition().x, sanitizer.getPosition().y,1,1.5f);
        sb.draw(player.getPlayer(),player.getPosition().x,player.getPosition().y,1.5f,1.5f);

        sb.end();

//        sr.setProjectionMatrix(cam.combined);
//        sr.begin(ShapeRenderer.ShapeType.Line);
//        sr.setColor(new Color(0,0,1,0));
//        sr.rect(player.getBounds().x,player.getBounds().y,player.getBounds().width,player.getBounds().height);
//        sr.rect(sanitizer.getBounds().x,sanitizer.getBounds().y,sanitizer.getBounds().width,sanitizer.getBounds().height);
//        for(Virus virus : virusArray){
//                sr.rect(virus.getBoundsTop().x,virus.getBoundsTop().y,virus.getBoundsTop().width,virus.getBoundsTop().height);
//                sr.rect(virus.getBoundsBottom().x,virus.getBoundsBottom().y,virus.getBoundsBottom().width,virus.getBoundsBottom().height);
//        }
//        sr.end();

        //Secondly draw the Hud
        hudBatch.setProjectionMatrix(normalProjection);
        hudBatch.begin();
        hudBatch.draw(currentPauseTexture,Gdx.graphics.getWidth()-iconSize,Gdx.graphics.getHeight()-iconSize,iconSize,iconSize);
        font.draw(hudBatch,"Score: "+points,0,Gdx.graphics.getHeight()-iconSize,Gdx.graphics.getWidth(), Align.center,true);
        hudBatch.end();
    }

    @Override
    public void dispose() {
        backGround.dispose();
        sanitizer.dispose();
        currentPauseTexture.dispose();
        pauseButton.dispose();
        playButton.dispose();
        player.dispose();
        font.dispose();
        hudBatch.dispose();
    }

    private void updateBackGround(){
        if(cam.position.x - (cam.viewportWidth/2)>bgPos1.x+Config.GAME_WORLD_WIDTH){
            bgPos1.add(Config.GAME_WORLD_WIDTH*2,0);
        }
        if(cam.position.x - (cam.viewportWidth/2)>bgPos2.x+Config.GAME_WORLD_WIDTH){
            bgPos2.add(Config.GAME_WORLD_WIDTH*2,0);
        }
    }
}
