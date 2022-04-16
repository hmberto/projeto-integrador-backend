package br.com.pucsp.projetointegrador.product.products;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.product.db.DB;
import br.com.pucsp.projetointegrador.product.db.GetFromDB;

public class ProductsDB {
	public static String NAME = ProductsDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(ProductsDB.class.getName());
	
	public StringBuffer getProducts(Map <String, String> variables, String distance, String session) {
		LOG.entering(NAME, "getPharmacies");
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
//			String sql1 = "SELECT * FROM Login_Sessao WHERE (id_session LIKE ?);";
//			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
//			statement1.setString(1, session);
//			
//			Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statement1);
//			statement1.close();
//			
//			String sql2 = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
//			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
//			statement2.setString(1, getLoginSession.get("id_usuario"));
//			
//			Map<String, String> getUser = getFromDB.getFromDB(variables, statement2);
//			statement2.close();
//			
//			String sql3 = "SELECT lat, lon FROM Endereco WHERE (id_endereco LIKE ?);";
//			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
//			statement3.setString(1, getUser.get("id_endereco"));
//			
//			Map<String, String> getAddress = getFromDB.getFromDB(variables, statement3);
//			statement3.close();
//			
//			String sql = "SELECT Farmacia.nome, (6371 *\n"
//					+ "        acos(\n"
//					+ "            cos(radians(" + getAddress.get("lat") + ")) *\n"
//					+ "            cos(radians(lat)) *\n"
//					+ "            cos(radians(" + getAddress.get("lon") + ") - radians(lon)) +\n"
//					+ "            sin(radians(" + getAddress.get("lat") + ")) *\n"
//					+ "            sin(radians(lat))\n"
//					+ "        )) AS distance\n"
//					+ "FROM Endereco, Farmacia WHERE Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
//			
//			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
//			
//			ResultSet f = statement.executeQuery();
//			
//			LOG.log(Level.INFO, "Nearby pharmacies products getted from DB");
			
			createPayload.object();
			
			List<String> pharmacies = new ArrayList<String>();
			pharmacies.add("Drogaria São Paulo");
			pharmacies.add("Ultrafarma");
			pharmacies.add("Pague Menos");
			pharmacies.add("Droga Raia");
			
			List<String> products = new ArrayList<String>();
			products.add("{ id: '11', amount: 'Unidade: 1un', name: 'Dipirona', pharmacy: 'Drogaria São Paulo', price: 'R$ 4,39', image: 'assets/products/dipirona_image.jpg', description: 'A dipirona, também conhecida como dipirona monoidratada ou dipirona sódica, é um remédio analgésico e antitérmico, que age reduzindo a produção de substâncias no corpo responsáveis por causar dor ou febre e, por isso, é indicado para baixar a febre e aliviar a dor, normalmente provocadas por gripes e resfriados, por exemplo.' }");
			products.add("{ id: '12', amount: 'Cartela: 4cpr', name: 'Neosaldina', pharmacy: 'Drogaria São Paulo', price: 'R$ 3,50', image: 'assets/products/neosadina_image.jpg', description: 'NEOSALDINA é um medicamento com atividade analgésica (diminui a dor) e antiespasmódica (diminui contração involuntária) indicado para o tratamento de diversos tipos de dor de cabeça, incluindo enxaquecas ou para o tratamento de cólicas.' }");
			products.add("{ id: '13', amount: 'Unidade: 240ml', name: 'Shampoo', pharmacy: 'Drogaria São Paulo', price: 'R$ 8,77', image: 'assets/products/shampoo_image.jpg', description: 'JOHNSON’S® Shampoo de Glicerina foi desenvolvido especialmente para a cabelo delicado do bebê. Suavidade comprovada por diversos estudos, que avaliaram a segurança e a eficácia do produto.' }");
			products.add("{ id: '14', amount: 'Unidade: 70ml', name: 'Desodorante', pharmacy: 'Drogaria São Paulo', price: 'R$ 12,50', image: 'assets/products/rexona_image.jpg', description: 'Nova Fórmula: 72 horas de proteção contra a transpiração e o mau odor, ativada pelo movimento.' }");
			products.add("{ id: '15', amount: 'Unidade: 1un', name: 'Vitamina C', pharmacy: 'Drogaria São Paulo', price: 'R$ 12,90', image: 'assets/products/vitamina_c_image.jpg', description: 'É indicado como suplemento vitamínico: auxiliar do sistema imunológico; antioxidante; pós-cirúrgico e cicatrizante; doenças crônicas e convalescença; dietas restritivas e inadequadas; como auxiliar nas anemias carenciais.' }");
			products.add("{ id: '16', amount: 'Unidade: 1un', name: 'Vitamina D', pharmacy: 'Drogaria São Paulo', price: 'R$ 24,99', image: 'assets/products/vitamina_d_image.jpg', description: 'A vitamina D é uma vitamina lipossolúvel produzida naturalmente no organismo através da exposição da pele à luz solar.' }");
			products.add("{ id: '17', amount: 'Unidade: 1un', name: 'Engov', pharmacy: 'Drogaria São Paulo', price: 'R$ 2,35', image: 'assets/products/engov_image.jpg', description: 'O Engov é um remédio que tem na composição maleato de mepiramina, hidróxido de alumínio, ácido acetilsalicílico e cafeína que têm ação analgésica, antiácida e estimulante sendo indicado para aliviar os sintomas de ressaca causados pela ingestão de bebidas alcoólicas, como dor de cabeça, náuseas, dor ou sensação de desconforto no estômago, cansaço e dor no corpo, por exemplo.' }");
			products.add("{ id: '18', amount: 'Unidade: 240ml', name: 'Sabonete Protex', pharmacy: 'Drogaria São Paulo', price: 'R$ 10,43', image: 'assets/products/protex_image.jpg', description: 'O Sabonete em Barra Protex Cream oferece a fragrância delicada e a hidratação que você gosta, com a proteção antibacteriana que você precisa.' }");
			products.add("{ id: '19', amount: 'Unidade: 1un', name: 'Claritin', pharmacy: 'Drogaria São Paulo', price: 'R$ 18,99', image: 'assets/products/claritin_image.jpg', description: 'O Claritin é indicado para o alívio dos sintomas associados com a rinite alérgica, como: coceira nasal, nariz escorrendo, espirros, ardor e coceira nos olhos, e para o alívio dos sinais e sintomas da urticária e de outras alergias da pele.' }");
			
			products.add("{ id: '21', amount: 'Unidade: 1un', name: 'Eno', pharmacy: 'Ultrafarma', price: 'R$ 5,00', image: 'assets/products/eno_image.jpg', description: 'Eno Sal de Fruta Tradicional é um antiácido indicado ázia, má digestão e outros transtornos estomacais, tais como excesso de acidez do estômago e indigestão ácida.' }");
			products.add("{ id: '22', amount: 'Unidade: 1un', name: 'Engov', pharmacy: 'Ultrafarma', price: 'R$ 2,35', image: 'assets/products/engov_image.jpg', description: 'O Engov é um remédio que tem na composição maleato de mepiramina, hidróxido de alumínio, ácido acetilsalicílico e cafeína que têm ação analgésica, antiácida e estimulante sendo indicado para aliviar os sintomas de ressaca causados pela ingestão de bebidas alcoólicas, como dor de cabeça, náuseas, dor ou sensação de desconforto no estômago, cansaço e dor no corpo, por exemplo.' }");
			products.add("{ id: '23', amount: 'Unidade: 240ml', name: 'Shampoo', pharmacy: 'Ultrafarma', price: 'R$ 8,77', image: 'assets/products/shampoo_image.jpg', description: 'JOHNSON’S® Shampoo de Glicerina foi desenvolvido especialmente para a cabelo delicado do bebê. Suavidade comprovada por diversos estudos, que avaliaram a segurança e a eficácia do produto.' }");
			products.add("{ id: '24', amount: 'Unidade: 1un', name: 'Claritin', pharmacy: 'Ultrafarma', price: 'R$ 18,99', image: 'assets/products/claritin_image.jpg', description: 'O Claritin é indicado para o alívio dos sintomas associados com a rinite alérgica, como: coceira nasal, nariz escorrendo, espirros, ardor e coceira nos olhos, e para o alívio dos sinais e sintomas da urticária e de outras alergias da pele.' }");
			products.add("{ id: '25', amount: 'Unidade: 1un', name: 'Dipirona', pharmacy: 'Ultrafarma', price: 'R$ 4,39', image: 'assets/products/dipirona_image.jpg', description: 'A dipirona, também conhecida como dipirona monoidratada ou dipirona sódica, é um remédio analgésico e antitérmico, que age reduzindo a produção de substâncias no corpo responsáveis por causar dor ou febre e, por isso, é indicado para baixar a febre e aliviar a dor, normalmente provocadas por gripes e resfriados, por exemplo.' }");
			products.add("{ id: '26', amount: 'Cartela: 4cpr', name: 'Neosaldina', pharmacy: 'Ultrafarma', price: 'R$ 3,50', image: 'assets/products/neosadina_image.jpg', description: 'NEOSALDINA é um medicamento com atividade analgésica (diminui a dor) e antiespasmódica (diminui contração involuntária) indicado para o tratamento de diversos tipos de dor de cabeça, incluindo enxaquecas ou para o tratamento de cólicas.' }");
			products.add("{ id: '27', amount: 'Unidade: 70ml', name: 'Desodorante', pharmacy: 'Ultrafarma', price: 'R$ 12,50', image: 'assets/products/rexona_image.jpg', description: 'Nova Fórmula: 72 horas de proteção contra a transpiração e o mau odor, ativada pelo movimento.' }");
			products.add("{ id: '28', amount: 'Unidade: 1un', name: 'Vitamina C', pharmacy: 'Ultrafarma', price: 'R$ 12,90', image: 'assets/products/vitamina_c_image.jpg', description: 'É indicado como suplemento vitamínico: auxiliar do sistema imunológico; antioxidante; pós-cirúrgico e cicatrizante; doenças crônicas e convalescença; dietas restritivas e inadequadas; como auxiliar nas anemias carenciais.' }");
			products.add("{ id: '29', amount: 'Unidade: 240ml', name: 'Sabonete Protex', pharmacy: 'Ultrafarma', price: 'R$ 10,43', image: 'assets/products/protex_image.jpg', description: 'O Sabonete em Barra Protex Cream oferece a fragrância delicada e a hidratação que você gosta, com a proteção antibacteriana que você precisa.' }");
			
			products.add("{ id: '31', amount: 'Unidade: 1un', name: 'Sabonete Protex', pharmacy: 'Pague Menos', price: 'R$ 10,43', image: 'assets/products/protex_image.jpg', description: 'O Sabonete em Barra Protex Cream oferece a fragrância delicada e a hidratação que você gosta, com a proteção antibacteriana que você precisa.' }");
			products.add("{ id: '32', amount: 'Unidade: 240ml', name: 'Desodorante', pharmacy: 'Pague Menos', price: 'R$ 12,50', image: 'assets/products/rexona_image.jpg', description: 'Nova Fórmula: 72 horas de proteção contra a transpiração e o mau odor, ativada pelo movimento.' }");
			products.add("{ id: '33', amount: 'Unidade: 240ml', name: 'Shampoo', pharmacy: 'Pague Menos', price: 'R$ 8,77', image: 'assets/products/shampoo_image.jpg', description: 'JOHNSON’S® Shampoo de Glicerina foi desenvolvido especialmente para a cabelo delicado do bebê. Suavidade comprovada por diversos estudos, que avaliaram a segurança e a eficácia do produto.' }");
			products.add("{ id: '34', amount: 'Unidade: 1un', name: 'Claritin', pharmacy: 'Pague Menos', price: 'R$ 18,99', image: 'assets/products/claritin_image.jpg', description: 'O Claritin é indicado para o alívio dos sintomas associados com a rinite alérgica, como: coceira nasal, nariz escorrendo, espirros, ardor e coceira nos olhos, e para o alívio dos sinais e sintomas da urticária e de outras alergias da pele.' }");
			products.add("{ id: '35', amount: 'Unidade: 1un', name: 'Dipirona', pharmacy: 'Pague Menos', price: 'R$ 4,39', image: 'assets/products/dipirona_image.jpg', description: 'A dipirona, também conhecida como dipirona monoidratada ou dipirona sódica, é um remédio analgésico e antitérmico, que age reduzindo a produção de substâncias no corpo responsáveis por causar dor ou febre e, por isso, é indicado para baixar a febre e aliviar a dor, normalmente provocadas por gripes e resfriados, por exemplo.' }");
			products.add("{ id: '36', amount: 'Cartela: 4cpr', name: 'Neosaldina', pharmacy: 'Pague Menos', price: 'R$ 3,50', image: 'assets/products/neosadina_image.jpg', description: 'NEOSALDINA é um medicamento com atividade analgésica (diminui a dor) e antiespasmódica (diminui contração involuntária) indicado para o tratamento de diversos tipos de dor de cabeça, incluindo enxaquecas ou para o tratamento de cólicas.' }");
			products.add("{ id: '37', amount: 'Unidade: 70ml', name: 'Desodorante', pharmacy: 'Pague Menos', price: 'R$ 12,50', image: 'assets/products/rexona_image.jpg', description: 'Nova Fórmula: 72 horas de proteção contra a transpiração e o mau odor, ativada pelo movimento.' }");
			products.add("{ id: '38', amount: 'Unidade: 1un', name: 'Vitamina C', pharmacy: 'Pague Menos', price: 'R$ 12,90', image: 'assets/products/vitamina_c_image.jpg', description: 'É indicado como suplemento vitamínico: auxiliar do sistema imunológico; antioxidante; pós-cirúrgico e cicatrizante; doenças crônicas e convalescença; dietas restritivas e inadequadas; como auxiliar nas anemias carenciais.' }");
			products.add("{ id: '39', amount: 'Unidade: 1un', name: 'Aspirina', pharmacy: 'Pague Menos', price: 'R$ 4,29', image: 'assets/products/aspirina_image.jpg', description: 'Controle o resfriado: os práticos comprimidos efervescentes Aspirina® C atuam de forma rápida e confiável, pois o ingrediente ativo pode ser rapidamente absorvido pelo corpo. Dor e febre são, portanto, efetivamente aliviadas com o benefício adicional da vitamina C.' }");
			
			products.add("{ id: '41', amount: 'Unidade: 1un', name: 'Vitamina C', pharmacy: 'Droga Raia', price: 'R$ 12,90', image: 'assets/products/vitamina_c_image.jpg', description: 'É indicado como suplemento vitamínico: auxiliar do sistema imunológico; antioxidante; pós-cirúrgico e cicatrizante; doenças crônicas e convalescença; dietas restritivas e inadequadas; como auxiliar nas anemias carenciais.' }");
			products.add("{ id: '42', amount: 'Unidade: 1un', name: 'Vitamina D', pharmacy: 'Droga Raia', price: 'R$ 24,99', image: 'assets/products/vitamina_d_image.jpg', description: 'A vitamina D é uma vitamina lipossolúvel produzida naturalmente no organismo através da exposição da pele à luz solar.' }");
			products.add("{ id: '43', amount: 'Unidade: 240ml', name: 'Shampoo', pharmacy: 'Droga Raia', price: 'R$ 8,77', image: 'assets/products/shampoo_image.jpg', description: 'JOHNSON’S® Shampoo de Glicerina foi desenvolvido especialmente para a cabelo delicado do bebê. Suavidade comprovada por diversos estudos, que avaliaram a segurança e a eficácia do produto.' }");
			products.add("{ id: '44', amount: 'Unidade: 1un', name: 'Claritin', pharmacy: 'Droga Raia', price: 'R$ 18,99', image: 'assets/products/claritin_image.jpg', description: 'O Claritin é indicado para o alívio dos sintomas associados com a rinite alérgica, como: coceira nasal, nariz escorrendo, espirros, ardor e coceira nos olhos, e para o alívio dos sinais e sintomas da urticária e de outras alergias da pele.' }");
			products.add("{ id: '45', amount: 'Unidade: 1un', name: 'Dipirona', pharmacy: 'Droga Raia', price: 'R$ 4,39', image: 'assets/products/dipirona_image.jpg', description: 'A dipirona, também conhecida como dipirona monoidratada ou dipirona sódica, é um remédio analgésico e antitérmico, que age reduzindo a produção de substâncias no corpo responsáveis por causar dor ou febre e, por isso, é indicado para baixar a febre e aliviar a dor, normalmente provocadas por gripes e resfriados, por exemplo.' }");
			products.add("{ id: '46', amount: 'Cartela: 4cpr', name: 'Neosaldina', pharmacy: 'Droga Raia', price: 'R$ 3,50', image: 'assets/products/neosadina_image.jpg', description: 'NEOSALDINA é um medicamento com atividade analgésica (diminui a dor) e antiespasmódica (diminui contração involuntária) indicado para o tratamento de diversos tipos de dor de cabeça, incluindo enxaquecas ou para o tratamento de cólicas.' }");
			products.add("{ id: '47', amount: 'Unidade: 70ml', name: 'Desodorante', pharmacy: 'Droga Raia', price: 'R$ 12,50', image: 'assets/products/rexona_image.jpg', description: 'Nova Fórmula: 72 horas de proteção contra a transpiração e o mau odor, ativada pelo movimento.' }");
			products.add("{ id: '48', amount: 'Unidade: 1un', name: 'Vitamina C', pharmacy: 'Droga Raia', price: 'R$ 12,90', image: 'assets/products/vitamina_c_image.jpg', description: 'É indicado como suplemento vitamínico: auxiliar do sistema imunológico; antioxidante; pós-cirúrgico e cicatrizante; doenças crônicas e convalescença; dietas restritivas e inadequadas; como auxiliar nas anemias carenciais.' }");
			products.add("{ id: '49', amount: 'Unidade: 240ml', name: 'Sabonete Protex', pharmacy: 'Droga Raia', price: 'R$ 10,43', image: 'assets/products/protex_image.jpg', description: 'O Sabonete em Barra Protex Cream oferece a fragrância delicada e a hidratação que você gosta, com a proteção antibacteriana que você precisa.' }");
			
//			int i = 0;
//			while(f.next()) {
			for(int a = 0; a < pharmacies.size(); a ++) {
				String pharmacyName = pharmacies.get(a);
				createPayload.key(pharmacyName);
				
				createPayload.object();
				
				for(int b = 0; b < products.size(); b ++) {
					JSONObject item = new JSONObject(products.get(b));
					if(pharmacyName.equals(item.get("pharmacy"))) {
						createPayload.key("product-" + (b+1));
						
						createPayload.object();
						
						createPayload.key("id").value(item.get("id"));
						createPayload.key("amount").value(item.get("amount"));
						createPayload.key("name").value(item.get("name"));
						createPayload.key("pharmacy").value(item.get("pharmacy"));
						createPayload.key("price").value(item.get("price"));
						createPayload.key("image").value(item.get("image"));
						createPayload.key("description").value(item.get("description"));
						
						createPayload.endObject();
					}
				}
				
				createPayload.endObject();
				
//				i++;
			}
			
			createPayload.endObject();
			
//			statement.close();
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		// LOG.exiting(NAME, "GetCar");
		LOG.log(Level.INFO, "Nearby pharmacies payload: " + payload);
		LOG.exiting(NAME, "getPharmacies");
		return payload;
	}
}