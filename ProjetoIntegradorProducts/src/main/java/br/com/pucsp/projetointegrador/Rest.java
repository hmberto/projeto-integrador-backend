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
import br.com.pucsp.projetointegrador.product.ProductsAnon;
import br.com.pucsp.projetointegrador.product.ProductsAnonStreet;
import br.com.pucsp.projetointegrador.product.SearchProducts;
import br.com.pucsp.projetointegrador.product.utils.ProjectVariables;

@Produces("application/json")
@Consumes("application/json")
public class Rest {
	public static String NAME = Rest.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(Rest.class.getName());
	
	ProjectVariables projectVariables = new ProjectVariables();
	Map <String, String> variables = projectVariables.projectVariables();
	
	URI uri = URI.create("https://projeto-integrador-frontend.herokuapp.com");
	
	@GET
	@Path("/")
	public Response getTest() throws Exception {
		String test = "{\n"
				+ "    \"Hello\":\"World!\"\n"
				+ "}";
		
		return Response.ok(test).build();
	}
	
	@GET
	@Path("/product/all-products/{distance}/{session}")
	public Response getProducts(@PathParam("session") String session, @PathParam("distance") String distance) {
		LOG.entering(NAME, "getProducts");
		try {
			Products products = new Products();
			JSONObject payload = products.getProducts(variables, distance, session);
			
			LOG.exiting(NAME, "getProducts");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find products: " + e);
		}
		
		LOG.exiting(NAME, "getProducts");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/product/all-products/{distance}/{lat}/{lon}")
	public Response getProductsAnon(@PathParam("lat") String lat, @PathParam("lon") String lon, @PathParam("distance") String distance) {
		LOG.entering(NAME, "getProductsAnon");
		try {
			ProductsAnon products = new ProductsAnon();
			JSONObject payload = products.getProducts(variables, distance, lat, lon);
			
			LOG.exiting(NAME, "getProductsAnon");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find products: " + e);
		}
		
		LOG.exiting(NAME, "getProductsAnon");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/product/all-products/{distance}/{street}/{district}/{state}/{city}")
	public Response getProductsAnonStreet(@PathParam("street") String street, @PathParam("district") String district, @PathParam("state") String state, @PathParam("city") String city, @PathParam("distance") String distance) {
		LOG.entering(NAME, "getProductsAnonStreet");
		try {
			ProductsAnonStreet products = new ProductsAnonStreet();
			JSONObject payload = products.getProducts(variables, distance, street, district, state, city);
			
			LOG.exiting(NAME, "getPharmacies");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find products: " + e);
		}
		
		LOG.exiting(NAME, "getProductsAnonStreet");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/product/search/{distance}/{session}/{productName}")
	public Response getProductsSearch(@PathParam("session") String session, @PathParam("distance") String distance, @PathParam("productName") String productName) {
		LOG.entering(NAME, "getProductsSearch");
		try {
			SearchProducts searchProducts = new SearchProducts();
			JSONObject payload = searchProducts.getProducts(variables, distance, session, productName);
			
			LOG.exiting(NAME, "getProductsSearch");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find products: " + e);
		}
		
		LOG.exiting(NAME, "getProductsSearch");
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