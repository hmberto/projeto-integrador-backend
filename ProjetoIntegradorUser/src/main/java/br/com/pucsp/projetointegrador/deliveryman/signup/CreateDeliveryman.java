package br.com.pucsp.projetointegrador.deliveryman.signup;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CreateDeliveryman")
public class CreateDeliveryman {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getCnh() {
		return cnh;
	}
	public void setCnh(String cnh) {
		this.cnh = cnh;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	String name;
	String cpf;
	String cnh;
	String category;
	
	private CreateDeliveryman() {}
}