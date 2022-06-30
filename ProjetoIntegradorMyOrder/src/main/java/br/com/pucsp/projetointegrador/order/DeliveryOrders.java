package br.com.pucsp.projetointegrador.order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;
import br.com.pucsp.projetointegrador.order.utils.LogMessage;

public class DeliveryOrders {
	private static String name = DeliveryOrder.class.getSimpleName();
	private static Logger log = Logger.getLogger(DeliveryOrder.class.getName());
	
	GetFromDB getFromDB = new GetFromDB();
	
	public JSONObject deliveryOrders(Map<String, String> variables, Map<String, String> cacheAddress, String deliverymanID, int statusID) throws SQLException, NumberFormatException, MalformedURLException, IOException {
		log.entering(name, "deliveryOrders");
		
		StringBuilder payload = new StringBuilder();
		JSONWriter createResource = new JSONWriter(payload);
		
		String sql = "SELECT * FROM Compra WHERE (id_status LIKE ?)";
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
		
		createResource.object();
		
		try {
			statement.setInt(1, statusID);
			
			ResultSet orders = statement.executeQuery();
			
			int i = 1;
			while(orders.next()) {
				createResource.key("delivery-" + i);
				createResource.object();
				
				String sqlPharmacy = "SELECT * FROM Farmacia WHERE (id_farmacia LIKE ?)";
				PreparedStatement statementPharmacy = DB.connect(variables).prepareStatement(sqlPharmacy);
				
				Map<String, String> pharmacy = new HashMap<String, String>();
				
				statementPharmacy.setInt(1, Integer.parseInt(orders.getString(8)));
				
				try {
					ResultSet g = statementPharmacy.executeQuery();
					ResultSetMetaData h = g.getMetaData();
					int columnCount = h.getColumnCount();
					
					while(g.next()) {
						for (int w = 1; w <= columnCount; w++) {
							pharmacy.put(h.getColumnName(w), g.getString(w));
						}
					}
				}
				catch (SQLException e) {
					throw new SQLException(LogMessage.message(e.toString()));
				}
				finally {
					statementPharmacy.close();
//					DB.disconnect();
				}
				
				String address = getPharmacyAddress(variables, cacheAddress, Integer.parseInt(pharmacy.get("id_endereco")));
				
				createResource.key("pharmacyAddress").value(address);
				createResource.key("userAddress").value(orders.getString(6));
				createResource.key("distance").value(orders.getString(3));
				createResource.key("orderId").value(orders.getString(1));
				createResource.key("pharmacyName").value(pharmacy.get("nome"));
				createResource.key("pharmacyCNPJ").value(pharmacy.get("cnpj"));
				
				createResource.endObject();
				
				i++;
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		createResource.endObject();
		
		JSONObject jsonPayload = new JSONObject(payload.toString());
		
		log.exiting(name, "deliveryOrders");
		return jsonPayload;
	}
	
	private String getPharmacyAddress(Map<String, String> variables, Map<String, String> cacheAddress, int idEndereco) throws SQLException, MalformedURLException, IOException {
		log.entering(name, "getPharmacyAddress");
		
		String sqlPharmacyAddress = "SELECT * FROM Endereco WHERE (id_endereco LIKE ?)";
		PreparedStatement statementPharmacyAddress = DB.connect(variables).prepareStatement(sqlPharmacyAddress);
		
		Map<String, String> pharmacyAddress = new HashMap<String, String>();
		
		try {
			statementPharmacyAddress.setInt(1, idEndereco);
			
			ResultSet g = statementPharmacyAddress.executeQuery();
			ResultSetMetaData h = g.getMetaData();
			int columnCount = h.getColumnCount();
			
			while(g.next()) {
				for (int i = 1; i <= columnCount; i++) {
					pharmacyAddress.put(h.getColumnName(i), g.getString(i));
				}
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementPharmacyAddress.close();
//			DB.disconnect();
		}
		
		String cep = pharmacyAddress.get("cep");
		String numero = pharmacyAddress.get("numero");
		String complemento = pharmacyAddress.get("complemento");
		
		String address = "";
		String addressFromCache = cacheAddress.get(cep);
		
		if(addressFromCache == null || addressFromCache.equals("null")) {
			log.log(Level.INFO, "Gettin pharmacy address from VIACEP");
			
			String url = "https://viacep.com.br/ws/" + cep + "/json/";
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
			
			log.log(Level.INFO, "VIACEP url: " + url);
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			
			String line;
			StringBuilder output = new StringBuilder();
			while ((line = br.readLine()) != null) {
				output.append(line);
			}
			
			conn.disconnect();
			
			JSONObject json = new JSONObject(output.toString());
			
			log.log(Level.INFO, "Address from VIACEP: " + json);
			
			String street = json.getString("logradouro");
			String district = json.getString("bairro");
			String city = json.getString("localidade");
			String state = json.getString("uf");
			String newCep = json.getString("cep");
			
			if(complemento == "" || complemento.equals("NULL")) {
				address = street + ", " + numero + " - " + district + ", " + city + " - " + state + ", " + newCep;
			}
			else {
				address = street + ", " + numero + " - " + complemento + " - " + district + ", " + city + " - " + state + ", " + newCep;
			}
			
			cacheAddress.put(cep, address);
		}
		else {
			log.log(Level.INFO, "Gettin pharmacy address from cache");
			
			address = addressFromCache;
		}
		
		log.exiting(name, "getPharmacyAddress");
		return address;
	}
}