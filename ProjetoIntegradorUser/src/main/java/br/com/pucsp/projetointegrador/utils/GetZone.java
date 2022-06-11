package br.com.pucsp.projetointegrador.utils;

import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONObject;

public class GetZone {
	private static String name = GetZone.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetZone.class.getName());
	
	public String getZone(String ip) throws IOException {
		log.entering(name, "getZone");
		GetZoneByIP getData = new GetZoneByIP();
		String data = getData.getData(ip);
		
		JSONObject object = new JSONObject(data);
		
		String zone = object.get("city") + " - " + object.get("region_code");
		
		log.exiting(name, "getZone");
		return zone;
	}
}