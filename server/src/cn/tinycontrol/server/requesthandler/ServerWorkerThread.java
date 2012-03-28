package cn.tinycontrol.server.requesthandler;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

import cn.tinycontrol.common.model.FeedbackPacket;

public class ServerWorkerThread implements Runnable {

    private int initialRTT;
    
    private InetAddress clientAddr;
    
    private int clientPort;
    
    private boolean isShutDown = false;
    
    private ByteArrayOutputStream dataStream;
    private Queue<FeedbackPacket> feedBackPacketQueue;
    private int R;
    private float X;
    
    public ServerWorkerThread(int initialRTT, InetAddress clientAddr, int clientPort) {
        this.initialRTT = initialRTT;
        this.clientAddr = clientAddr;
        this.clientPort = clientPort;
        
        feedBackPacketQueue = new LinkedList<FeedbackPacket>();
        dataStream = new ByteArrayOutputStream();
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(!isShutDown) {
            // Keep Sending data packets at rate X
            
        }
        // Close connections and so on
        // End the thread.
    }
    
    public void addData(byte[] data, int offset, int length) {
        // Add the data to the buffer
    }
    
    public void handleFeedBackPacket(FeedbackPacket clientPacket) {
        // Add the feedback packet to the queue and notify the run method and notify run() about the new sending rate X.
    }
    
    public void shutDown() {
        isShutDown = true;
        // TODO Remove from the Map
    }
    
}
