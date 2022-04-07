package br.com.pucsp.projetointegrador.user;

import java.util.Map;

import br.com.pucsp.projetointegrador.user.updateusers.UpdateUsers;
import br.com.pucsp.projetointegrador.user.updateusers.UpdateUsersDB;

public class Update {
	public boolean updateUsers(Map<String, String> variables, UpdateUsers user, int SESSION_LENGTH) {
		UpdateUsersDB updateUsersDB = new UpdateUsersDB();
		boolean check = updateUsersDB.updateUsersDB(variables, user, SESSION_LENGTH);
		
		return check;
	}
}