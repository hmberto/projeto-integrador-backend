package br.com.pucsp.projetointegrador.deliveryman.login;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LogInDeliveryman")
public class LogInDeliverymanUser {
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	String cpf;
	
	private LogInDeliverymanUser() {}
}