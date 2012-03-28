package cn.tinycontrol.server.core;

import cn.tinycontrol.server.requesthandler.ServerWorkerThread;

public class TinyControlSocket {
	
	private WorkerThread workerThread;
	
	class WorkerThread extends Thread {
		protected ServerWorkerThread workerImpl;
		
		public WorkerThread(ServerWorkerThread workerImpl) {
			this.workerImpl = workerImpl;
		}
	}
	
	public TinyControlSocket(ServerWorkerThread workerImpl) {
		this.workerThread = new WorkerThread(workerImpl);
	}

	public void write(byte[] data,int offset, int length ) {
		workerThread.workerImpl.addData(data, offset, length);
		// If the thread is not started start the same
		if(!workerThread.isAlive()) {
			workerThread.start();
		}
	}

	public void close() {
		workerThread.workerImpl.shutDown();
	}
}
