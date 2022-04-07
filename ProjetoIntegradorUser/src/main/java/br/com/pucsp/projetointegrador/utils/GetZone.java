package br.com.pucsp.projetointegrador.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

public class GetZone {
	public static String NAME = GetZone.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetZone.class.getName());
	
	public String getZone(String IP) {
		LOG.entering(NAME, "getZone");
		GetZoneByIP getData = new GetZoneByIP();
		String data = getData.getData(IP);
		
		JSONObject object = new JSONObject(data.toString());
		
		String zone = object.get("city") + " - " + object.get("region_code");
		
		LOG.log(Level.INFO, "IP user zone: " + zone);
		
		LOG.exiting(NAME, "getZone");
		return zone;
	}
}