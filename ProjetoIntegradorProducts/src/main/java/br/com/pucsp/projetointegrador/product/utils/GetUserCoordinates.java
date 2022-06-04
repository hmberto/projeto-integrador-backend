package br.com.pucsp.projetointegrador.product.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.product.db.DB;
import br.com.pucsp.projetointegrador.product.db.GetFromDB;

public class GetUserCoordinates {
	public static String NAME = GetUserCoordinates.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetUserCoordinates.class.getName());
	
	GetFromDB getFromDB = new GetFromDB();
	
	public String coordinates(String street, String number) {
		LOG.entering(NAME, "coordinates");
		String[] streetSplit= street.split(" ");
		
		String output = "";
		
		String address = "";
		for(int i = 0; i < streetSplit.length; i++) {
			if(i == 0) {
				address = streetSplit[i];
			}
			else {
				address = address + "+" + streetSplit[i];
			}
		}
		
		String url = "https://nominatim.openstreetmap.org/search.php?street=" + address + "%2c" + number + "&format=jsonv2";
		
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
	        
	        if (conn.getResponseCode() != 200) {
	            LOG.log(Level.SEVERE, "Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
	        }
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	        
	        String line;
	        while ((line = br.readLine()) != null) {
	            output += line;
	        }
	        
	        conn.disconnect();
		}
		catch (IOException e) {
			LOG.log(Level.SEVERE, "User coordinates not geted: " + e);
		}
		
		LOG.log(Level.INFO, "User coordinates getted");
		LOG.exiting(NAME, "coordinates");
		return output;
	}
	
	public Map<String, String> coordinatesFromDB(Map <String, String> variables, String session) {
		LOG.entering(NAME, "coordinatesFromDB");
		
		try {
			String sql1 = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
			statement1.setString(1, session);
			
			Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statement1);
			statement1.close();
			
			String sql2 = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
			statement2.setString(1, getLoginSession.get("id_usuario"));
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, statement2);
			statement2.close();
			
			String sql3 = "SELECT lat, lon FROM Endereco WHERE (id_endereco LIKE ?);";
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
			statement3.setString(1, getUser.get("id_endereco"));
			
			Map<String, String> getAddress = getFromDB.getFromDB(variables, statement3);
			statement3.close();
			
			return getAddress;
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "User not geted from the database: " + e);
		}
		
		LOG.exiting(NAME, "coordinatesFromDB");
		return null;
	}
}