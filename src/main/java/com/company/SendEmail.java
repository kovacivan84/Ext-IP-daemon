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

    protected static String username;
    protected static String password;
    protected static String receiverAddress;

    public SendEmail(String ip) {

        File cfgFile = new File("extipdaemon.cfg");
        Properties properties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(cfgFile);

            username = properties.getProperty("username");
            password = properties.getProperty("password");
            receiverAddress = properties.getProperty("receiverAddress");

            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("do_not_reply@watcher.com"));
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(receiverAddress));
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