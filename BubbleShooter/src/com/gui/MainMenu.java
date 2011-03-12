package com.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class MainMenu implements Frame {
	/** the SpriteBatch used to draw the background, logo and text **/
	private final SpriteBatch spriteBatch;
	private final Texture background;
	private final Texture title;
	private final Texture start;

	/** is done flag **/
	private boolean isDisposable = false;
	/** view & transform matrix **/
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();
	private int c = 0;

	public MainMenu (Application app) {
		spriteBatch = new SpriteBatch();
		background = new Texture(Gdx.files.internal("res/sky.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		title = new Texture(Gdx.files.internal("res/title.png"));
		title.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		start = new Texture(Gdx.files.internal("res/start01.png"));
		start.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	
	@Override public void dispose () {
		spriteBatch.dispose();
		background.dispose();
		title.dispose();
	}


	@Override
	public void update(Application app) {
		if (app.getInput().isTouched()) {
			if (app.getInput().getX() > 375 && app.getInput().getX() < 472 && app.getInput().getY() > 265 && app.getInput().getY() < 310) {
				c++;
				System.out.println("Start: " + c);
			}
		}
	}


	@Override
	public void render(Application app) {
		int centerX = Gdx.graphics.getWidth() / 2;
		int centerY = Gdx.graphics.getHeight() / 2;
		
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		viewMatrix.setToOrtho2D(0, 0, 480, 320);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);		
		spriteBatch.draw(background, 0, 0, 480, 320, 0, 0, 512, 512, false, false);
		spriteBatch.enableBlending();
		spriteBatch.draw(title, 120, 215, 250, 128, 0, 0, 256, 256, false, false);
		spriteBatch.draw(start, 375, 100, 250, 128, 0, 0, 320, 320, false, false);
		spriteBatch.setBlendFunction(GL10.GL_ONE,GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.end();
	}



	@Override
	public boolean isDisposable() {
		return isDisposable;
	}
}
