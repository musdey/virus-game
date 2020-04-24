package cloud.musdey.corona.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class PlayerNew {

    private Rectangle bounds;
    private static final int GRAVITY = -1;
    private static final float MOVEMENT = 2.5f;
    private Vector3 position;
    private Vector3 velocity;
    private Texture texture;
    private Animation playerAnimation;
    private Sound sound;

    public PlayerNew(int x, int y){
        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);
        texture = new Texture("handanimation.png");
        playerAnimation = new Animation(new TextureRegion(texture),4,0.5f);
        bounds = new Rectangle(x+0.5f,y+0.5f,0.5f,0.5f);
        sound = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));
    }

    public void update(float dt){
        playerAnimation.update(dt);
        if(position.y > 0){
            velocity.add(0, GRAVITY, 0);
        }
        velocity.scl(dt);
        position.add(MOVEMENT*dt,velocity.y,0);

        if(position.y <0){
            position.y = 0;
        }

        velocity.scl(1/dt);
        bounds.setPosition(position.x+0.5f,position.y+0.5f);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getPlayer() {
        return playerAnimation.getFrame();
    }

    public void jump(){
        velocity.y = 16;
        sound.play(0.3f);
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void dispose(){
        texture.dispose();
        sound.dispose();
    }
}
