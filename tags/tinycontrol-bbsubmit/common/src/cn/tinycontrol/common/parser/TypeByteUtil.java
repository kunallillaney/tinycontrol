package cn.tinycontrol.common.parser;

public class TypeByteUtil {

	public static int toInt(byte[] data, int index) {
		return (int) ((0xff & data[index]) << 24
				| (0xff & data[index + 1]) << 16
				| (0xff & data[index + 2]) << 8 | (0xff & data[index + 3]) << 0);
	}

	public static float toFloat(byte[] data, int index) {
		return Float.intBitsToFloat(toInt(data, index));
	}

	public static void toByta(int data, byte[] packet, int index) {
		packet[index] = (byte) ((data >> 24) & 0xff);
		packet[index + 1] = (byte) ((data >> 16) & 0xff);
		packet[index + 2] = (byte) ((data >> 8) & 0xff);
		packet[index + 3] = (byte) ((data >> 0) & 0xff);
	}

	public static void toByta(float data, byte[] packet, int index) {
		int temp = Float.floatToRawIntBits(data);
		toByta(temp, packet, index);
	}
}
