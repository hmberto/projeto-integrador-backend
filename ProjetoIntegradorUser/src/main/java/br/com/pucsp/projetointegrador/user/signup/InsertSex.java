package br.com.pucsp.projetointegrador.user.signup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.utils.LogMessage;
import br.com.pucsp.projetointegrador.utils.SQLStrings;

public class InsertSex {
	private static String name = InsertSex.class.getSimpleName();
	private static Logger log = Logger.getLogger(InsertSex.class.getName());
	
	private InsertSex () {}
	
	public static String insertSex(Map<String, String> variables, String sex) throws SQLException, ClassNotFoundException {
		log.entering(name, "insertSex");
		String genero = "";
		
		if (sex.equals("1")) {
			genero = "Não informar";
		} else if (sex.equals("2")) {
			genero = "Masculino";
		} else if (sex.equals("3")) {
			genero = "Feminino";
		} else {
			genero = "Outros";
		}
		
		String insertIfNotExistsSex = SQLStrings.insertIfNotExistsSex(genero, sex);
		PreparedStatement statement = DB.connect(variables).prepareStatement(insertIfNotExistsSex);
		
		try {
			statement.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		String getIdSex = "SELECT id_genero FROM Genero WHERE (nome LIKE ?) AND (sigla LIKE ?);";
		PreparedStatement getIdSexStat = DB.connect(variables).prepareStatement(getIdSex);
		String idSexGetted = "";
		
		try {
			getIdSexStat.setString(1, genero);
			getIdSexStat.setString(2, sex);
			
			ResultSet g = getIdSexStat.executeQuery();
			while(g.next()) {
				idSexGetted = g.getString(1);
			}
		}
		catch (Exception e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			getIdSexStat.close();
			DB.disconnect();
		}
		
		log.exiting(name, "insertSex");
		return idSexGetted;
	}
}