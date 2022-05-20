package br.com.pucsp.projetointegrador.user.contactus;

import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;

public class ContactUsDB {
	public boolean contactUsDB(UserMessage message) {
		String alphaNumeric = "123456789";
		String token = "";
		
		for(int i = 0; i < 6; i++) {
			int myindex = (int)(alphaNumeric.length() * Math.random());
			
			token = token + alphaNumeric.charAt(myindex);
		}
		
		String messageSubject = "Entrega de Farmácias - Solicitação #" + token;
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