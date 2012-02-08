package com.mlawrie.yajtl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 *
 * @author michaellawrie
 * Collects asynchronous TUIO events and dispatches them in the main thread during the logic() call to avoid threading issues for simple uses of TUIO.
 */

public class SingleThreadTUIOWrapper {
	TUIOReceiver tuio;
	ArrayList<WeakReference<TUIOEvent>> handlers = new ArrayList<WeakReference<TUIOEvent>>();
	TUIOEvent asyncEvents;
	
	public SingleThreadTUIOWrapper(TUIOReceiver tuio) {
		this.tuio = tuio;

		//TODO: Verify asyncEvents will be available for gc when appropriate.
		asyncEvents = new TUIOEvent() {
		
			public void moveCursorEvent(TUIOCursor c) {
				synchronized (events) {
					
					events.add(new Event(c, Event.MOVE));
				}
			}
			
			public void newCursorEvent(TUIOCursor c) {
				synchronized (events) {
					events.add(new Event(c, Event.NEW));
				}	
			}
			
			public void removeCursorEvent(TUIOCursor c) {
				synchronized (events) {
					events.add(new Event(c, Event.REMOVE));
				}
			}
		};
		
		
		tuio.setHandler(asyncEvents);
	}
	
	TUIOEvent singleRecipient = null;
	
	public void setSingleRecipient(TUIOEvent e) {
		singleRecipient = e;
	}
	
	public void unsetSingleRecipient() {
		singleRecipient = null;
	}

	
	
	public boolean removeHandler(TUIOEvent e) {
		try {
			WeakReference<TUIOEvent> removable = null;
			for (WeakReference<TUIOEvent> ref : handlers) {
				if (ref.get() == e) {
					removable = ref;
					break;
				}
			}
			handlers.remove(removable);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
	
	public boolean addHandler(TUIOEvent e) {
		if (handlers.contains(e))
			return false;
		
		handlers.add(new WeakReference<TUIOEvent>(e));
		return true;
	}
	
	ArrayList<Event> events = new ArrayList<Event>();
	

	public void logic() {
		synchronized (events) {
			for (Event e : events) {
				try {
					if (singleRecipient == null)
						for (WeakReference<TUIOEvent> handlerRef : handlers) {
							TUIOEvent handler = handlerRef.get();
							switch (e.type) {
							case Event.MOVE:
								handler.moveCursorEvent(e.c);
								break;
							case Event.NEW:
								handler.newCursorEvent(e.c);
								break;
							case Event.REMOVE:
								handler.removeCursorEvent(e.c);
								break;
							}
						}
					else
						switch (e.type) {
							case Event.MOVE:
								singleRecipient.moveCursorEvent(e.c);
								break;
							case Event.NEW:
								singleRecipient.newCursorEvent(e.c);
								break;
							case Event.REMOVE:
								singleRecipient.removeCursorEvent(e.c);
								break;
						}
				} catch (NullPointerException ee) {
					//Do nothing!
				}
			}
			events.clear();
		}
	}
}

class Event {
	TUIOCursor c;
	int type;
	
	public Event(TUIOCursor c, int type) {
		this.c = c;
		this.type = type;
	}
	
	static final int MOVE = 0, NEW = 1, REMOVE = 2;
}