package com.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Simulation {
	public transient SimulationListener listener;
	public Button button;
	public AnimatedSprite button1;
	public AnimatedSprite button2;
	private Texture playAnimation;

	public Simulation() {
		populate();
	}

	public void update(float delta) {
		pushPlay(delta);
	}

	private void populate() {
		// button = new Button(new Vector2(-16,306));
		playAnimation = new Texture(
				Gdx.files.internal("res/anim_btn_play_white.png"), true);
		playAnimation.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		// button1 = new AnimatedSprite(playAnimation, 0, 0, 305, 82, 6, 1);
		// button1.setPosition((480-305)/2, 522);
		// TextureAtlas atlas;
		// atlas = new TextureAtlas();
		// atlas.addRegion("1", playAnimation, 0, 0, 300, 85);
		// atlas.addRegion("2", playAnimation, 0, 85, 300, 85);
		// atlas.addRegion("3", playAnimation, 0, 170, 300, 85);
		// atlas.addRegion("4", playAnimation, 0, 255, 300, 85);
		// atlas.addRegion("5", playAnimation, 0, 340, 300, 85);
		// atlas.addRegion("6", playAnimation, 0, 425, 300, 85);
		button2 = new AnimatedSprite(playAnimation, 0, 0, 305, 82, 6, 1);
		button2.setPosition((480 - 305) / 2, 522);
	}

	public void pushPlay(float delta) {
		button2.update(delta);
//		 if (button != null) {
//		 button.update(delta);
//		 if (button.aliveTime > Button.ANIMATION_LIVE_TIME) {
//		 button = null;
//		 }
//		 }
	}
}
