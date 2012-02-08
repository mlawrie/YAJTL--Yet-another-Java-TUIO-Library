package com.mlawrie.yajtl;



public class TUIOCursor {
	protected long id;
	private float x, y;
	protected boolean alive;
	
	public boolean alive() {
		return alive;
	}
	
	public void alive(boolean b) {
		alive =b;
	}
	public long id() {
		return id;
	}
	
	public void id(long i) {
		id = i;
	}
	
	public float x() {
		return x;
	}
	
	public float y() {
		return y;
	}
	
	public TUIOCursor() {
		x = 0;
		y = 0;
	}
	
	public TUIOCursor(float _x, float _y, long _id) {
		x = _x;
		y = _y;
		id = _id;
	}
	
	public void signalMoved(float _x, float _y) {
		x = _x;
		y = _y;
	
	}
	
	public String toString() {
		return "(id: " + id + ", x: " + x + ", y: " + y +")";
	}
}

