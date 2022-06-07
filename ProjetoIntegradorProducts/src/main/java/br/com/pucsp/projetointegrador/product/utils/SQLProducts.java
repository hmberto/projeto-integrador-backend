package br.com.pucsp.projetointegrador.product.utils;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class SQLProducts {
	public static String products(String lat, String lon, String distance) {
		return "SELECT Farmacia.id_farmacia, (6371 *\n"
				+ "        acos(\n"
				+ "            cos(radians(" + lat + ")) *\n"
				+ "            cos(radians(lat)) *\n"
				+ "            cos(radians(" + lon + ") - radians(lon)) +\n"
				+ "            sin(radians(" + lat + ")) *\n"
				+ "            sin(radians(lat))\n"
				+ "        )) AS distance\n"
				+ "FROM Endereco, Farmacia WHERE Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
	}
	
	public static String productsIn() {
		return "SELECT * FROM produto WHERE id_produto in (";
	}
	
	public static String productsInSearch(String productName) {
		return "SELECT * FROM produto WHERE nome REGEXP '" + productName + "' AND id_produto in (";
	}
	
	public String productsAnonStreet(String distance, String street) throws IOException {
		String lat = "";
		String lon = "";
		
		GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
		
		String coordinates = getUserCoordinates.coordinates(street, "1");
		JSONArray jsonarray = new JSONArray(coordinates);
		
		for(int i=0; i<jsonarray.length(); i++){
	        JSONObject obj = jsonarray.getJSONObject(i);

	        lat = obj.getString("lat");
	        lon = obj.getString("lon");
		}
		
		return "SELECT Farmacia.id_farmacia, (6371 *\n"
				+ "        acos(\n"
				+ "            cos(radians(" + lat + ")) *\n"
				+ "            cos(radians(lat)) *\n"
				+ "            cos(radians(" + lon + ") - radians(lon)) +\n"
				+ "            sin(radians(" + lat + ")) *\n"
				+ "            sin(radians(lat))\n"
				+ "        )) AS distance\n"
				+ "FROM Endereco, Farmacia WHERE Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
	}
}