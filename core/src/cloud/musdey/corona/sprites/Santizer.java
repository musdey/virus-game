package cloud.musdey.corona.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Santizer {

    private Rectangle bounds;
    private static final int MOVEMENT = 100;
    private Vector3 position, velocity;
    private Texture texture;
    private Sound sound;
    private Random rand;

    public Santizer(){
        rand = new Random();
        float currentY = generateYPos();
        float currentX = generateXPos();
        position = new Vector3(currentX,currentY,0);
        velocity = new Vector3(0,0,0);
        texture = new Texture("santizer_crop.png");
        bounds = new Rectangle(currentX,currentY,1,1.5f);
        sound = Gdx.audio.newSound(Gdx.files.internal("spray.wav"));
    }

    public Texture getTexture(){
        return  texture;
    }

    private float generateXPos(){
        return (18+ rand.nextFloat() * (36 - 18));
    }

    private float generateYPos(){
        return (1+ rand.nextFloat() * (15 - 1));
    }

    public Vector3 getPosition() {
        return position;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public boolean collides(Rectangle player){
        if(player.overlaps(bounds)){
            sound.play(0.8f);
            return true;
        }else{
            return false;
        }
    }
    public void reposition(float x) {
        float currentX = generateXPos();
        float currentY = generateYPos();
        position.set(currentX+x,currentY,0);
        bounds.setPosition(currentX+x,currentY);
    }

    public void dispose(){
    texture.dispose();
    sound.dispose();
    }
}
