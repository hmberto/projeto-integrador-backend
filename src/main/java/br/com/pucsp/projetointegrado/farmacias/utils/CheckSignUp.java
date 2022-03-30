package br.com.pucsp.projetointegrado.farmacias.utils;

import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.client.signup.CreateUsers;

public class CheckSignUp {
	public static String NAME = CheckSignUp.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(CheckSignUp.class.getName());
	
	public boolean checkData(Map<String, String> variables, CreateUsers user) {
		LOG.entering(NAME, "checkData");
		
		int MIN_PASS_LENGTH = Integer.parseInt(variables.get("MIN_PASS_LENGTH"));
		int MAX_PASS_LENGTH = Integer.parseInt(variables.get("MAX_PASS_LENGTH"));
		
		boolean test = true;

		// Verifica Nome
		boolean validateName = user.getName().matches(variables.get("REGEX_NAMES_1"));
		String[] nomeSeparado = user.getName().split(" ");
		if(nomeSeparado.length >= 2 && user.getName().length() < 50 && validateName) {}
		else { test = false; }
		
		// Verifica CPF
		boolean validateCpfA = user.getCpf().matches(variables.get("REGEX_NUMBER"));
		boolean validateCpfB = user.getCpf().matches(variables.get("REGEX_CPF"));
		if(user.getCpf().length() == 11 && validateCpfA) {}
		else if (user.getCpf().length() == 14 && validateCpfB) {
			String cpf1 = user.getCpf().replace(".", "");
			String cpf2 = cpf1.replace("-", "");
			
			user.setCpf(cpf2);
		}
		else { test = false; }
		
		// Verifica Data de Nascimento
		boolean searchForCharactersD = user.getBirthDate().matches(variables.get("REGEX_DATE"));
		if(searchForCharactersD) {}
		else { test = false; }
		
		// Verifica Sexo
		boolean searchForCharactersE = user.getSex().matches("[1-4]");
		if(searchForCharactersE) {}
		else { test = false; }
		
		// Verifica E-mail
		boolean searchForCharactersF = user.getEmail().matches(variables.get("REGEX_EMAIL"));
		if(user.getEmail().length() < 60 && searchForCharactersF) {}
		else { test = false; }
		
		// Verifica Rua
		boolean searchForCharactersI = user.getStreet().matches(variables.get("REGEX_NAMES_2"));
		if(user.getStreet().length() < 100 && searchForCharactersI) {}
		else { test = false; }
		
		// Verifica Número da casa
		boolean searchForCharactersJ = user.getNumber().matches(variables.get("REGEX_NUMBER"));
		if(user.getNumber().length() >= 1 && user.getNumber().length() < 10 && searchForCharactersJ) {}
		else { test = false; }
		
		// Verifica Complemento
		boolean searchForCharactersR = user.getComplement().matches(variables.get("REGEX_NAMES_2"));
		if(user.getComplement().length() < 50 && searchForCharactersR) {}
		else { test = false; }
		
		// Verifica Bairro
		boolean searchForCharactersK = user.getDistrict().matches(variables.get("REGEX_NAMES_1"));
		if(user.getDistrict().length() < 30 && searchForCharactersK) {}
		else { test = false; }
		
		// Verifica CEP
		boolean searchForCharactersL = user.getZipCode().matches("[0-9]{5}[-][0-9]{3}");
		if(user.getZipCode().length() == 9 && searchForCharactersL) {}
		else { test = false; }
		
		// Verifica Cidade
		boolean searchForCharactersM = user.getCity().matches(variables.get("REGEX_NAMES_1"));
		if(user.getCity().length() < 30 && searchForCharactersM) {}
		else { test = false; }
		
		// Verifica Estado
		boolean searchForCharactersN = user.getState().matches("[A-Z]{2}");
		if(searchForCharactersN) {}
		else { test = false; }
		
		// Verifica Senha
		boolean searchForCharactersP = user.getPass().matches(variables.get("REGEX_PASS"));
		if(user.getPass().length() >= MIN_PASS_LENGTH && user.getPass().length() < MAX_PASS_LENGTH && searchForCharactersP) {}
		else { test = false; }
		
		LOG.exiting(NAME, "checkData");
		return test;
	}
}