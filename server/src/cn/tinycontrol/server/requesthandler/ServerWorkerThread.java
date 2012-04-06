package cn.tinycontrol.server.requesthandler;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;

import cn.tinycontrol.common.model.DataPacket;
import cn.tinycontrol.common.model.FeedbackPacket;
import cn.tinycontrol.server.core.TinyControlServerSocket;
import cn.tinycontrol.server.core.model.CurrentState;

public class ServerWorkerThread implements Runnable {

    private static final double FILTER_CONSTANT = 0.9; // q
    
    private static final double t_mbi = 64;

    private int initialRTT;
    
    private InetAddress clientAddr;
    
    private int clientPort;
    
    private boolean isShutDown = false;
    
    public boolean isStarted = false;
    
    private TinyControlServerSocket tcServerSocket; // Server Socket with which this thread is associated with.
    private PipedInputStream pipedInputStream;
    private PipedOutputStream pipedOutputStream;
    
    //private ByteBuffer byteBuffer;
    
    private NoFeedbackTimer noFeedbackTimer;
    
    private CurrentState curState = new CurrentState();
    
    public ServerWorkerThread(int initialRTT, InetAddress clientAddr, int clientPort, TinyControlServerSocket tcServerSocket) {
        this.initialRTT = initialRTT;
        this.clientAddr = clientAddr;
        this.clientPort = clientPort;
        this.tcServerSocket = tcServerSocket;
        
        pipedInputStream = new PipedInputStream();
        pipedOutputStream = new PipedOutputStream();
        try {
            pipedInputStream.connect(pipedOutputStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // Initialize the Server values - initial sending rate and so on.
        int wInit = Math.min(4 * DataPacket.PAYLOAD_LENGTH, Math.max(2 * DataPacket.PAYLOAD_LENGTH, 4380));
        curState.R = this.initialRTT;
        curState.initialRate = wInit/this.initialRTT * 1000;
        curState.X = curState.initialRate;
        curState.tld = System.currentTimeMillis(); // time when first RTT was received.
        System.out.println("Determined current sending rate as - "+curState.X);
        
        noFeedbackTimer = new NoFeedbackTimer(curState);
        // Set timer to expire after 2 seconds. Do not start the timer here.
        noFeedbackTimer.setExpireAfter(2000);
    }
    
    @Override
    public void run() {
        // Self-register in the Map
        MapHandler.getInstance().add(clientAddr, clientPort, this);
        noFeedbackTimer.startTimer(); // Initial case
        int sequenceNumber = 0;
        boolean testCondn = false;
        while(!testCondn) {
            // Keep Sending data packets at rate X
            int totalDataSent = 0; // in bytes
            long startTime = System.currentTimeMillis();
            byte[] data = new byte[DataPacket.PAYLOAD_LENGTH];
            for (int i = 0; i < data.length; i++) {
				data[i] = (byte)i;
				
			}
            while((System.currentTimeMillis() - startTime) < 1000) { // 1 second
                if(totalDataSent < curState.X) {
                    try {
                        //pipedInputStream.read(data , 0, DataPacket.PAYLOAD_LENGTH);
						DataPacket dataPacket = new DataPacket(sequenceNumber ++, tcServerSocket.getCurTime(), curState.R, data);
                        System.out.println("Sending data packet " + dataPacket);
						byte[] udpDataBytes = dataPacket.constructBytes();
                        DatagramPacket udpPacket = new DatagramPacket(udpDataBytes, udpDataBytes.length, clientAddr, clientPort);
                        tcServerSocket.getUdpSocket().send(udpPacket);
                        totalDataSent += DataPacket.PAYLOAD_LENGTH;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }                
            }
            if(totalDataSent < curState.X) {
                // It means that the server was unable to send the required amount of data in 1 second.
                System.err.println("Unable to send " + curState.X
                                + " bytes of data in 1 second. Total Data Sent in this second = " + totalDataSent);
            }
        }
        
        // Close connections and so on
        // End the thread.
    }
    
    public void addData(byte[] data, int offset, int length) {
        // Add the data to the buffer
        try {
            pipedOutputStream.write(data, offset, length);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void handleFeedBackPacket(FeedbackPacket clientPacket) {
        // Add the feedback packet to the queue and notify the run method and notify run() about the new sending rate X.
        // 1. Calculate the new round trip sample:
        int rSample = (tcServerSocket.getCurTime() - clientPacket.getTimeStamp()) - clientPacket.getElapsedTime();
        
        // 2. Update the round-trip time estimate:
        if(!curState.isAnyFeedbackPacketSeen()) {
            // Means that this is the first feedback packet received
            curState.R = rSample;
        } else {
            curState.R = (int) ((FILTER_CONSTANT * curState.R) + (1 - FILTER_CONSTANT)
                    * rSample);
        }
        
        // 3. Update the timeout interval, which is used for setting no feedback timer.
        int rto = (int)(Math.max(4*curState.R, 2*DataPacket.PAYLOAD_LENGTH/curState.X));
        
        // 4. Update the allowed sending rate by following Algorithm 1.
        runAlgorithmOne(curState, clientPacket);
        
        // 5. Reset no feedback timer to expire after RTO seconds
        noFeedbackTimer.resetTimer(rto);
        
    }
    
    public static void runAlgorithmOne(CurrentState curState, FeedbackPacket clientPacket) {
        int s = DataPacket.PAYLOAD_LENGTH;
        
        float p;
        if(clientPacket != null) {
            // Means that this method is called when FeedbackPacket is received
            p = clientPacket.getLossEventRate();
            curState.last_p = p;
            // Update XRecvSet
            curState.addIntoXRecvSet(clientPacket.getReceiveRate());
        } else {
            // Means that this method is called when NoFeedbackPacket timer expired
            p = curState.last_p;
        }
        
        float recvLimit = 2 * curState.getMax();
        if(p > 0) {
            // Calculate XBps using the TCP throughput equation 3.1.4
            double xBps = s/(curState.R * Math.sqrt(2*p/3)  + 12 * Math.sqrt(3*p/8) * p * (1+ 32 * p*p));
            curState.X = (float)Math.max(Math.min(xBps, recvLimit), s/t_mbi);
        } else {
            long tnow = System.currentTimeMillis();
            if (tnow-curState.tld >= curState.R) {
                curState.X = Math.max(Math.min(2*curState.X, recvLimit), curState.initialRate);
                curState.tld = tnow;
            }
        }
    }

    public void shutDown() {
        isShutDown = true;
        try {
            pipedOutputStream.close();
            pipedInputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Remove from the Map
    }
}
