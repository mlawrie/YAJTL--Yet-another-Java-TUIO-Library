package com.mlawrie.yajtl;

public interface TUIOEvent {

	void newCursorEvent(TUIOCursor c);
	void removeCursorEvent(TUIOCursor c);
	void moveCursorEvent(TUIOCursor c);
	
}
