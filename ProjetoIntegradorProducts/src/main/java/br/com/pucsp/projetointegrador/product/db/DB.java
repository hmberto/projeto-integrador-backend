package br.com.pucsp.projetointegrador.product.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.product.utils.LogMessage;

public class DB {
	private DB () {}
	
	private static String name = DB.class.getSimpleName();
	private static Logger log = Logger.getLogger(DB.class.getName());
	
    private static Connection connection;
    private static Properties properties;

    private static Properties getProperties(Map <String, String> variables) {
    	log.entering(name, "getProperties");
    	
    	if (properties == null) {
    		properties = new Properties();
    		properties.setProperty("user", variables.get("USERNAME"));
    		properties.setProperty("password", variables.get("PASSWORD"));
    		properties.setProperty("MaxPooledStatements", variables.get("MAX_POOL"));
    	}
    	
    	log.log(Level.INFO, "Properties setuped");
    	
    	log.exiting(name, "getProperties");
    	return properties;
    }

	public static Connection connect(Map <String, String> variables) {
		log.entering(name, "connect");
		
		if (connection == null) {
			try {
				Class.forName(variables.get("DATABASE_DRIVER"));
				connection = DriverManager.getConnection(variables.get("DATABASE_URL"), getProperties(variables));
				
				log.log(Level.INFO, "Connection started");
			} catch (Exception e) {
				log.log(Level.SEVERE, LogMessage.message(e.toString()));
			}
		}
		
		log.exiting(name, "connect");
		return connection;
	}

	public static void disconnect() {
		log.entering(name, "disconnect");
		
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                
                log.log(Level.INFO, "Connection closed");
            } catch (SQLException e) {
            	log.log(Level.SEVERE, LogMessage.message(e.toString()));
            }
        }
        
        log.exiting(name, "disconnect");
    }
}