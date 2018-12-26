package com.company;

import java.io.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static final String mUrl = "8.8.8.8";

    public static String mOldIP;
    public static String mNewIP;

    protected static String mUsername;
    protected static String mPassword;
    protected static String mStarttls;
    protected static String mSmtpAuth;
    protected static String mSmtpSrv;
    protected static String mSmtpPort;
    protected static String mReceiverAddress;

    public static void main(String[] args) {
//Checking if there is internet connection
        boolean internetConnectionTest = checkInternetConnection();
        if (internetConnectionTest) {
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
//Checking if cfg file exists
        if(cfgFile.exists() && !cfgFile.isDirectory()) {
//If file exists...
            initRunner();
        } else {
//If file doesn't exists
//Get user email configuration
            Scanner scanner = new Scanner(System.in);
            System.out.println("Sender username");
            mUsername = scanner.nextLine();
            System.out.println("Sender password");
            mPassword = scanner.nextLine();
            System.out.println("Set STARTTLS enabled (true/false)");
            mStarttls = scanner.nextLine();
            System.out.println("SMTP authentication (true/false)");
            mSmtpAuth = scanner.nextLine();
            System.out.println("SMTP server");
            mSmtpSrv = scanner.nextLine();
            System.out.println("SMTP port (25 or 465 or 587)");
            mSmtpPort = scanner.nextLine();
            System.out.println("Receiver address");
            mReceiverAddress = scanner.nextLine();
//Write cfg file
            try {
                outputStream = new FileOutputStream(cfgFile);
                properties.setProperty("username", mUsername);
                properties.setProperty("password", mPassword);
                properties.setProperty("starttls", mStarttls);
                properties.setProperty("smtpauth", mSmtpAuth);
                properties.setProperty("smtpsrv", mSmtpSrv);
                properties.setProperty("smtpport", mSmtpPort);
                properties.setProperty("receiverAddress", mReceiverAddress);
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
//Starting runner that will check for internet address change and calling SendEmail function
            initRunner();
        }
    }

    private static void initRunner() {
        String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("Daemon started at " + time);
//Starting daemon and getting initial IP address
        try {
            mNewIP = new GetExtIPAddress().getMyExtIPAddress();
            if (mNewIP == "kill") {
                System.exit(0);
            }
            new SendEmail(mNewIP);
            mOldIP = mNewIP;
        } catch (IOException e) {
            e.printStackTrace();
        }
//Starting timer task that will look for IP change
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    mNewIP = new GetExtIPAddress().getMyExtIPAddress();
                    if (mNewIP == "kill") {
                        System.exit(0);
                    }
                    if (!mOldIP.equals(mNewIP)) {
                        new SendEmail(mNewIP);
                        mOldIP = mNewIP;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 3600000, 3600000);
    }

    private static boolean checkInternetConnection() {
//Ping test to google server
        boolean reachable = false;
        try{
            InetAddress address = InetAddress.getByName(mUrl);
            reachable = address.isReachable(10000);
        } catch (Exception e){
            e.printStackTrace();
        }
        return reachable;
    }
}