package br.com.pucsp.projetointegrador.user.password;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ChangePass")
public class ChangePass {
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getNewPass() {
		return newPass;
	}
	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}
	
	String email;
	String token;
	String newPass;
	
	public ChangePass() {}
}