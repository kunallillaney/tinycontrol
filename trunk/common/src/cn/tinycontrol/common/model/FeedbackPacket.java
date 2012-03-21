package cn.tinycontrol.common.model;

public class FeedbackPacket {

	private int timeStamp;
	private int elapsedTime;
	private float receiveRate;
	private float lossEventRate;
	
	public int getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public float getReceiveRate() {
		return receiveRate;
	}
	public void setReceiveRate(float receiveRate) {
		this.receiveRate = receiveRate;
	}
	public float getLossEventRate() {
		return lossEventRate;
	}
	public void setLossEventRate(float lossEventRate) {
		this.lossEventRate = lossEventRate;
	}
	
}
