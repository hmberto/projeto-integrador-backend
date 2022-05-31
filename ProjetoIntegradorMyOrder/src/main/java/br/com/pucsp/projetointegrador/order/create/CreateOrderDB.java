package br.com.pucsp.projetointegrador.order.create;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;

public class CreateOrderDB {
	public static String NAME = CreateOrderDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(CreateOrderDB.class.getName());
	
	public StringBuffer createOrder(Map <String, String> variables, NewOrder order) {
		LOG.entering(NAME, "createOrder");
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
			String INSERT_IF_NOT_EXISTS_CARD_FLAG = "INSERT INTO Bandeira_Cartao ( `nome` )\n"
					+ "    SELECT * FROM (SELECT '" + order.getCardFlag() + "' ) AS tmp\n"
					+ "    WHERE NOT EXISTS (SELECT `nome`, `id_bandeira` FROM `Bandeira_Cartao` WHERE `nome` = '" + order.getCardFlag() + "' \n"
					+ "    ) LIMIT 1";
			
			PreparedStatement statementCardFlag = DB.connect(variables).prepareStatement(INSERT_IF_NOT_EXISTS_CARD_FLAG);
			statementCardFlag.execute();
			statementCardFlag.close();
			
			String GET_FK_CARD_FLAG = "SELECT id_bandeira FROM Bandeira_Cartao WHERE (nome LIKE ?);";
			PreparedStatement statementFKCardFlag = DB.connect(variables).prepareStatement(GET_FK_CARD_FLAG);
			statementFKCardFlag.setString(1, order.getCardFlag());
			
			String FK_CARD_FLAG = "";
			ResultSet f1 = statementFKCardFlag.executeQuery();
			while(f1.next()) {
				FK_CARD_FLAG = f1.getString(1);
			}
			statementFKCardFlag.close();
			
			String sqlA = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
			PreparedStatement statementA = DB.connect(variables).prepareStatement(sqlA);
			statementA.setString(1, order.getSession());
			
			Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statementA);
			statementA.close();
			
			String sqlCard = "INSERT INTO Cartao (numero, data_validade, cvv, id_usuario, id_bandeira) VALUES (?, ?, ?, ?, ?);";
			PreparedStatement statementCard = DB.connect(variables).prepareStatement(sqlCard);
			statementCard.setString(1, order.getCardNumber());
			statementCard.setString(2, order.getCardDate());
			statementCard.setString(3, order.getCardCvv());
			statementCard.setString(4, getLoginSession.get("id_usuario"));
			statementCard.setString(5, FK_CARD_FLAG);
			
			statementCard.execute();
			statementCard.close();
			
			String sqlCardId = "SELECT * FROM Cartao WHERE (numero LIKE ?);";
			PreparedStatement statementCardId = DB.connect(variables).prepareStatement(sqlCardId);
			statementCardId.setString(1, order.getCardNumber());
			
			Map<String, String> getCardId = getFromDB.getFromDB(variables, statementCardId);
			statementCardId.close();
			
			String sqlPayment = "INSERT INTO Forma_Pagamento (id_cartao) VALUES (?);";
			PreparedStatement statementPayment = DB.connect(variables).prepareStatement(sqlPayment);
			statementPayment.setString(1, getCardId.get("id_cartao"));
			
			statementPayment.execute();
			statementPayment.close();
			
			String sqlPaymentId = "SELECT * FROM Forma_Pagamento WHERE (id_cartao LIKE ?);";
			PreparedStatement statementPaymentId = DB.connect(variables).prepareStatement(sqlPaymentId);
			statementPaymentId.setString(1, getCardId.get("id_cartao"));
			
			Map<String, String> getPaymentId = getFromDB.getFromDB(variables, statementPaymentId);
			statementPaymentId.close();
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
			String time = dtf.format(LocalDateTime.now());
			
			String sqlDelivery = "INSERT INTO Entrega (valor_entrega, data_entrega, id_entregador) VALUES (?, ?, ?);";
			PreparedStatement statementDelivery = DB.connect(variables).prepareStatement(sqlDelivery);
			statementDelivery.setString(1, order.getDeliveryFee());
			statementDelivery.setString(2, time);
			statementDelivery.setString(3, null);
			
			statementDelivery.execute();
			statementDelivery.close();
			
			String sqlDeliveryId = "SELECT * FROM Entrega WHERE (valor_entrega LIKE ?) AND (data_entrega LIKE ?);";
			PreparedStatement statementDeliveryId = DB.connect(variables).prepareStatement(sqlDeliveryId);
			statementDeliveryId.setString(1, order.getDeliveryFee());
			statementDeliveryId.setString(2, time);
			
			Map<String, String> getDeliveryId = getFromDB.getFromDB(variables, statementDeliveryId);
			statementDeliveryId.close();
			
			String createOrder = "INSERT INTO Compra (data_compra, distancia_farmacia, tempo_entrega, taxa_entrega, local_entrega, id_usuario, id_farmacia, id_entrega, id_forma_pagamento, id_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement statementOrder = DB.connect(variables).prepareStatement(createOrder);
			statementOrder.setString(1, time);
			statementOrder.setString(2, order.getPharmacyDistance());
			statementOrder.setString(3, order.getDeliveryTime());
			statementOrder.setString(4, order.getDeliveryFee());
			statementOrder.setString(5, order.getDeliveryAddress());
			statementOrder.setString(6, getLoginSession.get("id_usuario"));
			statementOrder.setString(7, order.getPharmacyId());
			statementOrder.setString(8, getDeliveryId.get("id_entrega"));
			statementOrder.setString(9, getPaymentId.get("id_forma_pagamento"));
			statementOrder.setString(10, "4");
			
			statementOrder.execute();
			statementOrder.close();
			
			String sqlOrderId = "SELECT * FROM Compra WHERE (data_compra LIKE ?) AND (id_usuario LIKE ?) AND (id_farmacia LIKE ?);";
			PreparedStatement statementOrderId = DB.connect(variables).prepareStatement(sqlOrderId);
			statementOrderId.setString(1, time);
			statementOrderId.setString(2, getLoginSession.get("id_usuario"));
			statementOrderId.setString(3, order.getPharmacyId());
			
			Map<String, String> getOrderId = getFromDB.getFromDB(variables, statementOrderId);
			statementOrderId.close();
			
			createPayload.object();
			
			createPayload.key("orderId").value(getOrderId.get("id_compra"));
			
			createPayload.endObject();
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Order not created at the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		// LOG.exiting(NAME, "GetCar");
		LOG.log(Level.INFO, "Order ID: " + payload);
		LOG.exiting(NAME, "createOrder");
		return payload;
	}
}