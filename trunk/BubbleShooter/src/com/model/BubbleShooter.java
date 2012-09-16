package com.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gui.Frame;
import com.gui.GameLoop;
import com.gui.MainMenu;

public class BubbleShooter implements ApplicationListener {
	private boolean isInitialized = false;
	private SpriteBatch spriteBatch;

	/** the visible frame **/
	private Frame frame;
	
	@Override
	public void create() {
		if (!isInitialized) {
			spriteBatch = new SpriteBatch();
			frame = MainMenu.getInstance();
			frame.initialize();
			isInitialized = true;
			Gdx.input.setCatchBackKey(true);
		}
	}

	@Override
	public void dispose() {
		frame.dispose();
	}

	@Override
	public void pause() {
//		frame.setDisposable(true);

	}

	@Override
	public void render() {
		Application app = Gdx.app;
		frame.update(app);
		frame.render(app);
		
		if (frame.isDisposable()) {
			frame.setDisposable(false);
			frame.dispose();
			
			if (frame instanceof MainMenu) {
				//If the screen is currently showing MainMenu switch to GameLoop
				frame = new GameLoop(app);
			}
			else if (frame instanceof GameLoop) {
				//Switch to GameOver
				
				frame = MainMenu.getInstance();
			}
			
		}
	}

	@Override
	public void resize(int width, int height) {
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	@Override
	public void resume() {
//		spriteBatch = new SpriteBatch();
		
//		frame = MainMenu.getInstance();
//		frame.setDisposable(false);
//		frame.initialize();
	}

}
