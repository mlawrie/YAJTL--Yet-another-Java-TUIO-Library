package com.mlawrie.yajtl;
import java.net.SocketException;

import com.illposed.osc.*;
import java.util.*;



/*
 * Partial implementation of TUIO. Only utilizes to 2Dcur (cursor/finger/etc) messages, not the object messages used
 * in the Reactivision/Reactable project.
 */
public class TUIOReceiver {
	
	
	private OSCPortIn reciever;
	private OSCListener listener;
	protected Vector<TUIOCursor> cursors;
	
	int width, height;
	private int port;
	
	protected TUIOEvent eventHandler = null;
	public void setHandler(TUIOEvent te) {
		eventHandler = te;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getPort() {
		return port;
	}
	
	public TUIOReceiver(int w, int h) throws SocketException {
		init(w, h, 3333);
	}
	

	public TUIOReceiver(int screenWidth, int screenHeight, int _port) throws SocketException {
		init(screenWidth, screenHeight, _port);
	}
	
	protected void init(int sw, int sh, int _port) throws SocketException {
		cursors = new Vector<TUIOCursor>();
		System.out.println("TUIO: (" + sw + "x" + sh +") " + port);
		port = _port;
		width = sw;
		height = sh;
		
		reciever = new OSCPortIn(port);
		listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				Object[] args = message.getArguments();
				String command = (String)args[0];
				String address = message.getAddress();
				if (address.equals("/tuio/2Dcur")) {
					if (command.equals("set"))
						setCurCmd(args);
					
					if (command.equals("alive")) {
						aliveCurCmd(args);
					}
					if (command.equals("fseq"))
						fseqCurCmd(args);
				}
			}
		};
		
		reciever.addListener("/tuio/2Dobj",listener);
		reciever.addListener("/tuio/2Dcur",listener);
		
		reciever.startListening();

		System.out.print("started.");
		
	}
	
	
	private void setCurCmd(Object[] args) {
		synchronized (cursors) {
		long id  = ((Integer)args[1]).longValue();
		float x = ((Float)args[2]).floatValue() * width;
		float y = ((Float)args[3]).floatValue() * height;
	
		boolean found = false;
		for (Enumeration<TUIOCursor> e = cursors.elements(); e.hasMoreElements();) {
			TUIOCursor cur = e.nextElement();
			if (cur.id() == id) {
				if (cur.x() != x || cur.y() != y) {
					cur.signalMoved(x, y);
					if (eventHandler != null)
						eventHandler.moveCursorEvent(cur);
					
				} else {
					cur.signalMoved(x, y);
				}
				found = true;
				break;
			}
		}
		
			if (!found) {
				TUIOCursor cur = new TUIOCursor(x,y,id);
				
				cursors.add(cur);
				if (eventHandler != null)
					eventHandler.newCursorEvent(cur);
				
			}
		}
	}
	
	private void aliveCurCmd(Object[] args) {
		synchronized (cursors) {
		for (Enumeration<TUIOCursor> e = cursors.elements(); e.hasMoreElements();) {
			e.nextElement().alive(false);
		}
		
		for (int i=1; i<args.length; i++) {
			
			long id = ((Integer)args[i]).longValue();
			for (Enumeration<TUIOCursor> e = cursors.elements(); e.hasMoreElements();) {
				TUIOCursor cur = e.nextElement();
				if (cur.id() == id) {
					cur.alive(true);
				}
			}
		}
		
		for (Enumeration<TUIOCursor> e = cursors.elements(); e.hasMoreElements();) {
			TUIOCursor cur = e.nextElement();
			if (!cur.alive()) {
				if (eventHandler != null)
					eventHandler.removeCursorEvent(cur);
				cursors.remove(cur);
			}
			
		}
	}

	}
	
	private void fseqCurCmd(Object[] args) {
		//Nothing for now..
		//TODO: Implement this method.
	}

	public List<TUIOCursor> getCursorList() {
		return cursors;
	}
	
	/* Creates a new cursor. Override this method in order to use your own TUIOCursor class. */
	protected TUIOCursor createNewCursor(float x, float y, long id) {
		return new TUIOCursor(x,y,id);
	}

	
}
