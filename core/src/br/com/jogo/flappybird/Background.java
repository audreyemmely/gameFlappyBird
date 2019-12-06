package br.com.jogo.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {

    private Texture img;

    private float x1, x2;

    public Background(){
        img = new Texture("fundo.png");

        x1 = 0;
        x2 = 1150;
    }

    public void update(float delta){
        float SPEED = -400;

        x1 += SPEED * delta;
        x2 += SPEED * delta;

        if(x1 <= -1150){
            x1 = 0;
            x2 = 1150;
        }
    }

    public void draw(SpriteBatch batch){
        batch.draw(img, x1, 0, 1150, 2300);
        batch.draw(img, x2, 0, 1150, 2300);
    }

    public void dispose(){
        img.dispose();
    }
}