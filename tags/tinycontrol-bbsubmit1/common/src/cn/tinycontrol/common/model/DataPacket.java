package cn.tinycontrol.common.model;

import java.net.DatagramPacket;
import java.net.InetAddress;

import cn.tinycontrol.common.parser.TypeByteUtil;

public class DataPacket {
	
	public static int PACKET_LENGTH = 1012;
	
	public static int PAYLOAD_LENGTH = 1000; // this is s value

	private int sequenceNumber;
	private int timeStamp;
	private int RTT;
	private byte[] payLoad;
	
	private DatagramPacket udpPacket;

	public DataPacket(int sequenceNumber, int timeStamp, int RTT, byte[] payLoad) {
		this.sequenceNumber = sequenceNumber;
		this.timeStamp = timeStamp;
		this.RTT = RTT;
		this.payLoad = payLoad;
	}
	
	public DataPacket(DatagramPacket udpPacket) {
		this.udpPacket = udpPacket;
		byte[] dataBytes = udpPacket.getData();
		this.setSequenceNumber(TypeByteUtil.toInt(dataBytes , 0));
		this.setTimeStamp(TypeByteUtil.toInt(dataBytes, 4));
		this.setRTT(TypeByteUtil.toInt(dataBytes, 8));
		byte[] space = new byte[dataBytes.length - 12];
		System.arraycopy(dataBytes, 12, space, 0, dataBytes.length - 12);
		this.setPayLoad(space);		
	}
	
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
	
	public int getPort() {
		return udpPacket.getPort();
	}
	
	public InetAddress getSourceAddr() {
		return udpPacket.getAddress();
	}	
	
	public byte[] constructBytes() {
		byte[] packet = new byte[1012];
		TypeByteUtil.toByta(this.getSequenceNumber(), packet, 0);
		TypeByteUtil.toByta(this.getTimeStamp(), packet, 4);
		TypeByteUtil.toByta(this.getRTT(), packet, 8);
		System.arraycopy(this.getPayLoad(), 0, packet, 12,
				this.getPayLoad().length);
		return packet;
	}

	@Override
	public String toString() {
		return "DataPacket [sequenceNumber=" + sequenceNumber + ", timeStamp="
				+ timeStamp + ", RTT=" + RTT + "]";
	}
	
}
