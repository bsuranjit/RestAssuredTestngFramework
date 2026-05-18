package com.spotify.oauth2.utils;

import java.util.Properties;

public class ConfigLoader {

    private final Properties properties;
    private static ConfigLoader configLoader;

    private ConfigLoader() {
        properties = PropertyUtils.propertyLoader("config.properties");
    }

    public static ConfigLoader getInstance() {
        if (configLoader == null) {
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }
    public String getClientId() {
        // Check system property first (-D parameter from Jenkins/Maven)
        String prop = System.getProperty("CLIENT_ID");
        if (prop != null && !prop.isEmpty()) return prop;
        // Fall back to environment variable (Docker)
        String env = System.getenv("CLIENT_ID");
        if (env != null && !env.isEmpty()) return env;
        // Fall back to properties file (local)
        String fileProp = properties.getProperty("client_id");
        if (fileProp != null) return fileProp;
        throw new RuntimeException("CLIENT_ID not found");
    }

    public String getClientSecret() {
        String prop = System.getProperty("CLIENT_SECRET");
        if (prop != null && !prop.isEmpty()) return prop;
        String env = System.getenv("CLIENT_SECRET");
        if (env != null && !env.isEmpty()) return env;
        String fileProp = properties.getProperty("client_secret");
        if (fileProp != null) return fileProp;
        throw new RuntimeException("CLIENT_SECRET not found");
    }

    public String getRefreshToken() {
        String prop = System.getProperty("REFRESH_TOKEN");
        if (prop != null && !prop.isEmpty()) return prop;
        String env = System.getenv("REFRESH_TOKEN");
        if (env != null && !env.isEmpty()) return env;
        String fileProp = properties.getProperty("refresh_token");
        if (fileProp != null) return fileProp;
        throw new RuntimeException("REFRESH_TOKEN not found");
    }
    public String getGrantType() {
        String prop = System.getProperty("GRANT_TYPE");
        if (prop != null && !prop.isEmpty()) return prop;
        String env = System.getenv("GRANT_TYPE");
        if (env != null && !env.isEmpty()) return env;
        String fileProp = properties.getProperty("grant_type");
        if (fileProp != null) return fileProp;
        throw new RuntimeException("grant_type not found");
    }

    public String getUserId() {
        String prop = System.getProperty("USER_ID");
        if (prop != null && !prop.isEmpty()) return prop;
        String env = System.getenv("USER_ID");
        if (env != null && !env.isEmpty()) return env;
        String fileProp = properties.getProperty("user_id");
        if (fileProp != null) return fileProp;
        throw new RuntimeException("USER_ID not found");
    }
}

   /* public String getClientId() {
        // First check environment variable (Docker/Jenkins)
        String env = System.getenv("CLIENT_ID");
        if (env != null) return env;
        // Fall back to properties file (local)
        String prop = properties.getProperty("client_id");
        if (prop != null) return prop;
        throw new RuntimeException(
            "client_id not found in env or config.properties");
    }

    public String getClientSecret() {
        String env = System.getenv("CLIENT_SECRET");
        if (env != null) return env;
        String prop = properties.getProperty("client_secret");
        if (prop != null) return prop;
        throw new RuntimeException(
            "client_secret not found in env or config.properties");
    }

    public String getRefreshToken() {
        String env = System.getenv("REFRESH_TOKEN");
        if (env != null) return env;
        String prop = properties.getProperty("refresh_token");
        if (prop != null) return prop;
        throw new RuntimeException(
            "refresh_token not found in env or config.properties");
    }

    public String getGrantType() {
        String prop = properties.getProperty("grant_type");
        if (prop != null) return prop;
        throw new RuntimeException(
            "property grant_type is not specified in config.properties");
    }

    public String getUserId() {
        String env = System.getenv("USER_ID");
        if (env != null) return env;
        String prop = properties.getProperty("user_id");
        if (prop != null) return prop;
        throw new RuntimeException(
            "user_id not found in env or config.properties");
    }
}*/

/*package com.spotify.oauth2.utils;
import java.util.Properties;

public class ConfigLoader {
	private final Properties properties;
	private static ConfigLoader configLoader;
	
	private ConfigLoader() {
		properties = PropertyUtils.propertyLoader("C:\\Users\\parmi\\git\\RESTASSUREDFRAMEWORK\\RestAssuredFramework\\src\\test\\java\\resources\\config.properties");
	}
	public static ConfigLoader getInstance() {
		if(configLoader == null) {
			configLoader = new ConfigLoader();
		}
		return configLoader;
	}
	public String getClientId() {
		String prop = properties.getProperty("client_id");
		if(prop!=null)return prop;
		else throw new RuntimeException("property client_id is not specified in the config.property file");
	}
	public String getClientSecret() {
		String prop = properties.getProperty("client_secret");
		if(prop!=null)return prop;
		else throw new RuntimeException("property client_secret is not specified in the config.property file");
	}
	public String getRefreshToken() {
		String prop = properties.getProperty("refresh_token");
		if(prop!=null)return prop;
		else throw new RuntimeException("property refresh_token is not specified in the config.property file");
	}
	public String getGrantType() {
		String prop = properties.getProperty("grant_type");
		if(prop!=null)return prop;
		else throw new RuntimeException("property grant_type is not specified in the config.property file");
	}
	public String getUserId() {
		String prop = properties.getProperty("user_id");
		if(prop!=null)return prop;
		else throw new RuntimeException("property user_id is not specified in the config.property file");
	}
}*/
