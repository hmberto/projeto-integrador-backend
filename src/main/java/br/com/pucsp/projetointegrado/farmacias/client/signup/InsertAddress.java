package br.com.pucsp.projetointegrado.farmacias.client.signup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.db.DB;

public class InsertAddress {
	public static String NAME = InsertAddress.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(InsertAddress.class.getName());
	
	public static String insertAddress(Map<String, String> variables, CreateUsers user, String lat, String lon) {
		LOG.entering(NAME, "insertAddress");
		
		Map<String, String> states = states();
		List<String> streetType = streetType();
		
		String INSERT_IF_NOT_EXISTS_CITY = "INSERT INTO Cidade ( `nome` )\n"
				+ "    SELECT * FROM (SELECT '" + user.getCity() + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `id_cidade` FROM `Cidade` WHERE `nome` = '" + user.getCity() + "' \n"
				+ "    ) LIMIT 1";
		
		String INSERT_IF_NOT_EXISTS_DISTRICT = "INSERT INTO Bairro ( `nome` )\n"
				+ "    SELECT * FROM (SELECT '" + user.getDistrict() + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `id_bairro` FROM `Bairro` WHERE `nome` = '" + user.getDistrict() + "' \n"
				+ "    ) LIMIT 1";
		
		String INSERT_IF_NOT_EXISTS_UF = "INSERT INTO Sigla_Estado ( `nome` )\n"
				+ "    SELECT * FROM (SELECT '" + user.getState() + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `id_sigla_estado` FROM `Sigla_Estado` WHERE `nome` = '" + user.getState() + "' \n"
				+ "    ) LIMIT 1";
		
		String INSERT_IF_NOT_EXISTS_STATE_1 = "SELECT id_sigla_estado FROM Sigla_Estado WHERE (nome LIKE ?);";
		
		String userStreetType = "";
		for(String type : streetType) {
		     if(user.getStreet().toLowerCase().contains(type.toLowerCase())) {
		    	 userStreetType = type;
		     }
		}
		
		String INSERT_IF_NOT_EXISTS_ADDRESS_TYPE = "INSERT INTO Tipo_Endereco ( `nome` )\n"
				+ "    SELECT * FROM (SELECT '" + userStreetType + "' ) AS tmp\n"
				+ "    WHERE NOT EXISTS (SELECT `nome`, `id_tipo_endereco` FROM `Tipo_Endereco` WHERE `nome` = '" + userStreetType + "' \n"
				+ "    ) LIMIT 1";
		
		try {
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(INSERT_IF_NOT_EXISTS_CITY);
			statement1.execute();
			statement1.close();
			
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(INSERT_IF_NOT_EXISTS_DISTRICT);
			statement2.execute();
			statement2.close();
			
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(INSERT_IF_NOT_EXISTS_UF);
			statement3.execute();
			statement3.close();
			
			PreparedStatement statement4 = DB.connect(variables).prepareStatement(INSERT_IF_NOT_EXISTS_ADDRESS_TYPE);
			statement4.execute();
			statement4.close();
			
			PreparedStatement statement5 = DB.connect(variables).prepareStatement(INSERT_IF_NOT_EXISTS_STATE_1);
			statement5.setString(1, user.getState());
			
			ResultSet f = statement5.executeQuery();
			
			String id_sigla_estado = "";
			while(f.next()) {
				id_sigla_estado = f.getString(1);
			}
			statement5.close();
			
			String INSERT_IF_NOT_EXISTS_STATE_2 = "INSERT IGNORE INTO Estado (nome, id_sigla_estado) VALUES (?, ?);";

			PreparedStatement statement6 = DB.connect(variables).prepareStatement(INSERT_IF_NOT_EXISTS_STATE_2);
			statement6.setString(1, states.get(user.getState()));
			statement6.setString(2, id_sigla_estado);
			
			statement6.execute();
			statement6.close();
			
			String GET_FK_CITY = "SELECT id_cidade FROM Cidade WHERE (nome LIKE ?);";
			String GET_FK_DISTRICT = "SELECT id_bairro FROM Bairro WHERE (nome LIKE ?);";
			String GET_FK_STATE = "SELECT id_estado FROM Estado WHERE (nome LIKE ?);";
			String GET_FK_ADDRESS_TYPE = "SELECT id_tipo_endereco FROM Tipo_Endereco WHERE (nome LIKE ?);";
			
			String FK_CITY = "";
			String FK_DISTRICT = "";
			String FK_STATE = "";
			String FK_ADDRESS_TYPE = "";
			
			PreparedStatement statementFK1 = DB.connect(variables).prepareStatement(GET_FK_CITY);
			statementFK1.setString(1, user.getCity());
			
			ResultSet f1 = statementFK1.executeQuery();
			while(f1.next()) {
				FK_CITY = f1.getString(1);
			}
			statementFK1.close();
			
			PreparedStatement statementFK2 = DB.connect(variables).prepareStatement(GET_FK_DISTRICT);
			statementFK2.setString(1, user.getDistrict());
			
			ResultSet f2 = statementFK2.executeQuery();
			while(f2.next()) {
				FK_DISTRICT = f2.getString(1);
			}
			statementFK2.close();
			
			PreparedStatement statementFK3 = DB.connect(variables).prepareStatement(GET_FK_STATE);
			statementFK3.setString(1, states.get(user.getState()));
			
			ResultSet f3 = statementFK3.executeQuery();
			while(f3.next()) {
				FK_STATE = f3.getString(1);
			}
			statementFK3.close();
						
			PreparedStatement statementFK4 = DB.connect(variables).prepareStatement(GET_FK_ADDRESS_TYPE);
			statementFK4.setString(1, userStreetType);
			
			ResultSet f4 = statementFK4.executeQuery();
			while(f4.next()) {
				FK_ADDRESS_TYPE = f4.getString(1);
			}
			statementFK4.close();
			
			String sqlAddress = "INSERT INTO Endereco (nome, numero, complemento, cep, lat, lon, id_cidade, id_bairro, id_estado, id_tipo_endereco) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			PreparedStatement statementAddress = DB.connect(variables).prepareStatement(sqlAddress);
			
			String newStreet = user.getStreet().replace(userStreetType, "");
			
			statementAddress.setString(1, newStreet.trim());
			statementAddress.setString(2, user.getNumber());
			statementAddress.setString(3, user.getComplement());
			statementAddress.setString(4, user.getZipCode());
			statementAddress.setString(5, lat);
			statementAddress.setString(6, lon);
			statementAddress.setString(7, FK_CITY);
			statementAddress.setString(8, FK_DISTRICT);
			statementAddress.setString(9, FK_STATE);
			statementAddress.setString(10, FK_ADDRESS_TYPE);
			
			statementAddress.execute();
			
			String getIdAddress = "SELECT id_endereco FROM Endereco WHERE (nome LIKE ?) AND (numero LIKE ?) AND (complemento LIKE ?) AND (cep LIKE ?) AND (lat LIKE ?) AND (lon LIKE ?) AND (id_cidade LIKE ?) AND (id_bairro LIKE ?) AND (id_estado LIKE ?) AND (id_tipo_endereco LIKE ?);";
			
			PreparedStatement getIdAddressStat = DB.connect(variables).prepareStatement(getIdAddress);
			
			getIdAddressStat.setString(1, newStreet.trim());
			getIdAddressStat.setString(2, user.getNumber());
			getIdAddressStat.setString(3, user.getComplement());
			getIdAddressStat.setString(4, user.getZipCode());
			getIdAddressStat.setString(5, lat);
			getIdAddressStat.setString(6, lon);
			getIdAddressStat.setString(7, FK_CITY);
			getIdAddressStat.setString(8, FK_DISTRICT);
			getIdAddressStat.setString(9, FK_STATE);
			getIdAddressStat.setString(10, FK_ADDRESS_TYPE);
			
			String idAddressGetted = "";
			
			ResultSet f10 = getIdAddressStat.executeQuery();
			while(f10.next()) {
				idAddressGetted = f10.getString(1);
			}
			
			statementAddress.close();
			getIdAddressStat.close();
			
			LOG.log(Level.INFO, "User address created at address table! Address ID: " + idAddressGetted);
			
			LOG.exiting(NAME, "insertAddress");
			return idAddressGetted;
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "insertAddress");
		return "";
	}
	
