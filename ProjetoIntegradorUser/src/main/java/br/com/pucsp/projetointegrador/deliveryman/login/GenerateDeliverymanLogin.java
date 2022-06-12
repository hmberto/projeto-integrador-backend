package br.com.pucsp.projetointegrador.deliveryman.login;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GenerateDeliverymanLogin")
public class GenerateDeliverymanLogin {
	public String getIdEntregador() {
		return idEntregador;
	}
	public void setIdEntregador(String idEntregador) {
		this.idEntregador = idEntregador;
	}
	
	public String getNomeEntregador() {
		return nomeEntregador;
	}
	public void setNomeEntregador(String nomeEntregador) {
		this.nomeEntregador = nomeEntregador;
	}
	
	public String getCpfEntregador() {
		return cpfEntregador;
	}
	public void setCpfEntregador(String cpfEntregador) {
		this.cpfEntregador = cpfEntregador;
	}
	
	public String getCnhEntregador() {
		return cnhEntregador;
	}
	public void setCnhEntregador(String cnhEntregador) {
		this.cnhEntregador = cnhEntregador;
	}
	
	public String getCategoriaCnhEntregador() {
		return categoriaCnhEntregador;
	}
	public void setCategoriaCnhEntregador(String categoriaCnhEntregador) {
		this.categoriaCnhEntregador = categoriaCnhEntregador;
	}
	
	String idEntregador;
	String nomeEntregador;
	String cpfEntregador;
	String cnhEntregador;
	String categoriaCnhEntregador;
	
	public GenerateDeliverymanLogin(Map<String, String> user) {
		super();
		
		this.idEntregador = user.get("id_entregador");
		this.nomeEntregador = user.get("nome");
		this.cpfEntregador = user.get("cpf");
		this.cnhEntregador = user.get("cnh");
		this.categoriaCnhEntregador = user.get("categoria_cnh");
	}
}