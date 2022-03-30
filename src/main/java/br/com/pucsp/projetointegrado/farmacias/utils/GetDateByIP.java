package br.com.pucsp.projetointegrado.farmacias.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetDateByIP {
	public String getData(String IP) {
		String time = "";
//		GetZoneByIP getCountry = new GetZoneByIP();
//		String countryData = getCountry.getData(IP);
//		
//		JSONObject countryJson = new JSONObject(countryData.toString());
//		
//		String country = countryJson.get("country") + "";
//		
//		if(country.equals("BR")) {
//		GetByIP getData = new GetByIP();
//		String data = getData.getData(IP);
//		
//		JSONObject object = new JSONObject(data.toString());
//		
//		String datetime = object.get("datetime") + "";
//				
//		DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//		
//		Date date;
//		date = utcFormat.parse(datetime);
//		
//		DateFormat pstFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm'h'");
//		
//		time = pstFormat.format(date);
//		}
//		else {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm'h'");
		      
		time = dtf.format(LocalDateTime.now());
//		}
						
		// return "24/03/2022 às 12:45h";
		return time;
	}
}