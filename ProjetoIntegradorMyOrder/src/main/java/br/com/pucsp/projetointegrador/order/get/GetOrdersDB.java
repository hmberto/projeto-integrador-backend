package br.com.pucsp.projetointegrador.order.get;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.utils.LogMessage;

public class GetOrdersDB {
	private static String name = GetOrdersDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetOrdersDB.class.getName());
	
	List<Float> productsPrice = new ArrayList<Float>();

	public StringBuilder getOrders(Map<String, String> variables, PreparedStatement statementOrders) throws SQLException {
		String methodName = "getOrders";
		log.entering(name, methodName);
		
		StringBuilder payload = new StringBuilder();
		JSONWriter createPayload = new JSONWriter(payload);
		
		float totalProdutos = 0;

		try {
			createPayload.object();
			
			ResultSet orders = statementOrders.executeQuery();
			while(orders.next()) {
				productsPrice.clear();
				createPayload.key("order-" + orders.getString(1));
				createPayload.object();
				
				JSONObject products = getOrdersProducts(variables, orders.getString(1), orders.getString(7));
				
				totalProdutos = 0;
				
				for(int i = 0; i < productsPrice.size(); i++) {
					totalProdutos = totalProdutos + productsPrice.get(i);
				}
				
				createPayload.key("totalProdutos").value(String.format("%.2f", totalProdutos).replace(".", ","));
				createPayload.key("products").value(products);
				createPayload.key("idCompra").value(orders.getString(1));
				createPayload.key("dataCompra").value(orders.getString(2));
				createPayload.key("distanciaFarmacia").value(orders.getString(3));
				createPayload.key("tempoEntrega").value(orders.getString(4));
				createPayload.key("localEntrega").value(orders.getString(6));
				createPayload.key("idFarmacia").value(orders.getString(7));
				
				String sqlPharmacy = "SELECT nome,cnpj FROM Farmacia WHERE (id_farmacia LIKE ?);";
				List<String> propertiesPharmacy = new ArrayList<String>();
				propertiesPharmacy.add(orders.getString(7));
				Map<String, String> pharmacy = getFromDB(variables, sqlPharmacy, propertiesPharmacy);
				
				createPayload.key("nomeFarmacia").value(pharmacy.get("nome"));
				createPayload.key("cnpjFarmacia").value(pharmacy.get("cnpj"));
				
				String sqlDelivery = "SELECT valor_entrega,data_entrega FROM Entrega WHERE (id_entrega LIKE ?);";
				List<String> propertiesDelivery = new ArrayList<String>();
				propertiesDelivery.add(orders.getString(8));
				Map<String, String> delivery = getFromDB(variables, sqlDelivery, propertiesDelivery);
				
				createPayload.key("valorEntrega").value(delivery.get("valor_entrega").replace(".", ","));
				createPayload.key("totalPedido").value(String.format("%.2f", totalProdutos + Float.parseFloat(delivery.get("valor_entrega"))).replace(".", ","));
				createPayload.key("dataEntrega").value(delivery.get("data_entrega"));
				
				String sqlStatus = "SELECT nome FROM Status WHERE (id_status LIKE ?);";
				List<String> propertiesStatus = new ArrayList<String>();
				propertiesStatus.add(orders.getString(10));
				Map<String, String> status = getFromDB(variables, sqlStatus, propertiesStatus);
				
				createPayload.key("status").value(status.get("nome"));
				
				String sqlPayment = "SELECT id_cartao FROM Forma_Pagamento WHERE (id_forma_pagamento LIKE ?);";
				List<String> propertiesPayment = new ArrayList<String>();
				propertiesPayment.add(orders.getString(9));
				Map<String, String> payment = getFromDB(variables, sqlPayment, propertiesPayment);
				
				String sqlCard = "SELECT numero,id_bandeira FROM Cartao WHERE (id_cartao LIKE ?);";
				List<String> propertiesCard = new ArrayList<String>();
				propertiesCard.add(payment.get("id_cartao"));
				Map<String, String> card = getFromDB(variables, sqlCard, propertiesCard);
				
				List<String> cardNum = new ArrayList<String>();
				
				String cardNumber = card.get("numero");
				for (int i = 0; i < cardNumber.length(); i += 4) {
					cardNum.add(cardNumber.substring(i, Math.min(i + 4,cardNumber.length())));
				}
				
				createPayload.key("numeroCartao").value("**** " + cardNum.get(3));
				
				String sqlFlag = "SELECT nome FROM Bandeira_Cartao WHERE (id_bandeira LIKE ?);";
				List<String> propertiesFlag = new ArrayList<String>();
				propertiesFlag.add(card.get("id_bandeira"));
				Map<String, String> flag = getFromDB(variables, sqlFlag, propertiesFlag);
				
				createPayload.key("bandeiraCartao").value(flag.get("nome"));
				
				createPayload.endObject();
			}
			
			createPayload.endObject();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementOrders.close();
			DB.disconnect();
		}
		
		log.exiting(name, methodName);
		return payload;
	}
	
	public Map<String, String> getFromDB(Map<String, String> variables, String sqlString, List<String> propertie) throws SQLException {
		String methodName = "getFromDB";
		log.entering(name, methodName);
		
		Map<String, String> data = new HashMap<String, String>();
		
		PreparedStatement statementY = DB.connect(variables).prepareStatement(sqlString);
		
		try {
			for(int i = 0; i < propertie.size(); i++) {
				statementY.setString(i+1, propertie.get(i));
			}
			
			ResultSet g = statementY.executeQuery();
			ResultSetMetaData h = g.getMetaData();
			int columnCount = h.getColumnCount();
			
			while(g.next()) {
				for (int i = 1; i <= columnCount; i++) {
					data.put(h.getColumnName(i), g.getString(i));
				}
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementY.close();
		}
		
		log.exiting(name, methodName);
		return data;
	}
	
	public JSONObject getOrdersProducts(Map<String, String> variables, String orderId, String pharmacyId) throws SQLException {
		String methodName = "getOrdersProducts";
		
		log.entering(name, methodName);
		
		StringBuilder payload = new StringBuilder();
		JSONWriter createPayload = new JSONWriter(payload);
		
		String sqlProducts = "SELECT quantidade,valor_total_compra_itens,id_produto_farmacia FROM Compra_Itens WHERE (id_compra LIKE ?);";
		PreparedStatement statementProducts = DB.connect(variables).prepareStatement(sqlProducts);
		
		try {
			statementProducts.setString(1, orderId);
			
			createPayload.object();
			
			ResultSet orders = statementProducts.executeQuery();
			
			int i = 1;
			while(orders.next()) {
				createPayload.key("product-" + i);
				createPayload.object();
				
				createPayload.key("quantidade").value(orders.getString(1));
				createPayload.key("valorTotalItem").value(String.format("%.2f", Float.parseFloat(orders.getString(2))).replace(".", ","));
				
				String sqlProductP = "SELECT id_produto FROM Produto_Farmacia WHERE (id_produto_farmacia LIKE ?);";
				List<String> propertiesProductP = new ArrayList<String>();
				propertiesProductP.add(orders.getString(3));
				
				Map<String, String> productP = getFromDB(variables, sqlProductP, propertiesProductP);
				
				String newProductId = "";
				
				newProductId = productP.get("id_produto");
				createPayload.key("idProduto").value(productP.get("id_produto"));
				
				String sqlProduct = "SELECT nome,amount,image,description FROM Produto WHERE (id_produto LIKE ?);";
				List<String> propertiesProduct = new ArrayList<String>();
				propertiesProduct.add(newProductId);
				
				Map<String, String> product = getFromDB(variables, sqlProduct, propertiesProduct);
				
				createPayload.key("nomeProduto").value(product.get("nome"));
				createPayload.key("amountProduto").value(product.get("amount"));
				createPayload.key("imageProduto").value(product.get("image"));
				createPayload.key("descriptionProduto").value(product.get("description"));
				
				String sqlProductPharmacy = "SELECT valor_unitario FROM Produto_Farmacia WHERE (id_produto LIKE ?) AND (id_farmacia LIKE ?);";
				List<String> propertiesProductPharmacy = new ArrayList<String>();
				propertiesProductPharmacy.add(newProductId);
				propertiesProductPharmacy.add(pharmacyId);
				
				Map<String, String> productPharmacy = getFromDB(variables, sqlProductPharmacy, propertiesProductPharmacy);
				
				createPayload.key("valorUnitarioProduto").value(String.format("%.2f", Float.parseFloat(productPharmacy.get("valor_unitario"))).replace(".", ","));
				
				productsPrice.add(Float.parseFloat(orders.getString(2)));
				
				createPayload.endObject();
				
				i++;
			}
			
			createPayload.endObject();
			
			statementProducts.close();
			
			JSONObject products = new JSONObject(payload.toString());
			log.exiting(name, methodName);
			return products;
		}
		catch (Exception e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementProducts.close();
		}
	}
}