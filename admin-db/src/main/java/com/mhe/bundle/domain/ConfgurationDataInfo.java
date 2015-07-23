package com.mhe.bundle.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "OAPI_APP_PROPERTIES")
public class ConfgurationDataInfo {
	
	@Id
    private String id;

    private String key;
    private String environment;
    private String value;

    public ConfgurationDataInfo() {}



    /**
	 * @param key
	 * @param environment
	 * @param value
	 */
    public ConfgurationDataInfo(String key, String environment, String value) {
		this.key = key;
		this.environment = environment;
		this.value = value;
	}



	@Override
    public String toString() {
        return String.format(
                "ConfgurationDataInfo[id=%s, key='%s', environment='%s', value='%s']",
                id, key, environment, value);
    }



	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}



	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}



	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	
    

}
