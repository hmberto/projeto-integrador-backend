package br.com.pucsp.projetointegrador.utils;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.user.signup.CreateUsers;

public class CheckSignUp {
	private static String name = CheckSignUp.class.getSimpleName();
	private static Logger log = Logger.getLogger(CheckSignUp.class.getName());
	
	public boolean checkData(Map<String, String> variables, CreateUsers user) {
		log.entering(name, "checkData");
		
		String regexName = variables.get("REGEX_NAMES_1");
		
		boolean test = true;

		// Verifica Complemento
		boolean searchForCharactersR = user.getComplement().matches(variables.get("REGEX_NAMES_2"));
		if(user.getComplement().length() > 50 || !searchForCharactersR) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Complement");
			test = false; 
		}
		
		// Verifica CPF
		String cpf1 = user.getCpf().replace(".", "");
		String cpf2 = cpf1.replace("-", "");
		
		user.setCpf(cpf2);
		
		boolean validateCpfA = user.getCpf().matches(variables.get("REGEX_NUMBER"));
		if(!validateCpfA) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect CPF");
			test = false; 
		}
		
		// Verifica E-mail
		boolean searchForCharactersF = user.getEmail().toLowerCase().matches(variables.get("REGEX_EMAIL"));
		if(!searchForCharactersF) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Email");
			test = false; 
		}
		
		// Verifica Data de Nascimento
		boolean searchForCharactersD = user.getBirthDate().matches(variables.get("REGEX_DATE"));
		if(!searchForCharactersD) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Birth Date");
			test = false; 
		}
		
		// Verifica Sexo
		boolean searchForCharactersE = user.getSex().matches("[1-4]");
		if(!searchForCharactersE) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Sex");
			test = false; 
		}
		
		// Verifica Nome
		boolean validateName = user.getName().matches(regexName);
		String[] nomeSeparado = user.getName().split(" ");
		if(nomeSeparado.length < 2 || user.getName().length() > 50 || !validateName) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Name");
			test = false; 
		}
		
		// Verifica Rua
		boolean searchForCharactersI = user.getStreet().matches(variables.get("REGEX_NAMES_2"));
		if(!searchForCharactersI) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Street");
			test = false; 
		}
		
		// Verifica CEP
		boolean searchForCharactersL = user.getZipCode().matches("[0-9]{5}[-][0-9]{3}");
		if(!searchForCharactersL) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Zip Code");
			test = false; 
		}
		
		// Verifica Número da casa
		boolean searchForCharactersJ = user.getNumber().matches(variables.get("REGEX_NUMBER"));
		if(!searchForCharactersJ) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect House Number");
			test = false; 
		}
		
		// Verifica Estado
		boolean searchForCharactersN = user.getState().matches("[A-Z]{2}");
		if(!searchForCharactersN) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect State");
			test = false;
		}
		
		// Verifica Bairro
		boolean searchForCharactersK = user.getDistrict().matches(regexName);
		if(!searchForCharactersK) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect District");
			test = false; 
		}
		
		// Verifica Cidade
		boolean searchForCharactersM = user.getCity().matches(regexName);
		if(!searchForCharactersM) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect City");
			test = false;
		}
		
		// Verifica Senha
		boolean searchForCharactersP = user.getPass().matches(variables.get("REGEX_PASS"));
		if(!searchForCharactersP) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Pass");
			test = false;
		}
		
		log.exiting(name, "checkData");
		return test;
	}
}