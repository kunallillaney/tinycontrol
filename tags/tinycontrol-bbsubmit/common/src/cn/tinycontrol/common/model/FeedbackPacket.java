package cn.tinycontrol.common.model;

import java.net.DatagramPacket;
import java.net.InetAddress;

import cn.tinycontrol.common.parser.TypeByteUtil;

public class FeedbackPacket {

	public static int PACKET_LENGTH = 16;
	
	private int timeStamp;
	private int elapsedTime;
	private float receiveRate;
	private float lossEventRate;
	
	private DatagramPacket udpPacket;
	
	public FeedbackPacket(int timeStamp, int elapsedTime, float receiveRate, float lossEventRate) {
		this.timeStamp = timeStamp;
		this.elapsedTime = elapsedTime;
		this.receiveRate = receiveRate;
		this.lossEventRate = lossEventRate;
	}
	
	public FeedbackPacket(DatagramPacket udpPacket) {
		this.udpPacket = udpPacket;
		byte[] feedbackBytes = udpPacket.getData();
		this.setTimeStamp(TypeByteUtil.toInt(feedbackBytes , 0));
		this.setElapsedTime(TypeByteUtil.toInt(feedbackBytes, 4));
		this.setReceiveRate(TypeByteUtil.toFloat(feedbackBytes, 8));
		this.setLossEventRate(TypeByteUtil.toFloat(feedbackBytes, 12));
	}
	
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
	
	public int getPort() {
		return udpPacket.getPort();
	}
	
	public InetAddress getSourceAddr() {
		return udpPacket.getAddress();
	}	
	
	public byte[] constructBytes() {
		byte[] fPacket = new byte[PACKET_LENGTH];
		TypeByteUtil.toByta(this.getTimeStamp(), fPacket, 0);
		TypeByteUtil.toByta(this.getElapsedTime(), fPacket, 4);
		TypeByteUtil.toByta(this.getReceiveRate(), fPacket, 8);
		TypeByteUtil.toByta(this.getLossEventRate(), fPacket, 12);
		return fPacket;		
	}

	@Override
	public String toString() {
		return "FeedbackPacket [timeStamp=" + timeStamp + ", elapsedTime="
				+ elapsedTime + ", receiveRate=" + receiveRate
				+ ", lossEventRate=" + lossEventRate + "]";
	}
	
}
