package br.com.pucsp.projetointegrador.user.signup;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.CreateToken;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;

public class SignUpDB {
	public static String NAME = SignUpDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SignUpDB.class.getName());
	
	public boolean CreateUserDB(Map <String, String> variables, CreateUsers user, String lat, String lon) {
		LOG.entering(NAME, "CreateUserDB");
		
		CreateToken createToken = new CreateToken();
		String emailSession = createToken.createToken(variables);
		
		try {
			String idAddress = InsertAddress.insertAddress(variables, user, lat, lon);
			String idSex = InsertSex.insertSex(variables, user.getSex());
			
			String sql = "INSERT INTO Usuario (nome, email, cpf, ativo, birth_date, token_confirmacao_cadastro, id_genero, id_endereco) values (?, ?, ?, ?, ?, ?, ?, ?);";	
			
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
			
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail().toLowerCase());
			statement.setString(3, user.getCpf());
			statement.setBoolean(4, false);
			statement.setString(5, user.getBirthDate());
			statement.setString(6, emailSession);
			statement.setInt(7, Integer.parseInt(idSex));
			statement.setInt(8, Integer.parseInt(idAddress));
						
			statement.execute();
			statement.close();
			
			String userId = GetUserID.getUserID(variables, user.getEmail().toLowerCase());
			String passId = InsertPass.insertPass(variables, user.getPass(), userId, emailSession);
			
			String sqlPass = "INSERT INTO Login_Sessao (id_sessao, id_usuario, id_senha) values (?, ?, ?);";
			PreparedStatement statementSession = DB.connect(variables).prepareStatement(sqlPass);
			statementSession.setString(1, "NULL");
			statementSession.setString(2, userId);
			statementSession.setString(3, passId);
						
			statementSession.execute();
			statementSession.close();
			
			LOG.log(Level.INFO, "User created on database. ID: " + userId + " - Name: " + user.getName() + " - Email: " + user.getEmail().toLowerCase());
			
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
			
			LOG.exiting(NAME, "CreateUserDB");
			return true;
		}
		catch(Exception e) {
			LOG.log(Level.SEVERE, "User not created on the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "CreateUserDB");
		return false;
	}
}