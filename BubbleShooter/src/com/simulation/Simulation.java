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
		button = new AnimatedSprite(playAnimation, 0, 0, 305, 82, 6, 1,
				0.18125f, 0.24625f);
		button.setPosition((480 - 305) / 2, 522);

		inputProcessor.addSprite(button);
		
			//FIXME EXTRA BUTTONS
		button1 = new AnimatedSprite(playAnimation, 0, 0, 305, 82, 6, 1,
				0.18125f, 0.37125f);
		button1.setPosition((480 - 305) / 2, 422);
		inputProcessor.addSprite(button1);
		button2 = new AnimatedSprite(playAnimation, 0, 0, 305, 82, 6, 1,
				0.18125f, 0.49625f);
		button2.setPosition((480 - 305) / 2, 322);
		inputProcessor.addSprite(button2);
		button3 = new AnimatedSprite(playAnimation, 0, 0, 305, 82, 6, 1,
				0.18125f, 0.62125f);
		button3.setPosition((480 - 305) / 2, 222);
		inputProcessor.addSprite(button3);
		button4 = new AnimatedSprite(playAnimation, 0, 0, 305, 82, 6, 1,
				0.18125f, 0.74625f);
		button4.setPosition((480 - 305) / 2, 122);
		inputProcessor.addSprite(button4);
		button5 = new AnimatedSprite(playAnimation, 0, 0, 305, 82, 6, 1,
				0.18125f, 0.87125f);
		button5.setPosition((480 - 305) / 2, 22);
		inputProcessor.addSprite(button5);
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
