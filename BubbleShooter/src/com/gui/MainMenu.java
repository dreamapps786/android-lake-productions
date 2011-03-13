package com.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class MainMenu implements Frame {
	/** the SpriteBatch used to draw the background, logo and text **/
	private final SpriteBatch spriteBatch;
	private final Texture background;
	private final Texture title;
	private final Texture play;
	private final BitmapFont font;
	private Camera camera;

	/** is done flag **/
	private boolean isDisposable = false;
	/** view & transform matrix **/
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();

	public MainMenu(Application app) {
		spriteBatch = new SpriteBatch();
		background = new Texture(
				Gdx.files.internal("res/background_portrait.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		title = new Texture(Gdx.files.internal("res/title.png"));
		title.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		play = new Texture(Gdx.files.internal("res/btn_play.png"));
		play.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		font = new BitmapFont();
		font.setColor(Color.RED);
		
		float graphicsHeight = (float) Gdx.graphics.getHeight();
		float graphicsWidth = (float) Gdx.graphics.getWidth();
		float graphicsRatio = graphicsHeight/graphicsWidth;
		float theHeight = 32*graphicsRatio;
		if (graphicsRatio < 1.5f){
			theHeight = 48;
		}
		camera = new OrthographicCamera(32, theHeight);
		camera.translate(32 / 2, theHeight / 2, 0);
		camera.update();
		spriteBatch.getProjectionMatrix().set(camera.projection);
		spriteBatch.getTransformMatrix().set(camera.view);
	}

	@Override
	public void dispose()  {
		spriteBatch.dispose();
		background.dispose();
		title.dispose();
		play.dispose();
	}

	@Override
	public void update(Application app) {
		if (app.getInput().isTouched()) {
			System.out.println(app.getInput().getX());
			System.out.println(app.getInput().getY());
			if (app.getInput().getX() > 375 && app.getInput().getX() < 472
					&& app.getInput().getY() > 265
					&& app.getInput().getY() < 310) {

			}
		}
	}

	@Override
	public void render(Application app) {
		int centerX = Gdx.graphics.getWidth() / 2;
		int centerY = Gdx.graphics.getHeight() / 2;
		 app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT |
		 GL10.GL_DEPTH_BUFFER_BIT);
		app.getGraphics().getGL10().glViewport(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
//		spriteBatch.setProjectionMatrix(viewMatrix);
//		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
//		spriteBatch.setColor(Color.BLACK);
		spriteBatch.draw(background, centerX-background.getWidth()/2,
				centerY-background.getHeight()/2,0, 0, 1024, 1024);
		spriteBatch.enableBlending();
		spriteBatch.draw(title, 120, 256, 256, 256, 0, 0, 256, 256, false,
				false);
		spriteBatch
				.draw(play, centerX-play.getWidth()/2, 300, 0, 0, play.getWidth(), play.getHeight());
		font.draw(spriteBatch, "W: " + Gdx.graphics.getWidth() + " H: "
				+ Gdx.graphics.getHeight(), 200, 300);
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.end();
	}

	public void resize(int width, int height) {
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	@Override
	public boolean isDisposable() {
		return isDisposable;
	}
}
