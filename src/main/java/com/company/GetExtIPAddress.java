package com.company;

import java.net.*;
import java.io.*;

public class GetExtIPAddress {

    public static final String mUrl = "http://checkip.amazonaws.com";
//Getter for IP address
    public String getMyExtIPAddress() throws IOException {
        URL whatismyip = new URL(mUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));
        String ip = in.readLine();

        boolean testIp = validateIp(ip);

        if (!testIp) {
            System.out.println("Cannot get public ip address or information is invalid.\n Check your internet connection");
        } else if (testIp) {
            return ip;
        }
        System.out.println("Cannot get public ip address.\n Check your internet connection\nStopping daemon");
        return "kill";
    }
//Checking if ip address is in valid format
    private static boolean validateIp(String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            System.out.println("Not valid IP address");
            return false;
        }
    }
}