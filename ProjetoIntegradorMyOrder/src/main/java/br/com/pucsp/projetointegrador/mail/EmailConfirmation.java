package br.com.pucsp.projetointegrador.mail;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.pucsp.projetointegrador.order.utils.LogMessage;
import br.com.pucsp.projetointegrador.order.utils.ProjectVariables;

public class EmailConfirmation {
	private static String name = EmailConfirmation.class.getSimpleName();
	private static Logger log = Logger.getLogger(EmailConfirmation.class.getName());
	
	public void confirmation(String destinatario, String messageSubject, String messageSend) throws MessagingException {
		log.entering(name, "confirmation");
		
		ProjectVariables projectVariables = new ProjectVariables();
		final Map <String, String> variables = projectVariables.projectVariables();
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp-mail.outlook.com");
		props.put("mail.smtp.socketFactory.port", "587");
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(variables.get("EMAIL_PROJETO"), variables.get("SENHA_EMAIL_PROJETO"));
			}
		});
		
		session.setDebug(false);
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(variables.get("EMAIL_PROJETO")));

			Address[] toUser = InternetAddress.parse(destinatario);
			
			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject(messageSubject);
			message.setContent(messageSend, "text/html; charset=UTF-8");
			
			Transport.send(message);
			log.exiting(name, "confirmation");
		} catch (MessagingException e) {
			throw new MessagingException(LogMessage.message(e.toString()));
		}
	}
}