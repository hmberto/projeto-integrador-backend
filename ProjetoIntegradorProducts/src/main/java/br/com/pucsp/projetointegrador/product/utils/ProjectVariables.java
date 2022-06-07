package br.com.pucsp.projetointegrador.product.utils;

import java.util.HashMap;
import java.util.Map;

public class ProjectVariables {
	public Map<String, String>projectVariables() {
		String tableClients = "client";
		
		Map <String, String> variables = new HashMap<String, String>();
		
		variables.put("SESSION_LENGTH", "100");
		variables.put("MIN_PASS_LENGTH", "8");
		variables.put("MAX_PASS_LENGTH", "25");
		
		variables.put("DATABASE_DRIVER", "com.mysql.cj.jdbc.Driver");
		variables.put("DATABASE_URL", System.getenv("DATABASE_URL"));
		variables.put("USERNAME", System.getenv("USERNAME"));
		variables.put("PASSWORD", System.getenv("PASSWORD"));
		variables.put("MAX_POOL", "250");
		
		variables.put("EMAIL_PROJETO", System.getenv("EMAIL_PROJETO"));
		variables.put("SENHA_EMAIL_PROJETO", System.getenv("SENHA_EMAIL_PROJETO"));
		
		variables.put("LOGIN_1", "SELECT * FROM " + tableClients + " WHERE (email LIKE ?) AND (pass LIKE ?);");
		variables.put("LOGIN_2", "UPDATE " + tableClients + " SET session_id = ? WHERE (email LIKE ?);");
		
		variables.put("SIGNUP", "INSERT INTO " + tableClients + " (name, email, pass, street, house_number, complement, zip_code, state, city, district, cpf, birth_date, sex, email_confirmation, email_confirmed, lat, lon) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		
		variables.put("REGEX_NAMES_1", "[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ \\s]+");
		variables.put("REGEX_NAMES_2", "[0-9A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ . - \\s]+");
		variables.put("REGEX_NUMBER", "[0-9]+");
		variables.put("REGEX_CPF", "[0-9]{3}[.][0-9]{3}[.][0-9]{3}[-][0-9]{2}");
		variables.put("REGEX_DATE", "[0-9]{4}[-|/][0-9]{2}[-|/][0-9]{2}");
		variables.put("REGEX_EMAIL", "[0-9 a-z A-Z - _ .]+@[0-9 a-z A-Z - _ .]+");
		variables.put("REGEX_PASS", "[0-9 a-z A-Z - _ . ! @ #]+");
		
		return variables;
	}
}