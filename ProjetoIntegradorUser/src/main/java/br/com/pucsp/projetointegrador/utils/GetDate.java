package br.com.pucsp.projetointegrador.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetDate {
	
	private GetDate () {}
	
	public static String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm'h'");
		return dtf.format(LocalDateTime.now());
	}
}