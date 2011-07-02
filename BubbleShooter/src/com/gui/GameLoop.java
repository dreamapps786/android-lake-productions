package com.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.model.GameLoopRenderer;
import com.simulation.AnimatedSprite;
import com.simulation.MainInputProcessor;
import com.simulation.Simulation;

public class GameLoop implements Frame {
	private Simulation simulation;
	private GameLoopRenderer renderer;
	private boolean isDisposable = false;
	@SuppressWarnings("unused")
	private MainInputProcessor inputProcessor;
	private ArrayList<AnimatedSprite> sprites;


	public GameLoop(Application app){
		initialize();
	}

	@Override
	public void initialize() {
		this.sprites = new ArrayList<AnimatedSprite>();
		simulation = new Simulation(this);
		renderer = new GameLoopRenderer(this);
		inputProcessor = new MainInputProcessor(this);	
	}	
	
	@Override
	public void update(Application app) {
		simulation.update(app.getGraphics().getDeltaTime());
	}

	@Override
	public void render(Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render(app, simulation);
		GameLoopRenderer.draw("FPS: "+Gdx.graphics.getFramesPerSecond(), 150, 150, Color.RED);
	}
	
	@Override
	public void populate() {
		renderer.populate();
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
		sprites.add(sprite);
	}

	@Override
	public void removeSprite(AnimatedSprite sprite) {
		sprites.remove(sprite);
	}

	@Override
	public void setDisposable(boolean disposable) {
		// TODO Auto-generated method stub
		
	}
}