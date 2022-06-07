package br.com.pucsp.projetointegrador;

import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.Products;
import br.com.pucsp.projetointegrador.product.utils.GetUserCoordinates;
import br.com.pucsp.projetointegrador.product.utils.LogMessage;
import br.com.pucsp.projetointegrador.product.utils.ProjectVariables;
import br.com.pucsp.projetointegrador.product.utils.SQLProducts;

@Produces("application/json")
@Consumes("application/json")
public class Rest {
	private static String name = Rest.class.getSimpleName();
	private static Logger log = Logger.getLogger(Rest.class.getName());
	
	ProjectVariables projectVariables = new ProjectVariables();
	Map <String, String> variables = projectVariables.projectVariables();
	
	int sessionLength = Integer.parseInt(variables.get("SESSION_LENGTH"));
	
	URI uri = URI.create("https://projeto-integrador-frontend.herokuapp.com");
	
	@GET
	@Path("/")
	public Response getTest() {
		String test = "{\n"
				+ "    \"Hello\":\"World!\"\n"
				+ "}";
		
		return Response.ok(test).build();
	}
	
	@GET
	@Path("/product/all-products/{distance}/{session}")
	public Response getProducts(@PathParam("session") String session, @PathParam("distance") String distance) {
		log.entering(name, "getProducts");
		
		if(session.length() == sessionLength) {
			try {
				GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
				Map<String, String> getAddress = getUserCoordinates.coordinatesFromDB(variables, session);
				
				String sql1 = SQLProducts.products(getAddress.get("lat"), getAddress.get("lon"), distance);
				
				String sqlProduct = SQLProducts.productsIn();
				
				Products products = new Products();
				JSONObject payload = products.getProducts(variables, sql1, sqlProduct);
				
				log.exiting(name, "getProducts");
				return Response.ok(payload.toString()).build();
			}
			catch (Exception e) {
				log.log(Level.SEVERE, LogMessage.productsMessage(e.toString()));
			}
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/product/all-products/{distance}/{lat}/{lon}")
	public Response getProductsAnon(@PathParam("lat") String lat, @PathParam("lon") String lon, @PathParam("distance") String distance) {
		log.entering(name, "getProductsAnon");
		try {
			String sql2 = SQLProducts.products(lat, lon, distance);
			
			String sqlProduct = SQLProducts.productsIn();
			
			Products products = new Products();
			JSONObject payload = products.getProducts(variables, sql2, sqlProduct);
			
			log.exiting(name, "getProductsAnon");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.productsMessage(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/product/all-products/{distance}/{street}/{district}/{state}/{city}")
	public Response getProductsAnonStreet(@PathParam("street") String street, @PathParam("district") String district, @PathParam("state") String state, @PathParam("city") String city, @PathParam("distance") String distance) {
		log.entering(name, "getProductsAnonStreet");
		try {
			SQLProducts sqlProductsAnonStreet = new SQLProducts();
			String sql3 = sqlProductsAnonStreet.productsAnonStreet(distance, street);
			
			String sqlProduct = SQLProducts.productsIn();
			
			Products products = new Products();
			JSONObject payload = products.getProducts(variables, sql3, sqlProduct);
			
			log.exiting(name, "getPharmacies");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.productsMessage(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/product/search/{distance}/{session}/{productName}")
	public Response getProductsSearch(@PathParam("session") String session, @PathParam("distance") String distance, @PathParam("productName") String productName) {
		log.entering(name, "getProductsSearch");
		
		if(session.length() == sessionLength) {
			try {
				GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
				Map<String, String> getAddress = getUserCoordinates.coordinatesFromDB(variables, session);
				
				String sql4 = SQLProducts.products(getAddress.get("lat"), getAddress.get("lon"), distance);
				
				String sqlProduct = SQLProducts.productsInSearch(productName);
				
				Products products = new Products();
				JSONObject payload = products.getProducts(variables, sql4, sqlProduct);
				
				log.exiting(name, "getProductsSearch");
				return Response.ok(payload.toString()).build();
			} catch (Exception e) {
				log.log(Level.SEVERE, LogMessage.productsMessage(e.toString()));
			}
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/product/search/{distance}/{lat}/{lon}/{productName}")
	public Response getProductsSearchAnon(@PathParam("lat") String lat, @PathParam("lon") String lon, @PathParam("distance") String distance, @PathParam("productName") String productName) {
		log.entering(name, "getProductsSearchAnon");
		try {
			String sql5 = SQLProducts.products(lat, lon, distance);
			
			String sqlProduct = SQLProducts.productsInSearch(productName);
			
			Products products = new Products();
			JSONObject payload = products.getProducts(variables, sql5, sqlProduct);
			
			log.exiting(name, "getProductsSearchAnon");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.productsMessage(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/product/search/{distance}/{street}/{district}/{state}/{city}/{productName}")
	public Response getProductsSearchStreet(@PathParam("street") String street, @PathParam("district") String district, @PathParam("state") String state, @PathParam("city") String city, @PathParam("distance") String distance, @PathParam("productName") String productName) {
		log.entering(name, "getProductsSearchStreet");
		try {
			SQLProducts sqlProductsAnonStreet = new SQLProducts();
			String sql6 = sqlProductsAnonStreet.productsAnonStreet(distance, street);
			
			String sqlProduct = SQLProducts.productsInSearch(productName);
			
			Products products = new Products();
			JSONObject payload = products.getProducts(variables, sql6, sqlProduct);
			
			log.exiting(name, "getProductsSearchStreet");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.productsMessage(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@OPTIONS
	@Path("{path : .*}")
	public Response options() {
	    return Response.ok("")
	            .header("Access-Control-Allow-Origin", "*")
	            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
	            .header("Access-Control-Allow-Credentials", "true")
	            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
	            .header("Access-Control-Max-Age", "1209600")
	            .build();
	}
}