	public static List<String> streetType() {
		List<String> streetType = new ArrayList<String>();
		streetType.add("Área");
		streetType.add("Acesso");
		streetType.add("Acampamento");
		streetType.add("Acesso Local");
		streetType.add("Adro");
		streetType.add("Área Especial");
		streetType.add("Aeroporto");
		streetType.add("Alameda");
		streetType.add("Avenida Marginal Direita");
		streetType.add("Avenida Marginal Esquerda");
		streetType.add("Anel Viário");
		streetType.add("Antiga Estrada");
		streetType.add("Artéria");
		streetType.add("Alto");
		streetType.add("Atalho");
		streetType.add("Área Verde");
		streetType.add("Avenida");
		streetType.add("Avenida Contorno");
		streetType.add("Avenida Marginal");
		streetType.add("Avenida Velha");
		streetType.add("Balneário");
		streetType.add("Beco");
		streetType.add("Buraco");
		streetType.add("Belvedere");
		streetType.add("Bloco");
		streetType.add("Balão");
		streetType.add("Blocos");
		streetType.add("Bulevar");
		streetType.add("Bosque");
		streetType.add("Boulevard");
		streetType.add("Baixa");
		streetType.add("Cais");
		streetType.add("Calçada");
		streetType.add("Caminho");
		streetType.add("Canal");
		streetType.add("Chácara");
		streetType.add("Chapadão");
		streetType.add("Ciclovia");
		streetType.add("Circular");
		streetType.add("Conjunto");
		streetType.add("Conjunto Mutirão");
		streetType.add("Complexo Viário");
		streetType.add("Colônia");
		streetType.add("Comunidade");
		streetType.add("Condomínio");
		streetType.add("Corredor");
		streetType.add("Campo");
		streetType.add("Córrego");
		streetType.add("Contorno");
		streetType.add("Descida");
		streetType.add("Desvio");
		streetType.add("Distrito");
		streetType.add("Entre Bloco");
		streetType.add("Estrada Intermunicipal");
		streetType.add("Enseada");
		streetType.add("Entrada Particular");
		streetType.add("Entre Quadra");
		streetType.add("Escada");
		streetType.add("Escadaria");
		streetType.add("Estrada Estadual");
		streetType.add("Estrada Vicinal");
		streetType.add("Estrada de Ligação");
		streetType.add("Estrada Municipal");
		streetType.add("Esplanada");
		streetType.add("Estrada de Servidão");
		streetType.add("Estrada");
		streetType.add("Estrada Velha");
		streetType.add("Estrada Antiga");
		streetType.add("Estação");
		streetType.add("Estádio");
		streetType.add("Estância");
		streetType.add("Estrada Particular");
		streetType.add("Estacionamento");
		streetType.add("Evangélica");
		streetType.add("Elevada");
		streetType.add("Eixo Industrial");
		streetType.add("Favela");
		streetType.add("Fazenda");
		streetType.add("Ferrovia");
		streetType.add("Fonte");
		streetType.add("Feira");
		streetType.add("Forte");
		streetType.add("Galeria");
		streetType.add("Granja");
		streetType.add("Núcleo Habitacional");
		streetType.add("Ilha");
		streetType.add("Indeterminado");
		streetType.add("Ilhota");
		streetType.add("Jardim");
		streetType.add("Jardinete");
		streetType.add("Ladeira");
		streetType.add("Lagoa");
		streetType.add("Lago");
		streetType.add("Loteamento");
		streetType.add("Largo");
		streetType.add("Lote");
		streetType.add("Mercado");
		streetType.add("Marina");
		streetType.add("Modulo");
		streetType.add("Projeção");
		streetType.add("Morro");
		streetType.add("Monte");
		streetType.add("Núcleo");
		streetType.add("Núcleo Rural");
		streetType.add("Outeiro");
		streetType.add("Paralela");
		streetType.add("Passeio");
		streetType.add("Pátio");
		streetType.add("Praça");
		streetType.add("Praça de Esportes");
		streetType.add("Parada");
		streetType.add("Paradouro");
		streetType.add("Ponta");
		streetType.add("Praia");
		streetType.add("Prolongamento");
		streetType.add("Parque Municipal");
		streetType.add("Parque");
		streetType.add("Parque Residencial");
		streetType.add("Passarela");
		streetType.add("Passagem");
		streetType.add("Passagem de Pedestre");
		streetType.add("Passagem Subterrânea");
		streetType.add("Ponte");
		streetType.add("Porto");
		streetType.add("Quadra");
		streetType.add("Quinta");
		streetType.add("Quintas");
		streetType.add("Rua");
		streetType.add("Rua Integração");
		streetType.add("Rua de Ligação");
		streetType.add("Rua Particular");
		streetType.add("Rua Velha");
		streetType.add("Ramal");
		streetType.add("Recreio");
		streetType.add("Recanto");
		streetType.add("Retiro");
		streetType.add("Residencial");
		streetType.add("Reta");
		streetType.add("Ruela");
		streetType.add("Rampa");
		streetType.add("Rodo Anel");
		streetType.add("Rodovia");
		streetType.add("Rotula");
		streetType.add("Rua de Pedestre");
		streetType.add("Margem");
		streetType.add("Retorno");
		streetType.add("Rotatória");
		streetType.add("Segunda Avenida");
		streetType.add("Sitio");
		streetType.add("Servidão");
		streetType.add("Setor");
		streetType.add("Subida");
		streetType.add("Trincheira");
		streetType.add("Terminal");
		streetType.add("Trecho");
		streetType.add("Trevo");
		streetType.add("Túnel");
		streetType.add("Travessa");
		streetType.add("Travessa Particular");
		streetType.add("Travessa Velha");
		streetType.add("Unidade");
		streetType.add("Via");
		streetType.add("Via Coletora");
		streetType.add("Via Local");
		streetType.add("Via de Acesso");
		streetType.add("Vala");
		streetType.add("Via Costeira");
		streetType.add("Viaduto");
		streetType.add("Via Expressa");
		streetType.add("Vereda");
		streetType.add("Via Elevado");
		streetType.add("Vila");
		streetType.add("Viela");
		streetType.add("Vale");
		streetType.add("Via Litorânea");
		streetType.add("Via de Pedestre");
		streetType.add("Variante");
		streetType.add("Zigue-Zague");
		
		return streetType;
	}
	
	public static Map<String, String> states() {
		Map<String, String> states = new HashMap<String, String>();
		states.put("RO", "Rondônia");
		states.put("AC", "Acre");
		states.put("AM", "Amazonas");
		states.put("RR", "Roraima");
		states.put("PA", "Pará");
		states.put("AP", "Amapá");
		states.put("TO", "Tocantins");
		states.put("MA", "Maranhão");
		states.put("PI", "Piauí");
		states.put("CE", "Ceará");
		states.put("RN", "Rio Grande do Norte");
		states.put("PB", "Paraíba");
		states.put("PE", "Pernambuco");
		states.put("AL", "Alagoas");
		states.put("SE", "Sergipe");
		states.put("BA", "Bahia");
		states.put("MG", "Minas Gerais");
		states.put("ES", "Espírito Santo");
		states.put("RJ", "Rio de Janeiro");
		states.put("SP", "São Paulo");
		states.put("PR", "Paraná");
		states.put("SC", "Santa Catarina");
		states.put("RS", "Rio Grande do Sul (*)");
		states.put("MS", "Mato Grosso do Sul");
		states.put("MT", "Mato Grosso");
		states.put("GO", "Goiás");
		states.put("DF", "Distrito Federal");
		
		return states;
	}
}