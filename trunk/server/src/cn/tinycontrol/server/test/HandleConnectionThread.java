package cn.tinycontrol.server.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.tinycontrol.server.core.TinyControlSocket;

public class HandleConnectionThread extends Thread {
	
	TinyControlSocket sock; 
	
	public HandleConnectionThread(TinyControlSocket sock) {
		this.sock = sock;
	}
	
	@Override
	public void run() {
	
		File myFile = new File("C:\\MyFile.txt");
		byte[] mybytearray = new byte[(int) myFile.length()];
		BufferedInputStream bis;
		try {
			bis = new BufferedInputStream(new FileInputStream(myFile));
			bis.read(mybytearray, 0, mybytearray.length);
			int count = 0;
			//while (count  < (int)(2000*1000*1000*1000/mybytearray.length)) {
			while (count  < 66) {	
			    sock.write(mybytearray, 0, mybytearray.length-1);
				count++;
				//Thread.sleep(100);
			}
			sock.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
