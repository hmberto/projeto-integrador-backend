package br.com.pucsp.projetointegrador.user.login;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GenerateLogin")
public class GenerateLogin {
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	
	String session;
		
	public GenerateLogin(Map<Integer, String> session2) {
		super();
		
		this.session = session2.get(1);
	}
}