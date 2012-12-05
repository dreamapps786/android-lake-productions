package com.helpers.collision;

public class CollisionHelper {
	// Calculate the projection of a polygon on an axis
	// and returns it as a [min, max) interval
	public float[] projectPolygon(Vector axis, Polygon polygon) {
	    // To project a point on an axis use the dot product
	    float d = axis.getDotProduct(polygon.getPoints().get(0));
	    float min = d;
	    float max = d;
	    for (int i = 0; i < polygon.getPoints().size(); i++) {
	        d = polygon.getPoints().get(i).getDotProduct(axis);
	        if (d < min) {
	            min = d;
	        } else {
	            if (d> max) {
	                max = d;
	            }
	        }
	    }
	    return new float[] {min, max};
	}
	
	// Calculate the distance between [minA, maxA) and [minB, maxB)
	// The distance will be negative if the intervals overlap
	public float IntervalDistance(float minA, float maxA, float minB, float maxB) {
	    if (minA < minB) {
	        return minB - maxA;
	    } else {
	        return minA - maxB;
	    }
	}
	
	// Check if polygon A is going to collide with polygon B for the given velocity
	public PolygonCollisionResult PolygonCollision(Polygon polygonA, Polygon polygonB, Vector velocity) {
		PolygonCollisionResult result = new PolygonCollisionResult();
		result.Intersect = true;
		result.WillIntersect = true;

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

			// ===== 1. Find if the polygons are currently intersecting =====

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

			// Check if the polygon projections are currentlty intersecting
			if (IntervalDistance(minA, maxA, minB, maxB) > 0) result.Intersect = false;

			// ===== 2. Now find if the polygons *will* intersect =====

			// Project the velocity on the current axis
			float velocityProjection = axis.getDotProduct(velocity);

			// Get the projection of polygon A during the movement
			if (velocityProjection < 0) {
				minA += velocityProjection;
			} else {
				maxA += velocityProjection;
			}

			// Do the same test as above for the new projection
			float intervalDistance = IntervalDistance(minA, maxA, minB, maxB);
			if (intervalDistance > 0) result.WillIntersect = false;

			// If the polygons are not intersecting and won't intersect, exit the loop
			if (!result.Intersect && !result.WillIntersect) break;

			// Check if the current interval distance is the minimum one. If so store
			// the interval distance and the current distance.
			// This will be used to calculate the minimum translation vector
			intervalDistance = Math.abs(intervalDistance);
			if (intervalDistance < minIntervalDistance) {
				minIntervalDistance = intervalDistance;
				translationAxis = axis;

				Vector d = Vector.subtract(polygonA.getCenter(), polygonB.getCenter());
				if (d.getDotProduct(translationAxis) < 0) Vector.subtract(translationAxis, translationAxis);
			}
		}

		// The minimum translation vector can be used to push the polygons appart.
		// First moves the polygons by their velocity
		// then move polygonA by MinimumTranslationVector.
		if (result.WillIntersect) result.MinimumTranslationVector = Vector.mutliply(translationAxis, minIntervalDistance);
		
		return result;
	}
	
	// Structure that stores the results of the PolygonCollision function
	public class PolygonCollisionResult {
	    // Are the polygons going to intersect forward in time?
	    private boolean WillIntersect;
	    // Are the polygons currently intersecting?
	    private boolean Intersect;
	    // The translation to apply to the first polygon to push the polygons apart.
	    private Vector MinimumTranslationVector;
	    
		public boolean willIntersect() {
			return WillIntersect;
		}
		public boolean intersect() {
			return Intersect;
		}
		public Vector getMinimumTranslationVector() {
			return MinimumTranslationVector;
		}
	}
}
