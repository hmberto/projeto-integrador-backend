package br.com.pucsp.projetointegrador.pharmacy.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {
	public static String NAME = DB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(DB.class.getName());
	
//	private static final String DATABASE_DRIVER = EnvVariables.getEnvVariable("DATABASE_DRIVER");
//  private static final String DATABASE_URL = EnvVariables.getEnvVariable("DATABASE_URL");
//  private static final String USERNAME = EnvVariables.getEnvVariable("USERNAME");
//  private static final String PASSWORD = EnvVariables.getEnvVariable("PASSWORD");
//  private static final String MAX_POOL = EnvVariables.getEnvVariable("MAX_POOL");

    private static Connection connection;
    private static Properties properties;

    private static Properties getProperties(Map <String, String> variables) {
    	LOG.entering(NAME, "getProperties");
    	
    	if (properties == null) {
    		properties = new Properties();
    		properties.setProperty("user", variables.get("USERNAME"));
    		properties.setProperty("password", variables.get("PASSWORD"));
    		properties.setProperty("MaxPooledStatements", variables.get("MAX_POOL"));
    	}
    	
    	LOG.log(Level.INFO, "Properties setuped");
    	
    	LOG.exiting(NAME, "getProperties");
    	return properties;
    }

	public static Connection connect(Map <String, String> variables) {
		LOG.entering(NAME, "connect");
		
		if (connection == null) {
			try {
				Class.forName(variables.get("DATABASE_DRIVER"));
				connection = DriverManager.getConnection(variables.get("DATABASE_URL"), getProperties(variables));
				
				LOG.log(Level.INFO, "Connection started");
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "Connection not started" + e);
			}
		}
		
		LOG.exiting(NAME, "connect");
		return connection;
	}

	public static void disconnect() {
		LOG.entering(NAME, "disconnect");
		
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                
                LOG.log(Level.INFO, "Connection closed");
            } catch (SQLException e) {
            	LOG.log(Level.SEVERE, "Connection not closed" + e);
            }
        }
        
        LOG.exiting(NAME, "disconnect");
    }
}