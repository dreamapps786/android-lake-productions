package com.helpers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.helpers.extensions.BubbleTexture;
import com.helpers.extensions.BubbleTexture.BubbleColor;
import com.model.CollisionObject;
import com.model.CollisionObject.Direction;
import com.simulation.AnimatedBubbleSprite;
import com.simulation.AnimatedSprite;

public class BubbleGrid {
	private float gridPosX;
	private float gridPosY;
	private BubbleGridRectangle[][] boxes;
	private final int gridWidth = 15;
	private final int gridHeight = 18;
	private final int initialPopHeight = 8;

	public BubbleGrid(BubbleTexture[] bubbleTextures, float x, float y) {
		this.gridPosX = x;
		this.gridPosY = y + 800 - bubbleTextures[0].getHeight();
		boxes = new BubbleGridRectangle[gridHeight][gridWidth];
		populate(bubbleTextures);
	}

	private void populate(BubbleTexture[] bubbleTextures) {
		int counter = bubbleTextures.length;
		for (int yIndex = 0; yIndex < boxes.length; yIndex += 2) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length; xIndex++) {
				boxes[yIndex][xIndex] = new BubbleGridRectangle(xIndex * 32,
						yIndex * -27 + 800, 32, 32, xIndex, yIndex,
						bubbleTextures[(int) (Math.random() * counter)]);
				if (yIndex < initialPopHeight) {
					boxes[yIndex][xIndex].setOccupied(true);
				}
			}
		}
		for (int yIndex = 1; yIndex < boxes.length; yIndex += 2) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length - 1; xIndex++) {
				boxes[yIndex][xIndex] = new BubbleGridRectangle(
						(xIndex * 32) + 16, yIndex * -27 + 800, 32, 32, xIndex,
						yIndex, bubbleTextures[(int) (Math.random() * counter)]);
				if (yIndex < initialPopHeight) {
					boxes[yIndex][xIndex].setOccupied(true);
				}
			}
		}
	}

	public BubbleGridRectangle[][] getGrid() {
		return boxes;
	}

	public CollisionObject checkForCollision(float centerX, float centerY, float radius) {
		centerY+=32; //FIXME Constant to solve collision offset
		for (int yIndex = 0; yIndex < boxes.length; yIndex += 2) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length; xIndex++) {
				if (boxes[yIndex][xIndex].isOccupied) {
					List<Direction> collidingDirections = checkFourPointCollision(
							boxes[yIndex][xIndex], centerX, centerY, radius);
					if (collidingDirections.size() >= 1) {
						System.out.println("Hit "+collidingDirections.size()+" directions: "+collidingDirections+" Bubbles:"+boxes[yIndex][xIndex].toString());
						return new CollisionObject(boxes[yIndex][xIndex],
								collidingDirections);
					}

				}
			}
		}
		for (int yIndex = 1; yIndex < boxes.length; yIndex += 2) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length - 1; xIndex++) {
				if (boxes[yIndex][xIndex].isOccupied){
					List<Direction> collidingDirections = checkFourPointCollision(boxes[yIndex][xIndex],
								centerX, centerY, radius);
						if (collidingDirections.size() >= 1) {
							System.out.println("Hit "+collidingDirections.size()+" directions: "+collidingDirections.get(0).toString()+" Bubble:"+boxes[yIndex][xIndex].toString());
							return new CollisionObject(boxes[yIndex][xIndex], 
									collidingDirections);
						}
				}
			}
		}
		return null;
	}

	private boolean isFirstIteration = true;

	private List<Direction> checkFourPointCollision(
			BubbleGridRectangle gridBubbleRect, float activeBubbleCenterX,
			float activeBubbleCenterY, float radius) {
		List<Direction> collidingDirections = new ArrayList<Direction>(2);
		float wX = activeBubbleCenterX - radius;
		float wY = activeBubbleCenterY;
		float nwX = activeBubbleCenterX + radius
				* (float) Math.cos(Math.toRadians(135));
		float nwY = activeBubbleCenterY + radius
				* (float) Math.sin(Math.toRadians(135));
		float nX = activeBubbleCenterX;
		float nY = activeBubbleCenterY + radius;
		float neX = activeBubbleCenterX + radius
				* (float) Math.cos(Math.toRadians(45));
		float neY = activeBubbleCenterY + radius
				* (float) Math.sin(Math.toRadians(45));
		float eX = activeBubbleCenterX + radius;
		float eY = activeBubbleCenterY;
		if (isFirstIteration) { // Kun til debugging
			isFirstIteration = false;
//			System.out.println("west(" + wX + ", " + wY + ")");
//			System.out.println("northwest(" + nwX + ", " + nwY + ")");
//			System.out.println("north(" + nX + ", " + nY + ")");
//			System.out.println("northeast(" + neX + ", " + neY + ")");
//			System.out.println("east(" + eX + ", " + eY + ")");
		}
		// .overlapsLowerHalf() har erstattet .contains()
		if (gridBubbleRect.overlapsLowerHalf(wX, wY)) {
			collidingDirections.add(Direction.W);
		}
		if (gridBubbleRect.overlapsLowerHalf(nwX, nwY)) {
			collidingDirections.add(Direction.NW);
		}
		if (gridBubbleRect.overlapsLowerHalf(neX, neY)) {
			collidingDirections.add(Direction.NE);
		}
		if (gridBubbleRect.overlapsLowerHalf(eX, eY)) {
			collidingDirections.add(Direction.E);
		}
		return collidingDirections;
		// if (gridBubbleRect.overlapsLowerHalf(wX, wY) || // west point
		// gridBubbleRect.overlapsLowerHalf(nwX, nwY) || // northwest point
		// gridBubbleRect.overlapsLowerHalf(nX, nY) || // north point
		// gridBubbleRect.overlapsLowerHalf(neX, neY) || // northeast point
		// gridBubbleRect.overlapsLowerHalf(eX, eY)) { // east point
		// return true;
		// }
		// return false;
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
				if (i == 7 && j == 8) {
					System.out.println("HIT HIT");
				}
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
	
	public BubbleGridRectangle getLeftParentOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble)) return null;
		
		int bcX = ofBubble.coordinateX;
		int bcY = ofBubble.coordinateY;
		
		int leftParentX = (bcY % 2 == 0 ? bcX - 1 : bcX);
		int leftParentY = bcY - 1;
		
		if (!isInsideGridCoordinates(leftParentX, leftParentY)) return null;
		return boxes[leftParentY][leftParentX];
	}
	
	public BubbleGridRectangle getRightParentOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble)) return null;
		
		int bcX = ofBubble.coordinateX;
		int bcY = ofBubble.coordinateY;
		
		int rightParentX = (bcY % 2 == 0 ? bcX : bcX + 1);
		int rightParentY = bcY - 1;
		
		if (!isInsideGridCoordinates(rightParentX, rightParentY)) return null;
		return boxes[rightParentY][rightParentX];
	}
	
	public BubbleGridRectangle getLeftSiblingOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble)) return null;
		
		int bcX = ofBubble.coordinateX;
		int bcY = ofBubble.coordinateY;
		
		int leftSiblingX = bcX - 1;
		int leftSiblingY = bcY;
		
		if (!isInsideGridCoordinates(leftSiblingX, leftSiblingY)) return null;
		return boxes[leftSiblingY][leftSiblingX];
	}
	
	public BubbleGridRectangle getRightSiblingOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble)) return null;
		
		int bcX = ofBubble.coordinateX;
		int bcY = ofBubble.coordinateY;
		
		int rightSiblingX = bcX + 1;
		int rightSiblingY = bcY;
		
		if (!isInsideGridCoordinates(rightSiblingX, rightSiblingY)) return null;
		return boxes[rightSiblingY][rightSiblingX];
	}

	public BubbleGridRectangle getLeftChildOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble)) return null;
		
		int bcX = ofBubble.coordinateX;
		int bcY = ofBubble.coordinateY;
		
		int leftChildX = (bcY % 2 == 0 ? bcX - 1 : bcX);
		int leftChildY = bcY + 1;
		
		if (!isInsideGridCoordinates(leftChildX, leftChildY)) return null;
		return boxes[leftChildY][leftChildX];
	}
	
	public BubbleGridRectangle getRightChildOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble)) return null;
		
		int bcX = ofBubble.coordinateX;
		int bcY = ofBubble.coordinateY;
		
		int rightChildX = (bcY % 2 == 0 ? bcX : bcX + 1);
		int rightChildY = bcY + 1;
		
		if (!isInsideGridCoordinates(rightChildX, rightChildY)) return null;
		return boxes[rightChildY][rightChildX];
	}
	
	private boolean isValidBubble(BubbleGridRectangle bubble) {
		if(bubble == null) return false;
		if (!isInsideGridCoordinates(bubble.coordinateX, bubble.coordinateY)) return false; //Bubble doesn't exist in grid
		return true;
	}
	
	public boolean isInsideGridCoordinates(int coordX, int coordY) {
		return coordX >= 0 && coordY >= 0 && coordX < boxes[0].length && coordY < boxes.length;
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
	
	public int getGridWidth() {
		return gridWidth;
	}
	
	public BubbleGridRectangle getBubbleAt(int coordinateX, int coordinateY){
		return boxes[coordinateY][coordinateX];
	}

	@SuppressWarnings("serial")
	public class BubbleGridRectangle extends Rectangle {
		// private Rectangle rectangle;
		private AnimatedBubbleSprite bubble;
		private BubbleColor color;
		private boolean isOccupied;
		private int coordinateX;
		private int coordinateY;

		public BubbleGridRectangle(float x, float y, float width, float height,
				int coordinateX, int coordinateY, BubbleTexture bubbleTexture) {
			super(x, y, width, height);
			x = gridPosX + x;
			y = gridPosY + y;
			this.coordinateX = coordinateX;
			this.coordinateY = coordinateY;
			bubble = new AnimatedBubbleSprite("bubble" + x / 32 + "-" + y / 32,
					bubbleTexture, (int) x, (int) y, bubbleTexture.getWidth(),
					bubbleTexture.getHeight(), 0, 0, 0f, 0f);
			isOccupied = false;
			bubble.setPosition(x, y);
			color = bubbleTexture.getColor();
		}

		public boolean overlapsLowerHalf(float x, float y) {
			float topY = this.y + (this.height / 2);
			float bottomY = this.y;
			float leftX = this.x;
			float rightX = this.x + this.width;
			boolean insideX = x > leftX && x < rightX;
			boolean insideY = y > bottomY && y < topY;
			return insideX && insideY;
		}

		public boolean isOccupied() {
			return isOccupied;
		}

		public void setOccupied(boolean isOccupied) {
			this.isOccupied = isOccupied;
		}

		public AnimatedBubbleSprite getBubble() {
			return bubble;
		}

		public BubbleColor getColor() {
			return color;
		}

		public int getCoordinateX() {
			return coordinateX;
		}

		public void setCoordinateX(int coordinateX) {
			this.coordinateX = coordinateX;
		}

		public int getCoordinateY() {
			return coordinateY;
		}

		public void setCoordinateY(int coordinateY) {
			this.coordinateY = coordinateY;
		}
		
		public void placeBubble(BubbleTexture color){
			setOccupied(true);
			setBubbleTexture(color);
		}
		
		public void setBubbleTexture(BubbleTexture bt){
			this.getBubble().setBubbleTexture(bt);
			this.color = bt.getColor();
		}

		@Override
		public String toString() {
			return "[X: " + this.x + " Y: " + this.y+"]"+" Coordinates["+this.coordinateX+","+this.coordinateY+"]";
		}
	}
}
