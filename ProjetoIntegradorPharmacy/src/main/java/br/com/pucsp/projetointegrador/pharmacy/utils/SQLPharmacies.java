package br.com.pucsp.projetointegrador.pharmacy.utils;

public class SQLPharmacies {
	public static String sql(String lat, String lon, String distance) {
		return "SELECT Farmacia.nome, Farmacia.id_farmacia, (6371 *\n"
				+ "        acos(\n"
				+ "            cos(radians(" + lat + ")) *\n"
				+ "            cos(radians(lat)) *\n"
				+ "            cos(radians(" + lon + ") - radians(lon)) +\n"
				+ "            sin(radians(" + lat + ")) *\n"
				+ "            sin(radians(lat))\n"
				+ "        )) AS distance\n"
				+ "FROM Endereco, Farmacia WHERE Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
	}
	
	public static String products(StringBuilder stringfyProducts) {
		return "SELECT * FROM Produto WHERE id_produto in (" + stringfyProducts + ");";
	}
}