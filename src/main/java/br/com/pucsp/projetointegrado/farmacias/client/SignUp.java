package br.com.pucsp.projetointegrado.farmacias.client;

import br.com.pucsp.projetointegrado.farmacias.client.signup.CreateUsers;
import br.com.pucsp.projetointegrado.farmacias.client.signup.SignUpDB;
import br.com.pucsp.projetointegrado.farmacias.utils.CheckSignUp;

public class SignUp {
	public boolean createUser(int SESSION_LENGTH, CreateUsers user) {
		CheckSignUp validate = new CheckSignUp();
		boolean check = validate.checkData(user);
		
		SignUpDB createUser = new SignUpDB();
		boolean create = createUser.CreateUserDB(SESSION_LENGTH, user);
		
		if(check && create) {
			return true;			
		}
		else {
			return false;
		}
	}
}