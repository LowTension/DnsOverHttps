package com.github.digitalsoftwaresolutions;

import okhttp3.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Base64;

public class DnsServer extends Thread{
    ExecutorService mThreadPool = Executors.newFixedThreadPool(10);
    private int mPort;
    public DnsServer(int port){
        this.mPort = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket mUdpListener = new DatagramSocket(mPort);
            byte[] mUdpBuffer = new byte[576];
            while(true){
                try {
                    DatagramPacket udpPacket = new DatagramPacket(mUdpBuffer,mUdpBuffer.length);
                    mUdpListener.receive(udpPacket);
                    mThreadPool.submit(new Resolver(mUdpListener,udpPacket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

}
