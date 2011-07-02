package com.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.gui.Frame;

public class Simulation {
	private MainInputProcessor inputProcessor;
	public AnimatedSprite button;
	private Texture playAnimation;
	private Frame frame;

	public Simulation(Frame frame) {
		this.frame = frame;
		inputProcessor = new MainInputProcessor(frame);
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
		frame.addSprite(button);
	}

	public void pushPlay(float delta) {
		button.update(delta);
	}
}
