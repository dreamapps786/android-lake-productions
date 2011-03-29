package com.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.model.Renderer;
import com.simulation.Simulation;

public class MainMenu implements Frame {
	private final Simulation simulation;
	private final Renderer renderer;
	private boolean isDisposable = false;
	private static MainMenu mainMenu;

	private MainMenu() {
		simulation = new Simulation();
		renderer = new Renderer(Gdx.app);
		
	}
	
	public static MainMenu getInstance(){
		if (mainMenu == null) {
			mainMenu = new MainMenu();
		}
		return mainMenu;
	}

	@Override
	public void dispose() {
		renderer.dispose();
	}

	@Override
	public void update(Application app) {
		simulation.update(app.getGraphics().getDeltaTime());
	}

	@Override
	public void render(Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render(app, simulation);
		Renderer.draw("FPS: "+Gdx.graphics.getFramesPerSecond(), 150, 150);
	}

	@Override
	public boolean isDisposable() {
		return isDisposable;
	}
	
	public void setDisposable(boolean disposable){
		this.isDisposable = disposable;
	}
}
