//Application on top o TinyCLient
public class TinyControlClient {

	public static void main(String argv[]) throws Exception {

		//TinyClientSocket clientSocket = new TinyClientSocket("localhost", 9876);
		TinyClientSocket clientSocket = new TinyClientSocket("192.168.137.1", 9876);
		byte[] tinyData = new byte[10120];
		clientSocket.TinyClientReceive(tinyData, tinyData.length);
		clientSocket.TinyClientClose();
	}

}
