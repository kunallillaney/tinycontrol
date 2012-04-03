package cn.tinycontrol.server.core.model;

import java.util.ArrayList;
import java.util.List;

public class CurrentState {
    
    public int R;
    
    public float X; // in bytes/second
    
    private List<Float> xRecvSet = new ArrayList<Float>(2); // of size 2
    
    public float last_p;
    
    public float last_XBps;
    
    public void addIntoXRecvSet(Float xRecv) {
        xRecvSet.remove(0);
        xRecvSet.add(xRecv);
    }
    public void getMax() {
    	float maxRecv= Math.max(xRecvSet.get(0),xRecvSet.get(1));
    }

	public void addOnlyThis(Float xRecv) {
        xRecvSet.add(xRecv);
        xRecvSet.remove(1);
    }
    
}