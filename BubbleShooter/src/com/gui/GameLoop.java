package com.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.model.GameLoopRenderer;
import com.simulation.AnimatedSprite;
import com.simulation.MainInputProcessor;
import com.simulation.Simulation;

public class GameLoop implements Frame {
	private Simulation simulation;
	private GameLoopRenderer renderer;
	private static SpriteBatch spriteBatch;
	private boolean isDisposable = false;
	@SuppressWarnings("unused")
	private MainInputProcessor inputProcessor;
	private AnimatedSprite shooter;
	private AnimatedSprite activeBubble;
	private ArrayList<AnimatedSprite> bubbles;
	private int shooterRotation = 0;

	public GameLoop(Application app) {
		initialize();
	}

	@Override
	public void initialize() {
		spriteBatch = new SpriteBatch();
		simulation = new Simulation(this);
		renderer = new GameLoopRenderer(this);
		inputProcessor = new MainInputProcessor(this);
		bubbles = new ArrayList<AnimatedSprite>();
	}

	@Override
	public void update(Application app) {
		simulation.update(app.getGraphics().getDeltaTime());
	}

	@Override
	public void render(Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render(app, simulation);
		GameLoopRenderer.drawText("FPS: " + Gdx.graphics.getFramesPerSecond(),
				150, 150, Color.RED);
		if (activeBubble.isActive()) {
			animateActiveBubble(shooterRotation);
		}
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

	public void setShooter(AnimatedSprite shooter) {
		this.shooter = shooter;
	}

	public void setActiveBubble(AnimatedSprite activeBubble) {
		this.activeBubble = activeBubble;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		activeBubble.setActive(true);
		// if (!activeBubble.isActive()) {
		int cY = -y + 800; // y converted from input- to
							// animatedSprite-coordinates
		if (cY > shooter.getOriginY()) {
			int newRotAngle = (int) -Math.toDegrees(Math.atan((x - (shooter
					.getOriginX() + shooter.getX()))
					/ (cY - (shooter.getOriginY() + shooter.getY()))));
			if (newRotAngle > -60 && newRotAngle < 60) {
				shooter.rotate(-shooterRotation + newRotAngle);
				shooterRotation = newRotAngle;
				shootBubble(newRotAngle);
			}
		}
		// }
		return true;
	}

	public void shootBubble(double angle) {
		int dist = 40;
		double bC = Math.toDegrees(Math.sin(90));
		double a = -(dist * Math.toDegrees(Math.sin(Math.toRadians(angle))))
				/ bC;
		double b = (dist * Math.toDegrees(Math.sin(Math
				.toRadians(180 - angle - 90)))) / bC;
		int x = (int) (a + shooter.getOriginX() + shooter.getX());
		int y = (int) (b + shooter.getOriginY() + shooter.getY());
		System.out.println("a: " + a + " b: " + b);
		System.out.println("x: " + x + " y: " + y);
		activeBubble.setPosition(x - activeBubble.getOriginX(), y
				- activeBubble.getOriginY());
		// activeBubble.setActive(true);
		animateActiveBubble(angle);
	}

	private void animateActiveBubble(double angle) {
		// while (activeBubble.isActive()) {
		double speed = 0.1;
		double bC = Math.toDegrees(Math.sin(90));
		double a = -(speed * Math.toDegrees(Math.sin(Math.toRadians(angle))))
				/ bC;
		double b = (speed * Math.toDegrees(Math.sin(Math
				.toRadians(180 - angle - 90)))) / bC;
		// boolean changedDirection = false;
		// while (!changedDirection) {
		activeBubble.setPosition(activeBubble.getX() + (float) a,
				activeBubble.getY() + (float) b);
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch
				.draw(activeBubble, activeBubble.getX(), activeBubble.getY());
		spriteBatch.disableBlending();
		spriteBatch.end();
		System.out.println("ActiveBubleX: " + activeBubble.getX());
		if (activeBubble.getX() < 0
				|| activeBubble.getX() > 480 - activeBubble.getWidth()) {
			activeBubble.setActive(false);
			// changedDirection = true;
		}
		// }
	}

	@Override
	public void addSprite(AnimatedSprite sprite) {
		// sprites.add(sprite);
	}

	@Override
	public void removeSprite(AnimatedSprite sprite) {
		// sprites.remove(sprite);
	}

	@Override
	public void setDisposable(boolean disposable) {
		// TODO Auto-generated method stub
	}
}
