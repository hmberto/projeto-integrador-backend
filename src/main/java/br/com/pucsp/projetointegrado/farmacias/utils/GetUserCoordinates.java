package br.com.pucsp.projetointegrado.farmacias.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUserCoordinates {
	public String coordinates(String street, String number) {
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
	            System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
	        }
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	        
	        String line;
	        while ((line = br.readLine()) != null) {
	            output += line;
	        }
	        
	        conn.disconnect();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return output;
	}
}