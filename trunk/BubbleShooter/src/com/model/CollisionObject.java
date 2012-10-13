package com.model;

import java.util.ArrayList;
import java.util.List;

import com.helpers.BubbleGrid.BubbleGridRectangle;

public class CollisionObject {
	private BubbleGridRectangle collidingBubble;

	public BubbleGridRectangle getCollidingBubble() {
		return collidingBubble;
	}

	public void setCollidingBubble(BubbleGridRectangle collidingBubble) {
		this.collidingBubble = collidingBubble;
	}

	public List<Direction> getCollissionDirections() {
		return collissionDirection;
	}

	private List<Direction> collissionDirection;

	public CollisionObject(BubbleGridRectangle collidingBubble,
			List<Direction> directions) {
		this.collidingBubble = collidingBubble;
		this.collissionDirection = new ArrayList<CollisionObject.Direction>(directions);
	}

	public enum Direction {
		W, NW, N, NE, E, SE, S, SW
	}
}
