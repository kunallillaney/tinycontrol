package cn.tinycontrol.server.core;

import cn.tinycontrol.server.requesthandler.ServerWorkerThread;

public class TinyControlSocket {
	
	private ServerWorkerThread workerThread;

	public TinyControlSocket(ServerWorkerThread workerThread) {
		this.workerThread = workerThread;
	}

	public void write(byte[] data,int offset, int length ) {
		//workerRunnableThread.workerThread.addData(data, offset, length);
		// If the thread is not started start the same when the first write is called.
		if(!workerThread.isStarted) {
			new Thread(workerThread).start();
			workerThread.isStarted = true;
		} else {
			
		}
		
	}

	public void close() {
		workerThread.shutDown();
	}
}
