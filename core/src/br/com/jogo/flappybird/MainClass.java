package br.com.jogo.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainClass extends ApplicationAdapter {

	private Viewport viewport;

	private SpriteBatch batch;

	private Background back;

	private Felpudo felpudo;

	private Array<Pipe> pipes;
	private float timeToNext = 2.0f;

	private enum State {WAIT, PLAY, DIE, FINISH};
	private State state;
	private float timeToFinish;

	private int score = 0;
	private boolean counting = false;

	private BitmapFont font150;
	private BitmapFont font250;
	private GlyphLayout glyphLayout;


	@Override
	public void create () {
		viewport = new FitViewport(1150, 2300); //largura da tela
		batch = new SpriteBatch();
		back = new Background();
		felpudo = new Felpudo();
		pipes = new Array<Pipe>();
		pipes.add(new Pipe());
		state = State.WAIT;
		FreeTypeFontGenerator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.color = Color.WHITE;
		parameter.size = 150;
		font150 = generator.generateFont(parameter);

		parameter.size = 250;
		font250 = generator.generateFont(parameter);

		generator.dispose();

		glyphLayout = new GlyphLayout();
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		draw(batch);
		batch.end();
	}

	private void update(float delta){

		switch (state){
			case WAIT:
				felpudo.update(delta);

				back.update(delta);

				if (Gdx.input.justTouched()){
					state = State.PLAY;
					felpudo.fly();
				}
				break;
			case PLAY:
				if (felpudo.update(delta) == -1){
					state = State.DIE;
					timeToFinish = 0.5f;
				}
				back.update(delta);

				timeToNext -= delta;
				if (timeToNext <= 0){
					pipes.add(new Pipe());
					timeToNext = 2.0f;
				}

				for (int i = 0; i < pipes.size; i++){
					Pipe p = pipes.get(i);
					if (p.update(delta) == -1){
						pipes.removeIndex(i);
						i--;
					}
				}

				boolean aux = false;

				for (Pipe p:pipes){
					if (Intersector.overlaps(felpudo.body, p.up) || Intersector.overlaps(felpudo.body, p.down)){
						state = State.DIE;
						timeToFinish = 2f;
						felpudo.die();
					}

					if (Intersector.overlaps(felpudo.body, p.score)){
                        aux = true;
                        if (!counting){
                            score++;
                            counting = true;
                        }
                    }
				}

				if (!aux){
				    counting = false;
				}

				if (Gdx.input.justTouched()) felpudo.fly();
				break;
			case DIE:
				felpudo.update(delta);
				timeToFinish -= delta;
				if (timeToFinish <= 0){
					state = State.FINISH;
				}
				break;
			case FINISH:
				if (Gdx.input.justTouched()) reset();
		}

	}

	private void draw(SpriteBatch batch){
		back.draw(batch);
		for (Pipe p:pipes){
			p.draw(batch);
		}
		felpudo.draw(batch);

		switch (state){
			case WAIT:
				font150.draw(batch, "Toque!", (1750 - getTamX(font150, "Toque na tela!"))/2, 1600);
				break;
			case PLAY:
			case DIE:
				font150.draw(batch, String.valueOf(score), (1100 - getTamX(font150, String.valueOf(score)))/2, 2000);
				break;
			case FINISH:
				font150.draw(batch, "Pontos:", (1100 - getTamX(font150, "Pontos:"))/2, 1500);
				font250.draw(batch, String.valueOf(score), (1100 - getTamX(font250, String.valueOf(score)))/2, 1200);
				break;
		}
	}

	private void reset(){
		state = State.WAIT;
		felpudo = new Felpudo();
		pipes.clear();
		timeToNext = 2.0f;
		score = 0;
		counting = false;
	}

	private float getTamX(BitmapFont font, String text){
		glyphLayout.reset();
		glyphLayout.setText(font, text);
		return  glyphLayout.width;
	}


	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose () {

	}
}