package cn.tinycontrol.server.test;

import java.io.IOException;
import java.net.SocketException;

import cn.tinycontrol.server.core.TinyControlServerSocket;
import cn.tinycontrol.server.core.TinyControlSocket;

public class ServerTest {
	public static void main(String[] args) {
		TinyControlServerSocket servsock;
		try {
			servsock = new TinyControlServerSocket(9876);
			while(true) {
				TinyControlSocket sock = servsock.accept();
				//boolean condition = true;
				HandleConnectionThread htc = new HandleConnectionThread(sock);
				htc.start();
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
