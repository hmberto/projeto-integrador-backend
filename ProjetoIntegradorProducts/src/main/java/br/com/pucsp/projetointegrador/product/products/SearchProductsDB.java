package br.com.pucsp.projetointegrador.product.products;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.product.db.DB;
import br.com.pucsp.projetointegrador.product.db.GetFromDB;
import br.com.pucsp.projetointegrador.product.utils.GetAllProducts;

public class SearchProductsDB {
	public static String NAME = SearchProductsDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SearchProductsDB.class.getName());
	
	public StringBuffer getProducts(Map <String, String> variables, String distance, String session, String productName) {
		LOG.entering(NAME, "getProducts");
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuffer payload = new StringBuffer();
		
		try {
			String sqlA = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
			PreparedStatement statementA = DB.connect(variables).prepareStatement(sqlA);
			statementA.setString(1, session);
			
			Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statementA);
			statementA.close();
			
			String sqlB = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
			PreparedStatement statementB = DB.connect(variables).prepareStatement(sqlB);
			statementB.setString(1, getLoginSession.get("id_usuario"));
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, statementB);
			statementB.close();
			
			String sqlC = "SELECT lat, lon FROM Endereco WHERE (id_endereco LIKE ?);";
			PreparedStatement statementC = DB.connect(variables).prepareStatement(sqlC);
			statementC.setString(1, getUser.get("id_endereco"));
			
			Map<String, String> getAddress = getFromDB.getFromDB(variables, statementC);
			statementC.close();
			
			String sql1 = "SELECT Farmacia.id_farmacia, (6371 *\n"
					+ "        acos(\n"
					+ "            cos(radians(" + getAddress.get("lat") + ")) *\n"
					+ "            cos(radians(lat)) *\n"
					+ "            cos(radians(" + getAddress.get("lon") + ") - radians(lon)) +\n"
					+ "            sin(radians(" + getAddress.get("lat") + ")) *\n"
					+ "            sin(radians(lat))\n"
					+ "        )) AS distance\n"
					+ "FROM Endereco, Farmacia WHERE Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
			
			String sql3p1 = "SELECT * FROM produto WHERE nome REGEXP '" + productName + "' AND id_produto in (";
			
			GetAllProducts getAllProducts = new GetAllProducts();
			payload = getAllProducts.products(variables, sql1, sql3p1);
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		// LOG.exiting(NAME, "GetCar");
		LOG.log(Level.INFO, "Nearby products payload: " + payload);
		LOG.exiting(NAME, "getProducts");
		return payload;
	}
}