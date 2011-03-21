package com.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Simulation {
	public transient SimulationListener listener;
	public AnimatedSprite button;
	private Texture playAnimation;

	public Simulation() {
		populate();
	}

	public void update(float delta) {
		pushPlay(delta);
	}

	private void populate() {
		playAnimation = new Texture(
				Gdx.files.internal("res/anim_btn_play_none.png"), true);
		playAnimation.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		button = new AnimatedSprite(playAnimation, 0, 0, 305, 82, 6, 1);
		button.setPosition((480 - 305) / 2, 522);
	}

	public void pushPlay(float delta) {
		button.update(delta);
	}
}
