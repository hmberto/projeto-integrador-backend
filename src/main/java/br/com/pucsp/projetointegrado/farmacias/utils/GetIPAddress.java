package br.com.pucsp.projetointegrado.farmacias.utils;

import javax.servlet.http.HttpServletRequest;

public class GetIPAddress {
	public String getIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
				
		if (ipAddress == null) {  
		    ipAddress = request.getRemoteAddr();  
		}
		
		return ipAddress;
	}
}