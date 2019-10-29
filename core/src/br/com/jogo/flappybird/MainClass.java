package br.com.jogo.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

	@Override
	public void create () {
		viewport = new FitViewport(1150, 2300);
		batch = new SpriteBatch();
		back = new Background();
		felpudo = new Felpudo();
		pipes = new Array<Pipe>();
		pipes.add(new Pipe());
		state = State.WAIT;

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
				//detectar colisão com felpudo

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
	}

	private void reset(){
		state = State.WAIT;
		felpudo = new Felpudo();
		pipes.clear();
		timeToNext = 2.0f;
	}


	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose () {

	}
}