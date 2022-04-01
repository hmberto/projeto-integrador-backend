package br.com.pucsp.projetointegrado.farmacias.utils;

public class CalcFeeDelivery {
	public static String calcFeeDelivery(double distance) {
		double min = 6.00;
		double perKm = 0.99;
		
		double fee = (perKm*distance)+min;
		
		return String.format("%.2f", fee);
	}
}