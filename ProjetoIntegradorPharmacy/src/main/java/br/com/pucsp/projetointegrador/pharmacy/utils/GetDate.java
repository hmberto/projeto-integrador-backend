package br.com.pucsp.projetointegrador.pharmacy.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetDate {
	public static String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm'h'");
		String time = dtf.format(LocalDateTime.now());
		
		return time;
	}
}