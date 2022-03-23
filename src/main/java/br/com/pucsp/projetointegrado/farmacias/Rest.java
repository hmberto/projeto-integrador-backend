package br.com.pucsp.projetointegrado.farmacias;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.pucsp.projetointegrado.farmacias.client.LogIn;
import br.com.pucsp.projetointegrado.farmacias.client.SignUp;
import br.com.pucsp.projetointegrado.farmacias.client.login.GenerateLogin;
import br.com.pucsp.projetointegrado.farmacias.client.login.LogInUser;
import br.com.pucsp.projetointegrado.farmacias.client.signup.CreateUsers;

@Produces("application/json")
@Consumes("application/json")
public class Rest {
	@GET
	@Path("/")
	public Response getTest() throws Exception {
		String test = "Hello World!";
		
		return Response.ok(test).build();
	}
	
	@POST
	@Path("/client/login")
	public Response loginUsers(LogInUser login) {
		try {
			LogIn newLogin = new LogIn();
			Map<Integer, String> session = newLogin.authenticateUser(login.getEmail(), login.getPass(), login.getIP(), login.getNewLogin());
			
			if(session.get(1).length() == 50) {
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
			boolean check = SignUp.createUser(user);
			
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