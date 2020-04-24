package cloud.musdey.corona.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Virus {

    public static final int VIRUS_SIZE = 2;
    public static final int VIRUS_COUNT = 8;
    public static final int HOLE_SIZE = 8;
    private Texture virus;
    private Array<Vector2> virusArray;
    private Rectangle boundsTop,boundsBottom;
    private Random rand;

    public Virus(float x){
      virus = new Texture("virus_mad.png");
      virusArray = new Array<Vector2>();
      rand = new Random();
      int holeBottom  = 2+rand.nextInt((8-2)/2) *2;

      for(int i=0 ; i<VIRUS_COUNT*2; i= i+VIRUS_SIZE){
          if(i==holeBottom) {
              boundsBottom = new Rectangle(x,0,VIRUS_SIZE,holeBottom);
          }else if(i == holeBottom+VIRUS_SIZE){
              //boundsTop = new Rectangle(x,holeBottom+(VIRUS_SIZE),VIRUS_SIZE,16-(holeBottom+3));
          }else if(i == holeBottom+(VIRUS_SIZE*2)){
              virusArray.add(new Vector2(x,i));
              boundsTop = new Rectangle(x,holeBottom+(VIRUS_SIZE*2),VIRUS_SIZE,16-(holeBottom+(VIRUS_SIZE*2)));
          }else{
              virusArray.add(new Vector2(x,i));
          }
      }
    }

    public Rectangle getBoundsTop(){
        return boundsTop;
    }

    public Rectangle getBoundsBottom(){
        return boundsBottom;
    }

    public boolean collides(Rectangle player){
        return player.overlaps(boundsTop)|| player.overlaps(boundsBottom);
    }

    public Array<Vector2> getVirusPositions(){
        return virusArray;
    }

    public void reposition(float x){
        virusArray.clear();
        int holeBottom  = 2+rand.nextInt((8-2)/2) *2;
        for(int i=0 ; i<VIRUS_COUNT*2; i= i+VIRUS_SIZE){
            if(i==holeBottom) {
                boundsBottom = new Rectangle(x,0,VIRUS_SIZE,holeBottom);
            }else if(i == holeBottom+VIRUS_SIZE){
                //boundsTop = new Rectangle(x,holeBottom+(VIRUS_SIZE),VIRUS_SIZE,16-(holeBottom+3));
            }else if(i == holeBottom+(VIRUS_SIZE*2)){
                virusArray.add(new Vector2(x,i));
                boundsTop = new Rectangle(x,holeBottom+(VIRUS_SIZE*2),VIRUS_SIZE,16-(holeBottom+(VIRUS_SIZE*2)));
            }else{
                virusArray.add(new Vector2(x,i));
            }
        }
    }

    public float getX(){
        return virusArray.get(0).x;
    }

    public Texture getVirusTexture(){
        return virus;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void dispose(){
        virus.dispose();
    }

}
