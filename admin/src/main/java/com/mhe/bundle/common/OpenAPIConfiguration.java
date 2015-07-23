package com.mhe.bundle.common;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.common.signature.SharedConsumerSecret;


public class OpenAPIConfiguration {
	private static Logger logger = Logger.getInstance(OpenAPIConfiguration.class);
	private static Map<String, String> map;
	private static Map<String, String> systemMap;
	private static DataSource dataSource;
	private static String defaultPropsTableName;

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("in the init method properties");
		}
		systemMap = new HashMap<String, String>();
		String propsTableName = getNonNullString(System.getProperty("OPENAPI_PROPERTIES_TBLNAME"));
		final String environment;
		if (propsTableName.equals("") || propsTableName.trim().length() == 0) {
			logger.debug("there is no system property defined in weblogic.taking the default value");
			propsTableName = defaultPropsTableName;

		}
		if(StringUtils.isBlank( getEnvironment())){
			logger.debug("there is no system property defined for the Environment");
			environment = "LOCAL_ENV";
		}
		else{
			environment = getEnvironment();
		}
		String sql = "select key , " + environment +" from " + propsTableName ;
		if (logger.isDebugEnabled()) {
			logger.debug("sql is:" + sql);
		}
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		map = jt.query(sql, new ResultSetExtractor<Map<String, String>>()
			{
				@Override
				public Map<String, String> extractData(ResultSet rs)
						throws SQLException, DataAccessException {
					Map<String, String> mapTemp = new HashMap<String, String>();
					while (rs.next()) {
						mapTemp.put(getNonNullString(rs.getString("key")).trim(), getNonNullString(
								rs.getString(environment)).trim());
					}
					if (logger.isDebugEnabled()) {
						logger.debug("setting values in map.test_value:" + mapTemp.get("test_key"));
					}
					return mapTemp;
				}

			});
		systemMap.putAll(System.getenv());
	}

	public static String getSystemValue(String name) {
		try {
			return map.get(name) != null ? map.get(name) : systemMap.get(name);
		} catch (Exception e) {
			logger.error("Error while getting the value:", e);
			return null;
		}
	}

	

	public static Map<String, String> getAllSystemValues() {
		try {
			return systemMap;
		} catch (Exception e) {
			logger.error("Error while getting the value:", e);
			return null;
		}
	}

	public static Map<String, String> getAllDBValues() {
		try {
			return map;
		} catch (Exception e) {
			logger.error("Error while getting the value:", e);
			return null;
		}
	}


	private static String getEnvironment() {

		String environment = getNonNullString(System.getProperty("ENV"));
		logger.debug("Current environment from system properties as : {}", environment);

		return environment;
	}

	public String getDefaultPropsTableName() {
		return defaultPropsTableName;
	}

	public void setDefaultPropsTableName(String defaultPropsTableName) {
		this.defaultPropsTableName = defaultPropsTableName;
	}
	
	public static String getNonNullString(String str) {
		String returnValue = "";
		if (str != null) {
			returnValue = str;
		}
		return returnValue;
	}

}
