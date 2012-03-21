package cn.tinycontrol.common.model;

public class DataPacket {

	private int sequenceNumber;
	private int timeStamp;
	private int RTT;
	private byte[] payLoad;
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public int getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getRTT() {
		return RTT;
	}
	public void setRTT(int rTT) {
		RTT = rTT;
	}
	public byte[] getPayLoad() {
		return payLoad;
	}
	public void setPayLoad(byte[] payLoad) {
		this.payLoad = payLoad;
	}
}
