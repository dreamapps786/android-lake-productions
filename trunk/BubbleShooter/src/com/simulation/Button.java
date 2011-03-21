package com.simulation;

import com.badlogic.gdx.math.Vector2;

public class Button {
	public float aliveTime = 0;
	public final Vector2 position = new Vector2();
	public static final float ANIMATION_LIVE_TIME = 1;
	
	public Button (Vector2 position) {
		this.position.set(position);
	}

	public void update (float delta) {
		aliveTime += delta;
	}
}
