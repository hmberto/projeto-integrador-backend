package br.com.pucsp.projetointegrador.order.products;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;
import br.com.pucsp.projetointegrador.order.utils.CalcFeeDelivery;
import br.com.pucsp.projetointegrador.order.utils.CalcTimeDelivery;
import br.com.pucsp.projetointegrador.order.utils.ReplaceImageNames;

public class ProductsDB {
	public static String NAME = ProductsDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(ProductsDB.class.getName());
	
	public StringBuffer getProducts(Map <String, String> variables, GetProducts cart) {
		LOG.entering(NAME, "getProducts");
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
			String sqlA = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
			PreparedStatement statementA = DB.connect(variables).prepareStatement(sqlA);
			statementA.setString(1, cart.getSession());
			
			Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statementA);
			statementA.close();
			LOG.log(Level.INFO, "Login Session: " + getLoginSession);
			
			String sqlB = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
			PreparedStatement statementB = DB.connect(variables).prepareStatement(sqlB);
			statementB.setString(1, getLoginSession.get("id_usuario"));
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, statementB);
			statementB.close();
			LOG.log(Level.INFO, "User: " + getUser);
			
			String sqlC = "SELECT * FROM Endereco WHERE (id_endereco LIKE ?);";
			PreparedStatement statementC = DB.connect(variables).prepareStatement(sqlC);
			statementC.setString(1, getUser.get("id_endereco"));
			
			Map<String, String> getAddress = getFromDB.getFromDB(variables, statementC);
			statementC.close();
			LOG.log(Level.INFO, "User Address: " + getAddress);
			
			List<String> productsCart = new ArrayList<String>();
			for (String e : cart.getProducts()) {
				productsCart.add(e);
			}
			
			StringBuffer stringfyProductsCart = new StringBuffer();
			
			stringfyProductsCart.append(productsCart.toString().replace("[", "").replace("]", ""));
			stringfyProductsCart.toString();
			
			String sql1 = "SELECT Farmacia.cnpj,Farmacia.id_farmacia,Farmacia.id_endereco, (6371 *\n"
					+ "        acos(\n"
					+ "            cos(radians(" + getAddress.get("lat") + ")) *\n"
					+ "            cos(radians(lat)) *\n"
					+ "            cos(radians(" + getAddress.get("lon") + ") - radians(lon)) +\n"
					+ "            sin(radians(" + getAddress.get("lat") + ")) *\n"
					+ "            sin(radians(lat))\n"
					+ "        )) AS distance\n"
					+ "FROM Endereco, Farmacia WHERE Farmacia.nome in (\"" + cart.getPharmacyName() + "\") AND Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + cart.getDistance() + ";";
			
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql1);
			ResultSet f = statement.executeQuery();
			
			String pharmacieId = "";
			String pharmacieIdAddress = "";
			String pharmacieDistance = "";
			String PharmacyCnpj = "";
			
			while(f.next()) {
				PharmacyCnpj = f.getString(1);
				pharmacieId = f.getString(2);
				pharmacieIdAddress = f.getString(3);
				pharmacieDistance = f.getString(4);
			}
			statement.close();
			
			LOG.log(Level.INFO, "Pharmacie ID: " + pharmacieId);
			
			String sql2 = "select id_produto from Produto_Farmacia where id_farmacia in (" + pharmacieId + ") AND id_produto in (" + stringfyProductsCart + ");";
			
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
			ResultSet f2 = statement2.executeQuery();
			
			List<String> productsId = new ArrayList<String>();
			
			while(f2.next()) {
				productsId.add(f2.getString(1));
			}
			
			statement2.close();
			
			String sql3 = "SELECT * FROM Endereco WHERE (id_endereco LIKE ?);";
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
			statement3.setString(1, pharmacieIdAddress);
			
			Map<String, String> getPharmacyAddress = getFromDB.getFromDB(variables, statement3);
			statement3.close();
			
			createPayload.object();
			String pharmacyName = cart.getPharmacyName();
			
			createPayload.key("user");
			createPayload.object();
			
			createPayload.key("cep").value(getAddress.get("cep"));
			createPayload.key("numero").value(getAddress.get("numero"));
			createPayload.key("complemento").value(getAddress.get("complemento"));
			
			createPayload.endObject();
			
			createPayload.key(pharmacyName);
			createPayload.object();
			
			double num = Double.parseDouble(pharmacieDistance);
			String dist = String.format("%.1f", num);
			
			createPayload.key("pharmacyId").value(pharmacieId);
			createPayload.key("pharmacyCnpj").value(PharmacyCnpj);
			createPayload.key("cep").value(getPharmacyAddress.get("cep"));
			createPayload.key("distancia").value(dist + " km");
			createPayload.key("numero").value(getPharmacyAddress.get("numero"));
			createPayload.key("complemento").value(getPharmacyAddress.get("complemento"));
			createPayload.key("fee").value("R$ " + CalcFeeDelivery.calcFeeDelivery(num).replace(".", ","));
			createPayload.key("time").value(CalcTimeDelivery.calcTimeDelivery(Math.round((num*100.0)/100.0)));
			createPayload.key("imgpath").value(ReplaceImageNames.replaceNames(pharmacyName.toLowerCase()) + ".png");
			
			createPayload.endObject();
			
			createPayload.key("products");
			createPayload.object();
			
			for(int b = 0; b < productsId.size(); b ++) {
				createPayload.key("productId-" + b).value(productsId.get(b));
			}
			
			createPayload.endObject();
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
		LOG.log(Level.INFO, "Nearby products payload: " + payload);
		LOG.exiting(NAME, "getProducts");
		return payload;
	}
}