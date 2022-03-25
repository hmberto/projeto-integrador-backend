package br.com.pucsp.projetointegrado.farmacias.utils;

import org.json.JSONObject;

public class GetZone {
	public String getZone(String IP) {
		GetZoneByIP getData = new GetZoneByIP();
		String data = getData.getData(IP);
		
		JSONObject object = new JSONObject(data.toString());
		
		String zone = object.get("city") + " - " + object.get("region_code");
				
		return zone;
	}
}