package br.com.pucsp.projetointegrador.user;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.user.contactus.ContactUsDB;
import br.com.pucsp.projetointegrador.user.contactus.UserMessage;

public class ContactUs {
	public boolean contactUs(UserMessage message) throws MessagingException {
		ContactUsDB contactUsDB = new ContactUsDB();
		
		return contactUsDB.contactUsDB(message);
	}
}