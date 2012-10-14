package com.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Renderer;

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
import com.gui.GameLoop;
import com.helpers.BubbleGrid.BubbleGridRectangle;
import com.helpers.extensions.BubbleTexture;
import com.helpers.extensions.BubbleTexture.BubbleColor;
import com.model.CollisionObject;
import com.model.CollisionObject.Direction;
import com.simulation.AnimatedBubbleSprite;
import com.simulation.AnimatedSprite;
import com.simulation.Simulation;

public class GameLoopRenderer {
	private final Matrix4 viewMatrix = new Matrix4();
	private final TextureAtlas background;
	private final Matrix4 transformMatrix = new Matrix4();
	private static SpriteBatch spriteBatch;
	private static BitmapFont font;
	private AnimatedSprite shooter;
	private AnimatedBubbleSprite activeBubble;
	// private AnimatedSprite bubbleSplash;
	private List<AnimatedSprite> splashesToRender;
	private Texture shooterTexture;
	private BubbleTexture activeBubbleTexture;
	private Texture bubbleSplashTexture;
	private BubbleTexture[] bubbleTextures;
	private GameLoop gameloop;
	private BubbleGrid bubbleGrid;

	private List<AnimatedSprite> debugPoints;

	/**
	 * Bruges til at adskille renderer metoderne fra GameLoop
	 * 
	 * @param app
	 */
	public GameLoopRenderer(GameLoop gameloop) {
		this.gameloop = gameloop;
		background = new TextureAtlas();
		for (int i = 1; i <= 8; i++) {
			Texture backgroundPart = new Texture(Gdx.files.internal("res/bg/background_portrait_" + i + ".png"));
			backgroundPart.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion r = new TextureRegion();
			r.setRegion(backgroundPart);
			background.addRegion(i + "", r);
		}
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setScale(1.6f, 1.6f);
		bubbleTextures = new BubbleTexture[3];
		populate();
		bubbleGrid = new BubbleGrid(bubbleTextures, 0, 0);
		splashesToRender = new ArrayList<AnimatedSprite>();

		// activeBubble.setActive(true);
		// activeBubble.setPosition(65, 579);
	}

	public void render(Application app, Simulation simulation) {
		// GL10 gl = app.getGraphics().getGL10(); // Skal kaldes hvis GL skal
		// bruges igen
		// gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderBackground();
		renderShooter();
		renderBubbles();

		for (AnimatedSprite sprite : splashesToRender) {
			if (sprite.isActive()) {
				renderBubbleSplash(sprite);
			}
		}

		if (activeBubble.isActive()) {
			renderActiveBubble();
		}
	}

	public void populate() {
		shooterTexture = new Texture(Gdx.files.internal("res/shooter.png"), true);
		shooterTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		shooter = new AnimatedSprite("shooter", shooterTexture, 0, 0, 64, 64, 1, 1, 0, 0);
		shooter.setPosition((480 - 64) / 2, 0);
		gameloop.setShooter(shooter);

		activeBubbleTexture = new BubbleTexture(Gdx.files.internal("res/bubble_green.png"), true, BubbleColor.green);
		activeBubbleTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);

