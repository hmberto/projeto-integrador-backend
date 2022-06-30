package br.com.pucsp.projetointegrador;

import java.net.URI;
import java.util.HashMap;
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
import br.com.pucsp.projetointegrador.order.DeliveryOrder;
import br.com.pucsp.projetointegrador.order.DeliveryOrders;
import br.com.pucsp.projetointegrador.order.GetOrder;
import br.com.pucsp.projetointegrador.order.GetOrders;
import br.com.pucsp.projetointegrador.order.Products;
import br.com.pucsp.projetointegrador.order.create.NewOrder;
import br.com.pucsp.projetointegrador.order.products.GetProducts;
import br.com.pucsp.projetointegrador.order.utils.LogMessage;
import br.com.pucsp.projetointegrador.order.utils.ProjectVariables;

@Produces("application/json")
@Consumes("application/json")
public class Rest {
	private static String name = Rest.class.getSimpleName();
	private static Logger log = Logger.getLogger(Rest.class.getName());
	
	ProjectVariables projectVariables = new ProjectVariables();
	Map <String, String> variables = projectVariables.projectVariables();
	Map <String, String> cacheAddress = new HashMap<String, String>();
	
	URI uri = URI.create("https://projeto-integrador-frontend.herokuapp.com");
	
	@GET
	@Path("/")
	public Response getTest() {
		String test = "{\n"
				+ "    \"Hello\":\"World!\"\n"
				+ "}";
		
		return Response.ok(test).build();
	}
	
	@POST
	@Path("/validate")
	public Response getProducts(GetProducts cart) {
		log.entering(name, "getProducts");
		try {
			Products products = new Products();
			JSONObject payload = products.getProducts(variables, cart);
			
			log.exiting(name, "getProducts");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/order/complete")
	public Response createOrder(NewOrder order) {
		log.entering(name, "createOrder");
		try {
			CreateOrder createOrder = new CreateOrder();
			JSONObject payload = createOrder.createOrder(variables, order);
			
			log.exiting(name, "createOrder");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/orders/{session}")
	public Response getOrders(@PathParam("session") String session) {
		log.entering(name, "getOrders");
		try {
			GetOrders getOrders = new GetOrders();
			JSONObject orders = getOrders.getOrders(variables, session);
			
			log.exiting(name, "getOrders");
			return Response.ok(orders.toString()).build();
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/orders/{orderId}/{session}")
	public Response getOrder(@PathParam("session") String session, @PathParam("orderId") String orderId) {
		log.entering(name, "getOrder");
		try {
			GetOrder getOrder = new GetOrder();
			JSONObject orders = getOrder.getOrder(variables, orderId, session);
			
			log.exiting(name, "getOrder");
			return Response.ok(orders.toString()).build();
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/delivery/orders/{deliverymanID}/{statusId}")
	public Response deliveryOrders(@PathParam("deliverymanID") String deliverymanID, @PathParam("statusId") String statusId) {
		log.entering(name, "deliveryOrders");
		
		DeliveryOrders deliveryOrders = new DeliveryOrders();
		
		try {
			JSONObject payload = deliveryOrders.deliveryOrders(variables, cacheAddress, deliverymanID, Integer.parseInt(statusId));
			
			log.exiting(name, "deliveryOrders");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/delivery/order/{deliverymanID}/{orderID}/{statusID}")
	public Response deliveryOrder(@PathParam("deliverymanID") String deliverymanID, @PathParam("orderID") String orderID, @PathParam("statusID") String statusID) {
		log.entering(name, "deliveryOrder");
		
		DeliveryOrder deliveryOrder = new DeliveryOrder();
		
		try {
			boolean check = deliveryOrder.deliveryOrder(variables, deliverymanID, orderID, statusID);
			
			if(check) {
				log.exiting(name, "deliveryOrder");
				return Response.ok().build();
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
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