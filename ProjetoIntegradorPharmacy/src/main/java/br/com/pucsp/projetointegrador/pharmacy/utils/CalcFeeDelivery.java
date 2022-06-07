package br.com.pucsp.projetointegrador.pharmacy.utils;

public class CalcFeeDelivery {
	private CalcFeeDelivery () {}
	
	public static String calcFeeDelivery(double distance) {
		double min = 4.00;
		double perKm = 0.79;
		
		double fee = (perKm*distance)+min;
		
		return String.format("%.2f", fee);
	}
}