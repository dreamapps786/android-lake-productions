package com.model;

import com.helpers.BubbleGrid.BubbleGridRectangle;

public class CollissionObject {
	private BubbleGridRectangle collidingBubble;
	public BubbleGridRectangle getCollidingBubble() {
		return collidingBubble;
	}

	public void setCollidingBubble(BubbleGridRectangle collidingBubble) {
		this.collidingBubble = collidingBubble;
	}

	public Direction[] getCollissionDirection() {
		return collissionDirection;
	}

	private Direction[] collissionDirection = new Direction[2];

	public CollissionObject(BubbleGridRectangle collidingBubble, Direction direction1, Direction direction2) {
		this.collidingBubble = collidingBubble;
		this.collissionDirection[0] = direction1;
		this.collissionDirection[1] = direction2;
	}

	public enum Direction {
		W, NW, N, NE, E, SE, S, SW
	}
}
