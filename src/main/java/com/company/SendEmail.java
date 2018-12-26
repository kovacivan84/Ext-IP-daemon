package com.company;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;


public class SendEmail {

    protected static String mUsername;
    protected static String mPassword;
    protected static String mStarttls;
    protected static String mSmtpAuth;
    protected static String mSmtpSrv;
    protected static String mSmtpPort;
    protected static String mReceiverAddress;

    public SendEmail(String ip) {

        File cfgFile = new File("extipdaemon.cfg");
        Properties properties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(cfgFile);
            properties.load(inputStream);

            mUsername = properties.getProperty("username");
            mPassword = properties.getProperty("password");
            mStarttls = properties.getProperty("starttls");
            mSmtpAuth = properties.getProperty("smtpauth");
            mSmtpSrv = properties.getProperty("smtpsrv");
            mSmtpPort = properties.getProperty("smtpport");
            mReceiverAddress = properties.getProperty("receiverAddress");

            Properties props = new Properties();
            props.put("mail.smtp.starttls.enable", mStarttls);
            props.put("mail.smtp.auth", mSmtpAuth);
            props.put("mail.smtp.host", mSmtpSrv);
            props.put("mail.smtp.port", mSmtpPort);
            props.put("mail.smtp.ssl.trust", "*");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(mUsername, mPassword);
                        }
                    });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("do_not_reply@watcher.com"));
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(mReceiverAddress));
                message.setSubject("External IP address changed!");
                message.setText("New public IP address assigned is: " + ip);
                Transport.send(message);
                System.out.println("Email sent!\nYour current public IP address is: " + ip);
            } catch (AddressException e) {
                System.out.println("Check your email settings");
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Configuration file not found or damaged!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}