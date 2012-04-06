package cn.tinycontrol.server.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import cn.tinycontrol.server.core.TinyControlServerSocket;
import cn.tinycontrol.server.core.TinyControlSocket;

public class ServerTest {
	public static void main(String[] args) {
		TinyControlServerSocket servsock;
		try {
			servsock = new TinyControlServerSocket(9876);
			File myFile = new File("C:\\MyFile.txt");
			byte[] mybytearray = new byte[(int) myFile.length()];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
			bis.read(mybytearray, 0, mybytearray.length);
			
			TinyControlSocket sock = servsock.accept();
			//boolean condition = true;
			int count = 0;
			//while (count  < (int)(2000*1000*1000*1000/mybytearray.length)) {
			while (count  < 66) {	
			    sock.write(mybytearray, 0, mybytearray.length);
				count++;
				//Thread.sleep(100);
			}
			sock.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
