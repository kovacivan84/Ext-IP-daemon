package com.company;

import java.net.*;
import java.io.*;

public class GetExtIPAddress {

    private static boolean testIp = false;

    public String getMyExtIPAddress() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));
        String ip = in.readLine();

        testIp = validateIp(ip);

        if (testIp == false) {
            System.out.println("Cannot get public ip address or information is invalid.\n Check your internet connection");
        } else if (testIp == true) {
            return ip;
        }
        System.out.println("Cannot get public ip address.\n Check your internet connection\nStopping daemon");
        String kill = "kill";
        return kill;
    }

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
            return false;
        }
    }
}