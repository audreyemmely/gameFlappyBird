package br.com.jogo.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Felpudo {

    private Texture[] frames;

    private int curFrame;
    private float timeToNext;

    private Circle body;

    private float acel;
    private float speed;

    public Felpudo(){
        frames = new Texture[6];
        for (int i=0; i<=5; i++){
            frames[i] = new Texture("felpudo/felpudoVoa" + (i+1) + ".png");
        }
        curFrame = 0;
        timeToNext = 0.15f;

        body = new Circle(200, 2300/2, 60);

        acel = 0;
        speed = 0;
    }

    public void draw(SpriteBatch batch){
        batch.draw(frames[curFrame], body.x - 60, body.y - 60, 120, 120);
    }

    public int update(float delta){
        timeToNext -= delta;
        if (timeToNext <= 0){
            curFrame += 1;
            if (curFrame >= 6){
                curFrame = 0;
            }
            timeToNext = 0.15f;
        }
        speed += delta * acel;

        body.y += delta * speed;

        if (body.y <= -100 || body.y >= 2400) return -1;

        return 0;
    }

    public void  fly(){
        speed = 1000;
        acel = -2000;

    }
}
