package com.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.helpers.BubbleGrid.BubbleGridRectangle;
import com.helpers.GameLoopRenderer;
import com.helpers.PointService;
import com.simulation.AnimatedSprite;
import com.simulation.MainInputProcessor;
import com.simulation.Simulation;

public class GameLoop implements Frame {
	private Simulation simulation;
	private GameLoopRenderer renderer;
	private boolean isDisposable = false;
	@SuppressWarnings("unused")
	private MainInputProcessor inputProcessor;
	private AnimatedSprite shooter;
	private AnimatedSprite activeBubble;
	private AnimatedSprite bubbleSplash;
	private Rectangle boundsCollisionBox;
	private int shooterRotation = 0;

	// For debugging
	private int lastInputClickX = -1;
	private int lastInputClickY = -1;
	private List<AnimatedSprite> debugPoints;

	public GameLoop(Application app) {
		initialize();
	}

	@Override
	public void initialize() {
		new SpriteBatch();
		simulation = new Simulation(this);
		renderer = new GameLoopRenderer(this);
		inputProcessor = new MainInputProcessor(this);
		debugPoints = new ArrayList<AnimatedSprite>();
		boundsCollisionBox = new Rectangle(0, 0, 460, 800);
	}

	@Override
	public void update(Application app) {
		simulation.update(app.getGraphics().getDeltaTime());
		bubbleSplash.update(app.getGraphics().getDeltaTime());
	}

	@Override
	public void render(Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render(app, simulation);
		GameLoopRenderer.drawText("FPS: " + Gdx.graphics.getFramesPerSecond(),
				150, 150, Color.RED);
		GameLoopRenderer.drawText("Total Score: "+PointService.getTotalPoints(), 300, 100, Color.BLUE);
		GameLoopRenderer.drawText("Click (" + lastInputClickX + "x, "
				+ lastInputClickY + "y)", 95, 300, Color.RED);
		if (activeBubble.isActive()) {
			animateActiveBubble();
		}
		if (bubbleSplash.isActive()) {
			renderer.renderBubbleSplash();
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

	public void setBubbleSplash(AnimatedSprite bubbleSplash) {
		this.bubbleSplash = bubbleSplash;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {

		activeBubble.setActive(true);
		int cY = 800 - y; // y converted from input- to
							// animatedSprite-coordinates
		lastInputClickX = x;
		lastInputClickY = cY;

		if (cY > shooter.getOriginY()) {
			int newRotAngle = (int) -Math.toDegrees(Math.atan((x - (shooter
					.getOriginX() + shooter.getX()))
					/ (cY - (shooter.getOriginY() + shooter.getY()))));
			if (newRotAngle > -60 && newRotAngle < 60) {
				shooter.rotate(newRotAngle - shooterRotation);
				shooterRotation = newRotAngle;
				activeBubble.setDirection(newRotAngle);
				shootBubble();
			}
		}
		return true;
	}

	public void shootBubble() {
		int dist = 40;
		double angle = activeBubble.getDirection();
		double bC = Math.toDegrees(Math.sin(90));
		double a = -(dist * Math.toDegrees(Math.sin(Math.toRadians(angle))))
				/ bC;
		double b = (dist * Math.toDegrees(Math.sin(Math
				.toRadians(180 - angle - 90)))) / bC;
		int x = (int) (a + shooter.getOriginX() + shooter.getX());
		int y = (int) (b + shooter.getOriginY() + shooter.getY());
		activeBubble.setPosition(x - activeBubble.getOriginX(), y
				- activeBubble.getOriginY());
	}

	private void animateActiveBubble() {
		double speed = 8; //default:8
		double angle = activeBubble.getDirection();
		double bC = Math.toDegrees(Math.sin(90));
		double a = -(speed * Math.toDegrees(Math.sin(Math.toRadians(angle))))
				/ bC;
		double b = (speed * Math.toDegrees(Math.sin(Math
				.toRadians(180 - angle - 90)))) / bC;
		activeBubble.setPosition(activeBubble.getX() + (float) a,
				activeBubble.getY() + (float) b);

		if (!boundsCollisionBox.contains(activeBubble.getBoundingRectangle())) {
			changeDirection(activeBubble.getX(), activeBubble.getY());
		}
		BubbleGridRectangle collidingBubble = renderer.checkForCollission(
				activeBubble.getX() + (activeBubble.getWidth() / 2),
				activeBubble.getY() + activeBubble.getHeight() / 2,
				activeBubble.getHeight() / 2);
		if (collidingBubble != null) { // The bubble has to be a square
		// if (renderer.checkForCollission(activeBubble.getBoundingRectangle()))
		// {
			PointService.Score();
			System.out.println("ActiveBubble: "+activeBubble.getX() + "x, "
					+ activeBubble.getY() + "y");
			Gdx.input.vibrate(50);
			activeBubble.setActive(false);
//			bubbleSplash.setPosition(activeBubble.getX(), activeBubble.getY());
//			bubbleSplash.setActive(true);
		}
	}

	public void changeDirection(float x, float y) {
		double angle = activeBubble.getDirection();
		if (x < 0) {
			activeBubble.setDirection(angle - angle * 2);
		}
		if (x > boundsCollisionBox.getWidth() - activeBubble.getWidth() / 3) {
			activeBubble.setDirection(angle + angle * -1 * 2);

		}
	}

	// @Override
	// public void addSprite(AnimatedSprite sprite) {
	// // sprites.add(sprite);
	// }
	//
	// @Override
	// public void removeSprite(AnimatedSprite sprite) {
	// // sprites.remove(sprite);
	// }

	@Override
	public void setDisposable(boolean disposable) {
		// TODO Auto-generated method stub
	}
}
