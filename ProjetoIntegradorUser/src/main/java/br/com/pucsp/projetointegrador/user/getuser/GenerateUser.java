package br.com.pucsp.projetointegrador.user.getuser;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GenerateLogin")
public class GenerateUser {
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	String email;
	String cpf;
	String birthDate;
	String name;
	String number;
	String complement;
	String zipCode;
	String sex;
		
	public GenerateUser(Map<String, String> user) {
		super();
		
		this.name = user.get("name");
		this.number = user.get("number");
		this.complement = user.get("complement");
		this.zipCode = user.get("zipCode");
		this.sex = user.get("sex");
		this.email = user.get("email");
		this.cpf = user.get("cpf");
		this.birthDate = user.get("birthDate");
	}
}