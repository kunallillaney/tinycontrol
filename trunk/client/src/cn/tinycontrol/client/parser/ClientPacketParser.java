package cn.tinycontrol.client.parser;

import cn.tinycontrol.common.model.DataPacket;
import cn.tinycontrol.common.model.FeedbackPacket;
import cn.tinycontrol.common.parser.TypeByteUtil;

public class ClientPacketParser {

	public static DataPacket constructPacket(byte[] dataBytes) {
		DataPacket dpacket = new DataPacket();
		dpacket.setSequenceNumber(TypeByteUtil.toInt(dataBytes, 0));
		dpacket.setTimeStamp(TypeByteUtil.toInt(dataBytes, 4));
		dpacket.setRTT(TypeByteUtil.toInt(dataBytes, 8));
		byte[] space = new byte[dataBytes.length - 12];
		System.arraycopy(dataBytes, 12, space, 0, dataBytes.length - 12);
		dpacket.setPayLoad(space);
		return dpacket;
	}

	public static byte[] constructBytes(FeedbackPacket feedbackPacket) {
		byte[] fPacket = new byte[16];
		TypeByteUtil.toByta(feedbackPacket.getTimeStamp(), fPacket, 0);
		TypeByteUtil.toByta(feedbackPacket.getElapsedTime(), fPacket, 4);
		TypeByteUtil.toByta(feedbackPacket.getReceiveRate(), fPacket, 8);
		TypeByteUtil.toByta(feedbackPacket.getLossEventRate(), fPacket, 12);
		return fPacket;
	}
}
