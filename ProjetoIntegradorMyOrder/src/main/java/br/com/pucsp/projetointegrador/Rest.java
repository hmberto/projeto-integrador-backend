package br.com.pucsp.projetointegrador;

import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.order.CreateOrder;
import br.com.pucsp.projetointegrador.order.GetOrders;
import br.com.pucsp.projetointegrador.order.Products;
import br.com.pucsp.projetointegrador.order.create.NewOrder;
import br.com.pucsp.projetointegrador.order.products.GetProducts;
import br.com.pucsp.projetointegrador.order.utils.ProjectVariables;

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
	
	@POST
	@Path("/validate")
	public Response getProducts(GetProducts cart) {
		LOG.entering(NAME, "getProducts");
		try {
			Products products = new Products();
			JSONObject payload = products.getProducts(variables, cart);
			
			LOG.exiting(NAME, "getProducts");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find products: " + e);
		}
		
		LOG.exiting(NAME, "getProducts");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/order/complete")
	public Response createOrder(NewOrder order) {
		LOG.entering(NAME, "createOrder");
		try {
			CreateOrder createOrder = new CreateOrder();
			JSONObject payload = createOrder.createOrder(variables, order);
			
			LOG.exiting(NAME, "createOrder");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't create order: " + e);
		}
		
		LOG.exiting(NAME, "createOrder");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/orders/{session}")
	public Response getOrders(@PathParam("session") String session) {
		LOG.entering(NAME, "getOrders");
		try {
			GetOrders getOrders = new GetOrders();
			JSONObject orders = getOrders.getOrder(variables, session);
			
			LOG.exiting(NAME, "getOrders");
			return Response.ok(orders.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find orders: " + e);
		}
		
		LOG.exiting(NAME, "getOrders");
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