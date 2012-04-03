package cn.tinycontrol.server.requesthandler;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

import cn.tinycontrol.common.model.FeedbackPacket;
import cn.tinycontrol.server.core.TinyControlServerSocket;

public class ServerWorkerThread implements Runnable {

    private int initialRTT;
    
    private InetAddress clientAddr;
    
    private int clientPort;
    
    private boolean isShutDown = false;
    
    private TinyControlServerSocket tcServerSocket; // Server Socket with which this thread is associated with.
    private ByteArrayOutputStream dataStream;
    private Queue<FeedbackPacket> feedBackPacketQueue;
    private int R;
    private float X;
    
    public ServerWorkerThread(int initialRTT, InetAddress clientAddr, int clientPort, TinyControlServerSocket tcServerSocket) {
        this.initialRTT = initialRTT;
        this.clientAddr = clientAddr;
        this.clientPort = clientPort;
        this.tcServerSocket = tcServerSocket;
        
        feedBackPacketQueue = new LinkedList<FeedbackPacket>();
        dataStream = new ByteArrayOutputStream();
    }
    
    @Override
    public void run() {
        // Self-register in the Map
        MapHandler.getInstance().add(clientAddr, clientPort, this);
        
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
