package br.com.pucsp.projetointegrador.utils;

public class CalcTimeDelivery {
	public static String calcTimeDelivery(double distance) {
		String time = "";
		
		if(distance <= 1) {
			time = "20-30 min";
		}
		else if(distance <= 2) {
			time = "25-35 min";
		}
		else if(distance <= 3) {
			time = "35-45 min";
		}
		else if(distance <= 5) {
			time = "40-50 min";
		}
		else if(distance <= 7) {
			time = "45-55 min";
		}
		else if(distance <= 9) {
			time = "50-60 min";
		}
		else if(distance <= 10) {
			time = "60-70 min";
		}
		else if(distance <= 13) {
			time = "65-75 min";
		}
		else if(distance <= 15) {
			time = "70-80 min";
		}
		else if(distance <= 17) {
			time = "75-85 min";
		}
		else if(distance <= 20) {
			time = "80-90 min";
		}
		else {
			time = "Agendado";
		}
		
		return time;
	}
}