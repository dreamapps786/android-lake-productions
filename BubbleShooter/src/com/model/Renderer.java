package com.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.simulation.*;

public class Renderer {

	private static SpriteBatch spriteBatch;
	private final TextureAtlas background;
	private final Texture title;
	private final Texture play;
	private static BitmapFont font;
	private Mesh playMesh;
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();
	private static boolean showButton = true;

	public Renderer(Application app) {
		spriteBatch = new SpriteBatch();
		background = new TextureAtlas();
		for (int i = 1; i <= 8; i++) {
			Texture backgroundPart = new Texture(
					Gdx.files.internal("res/bg/background_portrait_" + i + ".png"));
			backgroundPart
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion r = new TextureRegion();
			r.setRegion(backgroundPart);
			background.addRegion(i + "", r);
		}
		title = new Texture(Gdx.files.internal("res/title.png"));
		title.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		play = new Texture(Gdx.files.internal("res/btn_play.png"));
		play.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont();
		font.setScale(2, 2);
		font.setColor(Color.RED);

		// playAnimation = new Texture(
		// Gdx.files.internal("res/anim_btn_play_white.png"), true);
		// playAnimation.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		// playMesh = new Mesh(true, 6 * 4, 0, new
		// VertexAttribute(Usage.Position,
		// 2, "a_position"), new VertexAttribute(Usage.TextureCoordinates,
		// 2, "a_texCoord"));
		// float[] vertices = new float[16 * 6];
		// int idx = 0;
		// for (int row = 0; row < 6; row++) {
		// vertices[idx++] = 1;
		// vertices[idx++] = 1;
		// vertices[idx++] = 0.60f;
		// vertices[idx++] = 0 + row * 0.16f;
		//
		// vertices[idx++] = -1;
		// vertices[idx++] = 1;
		// vertices[idx++] = 0;
		// vertices[idx++] = 0 + row * 0.16f;
		//
		// vertices[idx++] = -1;
		// vertices[idx++] = -1;
		// vertices[idx++] = 0f;
		// vertices[idx++] = 0.16f + row * 0.16f;
		//
		// vertices[idx++] = 1;
		// vertices[idx++] = -1;
		// vertices[idx++] = 0.60f;
		// vertices[idx++] = 0.16f + row * 0.16f;
		// }
		// playMesh.setVertices(vertices);
	}

	public void render(Application app, Simulation simulation) {
		GL10 gl = app.getGraphics().getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderBackground(gl);
		renderButtons(gl);
		if (!showButton) {
			renderButtonAnimations(gl, simulation.button);
		}
	}

	private void renderBackground(GL10 gl) {
		viewMatrix.setToOrtho2D(0, 0, 480, 800);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);
		// We want to place it in the upper left corner and subtract the
		// whitespace, so it doesn't show
		int i = 0;
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 2; col++) {
				spriteBatch.draw(background.getRegions().get(i).getTexture(), 0+col*256, 544-row*256, 256, 256, 0, 0, 256,256, false, false);
					i++;
			}
		}
//		spriteBatch.draw(background.getRegions().get(i), 0 - 272, 0 - 112, 1024, 1024, 0, 0, 1024,1024, false, false);	
		spriteBatch.end();
	}

	private void renderButtons(GL10 gl) {
		if (showButton) {
			spriteBatch.begin();
			spriteBatch.enableBlending();
			spriteBatch.setBlendFunction(GL10.GL_ONE,
					GL10.GL_ONE_MINUS_SRC_ALPHA);
			// We want to find the horizontal center of the screen
			// draw(element,(target width-picture
			// width)/2,y,width,height,cropX,cropY,prescale res Width,prescale
			// res Height,mirror left, mirror top)
			spriteBatch.draw(play, (480 - play.getWidth()) / 2, 306, 512, 512,
					0, 0, 512, 512, false, false);
			spriteBatch.disableBlending();
			spriteBatch.end();
		}
	}

	public static void renderButtonAnimations(GL10 gl, Sprite animSprite) {
		showButton = false;
		// playAnimation.bind();
		// gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
		// gl.glPushMatrix();
		// gl.glTranslatef(button.position.x, button.position.y,0);
		// System.out.println("ButtonX: "+button.position.x);
		// playMesh.render(GL10.GL_TRIANGLES,(int)((button.aliveTime /
		// Button.ANIMATION_LIVE_TIME) * 15) * 4, 4);
		// gl.glPopMatrix();
		// gl.glDisable(GL10.GL_BLEND);
		// Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.draw(animSprite, animSprite.getX(), animSprite.getY());
		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	public static void draw(String text, int x, int y) {
		spriteBatch.begin();
		spriteBatch.enableBlending();
		font.draw(spriteBatch, text, x, y);
		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	public void dispose() {
		spriteBatch.dispose();
		background.dispose();
		title.dispose();
		play.dispose();
		playMesh.dispose();
	}
}
