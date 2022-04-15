package br.com.pucsp.projetointegrador.product.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetZoneByIP {
	public static String NAME = GetZoneByIP.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetZoneByIP.class.getName());
	
	public String getData(String IP) {
		LOG.entering(NAME, "getData");
		String url = "https://ipapi.co/" + IP + "/json/";
		
		LOG.log(Level.INFO, "url user zone: " + url);
		
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "application/json");
	        
	        if (conn.getResponseCode() != 200) {
	            LOG.log(Level.SEVERE, "Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
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
			LOG.log(Level.SEVERE, "Zone by IP not getted: " + e);
		}
		
		LOG.exiting(NAME, "getData");
		return "";
	}
}