		bubbleSplashTexture = new Texture(Gdx.files.internal("res/bubble_splash.png"), true);
		bubbleSplashTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);

		bubbleTextures[0] = new BubbleTexture(Gdx.files.internal("res/bubble_blue.png"), true, BubbleTexture.BubbleColor.blue);
		bubbleTextures[0].setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		bubbleTextures[1] = new BubbleTexture(Gdx.files.internal("res/bubble_yellow.png"), true, BubbleTexture.BubbleColor.yellow);
		bubbleTextures[1].setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		bubbleTextures[2] = new BubbleTexture(Gdx.files.internal("res/bubble_green.png"), true, BubbleTexture.BubbleColor.green);
		bubbleTextures[2].setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// bubbleSplash = new AnimatedSprite("bubbleSplash",
		// bubbleSplashTexture, 0, 0, 32, 32, 2, 2, 0, 0);

		activeBubble = new AnimatedBubbleSprite("activeBubble", activeBubbleTexture, 0, 0, 32, 32, 0, 0, 0, 0);
		activeBubble.setActive(false);
		// bubbleSplash.setActive(false);
		// gameloop.setActiveBubble(activeBubble);
		// gameloop.setBubbleSplash(bubbleSplash);

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
				spriteBatch.draw(background.getRegions().get(i).getTexture(), 0 + col * 256, (800 - 256) - row * 256, 256, 256, 0, 0, 256,
						256, false, false);
				i++;
			}
		}
		spriteBatch.end();
	}

	private void renderShooter() {
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.draw(shooter, shooter.getX(), shooter.getY(), 32, 32, shooter.getWidth(), shooter.getHeight(), 1, 1,
				shooter.getRotation());
		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	public void renderActiveBubble() {
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.draw(activeBubble.getTexture(), activeBubble.getX(), activeBubble.getY());
		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	private void renderBubbles() {
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_MAX_ELEMENTS_INDICES);
		ArrayList<AnimatedSprite> bubbles = bubbleGrid.getBubbles();
		for (AnimatedSprite bubble : bubbles) {
			spriteBatch.draw(bubble.getTexture(), bubble.getX(), bubble.getY() - 800);
		}
		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	public void renderBubbleSplash(AnimatedSprite bubbleToSplash) {
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.draw(bubbleToSplash, bubbleToSplash.getX(), bubbleToSplash.getY());
		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	public static void drawText(String text, int x, int y, Color color) {
		spriteBatch.begin();
		spriteBatch.enableBlending();
		font.setColor(color); // Vil nok unødigt sænke performance
		font.draw(spriteBatch, text, x, y);
		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	public void dispose() {

	}

	public BubbleGridRectangle handleCollision(float centerX, float centerY, float radius, double direction) {
		CollisionObject collissionObject = bubbleGrid.checkForCollision(centerX, centerY, radius);
		if (collissionObject != null) {
			int collidingX = collissionObject.getCollidingBubble().getCoordinateX();
			int collidingY = collissionObject.getCollidingBubble().getCoordinateY();
			List<Direction> directions = collissionObject.getCollissionDirections();
			BubbleGridRectangle target = null;
			int rowXOffset = (collidingY % 2 == 1 ? 1 : 0);
			int coordXOfBubbleToPlace = -1; //If these value are not set, it indicates an unhandled case
			int coordYOfBubbleToPlace = -1;
				if (collidingX == 0 && collidingY % 2 == 0) { // To avoid
																// placing it
																// out of bounds
					System.out.println("Avoided out of bounds");
					coordXOfBubbleToPlace = collidingX + rowXOffset;
					coordYOfBubbleToPlace = collidingY + 1;
				} else if (collidingX == bubbleGrid.getGridWidth() - 1 && collidingY % 2 == 0) {
					System.out.println("Avoided out of bounds");
					coordXOfBubbleToPlace = collidingX - 1 + rowXOffset;
					coordYOfBubbleToPlace = collidingY + 1;
				} else {
					if (directions.size() == 1) {
						if (directions.contains(Direction.W)) {
							target = bubbleGrid.getGrid()[collidingY][collidingX + 1];
							if (!target.isOccupied()) {
								coordXOfBubbleToPlace = collidingX + 1;
								coordYOfBubbleToPlace = collidingY;
							}
						} else if (directions.contains(Direction.NW)) {
							target = bubbleGrid.getGrid()[collidingY + 1][collidingX + rowXOffset];
							if (!target.isOccupied()) {
								coordXOfBubbleToPlace = collidingX + rowXOffset;
								coordYOfBubbleToPlace = collidingY + 1;
							}
						} else if (directions.contains(Direction.NE)) {
							target = bubbleGrid.getGrid()[collidingY + 1][collidingX - 1 + rowXOffset];
							if (!target.isOccupied()) {
								coordXOfBubbleToPlace = collidingX - 1 + rowXOffset;
								coordYOfBubbleToPlace = collidingY + 1;
							}
						} else if (directions.contains(Direction.E)) {
							target = bubbleGrid.getGrid()[collidingY][collidingX - 1];
							if (!target.isOccupied()) {
								coordXOfBubbleToPlace = collidingX - 1;
								coordYOfBubbleToPlace = collidingY;
							}
						}
					} else if (directions.size() == 2) {
						if (directions.contains(Direction.W) && directions.contains(Direction.NW)) {
							if (direction >= 0) {
								target = bubbleGrid.getGrid()[collidingY + 1][collidingX + rowXOffset];
								if (!target.isOccupied()) {

									coordXOfBubbleToPlace = collidingX + rowXOffset;
									coordYOfBubbleToPlace = collidingY + 1;
								}
							} else {
								target = bubbleGrid.getGrid()[collidingY + 1][collidingX + rowXOffset];
								if (!target.isOccupied()) {

									coordXOfBubbleToPlace = collidingX + rowXOffset;
									coordYOfBubbleToPlace = collidingY + 1;
								}
							}
						} else if (directions.contains(Direction.NW) && directions.contains(Direction.NE)) {
							if (direction >= 0) {
								System.out.println(collidingX);
								target = bubbleGrid.getGrid()[collidingY + 1][collidingX + rowXOffset];
								if (!target.isOccupied()) {

									coordXOfBubbleToPlace = collidingX + rowXOffset;
									coordYOfBubbleToPlace = collidingY + 1;
								}
							} else {
								target = bubbleGrid.getGrid()[collidingY + 1][collidingX - 1 + rowXOffset];
								if (!target.isOccupied()) {

									coordXOfBubbleToPlace = collidingX - 1 + rowXOffset;
									coordYOfBubbleToPlace = collidingY + 1;
								} else {
									target = bubbleGrid.getGrid()[collidingY + 1][collidingX];
									if (!target.isOccupied()) {
										coordXOfBubbleToPlace = collidingX;
										coordYOfBubbleToPlace = collidingY + 1;
									}
								}
							}
						} else if (directions.contains(Direction.NE) && directions.contains(Direction.E)) {
							if (direction >= 0) {
								target = bubbleGrid.getGrid()[collidingY + 1][collidingX + rowXOffset];
								if (!target.isOccupied()) {
									coordXOfBubbleToPlace = collidingX + rowXOffset;
									coordYOfBubbleToPlace = collidingY + 1;
								}
							} else {
								target = bubbleGrid.getGrid()[collidingY + 1][collidingX - 1 + rowXOffset];
								if (!target.isOccupied()) {
									coordXOfBubbleToPlace = collidingX - 1 + rowXOffset;
									coordYOfBubbleToPlace = collidingY + 1;
								}
							}
						}
					}
				}
				BubbleGridRectangle bubbleToPlace = bubbleGrid.getGrid()[coordYOfBubbleToPlace][coordXOfBubbleToPlace];
				bubbleToPlace.placeBubble(activeBubble.getBubbleTexture());
				activeBubble.setActive(false);
				PointService.Score();
				destroySameColorBubbles(bubbleToPlace);
				return collissionObject.getCollidingBubble();
			}
		return null;
	}

	private boolean destroySameColorBubbles(BubbleGridRectangle bubble) {

		// -------------PSEUDO KODE---------------
		int noColorMatchesToExplode = 3;

		List<BubbleGridRectangle> bubblesToExplode = new ArrayList<BubbleGrid.BubbleGridRectangle>();
		checkNeighbours(bubble, bubblesToExplode, activeBubble.getBubbleTexture().getColor());

		if (bubblesToExplode.size() >= noColorMatchesToExplode) {
			System.out.println("Enough bubbles to explode (" + bubblesToExplode.size() + "): " + bubblesToExplode);
			// checkForFallingFruit(bubblesToExplode); //TODO Tilføj dem der
			// skal splashe pga. de ikke længere sidder fast på nogen ovenfra.
			for (int i = 0; i < bubblesToExplode.size(); i++) { // Måske modsat
																// rækkefølge i
																// stedet
				BubbleGridRectangle bubbleToExplode = bubblesToExplode.get(i);
				addSplash(bubbleToExplode.getCoordinateX(),bubbleToExplode.getCoordinateY());
				bubblesToExplode.get(i).setOccupied(false);
			}
		}

		// totalCount += checkNeighbour(bubble);
		return false;
	}

	// private int checkNeighbours(BubbleGridRectangle bubble) {
	private void checkNeighbours(BubbleGridRectangle bubbleToCheck, List<BubbleGridRectangle> bubblesToExplode, BubbleColor collidingColor) {
		System.out.println(bubbleToCheck + " color: " + bubbleToCheck.getColor());
		if (bubbleToCheck != null && bubbleToCheck.isOccupied() && bubbleToCheck.getColor() == collidingColor
				&& !bubblesToExplode.contains(bubbleToCheck)) {
			bubblesToExplode.add(bubbleToCheck);
			
			BubbleGridRectangle leftParent = bubbleGrid.getLeftParentOfBubble(bubbleToCheck);
			BubbleGridRectangle rightParent = bubbleGrid.getRightParentOfBubble(bubbleToCheck);
			BubbleGridRectangle leftSibling = bubbleGrid.getLeftSiblingOfBubble(bubbleToCheck);
			BubbleGridRectangle rightSibling = bubbleGrid.getRightSiblingOfBubble(bubbleToCheck);
			BubbleGridRectangle leftChild = bubbleGrid.getLeftChildOfBubble(bubbleToCheck);
			BubbleGridRectangle rightChild = bubbleGrid.getRightChildOfBubble(bubbleToCheck);
			
			checkNeighbours(leftParent, bubblesToExplode, collidingColor);
			checkNeighbours(rightParent, bubblesToExplode, collidingColor);
			checkNeighbours(leftSibling, bubblesToExplode, collidingColor);
			checkNeighbours(rightSibling, bubblesToExplode, collidingColor);
			checkNeighbours(leftChild, bubblesToExplode, collidingColor);
			checkNeighbours(rightChild, bubblesToExplode, collidingColor);
		}
	}

	public AnimatedSprite getActiveBubble() {
		return activeBubble;
	}

	public List<AnimatedSprite> getSplashesToRender() {
		return splashesToRender;
	}

	public void addSplash(int coordinateX, int coordinateY) {
		AnimatedSprite splash = new AnimatedSprite("bubbleSplash", bubbleSplashTexture, 0, 0, 32, 32, 2, 2, 0, 0);
		BubbleGridRectangle bubbleToSplash = bubbleGrid.getBubbleAt(coordinateX, coordinateY);
		splash.setPosition(bubbleToSplash.getX(), bubbleToSplash.getY());
		splash.setActive(true);
		this.splashesToRender.add(splash);

	}
}
