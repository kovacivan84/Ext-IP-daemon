package com.company;

import java.io.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static boolean internetConnectionTest;

    public static String oldIP;
    public static String newIP;

    protected static String username;
    protected static String password;
    protected static String starttls;
    protected static String smtpAuth;
    protected static String smtpSrv;
    protected static String smtpPort;
    protected static String receiverAddress;

    public static void main(String[] args) {

        internetConnectionTest = checkInternetConnection();
        if (internetConnectionTest == true) {
            initConfiguration();
        } else {
            System.out.println("You don't have internet access!");
            System.exit(0);
        }
    }

    private static void initConfiguration() {
        File cfgFile = new File("extipdaemon.cfg");
        Properties properties = new Properties();
        OutputStream outputStream = null;

        if(cfgFile.exists() && !cfgFile.isDirectory()) {
            initRunner();
        } else {
//Get user email configuration
            Scanner scanner = new Scanner(System.in);
            System.out.println("Sender username");
            username = scanner.nextLine();
            System.out.println("Sender password");
            password = scanner.nextLine();
            System.out.println("Set STARTTLS enabled (true/false)");
            starttls = scanner.nextLine();
            System.out.println("SMTP authentication (true/false)");
            smtpAuth = scanner.nextLine();
            System.out.println("SMTP server");
            smtpSrv = scanner.nextLine();
            System.out.println("SMTP port (25 or 465 or 587)");
            smtpPort = scanner.nextLine();
            System.out.println("Receiver address");
            receiverAddress = scanner.nextLine();

//Write cfg file
            try {
                outputStream = new FileOutputStream(cfgFile);
                properties.setProperty("username", username);
                properties.setProperty("password", password);
                properties.setProperty("starttls", starttls);
                properties.setProperty("smtpauth", smtpAuth);
                properties.setProperty("smtpsrv", smtpSrv);
                properties.setProperty("smtpport", smtpPort);
                properties.setProperty("receiverAddress", receiverAddress);
                properties.store(outputStream, null);

                System.out.println("Configuration file created\nIf data you have entered is invalid manually delete extipdaemon.cfg file and re-run daemon.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            initRunner();
        }
    }

    private static void initRunner() {
        String time = new SimpleDateFormat("ddMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
        System.out.println("Daemon started at " + time);
        try {
            newIP = new GetExtIPAddress().getMyExtIPAddress();
            if (newIP == "kill") {
                System.exit(0);
            }
            new SendEmail(newIP);
            oldIP = newIP;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    newIP = new GetExtIPAddress().getMyExtIPAddress();
                    if (newIP == "kill") {
                        System.exit(0);
                    }
                    if (!oldIP.equals(newIP)) {
                        new SendEmail(newIP);
                        oldIP = newIP;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 3600000, 3600000);
    }

    private static boolean checkInternetConnection() {
        boolean reachable = false;
        try{
            InetAddress address = InetAddress.getByName("8.8.8.8");
            reachable = address.isReachable(10000);
        } catch (Exception e){
            e.printStackTrace();
        }
        return reachable;
    }
}