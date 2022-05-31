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
	
	public String getCardFlag() {
		return cardFlag;
	}
	public void setCardFlag(String cardFlag) {
		this.cardFlag = cardFlag;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	public String getCardDate() {
		return cardDate;
	}
	public void setCardDate(String cardDate) {
		this.cardDate = cardDate;
	}
	
	public String getCardCvv() {
		return cardCvv;
	}
	public void setCardCvv(String cardCvv) {
		this.cardCvv = cardCvv;
	}
	
	public String getCardDoc() {
		return cardDoc;
	}
	public void setCardDoc(String cardDoc) {
		this.cardDoc = cardDoc;
	}
	
	public String getPharmacyDistance() {
		return pharmacyDistance;
	}
	public void setPharmacyDistance(String pharmacyDistance) {
		this.pharmacyDistance = pharmacyDistance;
	}
	
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
	public String getDeliveryFee() {
		return deliveryFee;
	}
	public void setDeliveryFee(String deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	
	public String getPharmacyCnpj() {
		return pharmacyCnpj;
	}
	public void setPharmacyCnpj(String pharmacyCnpj) {
		this.pharmacyCnpj = pharmacyCnpj;
	}
	
	public String getPharmacyId() {
		return pharmacyId;
	}
	public void setPharmacyId(String pharmacyId) {
		this.pharmacyId = pharmacyId;
	}
	
	String[] products;
	String session;
	String cardFlag;
	String cardNumber;
	String cardName;
	String cardDate;
	String cardCvv;
	String cardDoc;
	String pharmacyDistance;
	String deliveryTime;
	String deliveryFee;
	String deliveryAddress;
	String pharmacyCnpj;
	String pharmacyId;
	
	public NewOrder() {}
}