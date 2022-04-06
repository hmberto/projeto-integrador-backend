package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;

import br.com.pucsp.projetointegrado.farmacias.client.updateusers.UpdateUsers;
import br.com.pucsp.projetointegrado.farmacias.client.updateusers.UpdateUsersDB;

public class Update {
	public boolean updateUsers(Map<String, String> variables, UpdateUsers user, int SESSION_LENGTH) {
		UpdateUsersDB updateUsersDB = new UpdateUsersDB();
		boolean check = updateUsersDB.updateUsersDB(variables, user, SESSION_LENGTH);
		
		return check;
	}
}