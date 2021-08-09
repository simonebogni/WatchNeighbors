package classes;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public final class MailSender {
	private final static String charset = "utf-8";
	
	//Private constructor not to be exposed
	private MailSender(){
		super();
	}
	
	
	/**
	 * Sends an email with the given details
	 * @param receiver the address of the person that has to receive the mail
	 * @param subject the subject of the mail
	 * @param emailText the body of the mail
	 * @param isTextHtml true if the mail contains HTML code, false otherwise
	 * @return
	 */
	public static boolean sendMail(String receiver, String subject, String emailText, boolean isTextHtml){
		//set the properties
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		
		//email account authentication settings
		final String username = "lab2uninsubria@gmail.com";
		final String password = "WatchNeightbors";
		
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			//Creation of the text to be sent
			MimeMessage message = new MimeMessage(session);
			//Set the subject
			message.setSubject(subject);
			//Set the text/content
			if(isTextHtml){
				//HTML message
				message.setContent(emailText, "text/html; charset="+charset);
			} else {
				//normal text
				message.setText(emailText);
			}
			//Set the sender's address
			InternetAddress fromAddress = new InternetAddress(username);
			message.setFrom(fromAddress);
			//Set the receiver's address
			InternetAddress toAddress = new InternetAddress(receiver);
			message.setRecipient(Message.RecipientType.TO, toAddress);
			//Send message
			Transport.send(message);
		} catch (MessagingException me) {
			//if there is a MessagingException the mail has not been sent
			return false;
		}
		//if there is no MessagingException the mail has been sent
		return true;
	}
}
