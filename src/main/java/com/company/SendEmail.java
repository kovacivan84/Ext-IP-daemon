package com.company;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;


public class SendEmail {

    protected static String mUsername;
    protected static String mPassword;
    protected static String mReceiverAddress;

    public SendEmail(String ip) {

        File cfgFile = new File("extipdaemon.cfg");
        Properties properties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(cfgFile);

            mUsername = properties.getProperty("username");
            mPassword = properties.getProperty("password");
            mReceiverAddress = properties.getProperty("receiverAddress");

            Session session = Session.getInstance(properties,
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
                System.out.println("Email sent!\nNew IP address is: " + ip);
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}