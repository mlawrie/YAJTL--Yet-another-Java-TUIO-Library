package example;

import java.net.SocketException;

import com.mlawrie.yajtl.SingleThreadTUIOWrapper;
import com.mlawrie.yajtl.TUIOCursor;
import com.mlawrie.yajtl.TUIOEvent;
import com.mlawrie.yajtl.TUIOReceiver;

/*
 * Example of YAJTL with events firing synchronously (in the main thread only; suitable for applications where the gotchas of multithreading are not desired).
 * In this case, events are punctuated by 1000ms delays in the main thread.
 */
public class SyncExample {
	
	public static void main(String args[]) throws SocketException, InterruptedException {
		SingleThreadTUIOWrapper tuio = new SingleThreadTUIOWrapper(new TUIOReceiver(800,600));
		
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
		
		tuio.addHandler(myEventHandler);
		
		while (true) {
			//Do some stuff
			Thread.sleep(1000);
			
			//Call tuio logic update
			System.out.println("***** Calling tuio logic  *****");
			tuio.logic();
			System.out.println("***** Finished tuio logic *****");
			System.out.println();
			System.out.println();
		}
	}

}
