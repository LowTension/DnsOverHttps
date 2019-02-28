package com.github.digitalsoftwaresolutions;

public class Main {

    public static void main(String[] args) {
        try {
            DnsServer dnsServer = new DnsServer(53);
            dnsServer.start();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
