package br.com.pucsp.projetointegrador.deliveryman;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.deliveryman.login.LogInDeliverymanUser;
import br.com.pucsp.projetointegrador.deliveryman.login.LoginDeliverymanDB;

public class LogInDeliveryman {
	private static String name = LogInDeliveryman.class.getSimpleName();
	private static Logger log = Logger.getLogger(LogInDeliveryman.class.getName());
	
	public Map<String, String> authenticateUser(Map<String, String> variables, LogInDeliverymanUser login) throws MessagingException, ClassNotFoundException, SQLException, IOException {
		log.entering(name, "authenticateUser");
		
		LoginDeliverymanDB loginDB = new LoginDeliverymanDB();
		Map<String, String> user = loginDB.loginUser(variables, login.getCpf());
		
		log.exiting(name, "authenticateUser");
		return user;
	}
}