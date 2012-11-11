package com.gui;

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
	private final double speed = 500;
	private final int queueLength = 3;
	private Simulation simulation;
	private GameLoopRenderer renderer;
	private boolean isDisposable = false;
	@SuppressWarnings("unused")
	private MainInputProcessor inputProcessor;
	private AnimatedSprite shooter;
	private Rectangle boundsCollisionBox;
	private int shooterRotation = 0;

	// For debugging
	private int lastInputClickX = -1;
	private int lastInputClickY = -1;
	private float lastShotPosX = 0;
	private float lastShotPosY = 0;

	public GameLoop(Application app) {
		initialize();
	}

	@Override
	public void initialize() {
		new SpriteBatch();
		simulation = new Simulation(this);
		renderer = new GameLoopRenderer(this);
		inputProcessor = new MainInputProcessor(this);
		boundsCollisionBox = new Rectangle(0, 0, 460, 800);
	}

	@Override
	public void update(Application app) {
		simulation.update(app.getGraphics().getDeltaTime());
		for (AnimatedSprite splash : renderer.getSplashesToRender()) {
			splash.update(app.getGraphics().getDeltaTime());
		}
	}

	@Override
	public void render(Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render(app, simulation);
		GameLoopRenderer.drawText("FPS: " + Gdx.graphics.getFramesPerSecond(), 150, 150, Color.RED);
		GameLoopRenderer.drawText("Total Score: " + PointService.getTotalPoints(), 300, 100, Color.BLUE);
		GameLoopRenderer.drawText("Click (" + lastInputClickX + "x, " + lastInputClickY + "y)", 95, 300, Color.RED);
		if (renderer.getActiveBubble().isActive()) {
			animateActiveBubble(app.getGraphics().getDeltaTime());
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

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		renderer.getActiveBubble().setActive(true);
		int cY = 800 - y; // y converted from input- to
							// animatedSprite-coordinates
		lastInputClickX = x;
		lastInputClickY = cY;

		if (cY > shooter.getOriginY()) {
			int newRotAngle = (int) -Math.toDegrees(Math.atan((x - (shooter.getOriginX() + shooter.getX()))
					/ (cY - (shooter.getOriginY() + shooter.getY()))));
			if (newRotAngle > -60 && newRotAngle < 60) {
				shooter.rotate(newRotAngle - shooterRotation);
				shooterRotation = newRotAngle;
				renderer.getActiveBubble().setDirection(newRotAngle);
				shootBubble();
			}
		}
		return true;
	}

	public void shootBubble() {
		int dist = 40;
		double angle = renderer.getActiveBubble().getDirection();
		double bC = Math.toDegrees(Math.sin(90));
		double a = -(dist * Math.toDegrees(Math.sin(Math.toRadians(angle)))) / bC;
		double b = (dist * Math.toDegrees(Math.sin(Math.toRadians(180 - angle - 90)))) / bC;
		int x = (int) (a + shooter.getOriginX() + shooter.getX());
		int y = (int) (b + shooter.getOriginY() + shooter.getY());
		this.lastShotPosX = x - renderer.getActiveBubble().getOriginX();
		this.lastShotPosY = y - renderer.getActiveBubble().getOriginY();
		renderer.getActiveBubble().setPosition(this.lastShotPosX, this.lastShotPosY);
	}
	
	float oldX;
	float oldY;
	float currentX;
	float currentY;

	private void animateActiveBubble(float deltatime) {
		
		double angle = renderer.getActiveBubble().getDirection();
		double bC = Math.toDegrees(Math.sin(90));
		double a = -(deltatime*speed * Math.toDegrees(Math.sin(Math.toRadians(angle)))) / bC;
		double b = (deltatime*speed * Math.toDegrees(Math.sin(Math.toRadians(180 - angle - 90)))) / bC;
		renderer.getActiveBubble().setPosition(renderer.getActiveBubble().getX() + (float) a,
				renderer.getActiveBubble().getY() + (float) b);
		oldX = currentX;
		oldY = currentY;
		currentX = renderer.getActiveBubble().getX() + (float) a;
		currentY = renderer.getActiveBubble().getY() + (float) b;
		if (!boundsCollisionBox.contains(renderer.getActiveBubble().getBoundingRectangle())) {
			changeDirection(renderer.getActiveBubble().getX(), renderer.getActiveBubble().getY());
		}
		BubbleGridRectangle collidingBubble = renderer.handleCollision(renderer.getActiveBubble().getX()
				+ (renderer.getActiveBubble().getWidth() / 2), renderer.getActiveBubble().getY()
				+ renderer.getActiveBubble().getHeight() / 2, renderer.getActiveBubble().getHeight() / 2, renderer
				.getActiveBubble().getDirection());
		
		//NEW COLLISSION CHECK
		BubbleGridRectangle handleCollision1 = renderer.handleCollision1(oldX+(renderer.getActiveBubble().getWidth() / 2), oldY+ renderer.getActiveBubble().getHeight() / 2, currentX
				+ (renderer.getActiveBubble().getWidth() / 2), currentY
				+ renderer.getActiveBubble().getHeight() / 2, renderer
				.getActiveBubble().getDirection());
				
				
		if (collidingBubble != null) { // The bubble has to be a square
			renderer.resetActiveBubble(this.lastShotPosX, this.lastShotPosY);
			Gdx.input.vibrate(50);
		}
	}

	public void changeDirection(float x, float y) {
		double angle = renderer.getActiveBubble().getDirection();
		if (x < 0) {
			renderer.getActiveBubble().setDirection(angle - angle * 2);
		}
		if (x > boundsCollisionBox.getWidth() - renderer.getActiveBubble().getWidth() / 3) {
			renderer.getActiveBubble().setDirection(angle + angle * -1 * 2);

		}
	}
	
	public int getQueueLength() {
		return queueLength;
	}

	@Override
	public void setDisposable(boolean disposable) {
		// TODO Auto-generated method stub
	}
}
