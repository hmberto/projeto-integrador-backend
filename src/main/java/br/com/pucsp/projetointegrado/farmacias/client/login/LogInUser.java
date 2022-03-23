package br.com.pucsp.projetointegrado.farmacias.client.login;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LogInUser")
public class LogInUser {
	public String getIP() {
		return ip;
	}
	public void setIP(String ip) {
		this.ip = ip;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getNewLogin() {
		return newLogin;
	}
	public void setNewLogin(String newLogin) {
		this.newLogin = newLogin;
	}

	String ip;
	String email;
	String pass;
	String newLogin;
	
	public LogInUser() {}
}