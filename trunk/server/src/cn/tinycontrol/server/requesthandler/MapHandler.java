package cn.tinycontrol.server.requesthandler;

import java.net.InetAddress;
import java.util.HashMap;

import cn.tinycontrol.server.core.model.ClientConnectionInfo;

public class MapHandler {
	
	private static MapHandler _instance = null;
	
	HashMap<ClientConnectionInfo, ServerWorkerThread> record = new HashMap<ClientConnectionInfo, ServerWorkerThread>();
	public void add(InetAddress IP, int port,ServerWorkerThread thread) {
		ClientConnectionInfo key = new ClientConnectionInfo();
		key.IPAddress=IP;
		key.port=port;
		record.put(key,thread);
	}
	
	public void remove(InetAddress IP, int port) {
		ClientConnectionInfo key = new ClientConnectionInfo();
		key.IPAddress=IP;
		key.port=port;
		record.remove(key);
	}
	
	public  ServerWorkerThread get(InetAddress IP, int port) {
		ClientConnectionInfo key = new ClientConnectionInfo();
		key.IPAddress=IP;
		key.port=port;
		return record.get(key);
	}
	
	public synchronized static MapHandler getInstance(){
		if(_instance == null) {
			_instance = new MapHandler();
		}
		return _instance;
	}
}