package br.com.pucsp.projetointegrador.order.utils;

public class SQLOrders {
	private SQLOrders () {}
	
	public static String insertIfNotExistsCardFlag(String cardFlag) {
		return "INSERT INTO Bandeira_Cartao ( `nome` )\n"
				+ "    SELECT * FROM (SELECT '" + cardFlag + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `id_bandeira` FROM `Bandeira_Cartao` WHERE `nome` = '" + cardFlag + "' \n"
				+ "    ) LIMIT 1";
	}
	
	public static String sql(String lat, String lon, String distance, String pharmacyName) {
		return "SELECT Farmacia.cnpj,Farmacia.id_farmacia,Farmacia.id_endereco, (6371 *\n"
				+ "        acos(\n"
				+ "            cos(radians(" + lat + ")) *\n"
				+ "            cos(radians(lat)) *\n"
				+ "            cos(radians(" + lon + ") - radians(lon)) +\n"
				+ "            sin(radians(" + lat + ")) *\n"
				+ "            sin(radians(lat))\n"
				+ "        )) AS distance\n"
				+ "FROM Endereco, Farmacia WHERE Farmacia.nome in (\"" + pharmacyName + "\") AND Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
		
	}
	
	public static String sqlProductPharmacy(String pharmacieId, StringBuilder stringfyProductsCart) {
		return "select id_produto from Produto_Farmacia where id_farmacia in (" + pharmacieId + ") AND id_produto in (" + stringfyProductsCart + ");";
	}
}