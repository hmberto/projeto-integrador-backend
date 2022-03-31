package br.com.pucsp.projetointegrado.farmacias;

import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import br.com.pucsp.projetointegrado.farmacias.client.ConfirmEmail;
import br.com.pucsp.projetointegrado.farmacias.client.LogIn;
import br.com.pucsp.projetointegrado.farmacias.client.LogOut;
import br.com.pucsp.projetointegrado.farmacias.client.Pharmacies;
import br.com.pucsp.projetointegrado.farmacias.client.SignUp;
import br.com.pucsp.projetointegrado.farmacias.client.login.GenerateLogin;
import br.com.pucsp.projetointegrado.farmacias.client.login.LogInUser;
import br.com.pucsp.projetointegrado.farmacias.client.signup.CreateUsers;
import br.com.pucsp.projetointegrado.farmacias.utils.GetIPAddress;
import br.com.pucsp.projetointegrado.farmacias.utils.GetUserAgent;
import br.com.pucsp.projetointegrado.farmacias.utils.ProjectVariables;

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
	@Path("/client/login")
	public Response loginUsers(@Context HttpServletRequest request, LogInUser login) {
		LOG.entering(NAME, "loginUsers");
		boolean check = true;
		try {
			int MAX_PASS_LENGTH = Integer.parseInt(variables.get("MAX_PASS_LENGTH"));
			int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
			
			boolean emailMatches = login.getEmail().matches(variables.get("REGEX_EMAIL"));
			if(login.getEmail().length() > 10 && login.getEmail().length() < 60 && emailMatches) {}
			else { check = false; }
			
			boolean passMatches = login.getPass().matches(variables.get("REGEX_PASS"));
			// if(login.getPass().length() >= MIN_PASS_LENGTH && login.getPass().length() < MAX_PASS_LENGTH && passMatches) {}
			if(login.getPass().length() < MAX_PASS_LENGTH && passMatches) {}
			else { check = false; }
			
			if(check) {
				GetUserAgent getUserAgent = new GetUserAgent();
				String userAgent = getUserAgent.getUserAgent(request);
				
				GetIPAddress ipAddress = new GetIPAddress();
				String IP = ipAddress.getIp(request);
				
				LogIn newLogin = new LogIn();
				Map<Integer, String> session = newLogin.authenticateUser(userAgent, variables, login.getEmail(), login.getPass(), IP, login.getNewLogin());
				
				if(session.get(1).length() == SESSION_LENGTH) {
					LOG.exiting(NAME, "loginUsers");
					return Response.ok(new GenerateLogin(session)).build();
				}
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "User not authenticated: " + e);
		}
		
		LOG.log(Level.INFO, "Couldn't authenticate user!");
		LOG.exiting(NAME, "loginUsers");
		return Response.status(Response.Status.FORBIDDEN).build();
	}
	
	@POST
	@Path("/client/signup")
	public Response signupUsers(CreateUsers user) {
		LOG.entering(NAME, "signupUsers");
		try {
			SignUp SignUp = new SignUp();
			boolean check = SignUp.createUser(variables, user);
			
			if(check) {
				LOG.exiting(NAME, "signupUsers");
				return Response.status(Response.Status.CREATED).build();
			}
		}
		catch(Exception e) {
			LOG.log(Level.SEVERE, "User not created: " + e);
		}
		
		LOG.log(Level.INFO, "Couldn't create user!");
		LOG.exiting(NAME, "signupUsers");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@PUT
	@Path("/client/logout/{session}")
	public Response logoutUsers(@PathParam("session") String session) {
		LOG.entering(NAME, "logoutUsers");
		try {
			LogOut logoutUser = new LogOut();
			boolean check = logoutUser.logout(variables, session);
			
			if(check) {
				LOG.exiting(NAME, "logoutUsers");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		} 
		catch (Exception e) {
			LOG.log(Level.SEVERE, "User not unauthenticated: " + e);
		}
		
		LOG.log(Level.INFO, "Couldn't logout user!");
		LOG.exiting(NAME, "logoutUsers");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/client/confirm-email/{email}/{token}")
	public Response confirmEmail(@PathParam("token") String token, @PathParam("email") String email) {
		LOG.entering(NAME, "confirmEmail");
		try {
			ConfirmEmail confirmEmail = new ConfirmEmail();
			boolean check = confirmEmail.confirm(variables, token, email);
			
			if(check) {
				LOG.exiting(NAME, "confirmEmail");
				return Response.temporaryRedirect(uri).build();
				// return Response.status(Response.Status.NO_CONTENT).build();
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "User not confirmed: " + e);
		}
		
		LOG.log(Level.INFO, "Couldn't confirm user!");
		LOG.exiting(NAME, "confirmEmail");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/home/pharmacies/{distance}/{session}")
	public Response getPharmacies(@PathParam("session") String session, @PathParam("distance") String distance) {
		LOG.entering(NAME, "getPharmacies");
		try {
			if(distance.length() < 3) {
				if(session.length() > 10 && session.length() < 250) {
					Pharmacies pharmacies = new Pharmacies();
					JSONObject payload = pharmacies.getPharmacies(variables, distance, session);
					
					LOG.exiting(NAME, "getPharmacies");
					return Response.ok(payload.toString()).build();
				}
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Couldn't find pharmacies: " + e);
		}
		
		LOG.log(Level.INFO, "Couldn't find pharmacies!");
		LOG.exiting(NAME, "getPharmacies");
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