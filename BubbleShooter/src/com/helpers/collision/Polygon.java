package com.helpers.collision;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

	private List<Vector> points = new ArrayList<Vector>();
	private List<Vector> edges = new ArrayList<Vector>();

	public void BuildEdges() {
		Vector p1;
		Vector p2;
		edges.clear();
		for (int i = 0; i < points.size(); i++) {
			p1 = points.get(i);
			if (i + 1 >= points.size()) {
				p2 = points.get(0);
			} else {
				p2 = points.get(i + 1);
			}
			edges.add(Vector.subtract(p2,p1));
		}
	}

	public List<Vector> getEdges() {
		return edges;
	}

	public List<Vector> getPoints() {
		return points;
	}

	public Vector getCenter() {
		float totalX = 0;
		float totalY = 0;
		for (int i = 0; i < points.size(); i++) {
			totalX += points.get(i).getX();
			totalY += points.get(i).getY();
		}

		return new Vector(totalX / (float)points.size(), totalY / (float)points.size());
	}

	public void Offset(Vector v) {
		Offset(v.getX(), v.getY());
	}

	public void Offset(float x, float y) {
		for (int i = 0; i < points.size(); i++) {
			Vector p = points.get(i);
			points.set(i, new Vector(p.getX() + x, p.getY() + y));
		}
	}
	
	@Override
	public String toString() {
		String result = "";

		for (int i = 0; i < points.size(); i++) {
			if (result != "") result += " ";
			result += "{" + points.get(i).toString(true) + "}";
		}

		return result;
	}
}
