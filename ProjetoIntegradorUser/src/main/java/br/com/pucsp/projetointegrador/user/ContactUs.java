package br.com.pucsp.projetointegrador.user;

import javax.servlet.http.HttpServletRequest;

import br.com.pucsp.projetointegrador.user.contactus.ContactUsDB;
import br.com.pucsp.projetointegrador.user.contactus.UserMessage;

public class ContactUs {
	public boolean contactUs(HttpServletRequest request, UserMessage message) {
		ContactUsDB contactUsDB = new ContactUsDB();
		boolean check = contactUsDB.contactUsDB(message);
		
		return check;
	}
}