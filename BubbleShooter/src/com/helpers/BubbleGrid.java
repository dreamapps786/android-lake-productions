package com.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.simulation.AnimatedSprite;

public class BubbleGrid {
	float gridPosX;
	float gridPosY;
	private BubbleGridRectangle[][] boxes;

	public BubbleGrid(Texture[] bubbleTextures, float x, float y) {
		this.gridPosX = x;
		this.gridPosY = y + 800 - bubbleTextures[0].getHeight();
		boxes = new BubbleGridRectangle[12][15];
		populate(bubbleTextures);
	}

	private void populate(Texture[] bubbleTextures) {
		int counter = bubbleTextures.length;
		System.out.println("Counter: " + counter);
		for (int i = 0; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j] = new BubbleGridRectangle(j * 32, i * -27, 32, 32,
						bubbleTextures[(int) (Math.random() * counter)]);
				boxes[i][j].setOccupied(true);
			}
		}
		for (int i = 1; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length - 1; j++) {
				boxes[i][j] = new BubbleGridRectangle((j * 32) + 16, i * -27,
						32, 32, bubbleTextures[(int) (Math.random() * counter)]);
				boxes[i][j].setOccupied(true);
			}
		}
	}

	public BubbleGridRectangle[][] getGrid() {
		return boxes;
	}

	public boolean isColliding(float x, float y) {
		for (int i = 0; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].contains(x + 16, y + 16)
						&& boxes[i][j].isOccupied) {
					boxes[i + 1][j].setOccupied(true);
					return true;
				}
			}
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
		private Rectangle rectangle;
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
			return rectangle.toString();
		}
	}
}
