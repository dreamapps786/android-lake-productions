package com.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.simulation.AnimatedSprite;

public class BubbleGrid {
	float gridPosX;
	float gridPosY;
	private BubbleGridRectangle[][] boxes;
	private final int gridWidth = 15;
	private final int gridHeight = 18;
	private final int initialPopHeight = 8;

	public BubbleGrid(Texture[] bubbleTextures, float x, float y) {
		this.gridPosX = x;
		this.gridPosY = y + 800 - bubbleTextures[0].getHeight();
		boxes = new BubbleGridRectangle[gridHeight][gridWidth];
		populate(bubbleTextures);
	}

	private void populate(Texture[] bubbleTextures) {
		int counter = bubbleTextures.length;
		for (int i = 0; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j] = new BubbleGridRectangle(j * 32, i * -27 + 800,
						32, 32, bubbleTextures[(int) (Math.random() * counter)]);
				if (i < initialPopHeight) {
					boxes[i][j].setOccupied(true);
				}
			}
		}
		for (int i = 1; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length - 1; j++) {
				boxes[i][j] = new BubbleGridRectangle((j * 32) + 16, i * -27
						+ 800, 32, 32,
						bubbleTextures[(int) (Math.random() * counter)]);
				if (i < initialPopHeight) {
					boxes[i][j].setOccupied(true);
				}
			}
		}
	}

	public BubbleGridRectangle[][] getGrid() {
		return boxes;
	}

	public boolean isColliding(float centerX, float centerY, float radius) {
//	public boolean isColliding(Rectangle bounds) {
//		System.out.println("a bub(" + bounds.x + "x, " + bounds.y + "y)");
		for (int i = 0; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length; j++) {
//				if (i >= 6 && boxes[i][j].isOccupied) {
//					System.out.println("G-bubble (" + boxes[i][j].getX() + "x, " + boxes[i][j].getY() + "y) row: "
//							+ i + ", col: " + j + " A-bubble (" + centerX + ", " + centerY + ")");
//				}
				if (boxes[i][j].isOccupied && checkFivePointCollission(boxes[i][j], centerX, centerY, radius)) {
//					System.out.println("Collision (" + centerX + "x, " + centerY + "y) i: " + i + ", j: " + j);
//					System.out.println("Collision i: " + i + ", j: " + j);
					return true;
				}
			}
		}
		for (int i = 1; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length - 1; j++) {
//				if (i >= 6 && boxes[i][j].isOccupied) {
//					System.out.println("G-bubble (" + boxes[i][j].getX() + "x, " + boxes[i][j].getY() + "y) row: "
//							+ i + ", col: " + j + " A-bubble (" + centerX + ", " + centerY + ")");
//				}
				if (boxes[i][j].isOccupied && checkFivePointCollission(boxes[i][j], centerX, centerY, radius)) {
//					System.out.println("Collision (" + centerX + "x, " + centerY + "y) i: " + i + ", j: " + j);
//					System.out.println("Collision i: " + i + ", j: " + j);
					return true;
				}
			}
		}
//		System.out.println("No collision\n----- -----");
		return false;
	}
	
	private boolean deleteThis = true;
	
	private boolean checkFivePointCollission(Rectangle gridBubbleRect, float activeBubbleCenterX,
			float activeBubbleCenterY, float radius) {
		
		float wX = activeBubbleCenterX - radius;
		float wY = activeBubbleCenterY;
		float nwX = activeBubbleCenterX + radius * (float)Math.cos(Math.toRadians(135));
		float nwY = activeBubbleCenterY + radius * (float)Math.sin(Math.toRadians(135));
		float nX = activeBubbleCenterX;
		float nY = activeBubbleCenterY + radius;
		float neX = activeBubbleCenterX + radius * (float)Math.cos(Math.toRadians(45));
		float neY = activeBubbleCenterY + radius * (float)Math.sin(Math.toRadians(45));
		float eX = activeBubbleCenterX + radius;
		float eY = activeBubbleCenterY;
		if (deleteThis) {
			deleteThis = false;
			System.out.println("west(" + wX + ", " + wY+")");
			System.out.println("northwest(" + nwX + ", " + nwY+")");
			System.out.println("north(" + nX + ", " + nY+")");
			System.out.println("northeast(" + neX + ", " + neY+")");
			System.out.println("east(" + eX + ", " + eY+")");
		}
		
		if (gridBubbleRect.contains(wX, wY) || //west point
				gridBubbleRect.contains(nwX, nwY) || //northwest point
				gridBubbleRect.contains(nX, nY) || //north point
				gridBubbleRect.contains(neX, neY) || //northeast point
				gridBubbleRect.contains(eX, eY)) { //east point
			return true;
		}
		return false;
	}

	public void setOccupied(float x) {
		int iPos = (int) (x / 32);
		boxes[iPos][0].setOccupied(true);
	}

	public ArrayList<AnimatedSprite> getBubbles() {
		ArrayList<AnimatedSprite> results = new ArrayList<AnimatedSprite>(
				boxes.length * boxes[0].length);
		for (int i = 0; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isOccupied) {
					results.add(boxes[i][j].getBubble());
				}
			}
		}

		for (int i = 1; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length - 1; j++) {
				if (boxes[i][j].isOccupied) {
					results.add(boxes[i][j].getBubble());
				}
			}
		}
		return results;
	}

	@SuppressWarnings("unused")
	private void printBubbleGrid() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				System.out.print(boxes[i][j]);
				System.out.print(", ");
			}
			System.out.println();
		}
	}

	@SuppressWarnings("serial")
	public class BubbleGridRectangle extends Rectangle {
		// private Rectangle rectangle;
		private AnimatedSprite bubble;
		private boolean isOccupied;

		public BubbleGridRectangle(float x, float y, float width, float height,
				Texture bubbleTexture) {
			super(x, y, width, height);
			x = gridPosX + x;
			y = gridPosY + y;
			bubble = new AnimatedSprite("bubble" + x / 32 + "-" + y / 32,
					bubbleTexture, (int) x, (int) y, bubbleTexture.getWidth(),
					bubbleTexture.getHeight(), 0, 0, 0f, 0f);
			isOccupied = false;
			bubble.setPosition(x, y);
		}

		public boolean isOccupied() {
			return isOccupied;
		}

		public void setOccupied(boolean isOccupied) {
			this.isOccupied = isOccupied;
		}

		public AnimatedSprite getBubble() {
			return bubble;
		}

		@Override
		public String toString() {
			return "X: " + this.x + " Y: " + this.y;
		}
	}
}
