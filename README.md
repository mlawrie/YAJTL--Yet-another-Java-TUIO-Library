### YAJTL: Yet Another Java TUIO Library

YAJTL is a basic, easy to use TUIO library that is **NOT licensed with GPL or other licenses unfreindly to commercial work**. It only supports the 2DCur commands but very few projects require anything else.
The reference TUIO Java implementation is GPL-licensed, which makes it hard for people like me to write TUIO Java applications
that we intend to use commercially or license under something other than the GPL. Instead, this project is licensed under the permissive MIT license.

Beyond that, it is probably not as good as the reference implementation, though I really don't know as I have never used it.

----

### It is easy to use

Here is all the code needed to use it. Simple, huh?

	TUIOReceiver tuio = new TUIOReceiver(1024,768);
	tuio.setHandler(new TUIOEvent() {

		@Override
		public void moveCursorEvent(TUIOCursor c) {
			//Do something
		}

		@Override
		public void newCursorEvent(TUIOCursor c) {
			//Do something
		}

		@Override
		public void removeCursorEvent(TUIOCursor c) {
			//Do something
		}
	
	});


### Asynchronous vs Synchronous Use

In order to facilitate use of this library in simple uses like Processing, I've created a `SingleThreadTUIOWrapper` class. Since YAJTL by default operates in its own thread
and fires events anychronously, one must account for this asynchronous nature by offering some sort of thread safety in one's code. If that is not desired,
`SingleThreadTUIOWrapper` is the solution. The server still runs in its own thread, but events are fired from a special logic function when it is called. Typically, this logic function
should be called once per frame in your application. If you don't know what I am talking about, use the wrapper. You can see how it is used in `src/example/SyncExample.java`.

### Using in your own projects

Grab the jar from the `build/` folder and go to it. Check out the examples in `examples/`.
