package controller;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * Utility class to send emails on account register (should have been inside one of the controllers...)
 */
public class SendMail {

    /**
     * Fetches all unready notifications that are meant for managers
     * @param destinationEmail String with the email of the person trying to create account
     */
    public static void sendMail(String destinationEmail) {
        String to = destinationEmail;
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
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Account Registered");
            message.setText("Your account has been  registered in our app and is awaiting validation");
            Transport.send(message);
            System.out.println("Email sent!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
