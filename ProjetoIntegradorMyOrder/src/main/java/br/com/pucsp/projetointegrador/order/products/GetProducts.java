package br.com.pucsp.projetointegrador.order.products;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetProducts")
public class GetProducts {
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
	
	public String getPharmacyName() {
		return pharmacyName;
	}
	public void setPharmacyName(String pharmacyName) {
		this.pharmacyName = pharmacyName;
	}

	String[] products;
	String session;
	String distance;
	String pharmacyName;
	
	private GetProducts() {}
}