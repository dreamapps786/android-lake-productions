package com.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.gui.Frame;
import com.gui.MainMenu;


public class BubbleShooter implements ApplicationListener {
	private boolean isInitialized = false;
	
	/** the visible frame **/
	private Frame frame;
	
	@Override
	public void create() {
		if (!isInitialized) {
			frame = new MainMenu(Gdx.app);
			isInitialized = true;
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		Application app = Gdx.app;
		frame.update(app);
		frame.render(app);
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


}
