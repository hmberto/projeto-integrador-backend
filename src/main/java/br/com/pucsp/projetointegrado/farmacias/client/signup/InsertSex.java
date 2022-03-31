package br.com.pucsp.projetointegrado.farmacias.client.signup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.db.DB;

public class InsertSex {
	public static String NAME = InsertSex.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(InsertSex.class.getName());
	
	public static String insertSex(Map<String, String> variables, String sex) {
		LOG.entering(NAME, "insertSex");
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
		
		String INSERT_IF_NOT_EXISTS_SEX = "INSERT INTO Genero ( `nome`, `sigla` )\n"
				+ "    SELECT * FROM (SELECT '" + genero + "', '" + sex + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `sigla` FROM `Genero` WHERE `nome` = '" + genero + "' \n"
				+ "    ) LIMIT 1";
		
		try {
			PreparedStatement statement = DB.connect(variables).prepareStatement(INSERT_IF_NOT_EXISTS_SEX);
			statement.execute();
			statement.close();
			
			String getIdSex = "SELECT id_genero FROM Genero WHERE (nome LIKE ?) AND (sigla LIKE ?);";
			
			PreparedStatement getIdSexStat = DB.connect(variables).prepareStatement(getIdSex);
			getIdSexStat.setString(1, genero);
			getIdSexStat.setString(2, sex);
			
			String idSexGetted = "";
			
			ResultSet g = getIdSexStat.executeQuery();
			while(g.next()) {
				idSexGetted = g.getString(1);
			}
			
			getIdSexStat.close();
			
			LOG.log(Level.INFO, "User sex created at genre table! Genre ID: " + idSexGetted);
			LOG.exiting(NAME, "insertSex");
			return idSexGetted;
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "InsertSex.insertSex: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "insertSex");
		return "";
	}
}