package com.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.GL10;
import com.simulation.AnimatedSprite;

public interface Frame {
	/**
	 * Called when the screen should update itself, e.g. continue a simulation etc.
	 * 
	 * @param app the Application
	 */
	public void update (Application app);

	/**
	 * Called when a screen should render itself
	 * @param app
	 */
	public void render (Application app);
	
	/**
	 * Called by BubbleShooter to check if the screen is still in use or it can be disposed.
	 * When disposed another frame will be shown instead.
	 * 
	 * @return true when the screen is ready to be disposed, false otherwise
	 */
	public boolean isDisposable ();

	/**
	 * Cleans up all resources of the screen, e.g. meshes, textures etc.
	 */
	public void dispose ();
	
	public boolean touchUp(int x, int y, int pointer, int button);
	
	public void addSprite(AnimatedSprite sprite);
	
	public void removeSprite(AnimatedSprite sprite);
}
