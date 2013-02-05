package com.helpers.collision;

public class PolygonFactory {
	public static Polygon getPolygon(float centerX, float centerY, float radius, double direction, int sides) {
		if (sides < 3) {
			throw new RuntimeException("The polygon cannot have less than 3 sides");
		}
		
		Polygon result = null;
		
		float[] xPoints = new float[sides];
		float[] yPoints = new float[sides];

		double angD = 360d / (sides * 2) - 90;
		
		for (int i = 0; i < sides; i++) {
			double angR = Math.toRadians(angD);
			xPoints[i] = (float) ((radius * Math.cos(angR)) + centerX);
			yPoints[i] = (float) ((radius * Math.sin(angR)) + centerY);
			
			angD += 360d / sides;
		}
		
		//TODO Rotate the points
		float[] rotXPoints = new float[sides];
		float[] rotYPoints = new float[sides];
		
		double cosDir = Math.cos(Math.toRadians(direction));
		double sinDir = Math.sin(Math.toRadians(direction));
		
		for (int i = 0; i < sides; i++) {
			double xTranslated = xPoints[i] - centerX;
			double yTranslated = yPoints[i] - centerY;
			
			double xRotated = xTranslated * cosDir - yTranslated * sinDir;
			double yRotated = xTranslated * sinDir + yTranslated * cosDir;
			
			rotXPoints[i] = (float) (xRotated + centerX);
			rotYPoints[i] = (float) (yRotated + centerY);
		}
		
		result = new Polygon();
		
		for (int i = 0; i < sides; i++) {
			result.addPoint(new Vector(rotXPoints[i], rotYPoints[i]));
		}
		result.BuildEdges();
		
		return result;
	}
}
