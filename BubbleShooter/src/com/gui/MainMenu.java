package com.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.simulation.AnimatedSprite;
import com.simulation.MainInputProcessor;

public class MainMenu implements Frame {
	private boolean isDisposable = false;
	private static MainMenu mainMenu;
	private static SpriteBatch spriteBatch;
	private TextureAtlas background;
	private Texture title;
	private Texture play;
	private Texture exit;
	private static BitmapFont font;
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();
	@SuppressWarnings("unused")
	private MainInputProcessor inputProcessor;
	public AnimatedSprite playButton;
	public AnimatedSprite exitButton;
	private Texture playAnimation;
	private Texture exitAnimation;

	private MainMenu() {
		spriteBatch = new SpriteBatch();
		// initialize();
	}

	@Override
	public void initialize() {
		background = new TextureAtlas();
		for (int i = 1; i <= 8; i++) {
			Texture backgroundPart = new Texture(
					Gdx.files.internal("res/bg/background_portrait_" + i
							+ ".png"));
			backgroundPart
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion r = new TextureRegion();
			r.setRegion(backgroundPart);
			background.addRegion(i + "", r);
		}
		title = new Texture(Gdx.files.internal("res/title.png"), true);
		title.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		play = new Texture(Gdx.files.internal("res/btn_play.png"), true);
		play.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		exit = new Texture(Gdx.files.internal("res/btn_exit.png"), true);
		exit.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		font = new BitmapFont();
		font.setScale(2, 2);
		font.setColor(Color.RED);
		inputProcessor = new MainInputProcessor(this);
		populate();
	}

	public static MainMenu getInstance() {
//		if (mainMenu == null) {
			mainMenu = new MainMenu();
//		}
		return mainMenu;
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		background.dispose();
		title.dispose();
		play.dispose();
		exit.dispose();
		font.dispose();
		playAnimation.dispose();
		exitAnimation.dispose();
	}

	@Override
	public void update(Application app) {
		playButton.update(app.getGraphics().getDeltaTime());
		exitButton.update(app.getGraphics().getDeltaTime());
	}

	@Override
	public void render(Application app) {
		// GL10 gl = app.getGraphics().getGL10(); // Skal kaldes hvis GL skal
		// bruges igen
		// gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderBackground();

		if (!playButton.isActive()) {
			renderButtonAnimation(playButton);
		} else if (!exitButton.isActive()) {
			renderButtonAnimation(exitButton);
		} else {
			renderButtons();
		}
	}

	private void renderBackground() {
		viewMatrix.setToOrtho2D(0, 0, 480, 800);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);
		int i = 0;
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 2; col++) {
				spriteBatch.draw(background.getRegions().get(i).getTexture(),
						0 + col * 256, (800 - 256) - row * 256, 256, 256, 0, 0,
						256, 256, false, false);
				i++;
			}
		}
		spriteBatch.end();
	}

	private void renderButtons() {
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		// We want to find the horizontal center of the screen
		// draw(element,(target width-picture
		// width)/2,y,width,height,cropX,cropY,prescale res Width,prescale
		// res Height,mirror left, mirror top)

		if (exitButton.isActive()) {
			spriteBatch.draw(exit, (480 - exit.getWidth()) / 2, 100);
		}
		if (playButton.isActive()) {
			spriteBatch.draw(play, (480 - play.getWidth()) / 2, 306);
		}
		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	public void renderButtonAnimation(AnimatedSprite animSprite) {
		animSprite.setActive(false);
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.draw(animSprite, animSprite.getX(), animSprite.getY());
		spriteBatch.disableBlending();
		spriteBatch.end();
		if (!animSprite.isPlay()) {
			if (animSprite.getName().equals("play")) {
				setDisposable(true);
				playButton.setActive(true);
			}
			if (animSprite.getName().equals("exit")) {
				exitButton.setActive(true);
				this.dispose();
				Gdx.app.exit();
			}
		}
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (animatedSpriteIsTouched(this.playButton, x, y)) {
			this.playButton.isTouched();
			renderButtonAnimation(this.playButton);
		}
		if (animatedSpriteIsTouched(this.exitButton, x, y)) {
			System.out.println("Touch Exit");
			this.exitButton.isTouched();
			renderButtonAnimation(this.exitButton);

		}
		return false;
	}

	/**
	 * Bruges til at finde ud af om en AnimatedSprite (nok mest en knap) er
	 * trykket på.
	 * 
	 * @param target
	 *            - The <code>AnimatedSprite</code> that may be touched
	 * @param touchedX
	 *            - The x-coordinate from the touchUp()-method.
	 * @param touchedY
	 *            - The y-coordinate from the touchUp()-method.
	 * @return <code>true</code> if the target is touched.
	 */
	public boolean animatedSpriteIsTouched(AnimatedSprite target, int touchedX,
			int touchedY) {
		float spriteX = Gdx.graphics.getWidth() * target.getXrel2Screen();
		float spriteY = Gdx.graphics.getHeight() * target.getYrel2Screen();
		float spriteW = (Gdx.graphics.getWidth() / 480f) * target.getWidth();
		float spriteH = (Gdx.graphics.getHeight() / 800f) * target.getHeight();
		if (touchedX > spriteX && touchedX < spriteX + spriteW
				&& touchedY > spriteY && touchedY < spriteY + spriteH) {
			return true;
		}
		return false;
	}

	@Override
	public void populate() {
		playAnimation = new Texture(
				Gdx.files.internal("res/anim_btn_play_none.png"), true);
		playAnimation.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		exitAnimation = new Texture(
				Gdx.files.internal("res/anim_btn_play_none2.png"), true);
		exitAnimation.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		float buttonWidth = 305f;
		float buttonHeight = 82f;
		float playX = (480 - buttonWidth) / 2;
		float playY = 522;
		float exitY = 316;
		float xRel2Screen = (Gdx.graphics.getWidth() - buttonWidth - playX)
				/ Gdx.graphics.getWidth();

		playButton = new AnimatedSprite("play", playAnimation, playX, playY,
				(int) buttonWidth, (int) buttonHeight, 6, 1, xRel2Screen,
				GetYRel2Screen(playY, buttonHeight));
		exitButton = new AnimatedSprite("exit", exitAnimation, (480 - 305) / 2, 316,
				(int) buttonWidth, (int) buttonHeight, 6, 1, xRel2Screen,
				GetYRel2Screen(exitY, buttonHeight));
		preventInitializeLag(exitButton);
		preventInitializeLag(playButton);

	}

	private void preventInitializeLag(AnimatedSprite sprite) {
		fakeRender(sprite);
	}

	public void fakeRender(AnimatedSprite animSprite) {
		spriteBatch.begin();
		spriteBatch.draw(animSprite, Gdx.graphics.getWidth(), 0);
		spriteBatch.end();
	}

	private float GetYRel2Screen(float y, float buttonHeight) {
		return ((Gdx.graphics.getHeight() - buttonHeight - y) / Gdx.graphics
				.getHeight());
	}

	@Override
	public boolean isDisposable() {
		return isDisposable;
	}

	public void setDisposable(boolean disposable) {
		this.isDisposable = disposable;
	}
}
