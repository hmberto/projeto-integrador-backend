package br.com.pucsp.projetointegrado.farmacias.utils;

import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.client.signup.CreateUsers;

public class CheckSignUp {
	public static String NAME = CheckSignUp.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(CheckSignUp.class.getName());
	
	public boolean checkData(CreateUsers user) {
		LOG.entering(NAME, "CreateUser");
		
		boolean test = true;

		// Verifica Nome
		boolean searchForCharactersA = user.getName().matches("[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ \\s]+");
		if(user.getName().length() < 250 && searchForCharactersA) {}
		else { test = false; }
		
		// Verifica CPF
		boolean searchForCharactersB = user.getCpf().matches("[0-9]+");
		if(user.getCpf().length() == 11 && searchForCharactersB) {}
		else { test = false; }
		
		// Verifica Data de Nascimento
		boolean searchForCharactersD = user.getBirthDate().matches("[0-9]{4}[-|/][0-9]{2}[-|/][0-9]{2}");
		if(searchForCharactersD) {}
		else { test = false; }
		
		// Verifica Sexo
		boolean searchForCharactersE = user.getSex().matches("[1-4]");
		if(searchForCharactersE) {}
		else { test = false; }
		
		// Verifica E-mail
		boolean searchForCharactersF = user.getEmail().matches("[0-9 a-z A-Z .]+@[0-9 a-z A-Z .]+");
		if(user.getEmail().length() < 100 && searchForCharactersF) {}
		else { test = false; }
		
		// Verifica Rua
		boolean searchForCharactersI = user.getStreet().matches("[a-z A-Z ç Ç ã Ã á Á à À í Í é É ê Ê â Â ó Ó ô Ô ú Ú . -]+");
		if(user.getStreet().length() < 100 && searchForCharactersI) {}
		else { test = false; }
		
		// Verifica Número da casa
		boolean searchForCharactersJ = user.getNumber().matches("[0-9]+");
		if(user.getNumber().length() < 10 && searchForCharactersJ) {}
		else { test = false; }
		
		// Verifica Complemento
		boolean searchForCharactersR = user.getComplement().matches("[0-9 a-z A-Z - .]+");
		if(user.getComplement().length() < 100 && searchForCharactersR) {}
		else { test = false; }
		
		// Verifica Bairro
		boolean searchForCharactersK = user.getDistrict().matches("[^0-9]+");
		if(user.getDistrict().length() < 100 && searchForCharactersK) {}
		else { test = false; }
		
		// Verifica CEP
		boolean searchForCharactersL = user.getZipCode().matches("[0-9]{5}[-][0-9]{3}");
		if(user.getZipCode().length() < 100 && searchForCharactersL) {}
		else { test = false; }
		
		// Verifica Cidade
		boolean searchForCharactersM = user.getCity().matches("[a-z A-Z ç Ç ã Ã á Á à À í Í é É ê Ê â Â ó Ó ô Ô ú Ú]+");
		if(user.getCity().length() < 50 && searchForCharactersM) {}
		else { test = false; }
		
		// Verifica Estado
		boolean searchForCharactersN = user.getState().matches("[a-z A-Z ç Ç ã Ã á Á à À í Í é É ê Ê â Â ó Ó ô Ô ú Ú]+");
		if(user.getState().length() < 50 && searchForCharactersN) {}
		else { test = false; }
		
		// Verifica Senha
		boolean searchForCharactersP = user.getPass().matches("[0-9 a-z A-Z ! @ # _ - .]+");
		if(user.getPass().length() < 21 && searchForCharactersP) {}
		else { test = false; }
				
		LOG.exiting(NAME, "CreateUser");
		return test;
	}
}