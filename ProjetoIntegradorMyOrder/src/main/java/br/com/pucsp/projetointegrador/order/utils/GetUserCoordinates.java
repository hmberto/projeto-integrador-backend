package br.com.pucsp.projetointegrador.order.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetUserCoordinates {
	public static String NAME = GetUserCoordinates.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetUserCoordinates.class.getName());
	
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
}