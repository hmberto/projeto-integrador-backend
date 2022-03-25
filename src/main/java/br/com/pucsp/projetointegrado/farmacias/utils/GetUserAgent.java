package br.com.pucsp.projetointegrado.farmacias.utils;

import javax.servlet.http.HttpServletRequest;

public class GetUserAgent {
	public String getUserAgent(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent").toUpperCase();
		
		String browser = "";
		String OSName = "";
		
		String user = "";
		
		if(userAgent.contains("CHROME")) { browser = "Google Chrome"; }
		else if(userAgent.contains("FIREFOX")) { browser = "Firefox"; }
		else if(userAgent.contains("OPERA")) { browser = "Opera"; }
		else if(userAgent.contains("MSIE")) { browser = "Internet Explorer"; }
		else if(userAgent.contains("SAFARI")) { browser = "Safari"; }
		else { browser = "Desconhecido"; }

		if (userAgent.contains("Windows NT 10.0")) OSName="Windows 10";
		else if (userAgent.contains("Windows NT 6.2")) OSName="Windows 8";
		else if (userAgent.contains("Windows NT 6.1")) OSName="Windows 7";
		else if (userAgent.contains("Windows NT 6.0")) OSName="Windows Vista";
		else if (userAgent.contains("Windows NT 5.1")) OSName="Windows XP";
		else if (userAgent.contains("Windows NT 5.0")) OSName="Windows 2000";
		else if (userAgent.contains("Android")) OSName="Android";
		else if (userAgent.contains("webOS")) OSName="webOS";
		else if (userAgent.contains("iPhone")) OSName="iPhone";
		else if (userAgent.contains("iPad")) OSName="iPad";
		else if (userAgent.contains("iPod")) OSName="iPod";
		else if (userAgent.contains("BlackBerry")) OSName="BlackBerry";
		else if (userAgent.contains("Windows Phone")) OSName="Windows Phone";
		else if (userAgent.contains("Mac")) OSName="Mac/iOS";
		else if (userAgent.contains("X11")) OSName="UNIX";
		else if (userAgent.contains("Linux")) OSName="Linux";
		else { OSName="Desconhecido"; }
		
		if(browser.contains("Desconhecido") && OSName.contains("Desconhecido")) {
			user = "<br>";
		}
		else if(browser.contains("Desconhecido")) {
			user = OSName + "<br><br>";
		}
		else if (OSName.contains("Desconhecido")) {
			user = browser + "<br><br>";
		}
		else {
			user = OSName + " - " + browser + "<br><br>";
		}
		
		return user;
	}
}