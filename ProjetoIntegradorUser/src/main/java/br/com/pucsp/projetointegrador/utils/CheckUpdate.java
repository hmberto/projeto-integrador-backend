package br.com.pucsp.projetointegrador.utils;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.user.updateusers.UpdateUsers;

public class CheckUpdate {
	public static String NAME = CheckUpdate.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(CheckUpdate.class.getName());
	
	public boolean checkData(Map<String, String> variables, UpdateUsers user) {
		LOG.entering(NAME, "checkData");
		
		boolean test = true;

		// Verifica Nome
		boolean validateName = user.getName().matches(variables.get("REGEX_NAMES_1"));
		String[] nomeSeparado = user.getName().split(" ");
		if(nomeSeparado.length >= 2 && user.getName().length() < 50 && validateName) {
			LOG.log(Level.INFO, "CheckUpdate.checkData: Name OK");
		}
		else { 
			LOG.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect Name");
			test = false; 
		}
		
		// Verifica Sexo
		boolean searchForCharactersE = user.getSex().matches("[1-4]");
		if(searchForCharactersE) {
			LOG.log(Level.INFO, "CheckUpdate.checkData: Sex OK");
		}
		else {
			LOG.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect Sex");
			test = false; 
		}
		
		// Verifica Rua
		boolean searchForCharactersI = user.getStreet().matches(variables.get("REGEX_NAMES_2"));
		if(user.getStreet().length() < 100 && searchForCharactersI) {
			LOG.log(Level.INFO, "CheckUpdate.checkData: Street OK");
		}
		else { 
			LOG.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect Street");
			test = false; 
		}
		
		// Verifica Número da casa
		boolean searchForCharactersJ = user.getNumber().matches(variables.get("REGEX_NUMBER"));
		if(user.getNumber().length() >= 1 && user.getNumber().length() < 10 && searchForCharactersJ) {
			LOG.log(Level.INFO, "CheckUpdate.checkData: House Number OK");
		}
		else { 
			LOG.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect House Number");
			test = false; 
		}
		
		// Verifica Complemento
		boolean searchForCharactersR = user.getComplement().matches(variables.get("REGEX_NAMES_2"));
		if(user.getComplement().length() < 50 && searchForCharactersR) {
			LOG.log(Level.INFO, "CheckUpdate.checkData: Complement OK");
		}
		else {
			LOG.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect Complement");
			test = false; 
		}
		
		// Verifica Bairro
		boolean searchForCharactersK = user.getDistrict().matches(variables.get("REGEX_NAMES_1"));
		if(user.getDistrict().length() < 30 && searchForCharactersK) {
			LOG.log(Level.INFO, "CheckUpdate.checkData: District OK");
		}
		else {
			LOG.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect District");
			test = false; 
		}
		
		// Verifica CEP
		boolean searchForCharactersL = user.getZipCode().matches("[0-9]{5}[-][0-9]{3}");
		if(user.getZipCode().length() == 9 && searchForCharactersL) {
			LOG.log(Level.INFO, "CheckUpdate.checkData: Zip Code OK");
		}
		else {
			LOG.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect Zip Code");
			test = false; 
		}
		
		// Verifica Cidade
		boolean searchForCharactersM = user.getCity().matches(variables.get("REGEX_NAMES_1"));
		if(user.getCity().length() < 30 && searchForCharactersM) {
			LOG.log(Level.INFO, "CheckUpdate.checkData: City OK");
		}
		else {
			LOG.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect City");
			test = false; 
		}
		
		// Verifica Estado
		boolean searchForCharactersN = user.getState().matches("[A-Z]{2}");
		if(searchForCharactersN) {
			LOG.log(Level.INFO, "CheckUpdate.checkData: State OK");
		}
		else {
			LOG.log(Level.SEVERE, "CheckUpdate.checkData: Incorrect State");
			test = false; 
		}
		
		LOG.exiting(NAME, "checkData");
		return test;
	}
}