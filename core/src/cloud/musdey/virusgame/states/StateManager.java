package cloud.musdey.virusgame.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class StateManager {

    private Stack<cloud.musdey.virusgame.states.State> states;

    public StateManager(){
        states = new Stack<cloud.musdey.virusgame.states.State>();
    }

    public void push(cloud.musdey.virusgame.states.State state){
        states.push(state);
    }

    public void pop(){
        states.pop().dispose();
    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }
}
