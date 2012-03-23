package cn.tinycontrol.server.requesthandler;

import java.net.InetAddress;

import cn.tinycontrol.common.model.FeedbackPacket;

public class ServerWorkerThread implements Runnable {

    private int initialRTT;
    
    private InetAddress clientAddr;
    
    private int clientPort;
    
    public ServerWorkerThread(int initialRTT, InetAddress clientAddr, int clientPort) {
        this.initialRTT = initialRTT;
        this.clientAddr = clientAddr;
        this.clientPort = clientPort;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
    public void handleFeedBackPacket(FeedbackPacket clientPacket) {
        
    }
    
    public void shutDown() {
        
    }

}
