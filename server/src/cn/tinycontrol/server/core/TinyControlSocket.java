package cn.tinycontrol.server.core;

import cn.tinycontrol.server.requesthandler.ServerWorkerThread;

public class TinyControlSocket {
	
	private WorkerRunnableThread workerRunnableThread;
	
	class WorkerRunnableThread extends Thread {
		protected ServerWorkerThread workerThread;
		
		public WorkerRunnableThread(ServerWorkerThread workerThread) {
			this.workerThread = workerThread;
		}
	}
	
	public TinyControlSocket(ServerWorkerThread workerThread) {
		this.workerRunnableThread = new WorkerRunnableThread(workerThread);
	}

	public void write(byte[] data,int offset, int length ) {
		workerRunnableThread.workerThread.addData(data, offset, length);
		// If the thread is not started start the same when the first write is called.
		if(!workerRunnableThread.isAlive()) {
			workerRunnableThread.start();
		}
	}

	public void close() {
		workerRunnableThread.workerThread.shutDown();
	}
}
