package br.com.pucsp.projetointegrador.deliveryman;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.deliveryman.signup.CreateDeliveryman;
import br.com.pucsp.projetointegrador.deliveryman.signup.SignUpDeliverymanDB;

public class SignUpDeliveryman {
	private static String name =  SignUpDeliveryman.class.getSimpleName();
	private static Logger log = Logger.getLogger( SignUpDeliveryman.class.getName());
	
	public boolean createUser(Map<String, String> variables, CreateDeliveryman user) throws SQLException, ClassNotFoundException, MessagingException, IOException {
		log.entering(name, "createUser");
		
		String cpf = user.getCpf();
		cpf.replace(".", "");
		cpf.replace("-", "");
		user.setCpf(cpf);
		
		System.out.println(user.getCpf());
		
		SignUpDeliverymanDB signUpDeliveryman = new SignUpDeliverymanDB();
		boolean create = signUpDeliveryman.createUserDB(variables, user);
		
		log.exiting(name, "createUser");
		return create;
	}
}