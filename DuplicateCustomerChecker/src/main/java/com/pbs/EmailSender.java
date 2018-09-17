/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.pbs;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author administrator
 */
public class EmailSender {
    private static final String SMTP_SERVER = "smtp.office365.com";
    private static final int SMTP_PORT = 587;
    private static String SMTP_EMAIL = "";
    private static char[] SMTP_PASSWORD;
    
    private static String to = "";
    private static String from = "";

    private final String subject = "New customers";
    private final String messageContent = "Weekly customers report";

    Properties propertiesObject = new Properties();

    public void sendEmail() {
        final Session session = Session.getInstance(this.getEmailProperties(), new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_EMAIL, String.valueOf(SMTP_PASSWORD));
            }

        });
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        try {
            final Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(messageContent);
            message.setSentDate(new Date());            

            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String file = "C:\\Customers\\New customers " + date + ".csv";
            String fileName = "New customers " + date + ".csv";

            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (AddressException ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Properties getEmailProperties() {
        final Properties config = new Properties();
        config.put("mail.smtp.auth", "true");
        config.put("mail.smtp.starttls.enable", "true");
        config.put("mail.smtp.host", SMTP_SERVER);
        config.put("mail.smtp.port", SMTP_PORT);
        return config;
    }

    public static void setSMTP_EMAIL(String SMTP_EMAIL) {
        EmailSender.SMTP_EMAIL = SMTP_EMAIL;
    }

    public static void setSMTP_PASSWORD(char[] SMTP_PASSWORD) {
        EmailSender.SMTP_PASSWORD = SMTP_PASSWORD;
    }

    public static void setTo(String to) {
        EmailSender.to = to;
    }

    public static void setFrom(String from) {
        EmailSender.from = from;
    }
    
    
    
}
