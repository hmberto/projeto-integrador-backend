package br.com.pucsp.projetointegrado.farmacias.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class GetDateByIP {
	public String getData(String IP) {
		String time = "";
		try {
			GetByIP getData = new GetByIP();
			String data = getData.getData(IP);
			
			JSONObject object = new JSONObject(data.toString());
			
			String datetime = object.get("datetime") + "";
					
			DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
			Date date;
			date = utcFormat.parse(datetime);
			
			DateFormat pstFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm'h'");
			
			time = pstFormat.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		// return "24/03/2022 às 12:45h";
		return time;
	}
}