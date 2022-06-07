package br.com.pucsp.projetointegrador.order.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.order.create.NewOrder;
import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;

public class InsertProductsDB {
	private static String name = InsertProductsDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(InsertProductsDB.class.getName());
	
	public boolean insertProducts(Map <String, String> variables, NewOrder order, String orderId) throws SQLException {
		String methodName = "insertProducts";
		log.entering(name, methodName);
		
		GetFromDB getFromDB = new GetFromDB();
				
		String[] products = returnProducts(order).toArray(new String[returnProducts(order).size()]);
		
		for(int i = 0; i < products.length; i++) {
			String[] productInfo = products[i].split("-");
			String sqlProductsPharmacyId = "SELECT * FROM Produto_Farmacia WHERE (id_farmacia LIKE ?) AND (id_produto LIKE ?) AND (valor_unitario LIKE ?);";
			PreparedStatement statementProductsPharmacyId = DB.connect(variables).prepareStatement(sqlProductsPharmacyId);
			
			try {
				statementProductsPharmacyId.setString(1, order.getPharmacyId());
				statementProductsPharmacyId.setString(2, productInfo[0]);
				statementProductsPharmacyId.setFloat(3, Float.parseFloat(productInfo[2]));
				
				Map<String, String> getOrderId = getFromDB.getFromDB(statementProductsPharmacyId);
				statementProductsPharmacyId.close();
				
				float total = Float.parseFloat(productInfo[2]) * Integer.parseInt(productInfo[3]);
				
				insert(variables, productInfo[3], total, getOrderId.get("id_produto_farmacia"), orderId);
			}
			catch (SQLException e) {
				throw new SQLException(LogMessage.message(e.toString()));
			}
			finally {
				statementProductsPharmacyId.close();
				DB.disconnect();
			}
		}
		
		log.exiting(name, methodName);
		return true;
	}
	
	public void insert(Map <String, String> variables, String productInfo, Float total, String idProdutoFarmacia, String orderId) throws SQLException {
		String methodName = "insertProducts";
		log.entering(name, methodName);
		
		String sqlProductsOrder = "INSERT INTO Compra_Itens (quantidade, valor_total_compra_itens, id_produto_farmacia, id_compra) VALUES (?, ?, ?, ?);";
		PreparedStatement statement = DB.connect(variables).prepareStatement(sqlProductsOrder);
		
		try {
			statement.setString(1, productInfo);
			statement.setFloat(2, total);
			statement.setString(3, idProdutoFarmacia);
			statement.setString(4, orderId);
			
			statement.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
		}
		
		log.exiting(name, methodName);
	}
	
	public static Set<String> returnProducts(NewOrder order) {
		String methodName = "returnProducts";
		log.entering(name, methodName);
		
		Set<String> products = new HashSet<String>();
		
		for(int i = 0; i < order.getProducts().length; i++) {
			String newProduct = order.getProducts()[i].replace(",", ".") + "-" + counterArray(order.getProducts(), order.getProducts()[i]);
			
			products.add(newProduct);
		}
		
		log.exiting(name, methodName);
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