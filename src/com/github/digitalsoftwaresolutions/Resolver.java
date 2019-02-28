package com.github.digitalsoftwaresolutions;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Base64;

public class Resolver implements Runnable {
    DatagramPacket mPacket;
    DatagramSocket mServerSocket ;
    public Resolver(DatagramSocket server, DatagramPacket packet){
        this.mPacket = packet;
        this.mServerSocket = server;
    }
    @Override
    public void run() {
        final byte[] requestBytes = new byte[mPacket.getLength()];
        System.arraycopy(mPacket.getData(),0,requestBytes,0, mPacket.getLength());
        String data =  Base64.getUrlEncoder().withoutPadding().encodeToString(requestBytes);


        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("https://cloudflare-dns.com/dns-query?dns=" + data)
                .addHeader("accept","application/dns-message")
                .addHeader("content-type","application/dns-message")
                .build();

        try {
            Response response = client.newCall(request).execute();
            byte[] responseBytes = response.body().bytes();
            mPacket.setData(responseBytes);
            mServerSocket.send(mPacket);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}