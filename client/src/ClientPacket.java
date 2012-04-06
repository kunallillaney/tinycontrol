import cn.tinycontrol.common.model.DataPacket;


public class ClientPacket {

	private DataPacket dataPacket;
	private long time;
	
	
	public ClientPacket(DataPacket sample) {

		dataPacket=sample;
		time = TinyClientSocket.startTime - System.currentTimeMillis();
	
	}

	public DataPacket getDataPacket() {
		return dataPacket;
	}

	public void setDataPacket(DataPacket dataPacket) {
		this.dataPacket = dataPacket;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
}
