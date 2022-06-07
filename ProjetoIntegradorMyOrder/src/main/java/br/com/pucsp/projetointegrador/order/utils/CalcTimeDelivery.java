package br.com.pucsp.projetointegrador.order.utils;

public class CalcTimeDelivery {
	private CalcTimeDelivery() {}
	
	public static String calcTimeDelivery(double distance) {
		int time = (int) Math.round(((distance + 7) * 3));
		return time + "-" + (time + 10) + " min";
	}
}