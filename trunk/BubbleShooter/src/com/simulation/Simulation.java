package com.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Simulation {
	private MainInputProcessor inputProcessor;
	public AnimatedSprite button, button1, button2, button3, button4, button5;
	private Texture playAnimation;

	public Simulation() {
		inputProcessor = new MainInputProcessor();
		populate();
	}

	public void update(float delta) {
		pushPlay(delta);
	}

	private void populate() {
		playAnimation = new Texture(
				Gdx.files.internal("res/anim_btn_play_none.png"), true);
		playAnimation.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		button = new AnimatedSprite("play",playAnimation, 0, 0, 305, 82, 6, 1,
				0.18125f, 0.24625f);
		button.setPosition((480 - 305) / 2, 522);
		inputProcessor.addSprite(button);
	}

	public void pushPlay(float delta) {
		button.update(delta);
		button1.update(delta);
		button2.update(delta);
		button3.update(delta);
		button4.update(delta);
		button5.update(delta);
	}
}
