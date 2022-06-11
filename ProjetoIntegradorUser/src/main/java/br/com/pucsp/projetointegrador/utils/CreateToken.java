package br.com.pucsp.projetointegrador.utils;

import java.util.Map;

public class CreateToken {
	public String createToken(Map<String, String> variables) {
		int sessionLength = Integer.parseInt(variables.get("SESSION_LENGTH"));
		String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		StringBuilder token = new StringBuilder();
		
		for(int i = 0; i < sessionLength; i++) {
			double random = Math.random();
			int myindex = (int)(alphaNumeric.length() * random);
			
			token.append(alphaNumeric.charAt(myindex));
		}
		
		return token.toString();
	}
}