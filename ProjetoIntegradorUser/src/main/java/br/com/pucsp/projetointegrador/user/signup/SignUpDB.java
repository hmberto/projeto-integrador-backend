package br.com.pucsp.projetointegrador.user.signup;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.CreateToken;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class SignUpDB {
	private static String name = SignUpDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(SignUpDB.class.getName());
	
	public boolean createUserDB(Map <String, String> variables, CreateUsers user, String lat, String lon) throws SQLException, ClassNotFoundException, MessagingException {
		log.entering(name, "CreateUserDB");
		
		CreateToken createToken = new CreateToken();
		String emailSession = createToken.createToken(variables);
		
		String idAddress = InsertAddress.insertAddress(variables, user, lat, lon);
		String idSex = InsertSex.insertSex(variables, user.getSex());
		
		String sql = "INSERT INTO Usuario (nome, email, cpf, ativo, birth_date, token_confirmacao_cadastro, id_genero, id_endereco) values (?, ?, ?, ?, ?, ?, ?, ?);";	
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
		
		try {
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail().toLowerCase());
			statement.setString(3, user.getCpf());
			statement.setBoolean(4, false);
			statement.setString(5, user.getBirthDate());
			statement.setString(6, emailSession);
			statement.setInt(7, Integer.parseInt(idSex));
			statement.setInt(8, Integer.parseInt(idAddress));
						
			statement.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		String userId = GetUserID.getUserID(variables, user.getEmail().toLowerCase());
		String passId = InsertPass.insertPass(variables, user.getPass(), userId, emailSession);
		
		String sqlPass = "INSERT INTO Login_Sessao (id_sessao, id_usuario, id_senha) values (?, ?, ?);";
		PreparedStatement statementSession = DB.connect(variables).prepareStatement(sqlPass);
		
		try {
			statementSession.setString(1, "NULL");
			statementSession.setString(2, userId);
			statementSession.setString(3, passId);
						
			statementSession.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementSession.close();
			DB.disconnect();
		}
		
		String welcome = "";
		if (user.getSex().equals("2")) {
			welcome = "Bem vindo, ";
		} else if (user.getSex().equals("3")) {
			welcome = "Bem vinda, ";
		} else {
			welcome = "Bem vindx, ";
		}
		
		String[] nomeSeparado = user.getName().split(" ");
		
		String messageSubject = "Entrega de Farmácias - Confirme seu e-mail";
		String shortText = welcome + nomeSeparado[0] + "! Confirme que este é seu endereço de e-mail.";
		String info = "Clique no link abaixo para confirmar seu e-mail e liberar o acesso ao site.<br><br>Se você não é " + user.getName() + ", desconsidere este e-mail.";
		String btnText = "Confirmar e-mail";
		String btnLink = "https://projeto-integrador-user.herokuapp.com/user/confirm-email/" + user.getEmail().toLowerCase() + "/" + emailSession;
		String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
		
		EmailConfirmation sendEmail = new EmailConfirmation();
		sendEmail.confirmation(user.getEmail().toLowerCase(), messageSubject, messageText);
		
		log.exiting(name, "CreateUserDB");
		return true;
	}
}