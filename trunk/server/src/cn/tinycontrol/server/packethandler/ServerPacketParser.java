package cn.tinycontrol.server.packethandler;

import cn.tinycontrol.common.model.DataPacket;
import cn.tinycontrol.common.model.FeedbackPacket;
import cn.tinycontrol.common.parser.TypeByteUtil;

public class ServerPacketParser {
	public static FeedbackPacket constructPacket(byte[] feedbackBytes) {
		FeedbackPacket fpacket = new FeedbackPacket();
		fpacket.setTimeStamp(TypeByteUtil.toInt(feedbackBytes, 0));
		fpacket.setElapsedTime(TypeByteUtil.toInt(feedbackBytes, 4));
		fpacket.setReceiveRate(TypeByteUtil.toFloat(feedbackBytes, 8));
		fpacket.setLossEventRate(TypeByteUtil.toFloat(feedbackBytes, 12));
		return fpacket;
	}

	public static byte[] constructBytes(DataPacket dataPacket) {
		byte[] packet = new byte[1012];
		TypeByteUtil.toByta(dataPacket.getSequenceNumber(), packet, 0);
		TypeByteUtil.toByta(dataPacket.getTimeStamp(), packet, 4);
		TypeByteUtil.toByta(dataPacket.getRTT(), packet, 8);
		System.arraycopy(dataPacket.getPayLoad(), 0, packet, 12,
				dataPacket.getPayLoad().length);
		return packet;
	}
}
