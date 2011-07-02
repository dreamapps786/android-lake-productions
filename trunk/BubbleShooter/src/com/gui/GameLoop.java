package com.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.GL10;
import com.model.GameLoopRenderer;
import com.simulation.AnimatedSprite;
import com.simulation.Simulation;

public class GameLoop implements Frame {
	private final Simulation simulation;
	private final GameLoopRenderer renderer;
	private boolean isDisposable = false;


	public GameLoop(Application app){
		simulation = new Simulation(this);
		renderer = new GameLoopRenderer(app);
	}
	
	@Override
	public void update(Application app) {
		simulation.update(app.getGraphics().getDeltaTime());
	}

	@Override
	public void render(Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render(app, simulation);
//		Renderer.draw("FPS: "+Gdx.graphics.getFramesPerSecond(), 150, 150);
	}

	@Override
	public boolean isDisposable() {
		return isDisposable;
	}

	@Override
	public void dispose() {
		renderer.dispose();
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addSprite(AnimatedSprite sprite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSprite(AnimatedSprite sprite) {
		// TODO Auto-generated method stub
		
	}

}
