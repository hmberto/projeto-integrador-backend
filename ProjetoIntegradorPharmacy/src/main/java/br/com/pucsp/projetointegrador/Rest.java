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

import br.com.pucsp.projetointegrador.pharmacy.Pharmacies;
import br.com.pucsp.projetointegrador.pharmacy.PharmaciesAnon;
import br.com.pucsp.projetointegrador.pharmacy.PharmaciesAnonStreet;
import br.com.pucsp.projetointegrador.pharmacy.PharmacyID;
import br.com.pucsp.projetointegrador.pharmacy.utils.ProjectVariables;

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
	@Path("/pharmacy/home/{distance}/{session}")
	public Response getPharmacies(@PathParam("session") String session, @PathParam("distance") String distance) {
		LOG.entering(NAME, "getPharmacies");
		try {
			Pharmacies pharmacies = new Pharmacies();
			JSONObject payload = pharmacies.getPharmacies(variables, distance, session);
			
			LOG.exiting(NAME, "getPharmacies");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find pharmacies: " + e);
		}
		
		LOG.exiting(NAME, "getPharmacies");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/pharmacy/home/{distance}/{lat}/{lon}")
	public Response getPharmaciesAnon(@PathParam("lat") String lat, @PathParam("lon") String lon, @PathParam("distance") String distance) {
		LOG.entering(NAME, "getPharmaciesAnon");
		try {
			PharmaciesAnon pharmacies = new PharmaciesAnon();
			JSONObject payload = pharmacies.getPharmacies(variables, distance, lat, lon);
			
			LOG.exiting(NAME, "getPharmacies");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find pharmacies: " + e);
		}
		
		LOG.exiting(NAME, "getPharmaciesAnon");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/pharmacy/home/{distance}/{street}/{district}/{state}/{city}")
	public Response getPharmaciesAnonStreet(@PathParam("street") String street, @PathParam("district") String district, @PathParam("state") String state, @PathParam("city") String city, @PathParam("distance") String distance) {
		LOG.entering(NAME, "getPharmaciesAnon");
		try {
			PharmaciesAnonStreet pharmacies = new PharmaciesAnonStreet();
			JSONObject payload = pharmacies.getPharmacies(variables, distance, street, district, state, city);
			
			LOG.exiting(NAME, "getPharmacies");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find pharmacies: " + e);
		}
		
		LOG.exiting(NAME, "getPharmaciesAnon");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/pharmacy/{pharmacyId}")
	public Response getPharmacy(@PathParam("pharmacyId") String pharmacyId) {
		LOG.entering(NAME, "getPharmacy");
		try {
			PharmacyID pharmacyID = new PharmacyID();
			JSONObject payload = pharmacyID.getPharmacy(variables, pharmacyId);
			
			LOG.exiting(NAME, "getPharmacy");
			return Response.ok(payload.toString()).build();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find pharmacy: " + e);
		}
		
		LOG.exiting(NAME, "getPharmacy");
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