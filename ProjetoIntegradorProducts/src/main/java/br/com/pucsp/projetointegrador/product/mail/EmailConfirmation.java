package br.com.pucsp.projetointegrador.product.mail;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailConfirmation {
	public static String NAME = EmailConfirmation.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(EmailConfirmation.class.getName());
	
	public void confirmation(String destinatário, String messageSubject, String messageSend) {
		LOG.entering(NAME, "confirmation");
		
		LOG.log(Level.INFO, "Sending email to " + destinatário);
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("entregadefarmacias@gmail.com", "Puc@2022");
			}
		});

		/** Ativa Debug para sessão */
		session.setDebug(false);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("entregadefarmacias@gmail.com"));

			Address[] toUser = InternetAddress.parse(destinatário);
			
			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject(messageSubject);
			message.setContent(messageSend, "text/html");
			
			Transport.send(message);
			LOG.log(Level.INFO, "Email sent to " + destinatário);
		} catch (MessagingException e) {
			LOG.log(Level.SEVERE, "It wasn't possible to send the email: " + e);
		}
		LOG.exiting(NAME, "confirmation");
	}
}