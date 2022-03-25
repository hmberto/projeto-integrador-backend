package br.com.pucsp.projetointegrado.farmacias;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import br.com.pucsp.projetointegrado.farmacias.client.ConfirmEmail;
import br.com.pucsp.projetointegrado.farmacias.client.LogIn;
import br.com.pucsp.projetointegrado.farmacias.client.LogOut;
import br.com.pucsp.projetointegrado.farmacias.client.SignUp;
import br.com.pucsp.projetointegrado.farmacias.client.login.GenerateLogin;
import br.com.pucsp.projetointegrado.farmacias.client.login.LogInUser;
import br.com.pucsp.projetointegrado.farmacias.client.signup.CreateUsers;
import br.com.pucsp.projetointegrado.farmacias.utils.GetIPAddress;
import br.com.pucsp.projetointegrado.farmacias.utils.GetUserAgent;

@Produces("application/json")
@Consumes("application/json")
public class Rest {
	int SESSION_LENGTH = 100;
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
		try {
			GetUserAgent getUserAgent = new GetUserAgent();
			String userAgent = getUserAgent.getUserAgent(request);
			
			GetIPAddress ipAddress = new GetIPAddress();
			String IP = ipAddress.getIp(request);
			
			LogIn newLogin = new LogIn();
			Map<Integer, String> session = newLogin.authenticateUser(userAgent, SESSION_LENGTH, login.getEmail(), login.getPass(), IP, login.getNewLogin());
			
			if(session.get(1).length() == SESSION_LENGTH) {
				return Response.ok(new GenerateLogin(session)).build();
			}
			else {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Path("/client/signup")
	public Response signupUsers(CreateUsers user) {
		try {
			SignUp SignUp = new SignUp();
			boolean check = SignUp.createUser(SESSION_LENGTH, user);
			
			if(check) {
				return Response.status(Response.Status.CREATED).build();			}
			else {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
		catch(Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("/client/logout/{session}")
	public Response logoutUsers(@PathParam("session") String session) {
		try {
			LogOut logoutUser = new LogOut();
			boolean check = logoutUser.logout(SESSION_LENGTH, session);
			
			if(check) {
				return Response.temporaryRedirect(uri).build();
				
				// return Response.status(Response.Status.NO_CONTENT).build();
			}
			else {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("/client/confirm-email/{email}/{token}")
	public Response confirmEmail(@PathParam("token") String token, @PathParam("email") String email) {
		try {
			ConfirmEmail confirmEmail = new ConfirmEmail();
			boolean check = confirmEmail.confirm(token, email);
			
			if(check) {
				return Response.temporaryRedirect(uri).build();
				
				// return Response.status(Response.Status.NO_CONTENT).build();
			}
			else {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
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