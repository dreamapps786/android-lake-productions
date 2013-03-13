package com.helpers.collision;

import com.helpers.BubbleGrid;
import com.model.BubbleGridRectangle;
import com.model.CollisionObject;

public class CollisionHelper {
	// Calculate the projection of a polygon on an axis
	// and returns it as a [min, max] interval
	public static float[] projectPolygon(Vector axis, Polygon polygon) {
	    // To project a point on an axis use the dot product
	    float d = axis.getDotProduct(polygon.getPoints().get(0));
	    float min = d;
	    float max = d;
	    for (int i = 0; i < polygon.getPoints().size(); i++) {
	        d = polygon.getPoints().get(i).getDotProduct(axis);
	        if (d < min) {
	            min = d;
	        } else {
	            if (d > max) {
	                max = d;
	            }
	        }
	    }
	    return new float[] {min, max};
	}
	
	// Calculate the distance between [minA, maxA] and [minB, maxB]
	// The distance will be negative if the intervals overlap
	public static float intervalDistance(float minA, float maxA, float minB, float maxB) {
	    if (minA < minB) {
	        return minB - maxA;
	    } else {
	        return minA - maxB;
	    }
	}
	
	// Check if polygon A is going to collide with polygon B for the given velocity
	public static PolygonCollisionResult polygonCollision(Polygon polygonA, Polygon polygonB, Vector velocity) {
		PolygonCollisionResult result = new PolygonCollisionResult();
		result.intersect = true;
		result.willIntersect = true;

		int edgeCountA = polygonA.getEdges().size();
		int edgeCountB = polygonB.getEdges().size();
		float minIntervalDistance = Float.POSITIVE_INFINITY;
		Vector translationAxis = new Vector();
		Vector edge;

		// Loop through all the edges of both polygons
		for (int edgeIndex = 0; edgeIndex < edgeCountA + edgeCountB; edgeIndex++) {
			if (edgeIndex < edgeCountA) {
				edge = polygonA.getEdges().get(edgeIndex);
			} else {
				edge = polygonB.getEdges().get(edgeIndex - edgeCountA);
			}

			// ===== 1. Find out if the polygons are currently intersecting =====

			// Find the axis perpendicular to the current edge
			Vector axis = new Vector(-edge.getY(), edge.getX());
			axis.normalize();

			// Find the projection of the polygon on the current axis
			float[] minAndMaxA = projectPolygon(axis, polygonA);
			float[] minAndMaxB = projectPolygon(axis, polygonB);
			float minA = minAndMaxA[0];
			float maxA = minAndMaxA[1];
			float minB = minAndMaxB[0];
			float maxB = minAndMaxB[1];

			// Check if the polygon projections are currently intersecting
			if (intervalDistance(minA, maxA, minB, maxB) > 0) result.intersect = false;

			// ===== 2. Now find out if the polygons *will* intersect =====

			// Project the velocity on the current axis
			float velocityProjection = axis.getDotProduct(Vector.mutliply(velocity, 2));

			// Get the projection of polygon A during the movement
			if (velocityProjection < 0) {
				minA += velocityProjection;
			} else {
				maxA += velocityProjection;
			}

			// Do the same test as above for the new projection
			float intervalDistance = intervalDistance(minA, maxA, minB, maxB);
			if (intervalDistance > 0) result.willIntersect = false;

			// If the polygons are not intersecting and won't intersect, exit the loop
			if (!result.intersect && !result.willIntersect) break;

			// Check if the current interval distance is the minimum one. If so store
			// the interval distance and the current distance.
			// This will be used to calculate the minimum translation vector
			intervalDistance = Math.abs(intervalDistance);
			if (intervalDistance < minIntervalDistance) {
				minIntervalDistance = intervalDistance;
				translationAxis = axis;

				Vector d = Vector.subtract(polygonA.getCenter(), polygonB.getCenter());
				if (d.getDotProduct(translationAxis) < 0) {
//					translationAxis = //TODO Skal dette inkluderes?
					Vector.subtract(translationAxis, translationAxis);
				}
			}
		}

		// The minimum translation vector can be used to push the polygons apart.
		// First moves the polygons by their velocity
		// then move polygonA by MinimumTranslationVector.
		if (result.willIntersect) {
            result.minimumTranslationVector = Vector.mutliply(translationAxis, minIntervalDistance);
        }

		return result;
	}

    public static PolygonCollisionResult bubbleCollision(Polygon polygonA, BubbleGridRectangle bubble, float bubbleWidth, Vector velocity) {
        Polygon polygonB = PolygonFactory.getPolygon(bubble.getX() + bubble.getWidth()/2,
                bubble.getY() + bubble.getHeight()/2,
                bubble.getHeight() / 2, 0, BubbleGrid.sidesOfCollisionBubbles);
        PolygonCollisionResult res = polygonCollision(polygonA, polygonB, velocity);

        if (res.intersect() || res.willIntersect()) {
            res.collidingBubble = bubble;

            Vector centerA = polygonA.getCenter();
            Vector centerB = polygonB.getCenter();

            Vector touchingBubbleCenter;
            if (res.willIntersect()) {
                System.out.println("willIntersect");
                touchingBubbleCenter = Vector.add(centerA, res.minimumTranslationVector);
            } else {
                System.out.println("intersect");
                touchingBubbleCenter = centerA;
            }

            Vector touchRelation = Vector.subtract(touchingBubbleCenter, centerB);

            float x = touchRelation.getX();
            float limit = (bubbleWidth * 2)/3f;
            if (x < -limit) {
                res.collDirection = Direction.E;
            } else if (x < 0) {
                res.collDirection = Direction.NE;
            } else if (x < limit) {
                res.collDirection = Direction.NW;
            } else {
                res.collDirection = Direction.W;
            }
            return res;
        }

        return null;
    }
	// Structure that stores the results of the PolygonCollision function
	public static class PolygonCollisionResult {
	    // Are the polygons going to intersect forward in time?
	    private boolean willIntersect;
	    // Are the polygons currently intersecting?
	    private boolean intersect;
	    // The translation to apply to the first polygon to push the polygons apart.
	    private Vector minimumTranslationVector;
        //The direction the moving ball collided with the grid-ball.
        private Direction collDirection = Direction.NONE;

        private BubbleGridRectangle collidingBubble;
	    
		public boolean willIntersect() {
			return willIntersect;
		}
		public boolean intersect() {
			return intersect;
		}
		public Vector getMinimumTranslationVector() {
			return minimumTranslationVector;
		}

        public Direction getCollDirection() {
            return collDirection;
        }

        public BubbleGridRectangle getCollidingBubble(){
            return collidingBubble;
        }

        @Override
        public String toString() {
            return collidingBubble + " (" + collDirection + ") " + (intersect() ? "I" : (willIntersect() ? "wI" : ""));
        }
    }

    public enum Direction {
        NONE, W, NW, N, NE, E, SE, S, SW
    }
}
