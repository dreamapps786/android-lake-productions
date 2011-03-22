package com.simulation;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.model.Renderer;

public class MainInputProcessor extends InputAdapter {
	private ArrayList<AnimatedSprite> sprites;

	public MainInputProcessor() {
		super();
		Gdx.input.setInputProcessor(this);
		this.sprites = new ArrayList<AnimatedSprite>();
	}

	public void addSprite(AnimatedSprite sprite) {
		this.sprites.add(sprite);
	}

	public void removeSprite(AnimatedSprite sprite) {
		this.sprites.remove(sprite);
	}

	// @Override
	// public boolean keyDown(int arg0);
	//
	// @Override
	// public boolean keyTyped(char arg0);
	//
	// @Override
	// public boolean keyUp(int arg0);

	// @Override
	// public boolean scrolled(int arg0);

	// @Override
	// public boolean touchDown(int arg0, int arg1, int arg2, int arg3);

	// @Override
	// public boolean touchDragged(int arg0, int arg1, int arg2);

	// @Override
	// public boolean touchMoved(int arg0, int arg1);

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		boolean touched = false;
		int i = 0;
		while (!touched && i < sprites.size()) {
			AnimatedSprite s = sprites.get(i);
			float buttonX = Gdx.graphics.getWidth() * s.getXrel2Screen();
			float buttonY = Gdx.graphics.getHeight() * s.getYrel2Screen();
			float buttonW = (Gdx.graphics.getWidth() / 480f) * s.getWidth();
			float buttonH = (Gdx.graphics.getHeight() / 800f) * s.getHeight();

			if (x > buttonX && x < buttonX + buttonW && y > buttonY
					&& y < buttonY + buttonH) {
				s.isTouched();
				Renderer.renderButtonAnimations(Gdx.gl10, s);
			}
			i++;
		}

		return false;
	}
}
