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

import br.com.pucsp.projetointegrador.deliveryman.SignUpDeliveryman;
import br.com.pucsp.projetointegrador.deliveryman.login.GenerateDeliverymanLogin;
import br.com.pucsp.projetointegrador.deliveryman.login.LogInDeliverymanUser;
import br.com.pucsp.projetointegrador.deliveryman.LogInDeliveryman;
import br.com.pucsp.projetointegrador.deliveryman.signup.CreateDeliveryman;
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
import br.com.pucsp.projetointegrador.utils.LogMessage;
import br.com.pucsp.projetointegrador.utils.ProjectVariables;

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
	
	@POST
	@Path("/user/login")
	public Response loginUsers(@Context HttpServletRequest request, LogInUser login) {
		log.entering(name, "loginUsers");
		
		GetUserAgent getUserAgent = new GetUserAgent();
		String userAgent = getUserAgent.getUserAgent(request);
		
		GetIPAddress ipAddress = new GetIPAddress();
		String ip = ipAddress.getIp(request);
		
		try {
			LogIn newLogin = new LogIn();
			Map<Integer, String> session = newLogin.authenticateUser(variables, login, userAgent, ip);
			
			if(session.get(1).length() == sessionLength) {
				log.exiting(name, "loginUsers");
				return Response.ok(new GenerateLogin(session)).build();
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.FORBIDDEN).build();
	}
	
	@POST
	@Path("/user/signup")
	public Response signupUsers(CreateUsers user) {
		log.entering(name, "signupUsers");
		
		try {
			SignUp signUp = new SignUp();
			boolean check = signUp.createUser(variables, user);
			
			if(check) {
				log.exiting(name, "signupUsers");
				return Response.status(Response.Status.CREATED).build();
			}
		}
		catch(Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@PUT
	@Path("/user/logout/{session}")
	public Response logoutUsers(@PathParam("session") String session) {
		log.entering(name, "logoutUsers");
		
		try {
			LogOut logoutUser = new LogOut();
			boolean check = logoutUser.logout(variables, session);
			
			if(check) {
				log.exiting(name, "logoutUsers");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		} 
		catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/user/confirm-email/{email}/{token}")
	public Response confirmEmail(@PathParam("token") String token, @PathParam("email") String email) {
		log.entering(name, "confirmEmail");
		
		try {
			ConfirmEmail confirmEmail = new ConfirmEmail();
			boolean check = confirmEmail.confirm(variables, token, email.toLowerCase());
			
			if(check) {
				log.exiting(name, "confirmEmail");
				return Response.temporaryRedirect(uri).build();
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/user/new-password")
	public Response recNewPassword(NewPass pass) {
		log.entering(name, "recNewPassword");
		
		try {
			RecNewPassword recNewPassword = new RecNewPassword();
			boolean check = recNewPassword.changePass(variables, pass);
			
			if(check) {
				log.exiting(name, "recNewPassword");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
		catch(Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/user/change-password")
	public Response changePassword(ChangePass pass) {
		log.entering(name, "changePassword");
		
		try {
			ChangePassword changePassword = new ChangePassword();
			boolean check = changePassword.changePass(variables, pass);
			
			if(check) {
				log.exiting(name, "changePassword");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
		catch(Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@GET
	@Path("/user/get-user/{sessionId}")
	public Response getUser(@PathParam("sessionId") String sessionId) {
		log.entering(name, "getUser");
		
		try {
			if(sessionId.length() == sessionLength) {
				GetUser getUser = new GetUser();
				Map<String, String> user = getUser.user(variables, sessionId);
				
				if(user.get("email").length() >= 10) {
					log.exiting(name, "getUser");
					return Response.ok(new GenerateUser(user)).build();
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/user/update")
	public Response updateUsers(UpdateUsers user) {
		log.entering(name, "updateUsers");
		
		try {
			Update update = new Update();
			boolean check = update.updateUsers(variables, user);
			
			if(check) {
				log.exiting(name, "updateUsers");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
		catch(Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/user/experience/contact")
	public Response userExperience(@Context HttpServletRequest request, UserMessage message) {
		log.entering(name, "userExperience");
		
		try {
			ContactUs contactUs = new ContactUs();
			boolean check = contactUs.contactUs(message);
			
			if(check) {
				log.exiting(name, "userExperience");
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
		catch(Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return null;
	}
	
	@POST
	@Path("/deliveryman/login")
	public Response loginDeliveryman(@Context HttpServletRequest request, LogInDeliverymanUser login) {
		log.entering(name, "loginUsers");
		
		try {
			LogInDeliveryman logInDeliveryman = new LogInDeliveryman();
			Map<String, String> session = logInDeliveryman.authenticateUser(variables, login);
			
			log.exiting(name, "loginUsers");
			if(session.get("cpf").length() == 11) {
				return Response.ok(new GenerateDeliverymanLogin(session)).build();
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		
		return Response.status(Response.Status.FORBIDDEN).build();
	}
	
	@POST
	@Path("/deliveryman/signup")
	public Response signupDeliveryman(CreateDeliveryman user) {
		log.entering(name, "signupDeliveryman");
		
		try {
			SignUpDeliveryman signUpDeliveryman = new SignUpDeliveryman();
			boolean check = signUpDeliveryman.createUser(variables, user);
			
			if(check) {
				log.exiting(name, "signupDeliveryman");
				return Response.status(Response.Status.CREATED).build();
			}
		}
		catch(Exception e) {
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