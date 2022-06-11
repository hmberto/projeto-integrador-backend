package br.com.pucsp.projetointegrador.utils;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.user.updateusers.UpdateUsers;

public class CheckUpdate {
	private static String name = CheckUpdate.class.getSimpleName();
	private static Logger log = Logger.getLogger(CheckUpdate.class.getName());
	
	public boolean checkData(Map<String, String> variables, UpdateUsers user) {
		log.entering(name, "checkData");
		
		String regexName = variables.get("REGEX_NAMES_1");
		
		boolean test = true;

		// Verifica Nome
		boolean validateName = user.getName().matches(regexName);
		String[] nomeSeparado = user.getName().split(" ");
		if(nomeSeparado.length < 2 || user.getName().length() > 50 || !validateName) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Name");
			test = false; 
		}
		
		// Verifica Sexo
		boolean searchForCharactersE = user.getSex().matches("[1-4]");
		if(!searchForCharactersE) {
			log.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect Sex");
			test = false;
		}
		
		// Verifica Rua
		boolean searchForCharactersI = user.getStreet().matches(variables.get("REGEX_NAMES_2"));
		if(user.getStreet().length() >= 100 || !searchForCharactersI) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Street");
			test = false; 
		}
		
		// Verifica Número da casa
		boolean searchForCharactersJ = user.getNumber().matches(variables.get("REGEX_NUMBER"));
		if(user.getNumber().length() < 1 || user.getNumber().length() >= 10 || !searchForCharactersJ) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect House Number");
			test = false; 
		}
		
		// Verifica Complemento
		boolean searchForCharactersR = user.getComplement().matches(variables.get("REGEX_NAMES_2"));
		if(user.getComplement().length() > 50 || !searchForCharactersR) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Complement");
			test = false; 
		}
		
		// Verifica Bairro
		boolean searchForCharactersK = user.getDistrict().matches(regexName);
		if(!searchForCharactersK) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect District");
			test = false; 
		}
		
		// Verifica CEP
		boolean searchForCharactersL = user.getZipCode().matches("[0-9]{5}[-][0-9]{3}");
		if(user.getZipCode().length() != 9 || !searchForCharactersL) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect Zip Code");
			test = false; 
		}
		
		// Verifica Cidade
		boolean searchForCharactersM = user.getCity().matches(regexName);
		if(!searchForCharactersM) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect City");
			test = false;
		}
		
		// Verifica Estado
		boolean searchForCharactersN = user.getState().matches("[A-Z]{2}");
		if(!searchForCharactersN) {
			log.log(Level.SEVERE, "DcheckData.checkData: Incorrect State");
			test = false;
		}
		
		log.exiting(name, "checkData");
		return test;
	}
}