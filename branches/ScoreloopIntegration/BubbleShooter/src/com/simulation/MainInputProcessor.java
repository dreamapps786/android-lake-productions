package com.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.gui.Frame;
import com.gui.GameLoop;
import com.gui.MainMenu;

public class MainInputProcessor extends InputAdapter {
	private Frame frame;

	public MainInputProcessor(Frame frame) {
		this.frame = frame;
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		frame.touchUp(x, y, pointer, button);
		return false;
	}

	 @Override
	 public boolean keyDown(int keycode) {
		 if (keycode == Keys.BACK) {
			 frame.dispose();
			 if (frame instanceof MainMenu) {
				Gdx.app.exit();
			}
			 else if (frame instanceof GameLoop) {
				frame.setDisposable(true);
				
			}
		 }
		 return false;
	 }
	//
	// @Override
	// public boolean keyTyped(char arg0);
	//
//	 @Override
//	 public boolean keyUp(int arg0) {
//		 
//		 return false;
//	 }

	// @Override
	// public boolean scrolled(int arg0);

	// @Override
	// public boolean touchDown(int arg0, int arg1, int arg2, int arg3);

	// @Override
	// public boolean touchDragged(int arg0, int arg1, int arg2);

	// @Override
	// public boolean touchMoved(int arg0, int arg1);
}
