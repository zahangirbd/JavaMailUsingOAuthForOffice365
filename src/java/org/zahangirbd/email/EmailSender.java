package org.zahangirbd.email;

import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.zahangirbd.aad.TokenHandler;
import org.zahangirbd.util.FileUtil;

import com.sun.mail.smtp.SMTPTransport;

public class EmailSender {
	
    public static String prepareTokenForSMTP(String userName, String accessToken) {
        final String ctrlA = Character.toString((char) 1); // Character: Ctrl + A 
        final String coded = "user=" + userName + ctrlA + "auth=Bearer " + accessToken + ctrlA + ctrlA;
        return Base64.getEncoder().encodeToString(coded.getBytes());
    }
    
    private static Session prepareSession() {
        Properties props = new Properties();
       // props.put("mail.imap.ssl.enable", "true"); // required for Gmail
        props.put("mail.smtp.auth.xoauth2.disable", "false");
        props.put("mail.smtp.sasl.enable", "true");
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.debug", true);        
        Session session = Session.getInstance(props);
        return session;
    }
    
    public static void sendTestMail(String userName, String toAddresses, String accessToken) throws MessagingException {
    	if (userName == null || toAddresses == null || accessToken == null) {
    		System.err.println("sendTestMail(...): invalid input arguments, thus, returning");
    		return;
    	}
    	
        String token = prepareTokenForSMTP(userName, accessToken);
        Session session = prepareSession();
        try {
            Message m1 = testMessage(userName, session, toAddresses);
            SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
            transport.connect("smtp.office365.com", userName, null);
            transport.issueCommand("AUTH XOAUTH2 " + token, 235);
            transport.sendMessage(m1, m1.getAllRecipients());
            System.out.println("sendTestMail(...): A Test email has been sent to " + toAddresses + " successfully.");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public static Message testMessage(String from, Session session, String tos) {
        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            var recipients = InternetAddress.parse(tos);
            message.setRecipients(Message.RecipientType.TO,
                    recipients);

            // Set Subject: header field
            message.setSubject("Zahangir's example to send through Office365");

            /*
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("This is message body");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "/temp/WhatsApp Video 2020-04-09 at 09.00.52.mp4";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            
            // Send the complete message parts
            message.setContent(multipart);
            */
            String emailBody = "This is testing email through Office365";
            message.setContent(emailBody, "text/plain");
            
            return message;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    
    public static void main(String[] args) throws IOException, MessagingException {
    	Properties prop = FileUtil.readPropertiesFromResFile(EmailSender.class, "application.properties");
    	String userName = prop.getProperty("userName");
    	String toAddresses = prop.getProperty("toAddresses");
    	TokenHandler tokenHandler = new TokenHandler();
    	String accessToken = tokenHandler.getAccessToken();
    	sendTestMail(userName, toAddresses, accessToken);
	}
}
