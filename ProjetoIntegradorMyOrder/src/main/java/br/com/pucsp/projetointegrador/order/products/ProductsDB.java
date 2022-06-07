package br.com.pucsp.projetointegrador.order.products;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;
import br.com.pucsp.projetointegrador.order.utils.CalcFeeDelivery;
import br.com.pucsp.projetointegrador.order.utils.CalcTimeDelivery;
import br.com.pucsp.projetointegrador.order.utils.LogMessage;
import br.com.pucsp.projetointegrador.order.utils.ReplaceImageNames;
import br.com.pucsp.projetointegrador.order.utils.SQLOrders;

public class ProductsDB {
	private static String name = ProductsDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(ProductsDB.class.getName());
	
	public StringBuilder getProducts(Map <String, String> variables, GetProducts cart) throws SQLException {
		String methodName = "getProducts";
		log.entering(name, methodName);
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuilder payload = new StringBuilder();
		JSONWriter createPayload = new JSONWriter(payload);
		
		String sqlA = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
		PreparedStatement statementA = DB.connect(variables).prepareStatement(sqlA);
		Map<String, String> getLoginSession = new HashMap<String, String>();
		
		try {
			statementA.setString(1, cart.getSession());
			
			getLoginSession = getFromDB.getFromDB(statementA);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementA.close();
			DB.disconnect();
		}
		
		String sqlB = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
		PreparedStatement statementB = DB.connect(variables).prepareStatement(sqlB);
		Map<String, String> getUser = new HashMap<String, String>();
		
		try {
			statementB.setString(1, getLoginSession.get("id_usuario"));
			
			getUser = getFromDB.getFromDB(statementB);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementB.close();
			DB.disconnect();
		}
		
		String sqlC = "SELECT * FROM Endereco WHERE (id_endereco LIKE ?);";
		PreparedStatement statementC = DB.connect(variables).prepareStatement(sqlC);
		Map<String, String> getAddress = new HashMap<String, String>();
		
		try {
			statementC.setString(1, getUser.get("id_endereco"));
			
			getAddress = getFromDB.getFromDB(statementC);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementC.close();
			DB.disconnect();
		}
		
		List<String> productsCart = new ArrayList<String>();
		for (String e : cart.getProducts()) {
			productsCart.add(e);
		}
		
		StringBuilder stringfyProductsCart = new StringBuilder();
		
		stringfyProductsCart.append(productsCart.toString().replace("[", "").replace("]", ""));
		stringfyProductsCart.toString();
		
		String sql1 = SQLOrders.sql(getAddress.get("lat"), getAddress.get("lon"), cart.getDistance(), cart.getPharmacyName());
		
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql1);
		
		String pharmacieId = "";
		String pharmacieIdAddress = "";
		String pharmacieDistance = "";
		String pharmacyCnpj = "";
		
		try {
			ResultSet f = statement.executeQuery();
			
			while(f.next()) {
				pharmacyCnpj = f.getString(1);
				pharmacieId = f.getString(2);
				pharmacieIdAddress = f.getString(3);
				pharmacieDistance = f.getString(4);
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		String sql2 = SQLOrders.sqlProductPharmacy(pharmacieId, stringfyProductsCart);
		PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
		
		List<String> productsId = new ArrayList<String>();
		
		try {
			ResultSet f2 = statement2.executeQuery();
			
			while(f2.next()) {
				productsId.add(f2.getString(1));
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement2.close();
			DB.disconnect();
		}
		
		String sql3 = "SELECT * FROM Endereco WHERE (id_endereco LIKE ?);";
		PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
		
		Map<String, String> getPharmacyAddress = new HashMap<String, String>();
		
		try {
			statement3.setString(1, pharmacieIdAddress);
			
			getPharmacyAddress = getFromDB.getFromDB(statement3);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement3.close();
			DB.disconnect();
		}
		
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
		createPayload.key("pharmacyCnpj").value(pharmacyCnpj);
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
		
		log.exiting(name, methodName);
		return payload;
	}
}