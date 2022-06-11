package br.com.pucsp.projetointegrador.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class GetUserCoordinates {
	private static String name = GetUserCoordinates.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetUserCoordinates.class.getName());
	
	public String coordinates(String street, String number) throws IOException {
		log.entering(name, "coordinates");
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
		
		String url = "https://nominatim.openstreetmap.org/search.php?street=" + address + "%2c" + number + "&format=jsonv2";
		
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("GET");
        
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = br.readLine()) != null) {
            output.append(line);
        }
        
        conn.disconnect();
        
		log.exiting(name, "coordinates");
		return output.toString();
	}
}