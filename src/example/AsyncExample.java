package example;

import java.net.SocketException;

import com.mlawrie.yajtl.TUIOCursor;
import com.mlawrie.yajtl.TUIOEvent;
import com.mlawrie.yajtl.TUIOReceiver;

/*
 * Example of YAJTL with events firing asynchronously (not in the main thread).
 * Even though the main thread sleeps for 1000ms at a time, events fire immediately when they're received.
 */
public class AsyncExample {
	
	public static void main(String args[]) throws SocketException, InterruptedException {
		TUIOReceiver tuio = new TUIOReceiver(800,600);
		
		TUIOEvent myEventHandler = new TUIOEvent() {

			@Override
			public void moveCursorEvent(TUIOCursor c) {
				System.out.println("Move cursor: " + c);
			}

			@Override
			public void newCursorEvent(TUIOCursor c) {
				System.out.println("New cursor: " + c);
			}

			@Override
			public void removeCursorEvent(TUIOCursor c) {
				System.out.println("Remove cursor: " + c);
			}
			
		};
		
		tuio.setHandler(myEventHandler);
		
		while (true) {
			//Do some stuff
			Thread.sleep(1000);
		}
	}

}
