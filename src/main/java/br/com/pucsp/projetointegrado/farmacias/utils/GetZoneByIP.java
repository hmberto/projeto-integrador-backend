package br.com.pucsp.projetointegrado.farmacias.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetZoneByIP {
	public String getData(String IP) {
		String url = "https://ipapi.co/" + IP + "/json/";
		
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/json");
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
	        
	        return output;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}