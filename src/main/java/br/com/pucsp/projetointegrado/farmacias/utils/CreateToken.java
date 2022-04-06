package br.com.pucsp.projetointegrado.farmacias.utils;

import java.util.Map;

public class CreateToken {
	public String createToken(Map<String, String> variables) {
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String token = "";
		
		for(int i = 0; i < SESSION_LENGTH; i++) {
			int myindex = (int)(alphaNumeric.length() * Math.random());
			
			token = token + alphaNumeric.charAt(myindex);
		}
		
		return token;
	}
}