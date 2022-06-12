package br.com.pucsp.projetointegrador.deliveryman.signup;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class SignUpDeliverymanDB {
	private static String name = SignUpDeliverymanDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(SignUpDeliverymanDB.class.getName());
	
	public boolean createUserDB(Map <String, String> variables, CreateDeliveryman user) throws SQLException, ClassNotFoundException, MessagingException {
		log.entering(name, "CreateUserDB");
		
		String sql = "INSERT INTO Entregador (nome, cpf, cnh, categoria_cnh, ativo) values (?, ?, ?, ?, ?);";	
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
		
		try {
			statement.setString(1, user.getName());
			statement.setString(2, user.getCpf());
			statement.setString(3, user.getCnh());
			statement.setString(4, user.getCategory());
			statement.setBoolean(5, true);
						
			statement.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		log.exiting(name, "CreateUserDB");
		return true;
	}
}