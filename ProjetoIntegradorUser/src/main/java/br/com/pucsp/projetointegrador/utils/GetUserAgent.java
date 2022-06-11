package br.com.pucsp.projetointegrador.utils;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class GetUserAgent {
	private static String name = GetUserAgent.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetUserAgent.class.getName());
	
	public String getUserAgent(HttpServletRequest request) {
		log.entering(name, "getUserAgent");
		String userAgent = request.getHeader("User-Agent").toUpperCase();
		
		String browser = getUserBrowser(userAgent);
		String osName = getUserOS(userAgent);
		
		String user = "";
		
		if(browser.contains("Desconhecido") && osName.contains("Desconhecido")) {
			user = "";
		}
		else if(browser.contains("Desconhecido")) {
			user = "Sistema Operacional: " + osName + "<br><br>";
		}
		else if (osName.contains("Desconhecido")) {
			user = "Navegador: " + browser + "<br><br>";
		}
		else {
			user = osName + " - " + browser + "<br><br>";
		}
		
		log.exiting(name, "getUserAgent");
		return user;
	}
	
	public String getUserBrowser(String userAgent) {
		if(userAgent.contains("CHROME")) { return "Google Chrome"; }
		else if(userAgent.contains("FIREFOX")) { return "Firefox"; }
		else if(userAgent.contains("OPERA")) { return "Opera"; }
		else if(userAgent.contains("MSIE")) { return "Internet Explorer"; }
		else if(userAgent.contains("SAFARI")) { return "Safari"; }
		else if(userAgent.contains("Insomnia".toUpperCase())) { return "Insomnia"; }
		else if(userAgent.contains("POSTMANRUNTIME")) { return "Postman"; }
		else if(userAgent.contains("Edge".toUpperCase())) { return "Edge"; }
		else { return "Desconhecido"; }
	}
	
	public String getUserOS(String userAgent) {
		if (userAgent.contains("Windows NT 10.0".toUpperCase())) return "Windows";
		else if (userAgent.contains("Windows NT 6.2".toUpperCase())) return "Windows 8";
		else if (userAgent.contains("Windows NT 6.1".toUpperCase())) return "Windows 7";
		else if (userAgent.contains("Windows NT 6.0".toUpperCase())) return "Windows Vista";
		else if (userAgent.contains("Windows NT 5.1".toUpperCase())) return "Windows XP";
		else if (userAgent.contains("Windows NT 5.0".toUpperCase())) return "Windows 2000";
		else if (userAgent.contains("Android".toUpperCase())) return "Android";
		else if (userAgent.contains("webOS".toUpperCase())) return "webOS";
		else if (userAgent.contains("iPhone".toUpperCase())) return "iPhone";
		else if (userAgent.contains("iPad".toUpperCase())) return "iPad";
		else if (userAgent.contains("iPod".toUpperCase())) return "iPod";
		else if (userAgent.contains("Windows Phone".toUpperCase())) return "Windows Phone";
		else if (userAgent.contains("BlackBerry".toUpperCase())) return "BlackBerry";
		else if (userAgent.contains("Mac".toUpperCase())) return "Mac/iOS";
		else if (userAgent.contains("X11".toUpperCase()) || userAgent.contains("Linux".toUpperCase())) return "Linux";
		else { return "Desconhecido"; }
	}
}