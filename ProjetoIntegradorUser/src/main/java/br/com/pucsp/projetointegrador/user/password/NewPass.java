package br.com.pucsp.projetointegrador.user.password;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ChangePass")
public class NewPass {
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	String email;
	
	public NewPass() {}
}