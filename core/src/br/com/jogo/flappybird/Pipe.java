package br.com.jogo.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Pipe {

    private Texture img;

    private Rectangle up;
    private Rectangle down;

    public Pipe(){
        img = new Texture("cano.png");

        float y = MathUtils.random(300, 1400);

        up = new Rectangle(1250, y + 200, 200, 2300);
        down = new Rectangle(1250, y - 200 - 2300, 200, 2300);

    }

    public int update(float delta){
        float SPEED = -500;
        up.x += delta * SPEED;
        down.x += delta * SPEED;

        if (up.x <= -200){
            return -1;
        }

        return 0;
    }

    public void draw(SpriteBatch batch){
        batch.draw(img, up.x, up.y, up.width, up.height, 0, 0, img.getWidth(), img.getHeight(), false, true);
        batch.draw(img, down.x, down.y, down.width, down.height, 0,0, img.getWidth(), img.getHeight(), false, false);
    }

}
