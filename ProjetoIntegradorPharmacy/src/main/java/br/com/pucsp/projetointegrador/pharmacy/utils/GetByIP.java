package br.com.pucsp.projetointegrador.pharmacy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetByIP {
	public static String NAME = GetByIP.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetByIP.class.getName());
	
	public String getData(String IP) {
		LOG.entering(NAME, "getData");
		String url = "http://worldtimeapi.org/api/ip/" + IP;
		
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");
	        
	        if (conn.getResponseCode() != 200) {
	            System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
	        }
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	        String output = "";
	        String line;
	        while ((line = br.readLine()) != null) {
	            output += line;
	        }
	        
	        conn.disconnect();
	        
	        LOG.exiting(NAME, "getData");
	        return output;
		}
		catch (IOException e) {
			LOG.log(Level.SEVERE, "GetByIP.getData: " + e);
		}
		
		LOG.exiting(NAME, "getData");
		return "";
	}
}