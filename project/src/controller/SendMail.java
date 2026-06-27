package controller;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


public class SendMail {

    public static void sendMail(String dest) {
        String to = dest;
        final String from = "andresequeira99@gmail.com";
        final String pwd = "lqgk eknc efee uqvi";
        String host = "smtp.gmail.com";
        String port = "465";
        Properties properties = System.getProperties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", host);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator () {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, pwd);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session); // Create a default MimeMessage object
            message.setFrom(new InternetAddress(from)); // Set From: header field of the header
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Account Registered"); // Set Subject: header field
            message.setText("Your account has been  registered in our app and is awaiting validation"); // Now set the actual message
            Transport.send(message); // Send message
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
