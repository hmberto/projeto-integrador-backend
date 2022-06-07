package br.com.pucsp.projetointegrador.product.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.product.db.DB;
import br.com.pucsp.projetointegrador.product.db.GetFromDB;

public class GetUserCoordinates {
	private static String name = GetUserCoordinates.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetUserCoordinates.class.getName());
	
	GetFromDB getFromDB = new GetFromDB();
	
	public String coordinates(String street, String number) throws IOException {
		String methodName = "coordinates";
		log.entering(name, methodName);
		String[] streetSplit= street.split(" ");
		
		StringBuilder address = new StringBuilder();
		for(int i = 0; i < streetSplit.length; i++) {
			if(i == 0) {
				address.append(streetSplit[i]);
			}
			else {
				address.append("+" + streetSplit[i]);
			}
		}
		
		log.log(Level.INFO, "address: " + address.toString());
		
		String url = "https://nominatim.openstreetmap.org/search.php?street=" + address.toString() + "%2c" + number + "&format=jsonv2";
		
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("GET");
        
        if (conn.getResponseCode() != 200) {
        	throw new ProtocolException(LogMessage.message("API '" + url + "' - Status Code " + conn.getResponseCode()));
        }
        
        log.log(Level.INFO, "API '" + url + "' - Status Code " + conn.getResponseCode());
        
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = br.readLine()) != null) {
        	output.append(line);
        }
        
        conn.disconnect();
		
		log.log(Level.INFO, "User coordinates getted: " + output.toString());
		log.exiting(name, methodName);
		return output.toString();
	}
		
	public Map<String, String> coordinatesFromDB(Map <String, String> variables, String session) throws SQLException {
		String methodName = "coordinatesFromDB";
		log.entering(name, methodName);
		
		Map<String, String> getLoginSession = new HashMap<String, String>();
		String sql1 = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
		PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
		
		try {
			statement1.setString(1, session);
			
			getLoginSession = getFromDB.getFromDB(statement1);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement1.close();
			DB.disconnect();
		}
		
		log.log(Level.INFO, "Login Session: " + getLoginSession.toString());
		
		Map<String, String> getUser = new HashMap<String, String>();
		String sql2 = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
		PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
		
		try {
			statement2.setString(1, getLoginSession.get("id_usuario"));
			
			getUser = getFromDB.getFromDB(statement2);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement2.close();
			DB.disconnect();
		}
		
		log.log(Level.INFO, "User: " + getUser.toString());
		
		String sql3 = "SELECT lat, lon FROM Endereco WHERE (id_endereco LIKE ?);";
		PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
		
		try {
			statement3.setString(1, getUser.get("id_endereco"));
			
			log.exiting(name, methodName);
			return getFromDB.getFromDB(statement3);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement3.close();
			DB.disconnect();
		}
	}
}