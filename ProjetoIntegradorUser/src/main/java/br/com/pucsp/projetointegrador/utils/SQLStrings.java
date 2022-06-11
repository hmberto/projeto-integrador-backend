package br.com.pucsp.projetointegrador.utils;

public class SQLStrings {
	private SQLStrings () {}
	
	public static String insertIfNotExistsCity(String city) {
		return "INSERT INTO Cidade ( `nome` )\n"
				+ "    SELECT * FROM (SELECT '" + city + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `id_cidade` FROM `Cidade` WHERE `nome` = '" + city + "' \n"
				+ "    ) LIMIT 1";
	}
	
	public static String insertIfNotExistsDistrict(String district) {
		return "INSERT INTO Bairro ( `nome` )\n"
				+ "    SELECT * FROM (SELECT '" + district + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `id_bairro` FROM `Bairro` WHERE `nome` = '" + district + "' \n"
				+ "    ) LIMIT 1";
	}
	
	public static String insertIfNotExistsUf(String state) {
		return "INSERT INTO Sigla_Estado ( `nome` )\n"
				+ "    SELECT * FROM (SELECT '" + state + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `id_sigla_estado` FROM `Sigla_Estado` WHERE `nome` = '" + state + "' \n"
				+ "    ) LIMIT 1";
	}
	
	public static String insertIfNotExistsAddressType(String streetType ) {
		return "INSERT INTO Tipo_Endereco ( `nome` )\n"
				+ "    SELECT * FROM (SELECT '" + streetType + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `id_tipo_endereco` FROM `Tipo_Endereco` WHERE `nome` = '" + streetType + "' \n"
				+ "    ) LIMIT 1";
	}
	
	public static String insertIfNotExistsSex(String genero, String sex) {
		return "INSERT INTO Genero ( `nome`, `sigla` )\n"
				+ "    SELECT * FROM (SELECT '" + genero + "', '" + sex + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `sigla` FROM `Genero` WHERE `nome` = '" + genero + "' \n"
				+ "    ) LIMIT 1";
	}
}