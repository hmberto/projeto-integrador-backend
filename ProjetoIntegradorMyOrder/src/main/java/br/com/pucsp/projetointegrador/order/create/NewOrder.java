package br.com.pucsp.projetointegrador.order.create;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NewOrder")
public class NewOrder {
	public String[] getProducts() {
		return products;
	}
	public void setProducts(String[] products) {
		this.products = products;
	}
	
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	String[] products;
	String session;
	String distance;
	
	public NewOrder() {}
}