package br.com.pucsp.projetointegrador.deliveryman.login;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.CreateToken;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;
import br.com.pucsp.projetointegrador.utils.GetDate;
import br.com.pucsp.projetointegrador.utils.GetZone;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class LoginDeliverymanDB {
	private static String name = LoginDeliverymanDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(LoginDeliverymanDB.class.getName());
	
	public Map<String, String> loginUser(Map<String, String> variables, String cpf) throws MessagingException, ClassNotFoundException, SQLException, IOException {
		log.entering(name, "LoginUser");
		
		GetFromDB getFromDB = new GetFromDB();
		
		CreateToken createToken = new CreateToken();
		String userSession = createToken.createToken(variables);
		
		String sql1 = "SELECT * FROM Entregador WHERE (cpf LIKE ?) AND (ativo LIKE ?);";
		PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
		Map<String, String> getUser = new HashMap<String, String>();
		
		try {
			statement1.setString(1, cpf);
			statement1.setBoolean(2, true);
			
			getUser = getFromDB.getFromDB(statement1);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement1.close();
			DB.disconnect();
		}
		
		log.exiting(name, "LoginUser");
		return getUser;
	}
}