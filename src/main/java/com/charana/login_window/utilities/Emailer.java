package com.charana.login_window.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//https://support.postbox-inc.com/hc/en-us/articles/202200080-Setting-up-Yahoo-Mail
public class Emailer {

    private static String host = "smtp.mail.yahoo.com";
    private static String port = "587";
    private static String fromEmail = "javafxemailer@yahoo.com";
    private static String username = "javafxemailer";
    private static String password = "vWD-n5g-CDg-RUG";
    private static Properties props = System.getProperties();
    private static Session l_session = null;
    private static Logger logger = LoggerFactory.getLogger(Emailer.class);

    static {
        emailSettings();
        createSession();
    }

    public static void emailSettings() {
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    }

    public static void createSession() {
        l_session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public static void sendMessage(String toEmail, String subject, String msg) {
        Thread thread = new Thread("EMAILER"){
            @Override
            public void run() {
                try {
                    MimeMessage message = new MimeMessage(l_session);
                    message.setFrom(new InternetAddress(fromEmail));

                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                    message.setSubject(subject);
                    message.setContent(msg, "text/html");

                    Transport.send(message);
                    logger.debug("Message Sent");
                }
                catch (MessagingException mex) {
                    mex.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public static void main(String[] args){
        new Emailer();
    }

}