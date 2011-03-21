package com.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.model.Renderer;
import com.simulation.AnimatedSprite;
import com.simulation.Simulation;
import com.simulation.SimulationListener;

public class MainMenu implements Frame, SimulationListener {
	private final Simulation simulation;
	private final Renderer renderer;
	private boolean isDisposable = false;

	public MainMenu(Application app) {
		simulation = new Simulation();
		simulation.listener = this;
		renderer = new Renderer(app);
		
	}

	@Override
	public void dispose() {
		renderer.dispose();
	}

	@Override
	public void update(Application app) {
		simulation.update(app.getGraphics().getDeltaTime());
		if (app.getInput().isTouched()) {
			float inputX = app.getInput().getX();
			float inputY = app.getInput().getY();
			float buttonX = simulation.button.getX();
			float buttonY = simulation.button.getY();
			float buttonW = simulation.button.getWidth();
			float buttonH = simulation.button.getHeight();
			
			System.out.println("bX: " + buttonX + " bY: " + (buttonY-(buttonY-(app.getGraphics().getHeight()/2))*2) + "x: "
					+ inputX + " y: " + inputY);
			
			if (inputX > buttonX && inputX < buttonX + buttonW
					&& inputY < buttonY-(buttonY-(app.getGraphics().getHeight()/2))*2 && inputY > buttonY-(buttonY-(app.getGraphics().getHeight()/2))*2 - buttonH) {
				
				simulation.button.play();
				renderer.renderButtonAnimations(Gdx.gl10, simulation.button);
			}
			// if (app.getInput().getX() > ((480-512)/2)+103 &&
			// app.getInput().getX() < ((480-512)/2)+103+306
			// && app.getInput().getY() > 200
			// && app.getInput().getY() < 282) {
			// }
		}
	}

	@Override
	public void render(Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render(app, simulation);
		
		float inputX = app.getInput().getX();
		float inputY = app.getInput().getY();
		float buttonX = ((app.getGraphics().getWidth()-512)/2)+103;
		float buttonY = ((app.getGraphics().getWidth()-512)/2)+103+306;
		float buttonW = simulation.button.getWidth();
		float buttonH = simulation.button.getHeight();
		renderer.draw("bX: " + buttonX + " bY: " + (buttonY-(buttonY-(app.getGraphics().getHeight()/2))*2) + "x: "
				+ inputX + " y: " + inputY);
	}

	@Override
	public boolean isDisposable() {
		return isDisposable;
	}

	@Override
	public void playPushed() {
		// TODO Play sound
	}
}
