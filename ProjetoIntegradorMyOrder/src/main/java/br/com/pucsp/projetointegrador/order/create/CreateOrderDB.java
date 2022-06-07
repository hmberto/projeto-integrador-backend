package br.com.pucsp.projetointegrador.order.create;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;
import br.com.pucsp.projetointegrador.order.utils.InsertProductsDB;
import br.com.pucsp.projetointegrador.order.utils.LogMessage;
import br.com.pucsp.projetointegrador.order.utils.SQLOrders;

public class CreateOrderDB {
	private static String name = CreateOrderDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(CreateOrderDB.class.getName());
	
	public StringBuilder createOrder(Map <String, String> variables, NewOrder order) throws SQLException {
		String methodName = "createOrder";
		log.entering(name, methodName);
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuilder payload = new StringBuilder();
		JSONWriter createPayload = new JSONWriter(payload);
		
		boolean products = false;
		
		String insertIfNotExistsCardFlag = SQLOrders.insertIfNotExistsCardFlag(order.getCardFlag());
		
		PreparedStatement statementCardFlag = DB.connect(variables).prepareStatement(insertIfNotExistsCardFlag);
		
		try {
			statementCardFlag.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementCardFlag.close();
		}
		
		String getFKCardFlag = "SELECT id_bandeira FROM Bandeira_Cartao WHERE (nome LIKE ?);";
		PreparedStatement statementFKCardFlag = DB.connect(variables).prepareStatement(getFKCardFlag);
		String fkCardFlag = "";
		
		try {
			statementFKCardFlag.setString(1, order.getCardFlag());
			
			ResultSet f1 = statementFKCardFlag.executeQuery();
			while(f1.next()) {
				fkCardFlag = f1.getString(1);
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementFKCardFlag.close();
		}
		
		String sqlA = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
		PreparedStatement statementA = DB.connect(variables).prepareStatement(sqlA);
		
		Map<String, String> getLoginSession = new HashMap<String, String>();
		
		try {
			statementA.setString(1, order.getSession());
			
			getLoginSession = getFromDB.getFromDB(statementA);
			statementA.close();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementA.close();
		}
		
		String sqlCard = "INSERT INTO Cartao (numero, data_validade, cvv, id_usuario, id_bandeira) VALUES (?, ?, ?, ?, ?);";
		PreparedStatement statementCard = DB.connect(variables).prepareStatement(sqlCard);
		
		String idUsuario = getLoginSession.get("id_usuario");
		
		try {
			statementCard.setString(1, order.getCardNumber());
			statementCard.setString(2, order.getCardDate());
			statementCard.setString(3, order.getCardCvv());
			statementCard.setString(4, idUsuario);
			statementCard.setString(5, fkCardFlag);
			
			statementCard.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementCard.close();
		}
		
		String sqlCardId = "SELECT * FROM Cartao WHERE (numero LIKE ?);";
		PreparedStatement statementCardId = DB.connect(variables).prepareStatement(sqlCardId);
		Map<String, String> getCardId = new HashMap<String, String>();
		
		try {
			statementCardId.setString(1, order.getCardNumber());
			
			getCardId = getFromDB.getFromDB(statementCardId);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementCardId.close();
		}
		
		String sqlPayment = "INSERT INTO Forma_Pagamento (id_cartao) VALUES (?);";
		PreparedStatement statementPayment = DB.connect(variables).prepareStatement(sqlPayment);
		
		try {
			statementPayment.setString(1, getCardId.get("id_cartao"));
			
			statementPayment.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementPayment.close();
		}
		
		String sqlPaymentId = "SELECT * FROM Forma_Pagamento WHERE (id_cartao LIKE ?);";
		PreparedStatement statementPaymentId = DB.connect(variables).prepareStatement(sqlPaymentId);
		Map<String, String> getPaymentId = new HashMap<String, String>();
		
		try {
			statementPaymentId.setString(1, getCardId.get("id_cartao"));
			
			getPaymentId = getFromDB.getFromDB(statementPaymentId);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementPaymentId.close();
		}
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String time = dtf.format(LocalDateTime.now());

		String sqlDelivery = "INSERT INTO Entrega (valor_entrega, data_entrega, id_entregador) VALUES (?, ?, ?);";
		PreparedStatement statementDelivery = DB.connect(variables).prepareStatement(sqlDelivery);
		
		try {
			statementDelivery.setString(1, order.getDeliveryFee());
			statementDelivery.setString(2, time);
			statementDelivery.setString(3, null);
			
			statementDelivery.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementDelivery.close();
		}
		
		String sqlDeliveryId = "SELECT * FROM Entrega WHERE (valor_entrega LIKE ?) AND (data_entrega LIKE ?);";
		PreparedStatement statementDeliveryId = DB.connect(variables).prepareStatement(sqlDeliveryId);
		Map<String, String> getDeliveryId = new HashMap<String, String>();
		
		try {
			statementDeliveryId.setString(1, order.getDeliveryFee());
			statementDeliveryId.setString(2, time);
			
			getDeliveryId = getFromDB.getFromDB(statementDeliveryId);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementDeliveryId.close();
		}
		
		
		String createOrder = "INSERT INTO Compra (data_compra, distancia_farmacia, tempo_entrega, taxa_entrega, local_entrega, id_usuario, id_farmacia, id_entrega, id_forma_pagamento, id_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement statementOrder = DB.connect(variables).prepareStatement(createOrder);
		
		try {
			statementOrder.setString(1, time);
			statementOrder.setString(2, order.getPharmacyDistance());
			statementOrder.setString(3, order.getDeliveryTime());
			statementOrder.setString(4, order.getDeliveryFee());
			statementOrder.setString(5, order.getDeliveryAddress());
			statementOrder.setString(6, idUsuario);
			statementOrder.setString(7, order.getPharmacyId());
			statementOrder.setString(8, getDeliveryId.get("id_entrega"));
			statementOrder.setString(9, getPaymentId.get("id_forma_pagamento"));
			statementOrder.setString(10, "4");
			
			statementOrder.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementOrder.close();
		}
		
		String sqlOrderId = "SELECT * FROM Compra WHERE (data_compra LIKE ?) AND (id_usuario LIKE ?) AND (id_farmacia LIKE ?);";
		PreparedStatement statementOrderId = DB.connect(variables).prepareStatement(sqlOrderId);
		
		try {
			statementOrderId.setString(1, time);
			statementOrderId.setString(2, idUsuario);
			statementOrderId.setString(3, order.getPharmacyId());
			
			Map<String, String> getOrderId = getFromDB.getFromDB(statementOrderId);
			
			createPayload.object();
			
			createPayload.key("orderId").value(getOrderId.get("id_compra"));
			
			createPayload.endObject();
			
			InsertProductsDB insertProductsDB = new InsertProductsDB();
			products = insertProductsDB.insertProducts(variables, order, getOrderId.get("id_compra"));
			
			if(products) {
				log.exiting(name, methodName);
				return payload;
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementOrderId.close();
			DB.disconnect();
		}
		
		return null;
	}
}