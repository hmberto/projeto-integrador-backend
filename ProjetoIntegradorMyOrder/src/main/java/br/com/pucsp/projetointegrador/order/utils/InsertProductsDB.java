package br.com.pucsp.projetointegrador.order.utils;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.order.create.NewOrder;
import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;

public class InsertProductsDB {
	public static String NAME = InsertProductsDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(InsertProductsDB.class.getName());
	
	public boolean insertProducts(Map <String, String> variables, NewOrder order, String orderId) {
		LOG.entering(NAME, "insertProducts");
		
		GetFromDB getFromDB = new GetFromDB();
				
		String[] products = returnProducts(order).toArray(new String[returnProducts(order).size()]);
		
		for(int i = 0; i < products.length; i++) {
			LOG.log(Level.INFO, "product: " + products[i]);
			
			try {
				String[] productInfo = products[i].split("-");
				
				String sqlProductsPharmacyId = "SELECT * FROM Produto_Farmacia WHERE (id_farmacia LIKE ?) AND (id_produto LIKE ?) AND (valor_unitario LIKE ?);";
				PreparedStatement statementProductsPharmacyId = DB.connect(variables).prepareStatement(sqlProductsPharmacyId);
				statementProductsPharmacyId.setString(1, order.getPharmacyId());
				statementProductsPharmacyId.setString(2, productInfo[0]);
				statementProductsPharmacyId.setFloat(3, Float.parseFloat(productInfo[2]));
				
				Map<String, String> getOrderId = getFromDB.getFromDB(variables, statementProductsPharmacyId);
				statementProductsPharmacyId.close();
				
				float total = Float.parseFloat(productInfo[2]) * Integer.parseInt(productInfo[3]);
				
				String sqlProductsOrder = "INSERT INTO Compra_Itens (quantidade, valor_total_compra_itens, id_produto_farmacia, id_compra) VALUES (?, ?, ?, ?);";
				PreparedStatement statement = DB.connect(variables).prepareStatement(sqlProductsOrder);
				statement.setString(1, productInfo[3]);
				statement.setFloat(2, total);
				statement.setString(3, getOrderId.get("id_produto_farmacia"));
				statement.setString(4, orderId);
				
				statement.execute();
				statement.close();
			}
			catch (Exception e) {
				LOG.log(Level.SEVERE, "Item not created at the database: " + e);
			}
		}
		
		LOG.exiting(NAME, "insertProducts");
		return true;
	}
	
	public static Set<String> returnProducts(NewOrder order) {
		LOG.entering(NAME, "returnProducts");
		
		Set<String> products = new HashSet<String>();
		
		for(int i = 0; i < order.getProducts().length; i++) {
			LOG.log(Level.INFO, "product: " + order.getProducts()[i] + " - Quantidade: " + counterArray(order.getProducts(), order.getProducts()[i]));
			
			String newProduct = order.getProducts()[i] + "-" + counterArray(order.getProducts(), order.getProducts()[i]);
			
			products.add(newProduct);
		}
		
		LOG.exiting(NAME, "returnProducts");
		return products;
	}
	
	public static int counterArray(String[] products, Object product){
	    int contador = 0;
	    if (products != null){
	        for (Object item : products){
	            if (item != null && item.equals(product)){
	                contador++;
	            }
	        }
	    }
	    return contador;
	}
}