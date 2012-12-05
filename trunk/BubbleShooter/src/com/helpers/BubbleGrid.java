package com.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.helpers.extensions.BubbleTexture;
import com.model.BubbleGridRectangle;
import com.model.CollisionObject;
import com.model.CollisionObject.Direction;
import com.simulation.AnimatedSprite;

public class BubbleGrid {
	private BubbleGridRectangle[][] boxes;
	private final int gridWidth = 14;
	private int gridHeight = 18;
	private final int initialPopHeight = 8;
	private final int marginY = 6;
	private final int marginX = 1;

	public BubbleGrid(BubbleTexture[] bubbleTextures, float x, float y) {
		boxes = new BubbleGridRectangle[gridHeight][gridWidth];
		GameRuler.setTotalBubbleCount(this.gridHeight * this.gridWidth);
		GameRuler.setMaxRowCount(gridHeight);
		populate(bubbleTextures);
	}

	private void populate(BubbleTexture[] bubbleTextures) {
		int counter = bubbleTextures.length;
		for (int yIndex = 0; yIndex < boxes.length; yIndex++) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length; xIndex++) {
				int offSetX = yIndex % 2 == 0 ? 0 : 16;
				boxes[yIndex][xIndex] = new BubbleGridRectangle(xIndex * 32 + (xIndex * marginX) + offSetX, yIndex * -32 + 800
						- (yIndex * -marginY), 32, 32, xIndex, yIndex, bubbleTextures[(int) (Math.random() * counter)]);
				if (yIndex < initialPopHeight) {
					boxes[yIndex][xIndex].setOccupied(true);
				}
			}
		}
	}

	public BubbleGridRectangle[][] getGrid() {
		return boxes;
	}

	public CollisionObject checkForCollision(float centerX, float centerY, float radius, double direction, double distance) {
		//TODO Calculate rectangle
		double movingBubbleV1X;
		double movingBubbleV1Y;
		double movingBubbleV2X;
		double movingBubbleV2Y;
		double movingBubbleV3X;
		double movingBubbleV3Y;
		double movingBubbleV4X;
		double movingBubbleV4Y;
		
		
		
		centerY += 32; // FIXME Constant to solve collision offset
		for (int yIndex = 0; yIndex < boxes.length; yIndex++) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length; xIndex++) {
				if (boxes[yIndex][xIndex].isOccupied()) {
					List<Direction> collidingDirections = checkFourPointCollision(boxes[yIndex][xIndex], centerX, centerY, radius);
					if (collidingDirections.size() >= 1) {
						System.out.println("Hit " + collidingDirections.size() + " directions: " + collidingDirections + " Bubbles:"
								+ boxes[yIndex][xIndex].toString());
						return new CollisionObject(boxes[yIndex][xIndex], collidingDirections);
					}
				}
			}
		}
		return null;
	}

	private boolean isFirstIteration = true;

	private List<Direction> checkFourPointCollision(BubbleGridRectangle gridBubbleRect, float activeBubbleCenterX,
			float activeBubbleCenterY, float radius) {
		List<Direction> collidingDirections = new ArrayList<Direction>(2);
		float wX = activeBubbleCenterX - radius;
		float wY = activeBubbleCenterY;
		float nwX = activeBubbleCenterX + radius * (float) Math.cos(Math.toRadians(135));
		float nwY = activeBubbleCenterY + radius * (float) Math.sin(Math.toRadians(135));
		float nX = activeBubbleCenterX;
		float nY = activeBubbleCenterY + radius;
		float neX = activeBubbleCenterX + radius * (float) Math.cos(Math.toRadians(45));
		float neY = activeBubbleCenterY + radius * (float) Math.sin(Math.toRadians(45));
		float eX = activeBubbleCenterX + radius;
		float eY = activeBubbleCenterY;
		if (isFirstIteration) { // Kun til debugging
			isFirstIteration = false;
			// System.out.println("west(" + wX + ", " + wY + ")");
			// System.out.println("northwest(" + nwX + ", " + nwY + ")");
			// System.out.println("north(" + nX + ", " + nY + ")");
			// System.out.println("northeast(" + neX + ", " + neY + ")");
			// System.out.println("east(" + eX + ", " + eY + ")");
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
		ArrayList<AnimatedSprite> results = new ArrayList<AnimatedSprite>(boxes.length * boxes[0].length);
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isOccupied()) {
					results.add(boxes[i][j].getBubble());
				}
			}
		}
		return results;
	}

	public List<BubbleGridRectangle> getHangingBubbles(int fromRow) {
		List<BubbleGridRectangle> hangingBubbles = new ArrayList<BubbleGridRectangle>();
		int evenStartRow = (fromRow % 2 == 0 ? fromRow : fromRow + 1);
		int oddStartRow = (fromRow % 2 == 1 ? fromRow : fromRow + 1);

		for (int i = evenStartRow; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length; j++) {
				BubbleGridRectangle bubble = boxes[i][j];
				if (bubble.isOccupied() && isHangingBubble(bubble)) {
					hangingBubbles.add(bubble);
				}
			}
		}

		for (int i = oddStartRow; i < boxes.length; i += 2) {
			for (int j = 0; j < boxes[i].length; j++) {
				BubbleGridRectangle bubble = boxes[i][j];
				if (bubble.isOccupied() && isHangingBubble(bubble)) {
					hangingBubbles.add(bubble);
				}
			}
		}
		return hangingBubbles;
	}

	private boolean isHangingBubble(BubbleGridRectangle bubble) {
		BubbleGridRectangle leftParent = getLeftParentOfBubble(bubble);
		BubbleGridRectangle rightParent = getRightParentOfBubble(bubble);
		BubbleGridRectangle leftSibling = getLeftSiblingOfBubble(bubble);
		BubbleGridRectangle rightSibling = getRightSiblingOfBubble(bubble);
		BubbleGridRectangle leftChild = getLeftChildOfBubble(bubble);
		BubbleGridRectangle rightChild = getRightChildOfBubble(bubble);

		if (leftParent == null || leftParent.isOccupied()) { // Is attached to
																// the top
			return false;
		}
		if (rightParent == null || rightParent.isOccupied()) { // Is attached to
																// the top
			return false;
		}
		if (leftSibling != null && leftSibling.isOccupied()) {
			return false;
		}
		if (rightSibling != null && rightSibling.isOccupied()) {
			return false;
		}
		if (leftChild != null && leftChild.isOccupied()) {
			return false;
		}
		if (rightChild != null && rightChild.isOccupied()) {
			return false;
		}
		return true;
	}

	public List<BubbleGridRectangle> getNeighboursOfBubble(BubbleGridRectangle ofBubble) {
		List<BubbleGridRectangle> res = new ArrayList<BubbleGridRectangle>();
		res.add(getLeftParentOfBubble(ofBubble));
		res.add(getRightParentOfBubble(ofBubble));
		res.add(getLeftSiblingOfBubble(ofBubble));
		res.add(getRightSiblingOfBubble(ofBubble));
		res.add(getLeftChildOfBubble(ofBubble));
		res.add(getRightChildOfBubble(ofBubble));
		return res;
	}

	public BubbleGridRectangle getLeftParentOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int leftParentX = (bcY % 2 == 0 ? bcX - 1 : bcX);
		int leftParentY = bcY - 1;

		if (!isInsideGridCoordinates(leftParentX, leftParentY))
			return null;
		return boxes[leftParentY][leftParentX];
	}

	public BubbleGridRectangle getRightParentOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int rightParentX = (bcY % 2 == 0 ? bcX : bcX + 1);
		int rightParentY = bcY - 1;

		if (!isInsideGridCoordinates(rightParentX, rightParentY))
			return null;
		return boxes[rightParentY][rightParentX];
	}

	public BubbleGridRectangle getLeftSiblingOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int leftSiblingX = bcX - 1;
		int leftSiblingY = bcY;

		if (!isInsideGridCoordinates(leftSiblingX, leftSiblingY))
			return null;
		return boxes[leftSiblingY][leftSiblingX];
	}

	public BubbleGridRectangle getRightSiblingOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int rightSiblingX = bcX + 1;
		int rightSiblingY = bcY;

		if (!isInsideGridCoordinates(rightSiblingX, rightSiblingY))
			return null;
		return boxes[rightSiblingY][rightSiblingX];
	}

	public BubbleGridRectangle getLeftChildOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int leftChildX = (bcY % 2 == 0 ? bcX - 1 : bcX);
		int leftChildY = bcY + 1;

		if (!isInsideGridCoordinates(leftChildX, leftChildY))
			return null;
		return boxes[leftChildY][leftChildX];
	}

	public BubbleGridRectangle getRightChildOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int rightChildX = (bcY % 2 == 0 ? bcX : bcX + 1);
		int rightChildY = bcY + 1;

		if (!isInsideGridCoordinates(rightChildX, rightChildY))
			return null;
		return boxes[rightChildY][rightChildX];
	}

	private boolean isValidBubble(BubbleGridRectangle bubble) {
		if (bubble == null)
			return false;
		if (!isInsideGridCoordinates(bubble.getCoordinateX(), bubble.getCoordinateY()))
			return false; // Bubble doesn't exist in grid
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

	public BubbleGridRectangle getBubbleAt(int coordinateX, int coordinateY) {
		return boxes[coordinateY][coordinateX];
	}

	public void insertNewRow() {
		moveRowsOneLine();
	}

	private void moveRowsOneLine() {
		for (int yIndex = boxes.length-1; yIndex >= 0; yIndex--) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length; xIndex++) {
				BubbleGridRectangle bubble = getBubbleAt(xIndex, yIndex);				
				bubble.setCoordinateY(bubble.getCoordinateY()+1);
			}
		}
	}

}
