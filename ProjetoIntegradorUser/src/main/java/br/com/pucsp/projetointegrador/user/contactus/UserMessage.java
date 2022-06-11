package br.com.pucsp.projetointegrador.user.contactus;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserMessage")
public class UserMessage {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	String name;
	String email;
	String message;
	
	private UserMessage() {}
}