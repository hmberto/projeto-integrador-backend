package br.com.pucsp.projetointegrado.farmacias.utils;

import java.util.Map;
import java.util.logging.Level;
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
		if(nomeSeparado.length >= 2 && user.getName().length() < 50 && validateName) {
			LOG.log(Level.INFO, "checkData.checkData: Name OK");
		}
		else { 
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect Name");
			test = false; 
		}
		
		// Verifica CPF
		boolean validateCpfA = user.getCpf().matches(variables.get("REGEX_NUMBER"));
		boolean validateCpfB = user.getCpf().matches(variables.get("REGEX_CPF"));
		if(user.getCpf().length() == 11 && validateCpfA) {
			LOG.log(Level.INFO, "checkData.checkData: CPF OK");
		}
		else if (user.getCpf().length() == 14 && validateCpfB) {
			String cpf1 = user.getCpf().replace(".", "");
			String cpf2 = cpf1.replace("-", "");
			
			user.setCpf(cpf2);
			LOG.log(Level.INFO, "checkData.checkData: CPF OK");
		}
		else { 
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect CPF");
			test = false; 
		}
		
		// Verifica Data de Nascimento
		boolean searchForCharactersD = user.getBirthDate().matches(variables.get("REGEX_DATE"));
		if(searchForCharactersD) {
			LOG.log(Level.INFO, "checkData.checkData: Birth Date OK");
		}
		else {
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect Birth Date");
			test = false; 
		}
		
		// Verifica Sexo
		boolean searchForCharactersE = user.getSex().matches("[1-4]");
		if(searchForCharactersE) {
			LOG.log(Level.INFO, "checkData.checkData: Sex OK");
		}
		else {
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect Sex");
			test = false; 
		}
		
		// Verifica E-mail
		boolean searchForCharactersF = user.getEmail().matches(variables.get("REGEX_EMAIL"));
		if(user.getEmail().length() < 60 && searchForCharactersF) {
			LOG.log(Level.INFO, "checkData.checkData: Email OK");
		}
		else { 
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect Email");
			test = false; 
		}
		
		// Verifica Rua
		boolean searchForCharactersI = user.getStreet().matches(variables.get("REGEX_NAMES_2"));
		if(user.getStreet().length() < 100 && searchForCharactersI) {
			LOG.log(Level.INFO, "checkData.checkData: Street OK");
		}
		else { 
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect Street");
			test = false; 
		}
		
		// Verifica Número da casa
		boolean searchForCharactersJ = user.getNumber().matches(variables.get("REGEX_NUMBER"));
		if(user.getNumber().length() >= 1 && user.getNumber().length() < 10 && searchForCharactersJ) {
			LOG.log(Level.INFO, "checkData.checkData: House Number OK");
		}
		else { 
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect House Number");
			test = false; 
		}
		
		// Verifica Complemento
		boolean searchForCharactersR = user.getComplement().matches(variables.get("REGEX_NAMES_2"));
		if(user.getComplement().length() < 50 && searchForCharactersR) {
			LOG.log(Level.INFO, "checkData.checkData: Complement OK");
		}
		else {
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect Complement");
			test = false; 
		}
		
		// Verifica Bairro
		boolean searchForCharactersK = user.getDistrict().matches(variables.get("REGEX_NAMES_1"));
		if(user.getDistrict().length() < 30 && searchForCharactersK) {
			LOG.log(Level.INFO, "checkData.checkData: District OK");
		}
		else {
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect District");
			test = false; 
		}
		
		// Verifica CEP
		boolean searchForCharactersL = user.getZipCode().matches("[0-9]{5}[-][0-9]{3}");
		if(user.getZipCode().length() == 9 && searchForCharactersL) {
			LOG.log(Level.INFO, "checkData.checkData: Zip Code OK");
		}
		else {
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect Zip Code");
			test = false; 
		}
		
		// Verifica Cidade
		boolean searchForCharactersM = user.getCity().matches(variables.get("REGEX_NAMES_1"));
		if(user.getCity().length() < 30 && searchForCharactersM) {
			LOG.log(Level.INFO, "checkData.checkData: City OK");
		}
		else {
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect City");
			test = false; 
		}
		
		// Verifica Estado
		boolean searchForCharactersN = user.getState().matches("[A-Z]{2}");
		if(searchForCharactersN) {
			LOG.log(Level.INFO, "checkData.checkData: State OK");
		}
		else {
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect State");
			test = false; 
		}
		
		// Verifica Senha
		boolean searchForCharactersP = user.getPass().matches(variables.get("REGEX_PASS"));
		if(user.getPass().length() >= MIN_PASS_LENGTH && user.getPass().length() < MAX_PASS_LENGTH && searchForCharactersP) {
			LOG.log(Level.INFO, "checkData.checkData: Pass OK");
		}
		else { 
			LOG.log(Level.SEVERE, "DcheckData.checkData: Incorrect Pass");
			test = false; 
		}
		
		LOG.exiting(NAME, "checkData");
		return test;
	}
}