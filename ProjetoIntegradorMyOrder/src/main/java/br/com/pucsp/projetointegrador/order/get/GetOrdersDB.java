package br.com.pucsp.projetointegrador.order.get;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;

public class GetOrdersDB {
	public static String NAME = GetOrdersDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetOrdersDB.class.getName());
	
	List<Float> productsPrice = new ArrayList<Float>();

	public StringBuffer getOrders(Map<String, String> variables, String session, PreparedStatement statementOrders) {
		LOG.entering(NAME, "getOrders");
		
		StringBuffer payload = new StringBuffer();
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
					createPayload.key("valorEntrega").value(delivery.getString(1).replace(".", ","));
					createPayload.key("totalPedido").value(String.format("%.2f", totalProdutos + Float.parseFloat(delivery.getString(1))).replace(".", ","));
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

		LOG.log(Level.INFO, "Orders: " + payload);
		LOG.exiting(NAME, "getOrders");
		return payload;
	}
	
	public JSONObject getOrdersProducts(Map<String, String> variables, String orderId, String pharmacyId) {
		LOG.entering(NAME, "getOrdersProducts");
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
			String sqlProducts = "SELECT quantidade,valor_total_compra_itens,id_produto_farmacia FROM Compra_Itens WHERE (id_compra LIKE ?);";
			PreparedStatement statementProducts = DB.connect(variables).prepareStatement(sqlProducts);
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
				PreparedStatement statementProductP = DB.connect(variables).prepareStatement(sqlProductP);
				statementProductP.setString(1, orders.getString(3));
				
				String newProductId = "";
				ResultSet productP = statementProductP.executeQuery();
				
				while(productP.next()) {
					newProductId = productP.getString(1);
					createPayload.key("idProduto").value(productP.getString(1));
				}
				statementProductP.close();
				
				String sqlProduct = "SELECT nome,amount,image,description FROM Produto WHERE (id_produto LIKE ?);";
				PreparedStatement statementProduct = DB.connect(variables).prepareStatement(sqlProduct);
				statementProduct.setString(1, newProductId);
											
				ResultSet product = statementProduct.executeQuery();
				
				while(product.next()) {
					createPayload.key("nomeProduto").value(product.getString(1));
					createPayload.key("amountProduto").value(product.getString(2));
					createPayload.key("imageProduto").value(product.getString(3));
					createPayload.key("descriptionProduto").value(product.getString(4));
				}
				statementProduct.close();
				
				String sqlProductPharmacy = "SELECT valor_unitario FROM Produto_Farmacia WHERE (id_produto LIKE ?) AND (id_farmacia LIKE ?);";
				PreparedStatement statementProductPharmacy = DB.connect(variables).prepareStatement(sqlProductPharmacy);
				statementProductPharmacy.setString(1, newProductId);
				statementProductPharmacy.setString(2, pharmacyId);
				
				ResultSet productPharmacy = statementProductPharmacy.executeQuery();
				
				while(productPharmacy.next()) {
					createPayload.key("valorUnitarioProduto").value(String.format("%.2f", Float.parseFloat(productPharmacy.getString(1))).replace(".", ","));
				}
				statementProductPharmacy.close();
				
				productsPrice.add(Float.parseFloat(orders.getString(2)));
				
				createPayload.endObject();
				
				i++;
			}
			
			createPayload.endObject();
			
			LOG.log(Level.INFO, "Products: " + payload);
			LOG.exiting(NAME, "getOrdersProducts");
			
			statementProducts.close();
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Products not getted from the database: " + e);
		}
		
		JSONObject products = new JSONObject(payload.toString());
		return products;
	}
}