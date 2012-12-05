package com.helpers.collision;

public class Vector {

	public Float x;
	public Float y;

	public static Vector fromPoint(Point p) {
		return Vector.fromPoint(p.getX(), p.getY());
	}

	public static Vector fromPoint(float x, float y) {
		return new Vector(x, y);
	}
	
	public Vector() {
		//Empty constructor
	}

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getMagnitude() {
		return (float)Math.sqrt(x * x + y * y);
	}

	public void normalize() {
		float magnitude = getMagnitude();
		x = x / magnitude;
		y = y / magnitude;
	}

	public Vector getNormalized() {
		float magnitude = getMagnitude();

		return new Vector(x / magnitude, y / magnitude);
	}

	public float getDotProduct(Vector vector) {
		return this.x * vector.x + this.y * vector.y;
	}

	public float getDistanceTo(Vector vector) {
		return (float)Math.sqrt(Math.pow(vector.x - this.x, 2) + Math.pow(vector.y - this.y, 2));
	}
//	public static implicit operator Point(Vector p) {
//		return new Point((int)p.X, (int)p.Y);
//	}
//
//	public static implicit operator PointF(Vector p) {
//		return new PointF(p.X, p.Y);
//	}
//	//Adding a vector to another
//	public static Vector operator +(Vector a, Vector b) {
//		return new Vector(a.X + b.X, a.Y + b.Y);
//	}
//	//Inverting
//	public static Vector operator -(Vector a) {
//		return new Vector(-a.X, -a.Y);
//	}
//
//	public static Vector operator -(Vector a, Vector b) {
//		return new Vector(a.X - b.X, a.Y - b.Y);
//	}
	public static Vector subtract(Vector v, Vector w) {
		return new Vector(v.getX() - w.getX(), v.getY() - w.getY());
	}
//
//	public static Vector operator *(Vector a, float b) {
//		return new Vector(a.X * b, a.Y * b);
//	}
	public static Vector mutliply(Vector v, float factor) {
		return new Vector(v.getX() * factor, v.getY() * factor);
	}
//
//	public static Vector operator *(Vector a, int b) {
//		return new Vector(a.X * b, a.Y * b);
//	}
//
//	public static Vector operator *(Vector a, double b) {
//		return new Vector((float)(a.X * b), (float)(a.Y * b));
//	}
	
	@Override
	public boolean equals(Object obj) {
		Vector v = (Vector)obj;

		return x == v.x && y == v.y;
	}
	
	public boolean equals(Vector v) {
		return x == v.x && y == v.y;
	}
	
	@Override
	public int hashCode() {
		return x.hashCode() ^ y.hashCode();
	}

//	public static boolean operator ==(Vector a, Vector b) {
//		return a.X == b.X && a.Y == b.Y;
//	}
//
//	public static boolean operator !=(Vector a, Vector b) {
//		return a.X != b.X || a.Y != b.Y;
//	}
	
	@Override
	public String toString() {
		return x + ", " + y;
	}
	public String toString(boolean rounded) {
		if (rounded) {
			return (int)Math.round(x) + ", " + (int)Math.round(y);
		} else {
			return toString();
		}
	}

	public Float getX() {
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	public Float getY() {
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}
	
	private class Point {
		private int x;
		private int y;

		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
	}
	
	private class PointF {
		private float x;
		private float y;

		public float getX() {
			return x;
		}
		public void setX(float x) {
			this.x = x;
		}
		public float getY() {
			return y;
		}
		public void setY(float y) {
			this.y = y;
		}
	}
}
