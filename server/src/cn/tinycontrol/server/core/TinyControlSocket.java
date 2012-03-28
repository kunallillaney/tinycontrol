package cn.tinycontrol.server.core;

import cn.tinycontrol.server.requesthandler.ServerWorkerThread;

public class TinyControlSocket {
	private ServerWorkerThread workerThread;
	
	public TinyControlSocket(ServerWorkerThread workerThread) {
		this.workerThread = workerThread;
	}

	public void write(byte[] data,int offset, int length ) {
		workerThread.addData(data, offset, length);
		// If the thread is not started start the same
		if(!workerThread.isRunning()) {
			new Thread(workerThread).start();
		}
	}

	public void close() {
		workerThread.shutDown();
	}
}
