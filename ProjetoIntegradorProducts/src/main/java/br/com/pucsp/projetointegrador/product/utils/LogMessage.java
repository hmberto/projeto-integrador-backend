package br.com.pucsp.projetointegrador.product.utils;

public class LogMessage {
	public static String message(String e) {
		return "Message: " + e;
	}
	
	public static String productsMessage(String e) {
		return "Couldn't find products: " + e;
	}
}