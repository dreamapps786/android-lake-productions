package com.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.simulation.AnimatedSprite;

public class BubbleGrid {
	float gridPosX;
	float gridPosY;
	private BubbleGridRectangle[][] boxes;

	public BubbleGrid(Texture bubbleTexture, float x, float y) {
		this.gridPosX = x;
		this.gridPosY = y + 800 - bubbleTexture.getHeight();
		boxes = new BubbleGridRectangle[15][20];
		populate(bubbleTexture);
	}

	private void populate(Texture bubbleTexture) {
		for (int i = 0; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j] = new BubbleGridRectangle(j * 32, i * 32, 32, 32,
						bubbleTexture);
			}
		}
		for (int i = 1; i < boxes.length; i += 2) {
			System.out.println(boxes[i].length);
			for (int j = 0; j < boxes[i].length - 1; j++) {
				boxes[i][j] = new BubbleGridRectangle((j * 32) + 16, i * 32,
						32, 32, bubbleTexture);
			}
		}

		boxes[0][0].setOccupied(true);
		System.out.println(boxes[1][0].getX() +" "+boxes[1][0].getY());
		boxes[1][0].setOccupied(true);
		boxes[0][1].setOccupied(true);
		boxes[1][1].setOccupied(true);
		boxes[2][0].setOccupied(true);
		boxes[0][3].setOccupied(true);
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
				boxes.length * boxes[boxes.length - 1].length);
		for (int i = 0; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isOccupied) {
					results.add(boxes[i][j].getBubble());
				}
			}
		}
		// System.out.println(results.get(0).getX());
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
