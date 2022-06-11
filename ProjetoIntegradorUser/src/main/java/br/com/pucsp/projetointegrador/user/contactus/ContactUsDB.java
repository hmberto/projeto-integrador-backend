package br.com.pucsp.projetointegrador.user.contactus;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;

public class ContactUsDB {
	public boolean contactUsDB(UserMessage message) throws MessagingException {
		String alphaNumeric = "123456789";
		
		StringBuilder token = new StringBuilder();
		double random = Math.random();
		
		for(int i = 0; i < 6; i++) {
			int myindex = (int)(alphaNumeric.length() * random);
			
			token.append(alphaNumeric.charAt(myindex));
		}
		
		String messageSubject = "Entrega de Farmácias - Solicitação #" + token.toString();
		String shortText = "Olá, " + message.getName() + ".";
		String info = "Já recebemos sua solicitação. <br><br>Não se preocupe, já estamos trabalhando nisso.";
		String btnText = "Visitar site";
		String btnLink = "https://projeto-integrador-frontend.herokuapp.com";
		String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
		
		EmailConfirmation sendEmail = new EmailConfirmation();
		sendEmail.confirmation(message.getEmail(), messageSubject, messageText);
		
		return true;
	}
}