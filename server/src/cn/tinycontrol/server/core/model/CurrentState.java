package cn.tinycontrol.server.core.model;

import java.util.ArrayList;
import java.util.List;

class CurrentState {
    
    public int R;
    
    public float X; // in bytes/second
    
    private List<Integer> xRecvSet = new ArrayList<Integer>(2); // of size 2
    
    public float last_p;
    
    public float last_XBps;
    
    public void addIntoXRecvSet(Integer xRecv) {
        xRecvSet.remove(0);
        xRecvSet.add(xRecv);
    }
    
    public void addOnlyThis(Integer xRecv) {
        xRecvSet.add(xRecv);
        xRecvSet.remove(1);
    }
    
}