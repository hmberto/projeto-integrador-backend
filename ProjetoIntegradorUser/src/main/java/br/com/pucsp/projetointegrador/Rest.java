package br.com.pucsp.projetointegrador;

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

import br.com.pucsp.projetointegrador.user.ChangePassword;
import br.com.pucsp.projetointegrador.user.ConfirmEmail;
import br.com.pucsp.projetointegrador.user.ContactUs;
import br.com.pucsp.projetointegrador.user.GetUser;
import br.com.pucsp.projetointegrador.user.LogIn;
import br.com.pucsp.projetointegrador.user.LogOut;
import br.com.pucsp.projetointegrador.user.RecNewPassword;
import br.com.pucsp.projetointegrador.user.SignUp;
import br.com.pucsp.projetointegrador.user.Update;
import br.com.pucsp.projetointegrador.user.contactus.UserMessage;
import br.com.pucsp.projetointegrador.user.getuser.GenerateUser;
import br.com.pucsp.projetointegrador.user.login.GenerateLogin;
import br.com.pucsp.projetointegrador.user.login.LogInUser;
import br.com.pucsp.projetointegrador.user.password.ChangePass;
import br.com.pucsp.projetointegrador.user.password.NewPass;
import br.com.pucsp.projetointegrador.user.signup.CreateUsers;
import br.com.pucsp.projetointegrador.user.updateusers.UpdateUsers;
import br.com.pucsp.projetointegrador.utils.GetIPAddress;
import br.com.pucsp.projetointegrador.utils.GetUserAgent;
import br.com.pucsp.projetointegrador.utils.ProjectVariables;

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
	@Path("/user/login")
	public Response loginUsers(@Context HttpServletRequest request, LogInUser login) {
		LOG.entering(NAME, "loginUsers");
		boolean check = true;
		try {
			int MAX_PASS_LENGTH = Integer.parseInt(variables.get("MAX_PASS_LENGTH"));
			int MIN_PASS_LENGTH = Integer.parseInt(variables.get("MIN_PASS_LENGTH"));
			int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
			
			boolean emailMatches = login.getEmail().matches(variables.get("REGEX_EMAIL"));
			if(login.getEmail().length() > 10 && login.getEmail().length() < 60 && emailMatches) {}
			else { check = false; }
			
			boolean passMatches = login.getPass().matches(variables.get("REGEX_PASS"));
			if(login.getPass().length() >= MIN_PASS_LENGTH && login.getPass().length() < MAX_PASS_LENGTH && passMatches) {}
			else { check = false; }
			
			if(check) {
				GetUserAgent getUserAgent = new GetUserAgent();
				String userAgent = getUserAgent.getUserAgent(request);
				
				GetIPAddress ipAddress = new GetIPAddress();
				String IP = ipAddress.getIp(request);
				
				LogIn newLogin = new LogIn();
				Map<Integer, String> session = newLogin.authenticateUser(userAgent, variables, login.getEmail().toLowerCase(), login.getPass(), IP, login.getNewLogin());
				
				if(session.get(1).length() == SESSION_LENGTH) {
					LOG.exiting(NAME, "loginUsers");
					return Response.ok(new GenerateLogin(session)).build();
				}
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "User not authenticated: " + e);
		}
		
		LOG.exiting(NAME, "loginUsers");
		return Response.status(Response.Status.FORBIDDEN).build();
	}
	
	@POST
	@Path("/user/signup")
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
	@Path("/user/logout/{session}")
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
	@Path("/user/confirm-email/{email}/{token}")
	public Response confirmEmail(@PathParam("token") String token, @PathParam("email") String email) {
		LOG.entering(NAME, "confirmEmail");
		
		try {
			ConfirmEmail confirmEmail = new ConfirmEmail();
			boolean check = confirmEmail.confirm(variables, token, email.toLowerCase());
			
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
	
	@POST
	@Path("/user/new-password")
	public Response recNewPassword(NewPass pass) {
		LOG.entering(NAME, "recNewPassword");
		
		try {
			RecNewPassword recNewPassword = new RecNewPassword();
			boolean check = recNewPassword.changePass(variables, pass);
			
			if(check) {
				LOG.exiting(NAME, "recNewPassword");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
		catch(Exception e) {
			LOG.log(Level.SEVERE, "Couldn't get new user password: " + e);
		}
		
		LOG.log(Level.INFO, "Couldn't get new user password!");
		LOG.exiting(NAME, "recNewPassword");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/user/change-password")
	public Response changePassword(ChangePass pass) {
		LOG.entering(NAME, "changePassword");
		
		try {
			ChangePassword changePassword = new ChangePassword();
			boolean check = changePassword.changePass(variables, pass);
			
			if(check) {
				LOG.exiting(NAME, "changePassword");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
		catch(Exception e) {
			LOG.log(Level.SEVERE, "Couldn't change user password: " + e);
		}
		
		LOG.log(Level.INFO, "Couldn't change user password!");
		LOG.exiting(NAME, "changePassword");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/user/get-user/{sessionId}")
	public Response getUser(@PathParam("sessionId") String sessionId) {
		LOG.entering(NAME, "getUser");
		
		try {
			int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
			if(sessionId.length() == SESSION_LENGTH) {
				GetUser getUser = new GetUser();
				Map<String, String> user = getUser.user(variables, sessionId);
				
				if(user.get("email").length() >= 10) {
					LOG.exiting(NAME, "getUser");
					return Response.ok(new GenerateUser(user)).build();
				}
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "User not confirmed: " + e);
		}
		
		LOG.log(Level.INFO, "Couldn't confirm user!");
		LOG.exiting(NAME, "confirmEmail");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/user/update")
	public Response updateUsers(UpdateUsers user) {
		LOG.entering(NAME, "updateUsers");
		
		try {
			Update update = new Update();
			boolean check = update.updateUsers(variables, user);
			
			if(check) {
				LOG.exiting(NAME, "updateUsers");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
		catch(Exception e) {
			LOG.log(Level.SEVERE, "User not updated: " + e);
		}
		
		LOG.log(Level.INFO, "Couldn't update user!");
		LOG.exiting(NAME, "updateUsers");
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/user/experience/contact")
	public Response userExperience(@Context HttpServletRequest request, UserMessage message) {
		LOG.entering(NAME, "userExperience");
		
		try {
			ContactUs contactUs = new ContactUs();
			boolean check = contactUs.contactUs(request, message);
			
			if(check) {
				LOG.exiting(NAME, "userExperience");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
		catch(Exception e) {
			LOG.log(Level.SEVERE, "Couldn't message us: " + e);
		}
		
		LOG.exiting(NAME, "userExperience");
		return null;
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