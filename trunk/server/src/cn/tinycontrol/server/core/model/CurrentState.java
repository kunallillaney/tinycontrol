package cn.tinycontrol.server.core.model;

import java.util.ArrayList;
import java.util.List;

public class CurrentState {
    
    public int R;
    
    public float X; // in bytes/second
    
    private List<Float> xRecvSet = new ArrayList<Float>(2); // of size 2
    
    public float last_p;
    
    public float last_XBps;
    
    private boolean isAnyFeedbackPacketSeen;
    
    public long tld;

    public int initialRate;
    
    public void addIntoXRecvSet(Float xRecv) {
        if(!xRecvSet.isEmpty()) {
        	xRecvSet.remove(0);
        }
        xRecvSet.add(xRecv);
    }
    
    public float getMax() {
    	float maxRecv = 0;
    	if(xRecvSet.size() == 2) {
        	maxRecv = Math.max(xRecvSet.get(0),xRecvSet.get(1));
    	} else if(xRecvSet.size() == 1) {
    		maxRecv = xRecvSet.get(0);
    	}
    	return maxRecv;
    }

	public void addOnlyThis(Float xRecv) {
		xRecvSet = new ArrayList<Float>(2);
        xRecvSet.add(xRecv);
    }

	public boolean isAnyFeedbackPacketSeen() {
		return isAnyFeedbackPacketSeen;
	}

	public void setAnyFeedbackPacketSeen(boolean isAnyFeedbackPacketSeen) {
		this.isAnyFeedbackPacketSeen = isAnyFeedbackPacketSeen;
	}
    
}