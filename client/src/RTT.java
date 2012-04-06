
public class RTT {
	
	public static long RTT = 0;

	public static long getRTT() {
		return RTT;
	}

	public static void setRTT() {
		RTT = PacketHistory.getInstance().getLastPacket().getDataPacket().getRTT();
	}
	
	
	
}
