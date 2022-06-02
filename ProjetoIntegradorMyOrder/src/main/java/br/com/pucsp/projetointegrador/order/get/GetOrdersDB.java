package br.com.pucsp.projetointegrador.order.get;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;

public class GetOrdersDB {
	public static String NAME = GetOrdersDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetOrdersDB.class.getName());

	public StringBuffer getOrders(Map<String, String> variables, String session, PreparedStatement statementOrders) {
		LOG.entering(NAME, "getOrders");
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);

		try {
			createPayload.object();
			
			ResultSet orders = statementOrders.executeQuery();
			while(orders.next()) {
				createPayload.key("order-" + orders.getString(1));
				createPayload.object();
				
				createPayload.key("idCompra").value(orders.getString(1));
				createPayload.key("dataCompra").value(orders.getString(2));
				createPayload.key("distanciaFarmacia").value(orders.getString(3));
				createPayload.key("tempoEntrega").value(orders.getString(4));
				createPayload.key("taxaEntrega").value(orders.getString(5));
				createPayload.key("localEntrega").value(orders.getString(6));
				createPayload.key("idFarmacia").value(orders.getString(7));
				
				String sqlPharmacy = "SELECT nome,cnpj FROM Farmacia WHERE (id_farmacia LIKE ?);";
				PreparedStatement statementPharmacy = DB.connect(variables).prepareStatement(sqlPharmacy);
				statementPharmacy.setString(1, orders.getString(7));
								
				ResultSet pharmacy = statementPharmacy.executeQuery();
				while(pharmacy.next()) {
					createPayload.key("nomeFarmacia").value(pharmacy.getString(1));
					createPayload.key("cnpjFarmacia").value(pharmacy.getString(2));
				}
				statementPharmacy.close();
				
				String sqlDelivery = "SELECT valor_entrega,data_entrega FROM Entrega WHERE (id_entrega LIKE ?);";
				PreparedStatement statementDelivery = DB.connect(variables).prepareStatement(sqlDelivery);
				statementDelivery.setString(1, orders.getString(8));
								
				ResultSet delivery = statementDelivery.executeQuery();
				while(delivery.next()) {
					createPayload.key("valorEntrega").value(delivery.getString(1));
					createPayload.key("dataEntrega").value(delivery.getString(2));
				}
				statementDelivery.close();
				
				String sqlStatus = "SELECT nome FROM Status WHERE (id_status LIKE ?);";
				PreparedStatement statementStatus = DB.connect(variables).prepareStatement(sqlStatus);
				statementStatus.setString(1, orders.getString(10));
								
				ResultSet status = statementStatus.executeQuery();
				while(status.next()) {
					createPayload.key("status").value(status.getString(1));
				}
				statementStatus.close();
				
				String sqlPayment = "SELECT id_cartao FROM Forma_Pagamento WHERE (id_forma_pagamento LIKE ?);";
				PreparedStatement statementPayment = DB.connect(variables).prepareStatement(sqlPayment);
				statementPayment.setString(1, orders.getString(9));
								
				ResultSet payment = statementPayment.executeQuery();
				while(payment.next()) {
					String sqlCard = "SELECT numero,id_bandeira FROM Cartao WHERE (id_cartao LIKE ?);";
					PreparedStatement statementCard = DB.connect(variables).prepareStatement(sqlCard);
					statementCard.setString(1, payment.getString(1));
									
					ResultSet card = statementCard.executeQuery();
					while(card.next()) {
						List<String> cardNum = new ArrayList<String>();
						
						for (int i = 0; i < card.getString(1).length(); i += 4) {
							cardNum.add(card.getString(1).substring(i, Math.min(i + 4,card.getString(1).length())));
						}
						
						createPayload.key("numeroCartao").value("**** " + cardNum.get(3));
						
						String sqlFlag = "SELECT nome FROM Bandeira_Cartao WHERE (id_bandeira LIKE ?);";
						PreparedStatement statementFlag = DB.connect(variables).prepareStatement(sqlFlag);
						statementFlag.setString(1, orders.getString(10));
										
						ResultSet flag = statementFlag.executeQuery();
						while(flag.next()) {
							createPayload.key("bandeiraCartao").value(flag.getString(1));
						}
						statementFlag.close();
					}
					statementCard.close();
				}
				statementPayment.close();
				
				createPayload.endObject();
			}
			
			statementOrders.close();
			
			createPayload.endObject();
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Orders not getted from the database: " + e);
		}
		finally {
			DB.disconnect();
		}

		// LOG.exiting(NAME, "GetCar");
		LOG.log(Level.INFO, "Orders: " + payload);
		LOG.exiting(NAME, "getOrders");
		return payload;
	}
}