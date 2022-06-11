package br.com.pucsp.projetointegrador.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class GetZoneByIP {
	private static String name = GetZoneByIP.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetZoneByIP.class.getName());
	
	public String getData(String ip) throws IOException {
		log.entering(name, "getData");
		String url = "https://ipapi.co/" + ip + "/json/";
		
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "application/json");
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	        
	        StringBuilder output = new StringBuilder();
	        String line;
	        while ((line = br.readLine()) != null) {
	            output.append(line);
	        }
	        
	        conn.disconnect();
	        
	        log.exiting(name, "getData");
	        return output.toString();
		}
		catch (IOException e) {
			throw new IOException(LogMessage.message(e.toString()));
		}
	}
}