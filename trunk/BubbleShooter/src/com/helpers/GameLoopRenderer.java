package com.helpers;

import java.util.ArrayList;
import java.util.List;
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
import com.model.BubbleGridRectangle;
import com.helpers.extensions.BubbleQueueList;
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
	private List<AnimatedSprite> splashesToRender;
	private Texture shooterTexture;
	private BubbleQueueList upcomingBubbles;
	private Texture bubbleSplashTexture;
	private BubbleTexture[] bubbleTextures;
	private GameLoop gameloop;
	private BubbleGrid bubbleGrid;
	private GameRuler gameruler;
	private boolean isDestroyingBubbles = false;

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
		gameruler = new GameRuler(bubbleGrid);
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
		renderQueuedBubbles();
		if (isDestroyingBubbles) {
			for (int i = splashesToRender.size() - 1; i >= 0; i--) {
				AnimatedSprite sprite = splashesToRender.get(i);
				if (sprite.isActive()) {
					renderBubbleSplash(sprite);
				} else {
					splashesToRender.remove(i);
				}
			}
			if (splashesToRender.size() == 0) {
				GameRuler.bubbleShot();
				isDestroyingBubbles = false;
			}
		}
		// if (activeBubble.isActive()) {
		renderActiveBubble();
		// }
	}

	public void populate() {
		shooterTexture = new Texture(Gdx.files.internal("res/shooter.png"), true);
		shooterTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		shooter = new AnimatedSprite("shooter", shooterTexture, (480 - 64) / 2, 0, 64, 64, 1, 1, 0, 0);
		gameloop.setShooter(shooter);

		bubbleSplashTexture = new Texture(Gdx.files.internal("res/bubble_splash.png"), true);
		bubbleSplashTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);

		bubbleTextures[0] = new BubbleTexture(Gdx.files.internal("res/bubble_blue.png"), true, BubbleTexture.BubbleColor.blue);
		bubbleTextures[0].setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		bubbleTextures[1] = new BubbleTexture(Gdx.files.internal("res/bubble_yellow.png"), true, BubbleTexture.BubbleColor.yellow);
		bubbleTextures[1].setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		bubbleTextures[2] = new BubbleTexture(Gdx.files.internal("res/bubble_green.png"), true, BubbleTexture.BubbleColor.green);
		bubbleTextures[2].setFilter(TextureFilter.Linear, TextureFilter.Linear);

		upcomingBubbles = new BubbleQueueList();

		for (int i = 0; i < gameloop.getQueueLength(); i++) {
			BubbleTexture randomBubbleTexture = getRandomBubbleTexture();
			upcomingBubbles.addBubble(randomBubbleTexture);
		}
		BubbleTexture randomBubbleTexture = getRandomBubbleTexture();
		activeBubble = new AnimatedBubbleSprite("activeBubble", randomBubbleTexture, 0, 0, 32, 32, 0, 0, 0, 0);
		activeBubble.setPosition((480 - 64) / 2 + 16, 60);
		setActiveBubbleTexture(randomBubbleTexture);
		activeBubble.setActive(false);
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
		int counter = 0;
		for (AnimatedSprite bubble : bubbles) {
			if (counter == 0) {
				// System.out.println(bubble.getX()+" "+(bubble.getY() - 800 -
				// bubble.getHeight()));

			}
			counter++;
			spriteBatch.draw(bubble.getTexture(), bubble.getX(), bubble.getY() - bubble.getHeight());
		}
		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	private void renderQueuedBubbles() {
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_MAX_ELEMENTS_INDICES);
		ArrayList<AnimatedBubbleSprite> bubbles = upcomingBubbles;

		for (AnimatedSprite bubble : bubbles) {
			spriteBatch.draw(bubble.getTexture(), bubble.getX(), bubble.getY() - bubble.getHeight());
		}

		spriteBatch.disableBlending();
		spriteBatch.end();
	}

	public void renderBubbleSplash(AnimatedSprite bubbleToSplash) {
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.draw(bubbleToSplash, bubbleToSplash.getX(), bubbleToSplash.getY() - bubbleToSplash.getHeight());
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

	public BubbleGridRectangle handleCollision1(float startX, float startY, float endX, float endY, double direction) {
		float rectEdge1x;
		float rectEdge1y;
		float rectEdge2x;
		float rectEdge2y;
		float rectEdge3x;
		float rectEdge3y;
		float rectEdge4x;
		float rectEdge4y;

		return null;
	}

	public void stuff() {
		boolean collidedWithBubbles = false;
		boolean overTheTop = false;
		if (collidedWithBubbles) {

		} else if (overTheTop) {

		}
	}

	public BubbleGridRectangle handleCollision(float centerX, float centerY, float radius, double direction, double distance) {
		CollisionObject collissionObject = bubbleGrid.checkForCollision(centerX, centerY, radius, direction, distance);
		if (collissionObject != null) {
			int collidingX = collissionObject.getCollidingBubble().getCoordinateX();
			int collidingY = collissionObject.getCollidingBubble().getCoordinateY();
			List<Direction> directions = collissionObject.getCollissionDirections();
			BubbleGridRectangle target = null;
			int rowXOffset = (collidingY % 2 == 1 ? 1 : 0);
			int coordXOfBubbleToPlace = -1; // If these value are not set, it
											// indicates an unhandled case
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
						target = bubbleGrid.getBubbleAt(collidingX + 1, collidingY);
						if (target == null) {
							coordXOfBubbleToPlace = collidingX + 1;
							coordYOfBubbleToPlace = collidingY;
						}
					} else if (directions.contains(Direction.NW)) {
						target = bubbleGrid.getBubbleAt(collidingX + rowXOffset, collidingY + 1);
						if (target == null) {
							coordXOfBubbleToPlace = collidingX + rowXOffset;
							coordYOfBubbleToPlace = collidingY + 1;
						}
					} else if (directions.contains(Direction.NE)) {
						target = bubbleGrid.getBubbleAt(collidingX - 1 + rowXOffset, collidingY + 1);
						if (target == null) {
							coordXOfBubbleToPlace = collidingX - 1 + rowXOffset;
							coordYOfBubbleToPlace = collidingY + 1;
						}
					} else if (directions.contains(Direction.E)) {
						target = bubbleGrid.getBubbleAt(collidingX - 1, collidingY);
						if (target == null) {
							coordXOfBubbleToPlace = collidingX - 1;
							coordYOfBubbleToPlace = collidingY;
						}
					}
				} else if (directions.size() == 2) {
					if (directions.contains(Direction.W) && directions.contains(Direction.NW)) {
						if (direction >= 0) {
							target = bubbleGrid.getBubbleAt(collidingX + rowXOffset, collidingY + 1);
							if (target == null) {

								coordXOfBubbleToPlace = collidingX + rowXOffset;
								coordYOfBubbleToPlace = collidingY + 1;
							}
						} else {
							target = bubbleGrid.getBubbleAt(collidingX + rowXOffset, collidingY + 1);
							if (target == null) {

								coordXOfBubbleToPlace = collidingX + rowXOffset;
								coordYOfBubbleToPlace = collidingY + 1;
							}
						}
					} else if (directions.contains(Direction.NW) && directions.contains(Direction.NE)) {
						if (direction >= 0) {
							System.out.println(collidingX);
							target = bubbleGrid.getBubbleAt(collidingX + rowXOffset, collidingY + 1);
							if (target == null) {

								coordXOfBubbleToPlace = collidingX + rowXOffset;
								coordYOfBubbleToPlace = collidingY + 1;
							}
						} else {
							target = bubbleGrid.getBubbleAt(collidingX - 1 + rowXOffset, collidingY + 1);
							if (target == null) {

								coordXOfBubbleToPlace = collidingX - 1 + rowXOffset;
								coordYOfBubbleToPlace = collidingY + 1;
							} else {
								target = bubbleGrid.getBubbleAt(collidingX, collidingY + 1);
								if (target == null) {
									coordXOfBubbleToPlace = collidingX;
									coordYOfBubbleToPlace = collidingY + 1;
								}
							}
						}
					} else if (directions.contains(Direction.NE) && directions.contains(Direction.E)) {
						if (direction >= 0) {
							target = bubbleGrid.getBubbleAt(collidingX + rowXOffset, collidingY + 1);
							if (target == null) {
								coordXOfBubbleToPlace = collidingX + rowXOffset;
								coordYOfBubbleToPlace = collidingY + 1;
							}
						} else {
							target = bubbleGrid.getBubbleAt(collidingX - 1 + rowXOffset, collidingY + 1);
							if (target == null) {
								coordXOfBubbleToPlace = collidingX - 1 + rowXOffset;
								coordYOfBubbleToPlace = collidingY + 1;
							}
						}
					}
				}
			}
			// try {
			BubbleGridRectangle placedBubble = bubbleGrid.placeBubble(activeBubble.getBubbleTexture(), coordXOfBubbleToPlace,
					coordYOfBubbleToPlace);
			activeBubble.setActive(false);
			boolean destroyingBubbles = destroySameColorBubbles(placedBubble);
			if (!destroyingBubbles) { //Bubbleshot bliver kaldt, når splashanimationen er færdig, ellers bliver den kaldt her.
				GameRuler.bubbleShot();
			}
			// } catch (Exception e) {
			// System.out.println("message" + e.getMessage());
			// }
			return collissionObject.getCollidingBubble();
		}
		return null;
	}

	private boolean destroySameColorBubbles(BubbleGridRectangle bubble) {

		int noColorMatchesToExplode = 3;

		List<BubbleGridRectangle> bubblesToExplode = new ArrayList<BubbleGridRectangle>();
		checkNeighbours(bubble, bubblesToExplode, activeBubble.getBubbleTexture().getColor());
		System.out.println("Matching bubbles: " + bubblesToExplode.size());
		if (bubblesToExplode.size() >= noColorMatchesToExplode) {
			System.out.println("Enough bubbles to explode (" + bubblesToExplode.size() + "): " + bubblesToExplode);
			for (int i = 0; i < bubblesToExplode.size(); i++) {
				BubbleGridRectangle bubbleToExplode = bubblesToExplode.get(i);
				addSplash(bubbleToExplode.getCoordinateX(), bubbleToExplode.getCoordinateY());
				bubbleGrid.removeBubbleAt(bubbleToExplode.getCoordinateX(), bubbleToExplode.getCoordinateY());
			}
			// TODO HANGING BUBBLES
			// List<BubbleGridRectangle> hangingBubbles =
			// handleHangingBubbles(bubblesToExplode);
			// System.out.println("Hanging Fruit ("+hangingBubbles.size()+"bubbles): "
			// + hangingBubbles);
			// for (int i = 0; i < hangingBubbles.size(); i++) {
			// BubbleGridRectangle hangingBubble = hangingBubbles.get(i);
			// addSplash(hangingBubble.getCoordinateX(),
			// hangingBubble.getCoordinateY());
			// hangingBubble.setOccupied(false);
			// }
			return true;
		}
		return false;
	}

	// TODO Kig p� isConnectedVertexes i Graph Traversal: Opg3App.java
	private List<BubbleGridRectangle> handleHangingBubbles(List<BubbleGridRectangle> bubblesToExplode) {
		List<BubbleGridRectangle> hangingBubbles = newEmptyList();
		for (BubbleGridRectangle bubble : bubblesToExplode) {
			hangingBubbles.addAll(checkForHangingBubbles(bubble, newEmptyList(), newEmptyList()));
			// BubbleGridRectangle leftParent =
			// bubbleGrid.getLeftParentOfBubble(bubble);
			// BubbleGridRectangle rightParent =
			// bubbleGrid.getRightParentOfBubble(bubble);
			// BubbleGridRectangle leftSibling =
			// bubbleGrid.getLeftSiblingOfBubble(bubble);
			// BubbleGridRectangle rightSibling =
			// bubbleGrid.getRightSiblingOfBubble(bubble);
			// BubbleGridRectangle leftChild =
			// bubbleGrid.getLeftChildOfBubble(bubble);
			// BubbleGridRectangle rightChild =
			// bubbleGrid.getRightChildOfBubble(bubble);
			//
			// if (leftParent != null && leftParent != null) {
			// hangingBubbles.addAll(checkForHangingBubbles(leftParent,
			// newEmptyList(), newEmptyList()));
			// }
			// if (rightParent != null && rightParent != null) {
			// hangingBubbles.addAll(checkForHangingBubbles(rightParent,
			// newEmptyList(), newEmptyList()));
			// }
			// if (leftSibling != null && leftSibling != null) {
			// hangingBubbles.addAll(checkForHangingBubbles(leftSibling,
			// newEmptyList(), newEmptyList()));
			// }
			// if (rightSibling != null && rightSibling != null) {
			// hangingBubbles.addAll(checkForHangingBubbles(rightSibling,
			// newEmptyList(), newEmptyList()));
			// }
			// if (leftChild != null && leftChild != null) {
			// hangingBubbles.addAll(checkForHangingBubbles(leftChild,
			// newEmptyList(), newEmptyList()));
			// }
			// if (rightChild != null && rightChild != null) {
			// hangingBubbles.addAll(checkForHangingBubbles(rightChild,
			// newEmptyList(), newEmptyList()));
			// }
		}
		return hangingBubbles;
	}

	private List<BubbleGridRectangle> newEmptyList() {
		return new ArrayList<BubbleGridRectangle>();
	}

	private List<BubbleGridRectangle> checkForHangingBubbles(BubbleGridRectangle bubble, List<BubbleGridRectangle> visited,
			List<BubbleGridRectangle> hangingBubbles) {
		hangingBubbles.add(bubble);
		visited.add(bubble);

		BubbleGridRectangle leftParent = bubbleGrid.getLeftParentOfBubble(bubble);
		BubbleGridRectangle rightParent = bubbleGrid.getRightParentOfBubble(bubble);
		BubbleGridRectangle leftSibling = bubbleGrid.getLeftSiblingOfBubble(bubble);
		BubbleGridRectangle rightSibling = bubbleGrid.getRightSiblingOfBubble(bubble);
		BubbleGridRectangle leftChild = bubbleGrid.getLeftChildOfBubble(bubble);
		BubbleGridRectangle rightChild = bubbleGrid.getRightChildOfBubble(bubble);

		if (leftParent == null && rightParent == null) { // Is attached to the
															// top
			return new ArrayList<BubbleGridRectangle>(); // Return empty list
															// (no hanging
															// bubbles)
		}
		if (leftParent != null && !visited.contains(leftParent)) {
			hangingBubbles = checkForHangingBubbles(leftParent, visited, hangingBubbles);
		}
		if (rightParent != null && !visited.contains(rightParent)) {
			hangingBubbles = checkForHangingBubbles(rightParent, visited, hangingBubbles);
		}
		if (leftSibling != null && !visited.contains(leftSibling)) {
			hangingBubbles = checkForHangingBubbles(leftSibling, visited, hangingBubbles);
		}
		if (rightSibling != null && !visited.contains(rightSibling)) {
			hangingBubbles = checkForHangingBubbles(rightSibling, visited, hangingBubbles);
		}
		if (leftChild != null && !visited.contains(leftChild)) {
			hangingBubbles = checkForHangingBubbles(leftChild, visited, hangingBubbles);
		}
		if (rightChild != null && !visited.contains(rightChild)) {
			hangingBubbles = checkForHangingBubbles(rightChild, visited, hangingBubbles);
		}
		return hangingBubbles;
	}

	private void checkNeighbours(BubbleGridRectangle bubbleToCheck, List<BubbleGridRectangle> bubblesToExplode, BubbleColor collidingColor) {
		if (bubbleToCheck != null && !bubblesToExplode.contains(bubbleToCheck)) {
			System.out.println(bubbleToCheck + " color: " + bubbleToCheck.getColor() + " textColor: "
					+ bubbleToCheck.getBubble().getBubbleTexture().getColor());
			bubblesToExplode.add(bubbleToCheck);

			BubbleGridRectangle leftParent = bubbleGrid.getLeftParentOfBubble(bubbleToCheck);
			BubbleGridRectangle rightParent = bubbleGrid.getRightParentOfBubble(bubbleToCheck);
			BubbleGridRectangle leftSibling = bubbleGrid.getLeftSiblingOfBubble(bubbleToCheck);
			BubbleGridRectangle rightSibling = bubbleGrid.getRightSiblingOfBubble(bubbleToCheck);
			BubbleGridRectangle leftChild = bubbleGrid.getLeftChildOfBubble(bubbleToCheck);
			BubbleGridRectangle rightChild = bubbleGrid.getRightChildOfBubble(bubbleToCheck);

			if (leftParent != null && leftParent.getColor() == collidingColor) {
				checkNeighbours(leftParent, bubblesToExplode, collidingColor);
			}
			if (rightParent != null && rightParent.getColor() == collidingColor) {
				checkNeighbours(rightParent, bubblesToExplode, collidingColor);
			}
			if (leftSibling != null && leftSibling.getColor() == collidingColor) {
				checkNeighbours(leftSibling, bubblesToExplode, collidingColor);
			}
			if (rightSibling != null && rightSibling.getColor() == collidingColor) {
				checkNeighbours(rightSibling, bubblesToExplode, collidingColor);
			}
			if (leftChild != null && leftChild.getColor() == collidingColor) {
				checkNeighbours(leftChild, bubblesToExplode, collidingColor);
			}
			if (rightChild != null && rightChild.getColor() == collidingColor) {
				checkNeighbours(rightChild, bubblesToExplode, collidingColor);
			}
		}
	}

	public AnimatedSprite getActiveBubble() {
		return activeBubble;
	}

	public List<AnimatedSprite> getSplashesToRender() {
		return splashesToRender;
	}

	public void addSplash(int coordinateX, int coordinateY) {
		BubbleGridRectangle bubbleToSplash = bubbleGrid.getBubbleAt(coordinateX, coordinateY);
		System.out.println("BubbleToSplash CorX "+coordinateX);
		System.out.println("BubbleToSplash X: "+bubbleToSplash.getX());
		AnimatedSprite splash = new AnimatedSprite("bubbleSplash", bubbleSplashTexture, bubbleToSplash.getX(), bubbleToSplash.getY(), 32,
				32, 2, 2, 0, 0);
		splash.setAnimationRate(0.5f);
		preventInitializeLag(splash);
		splash.setActive(true);
		splash.play();
		this.splashesToRender.add(splash);
		this.isDestroyingBubbles = true;
	}

	public void resetActiveBubble(float x, float y) {
		setActiveBubbleTexture(upcomingBubbles.takeBubble().getBubbleTexture());
		this.activeBubble.setPosition(x, y);
		upcomingBubbles.addBubble(getRandomBubbleTexture());
	}

	private void preventInitializeLag(AnimatedSprite sprite) {
		fakeRender(sprite);
	}

	public void fakeRender(AnimatedSprite animSprite) {
		spriteBatch.begin();
		spriteBatch.draw(animSprite, Gdx.graphics.getWidth(), 0);
		spriteBatch.end();
	}

	public BubbleTexture getRandomBubbleTexture() {
		return bubbleTextures[((int) (Math.random() * bubbleTextures.length))];
	}

	public void setActiveBubbleTexture(BubbleTexture bt) {
		activeBubble.setBubbleTexture(bt);
	}

	public GameRuler getGameruler() {
		return gameruler;
	}
}
