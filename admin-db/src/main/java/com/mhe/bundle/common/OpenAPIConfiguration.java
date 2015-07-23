package com.mhe.bundle.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.GrantedAuthority;

import com.mhe.bundle.data.ConfigurationRepository;
import com.mhe.bundle.domain.ConfgurationDataInfo;

public class OpenAPIConfiguration {
	private static Logger logger = Logger
			.getInstance(OpenAPIConfiguration.class);
	private static List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
	// this intialization method is invoked by spring and load properties from
	// db
	private static Map<String, String> map;
	private static Map<String, String> systemMap;
	private static DataSource dataSource;
	private static String defaultPropsTableName = "OAPI_APP_PROPERTIES";
	
	@Autowired
	private ConfigurationRepository repository;
	
	@Autowired
	private MongoOperations mongoOperation;

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("in the init method properties");
		}
		systemMap = new HashMap<String, String>();
		String propsTableName = getNonNullString(System
				.getProperty("OPENAPI_PROPERTIES_TBLNAME"));
		final String environment;
		if (propsTableName.equals("") || propsTableName.trim().length() == 0) {
			logger.debug("there is no system property defined in weblogic.taking the default value");
			propsTableName = defaultPropsTableName;

		}
		if (StringUtils.isBlank(getEnvironment())) {
			logger.debug("there is no system property defined for the Environment");
			environment = "LOCAL_ENV";
		} else {
			environment = getEnvironment();
		}
		String sql = "select key , " + environment + " from " + propsTableName;
		if (logger.isDebugEnabled()) {
			logger.debug("sql is:" + sql);
		}
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		map = jt.query(sql, new ResultSetExtractor<Map<String, String>>() {
			@Override
			public Map<String, String> extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				Map<String, String> mapTemp = new HashMap<String, String>();
				while (rs.next()) {
					mapTemp.put(getNonNullString(rs.getString("key")).trim(),
							getNonNullString(rs.getString(environment)).trim());
				}
				if (logger.isDebugEnabled()) {
					logger.debug("setting values in map.test_value:"
							+ mapTemp.get("test_key"));
				}
				return mapTemp;
			}

		});
		systemMap.putAll(System.getenv());
	}
	
	
	public void initMongo() {
		//ConfgurationDataInfo c = new ConfgurationDataInfo("app.servers","prod_aws_1a","http:\\10.228.10.12:7022,http:\\10.228.11.142:7022,http:\\10.228.8.152:7022,http:\\10.228.8.27:7022,http:\\10.228.9.7:7022,http:\\10.228.16.162:7022,http:\\10.228.16.240:7022,http:\\10.228.17.172:7022,http:\\10.228.17.19:7022,http:\\10.228.19.97:7022,http:\\10.228.25.150:7022,http:\\10.228.25.162:7022,http:\\10.228.26.139:7022,http:\\10.228.26.44:7022,http:\\10.228.27.94:7022");

		//repository.save(c);
		//c = new ConfgurationDataInfo("env.type","prod_aws_1a","prod");
		//repository.save(c);
		
		map = new HashMap<String, String>();
		
		List<ConfgurationDataInfo> confgurationDataInfos = repository.findAll();
		System.out.println("Hiiii ::: "+confgurationDataInfos);
		Query query = new Query();
		query.addCriteria(Criteria.where("key").is("app.servers"));
		
		System.out.println("Hiiii app.servers ::: "+mongoOperation.findOne(query, ConfgurationDataInfo.class));
		
		for (ConfgurationDataInfo confgurationDataInfo : confgurationDataInfos){
			if(confgurationDataInfo.getKey() != null && confgurationDataInfo.getValue() != null){
				map.put(confgurationDataInfo.getKey(), confgurationDataInfo.getValue());
			}
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
	
	public static String getSystemValue(String name) {
		try {
			return map.get(name) != null ? map.get(name) : systemMap.get(name);
		} catch (Exception e) {
			logger.error("Error while getting the value:", e);
			return null;
		}
	}
	
	private static String getEnvironment() {

		String environment = getNonNullString(System.getProperty("ENV"));
		environment = "PROD_AWS_1A";
		logger.debug("Current environment from system properties as : {}",
				environment);
		
		return environment;
	}

	public static String getNonNullString(String str) {
		String returnValue = "";
		if (str != null) {
			returnValue = str;
		}
		return returnValue;
	}
	
	public String getDefaultPropsTableName() {
		return defaultPropsTableName;
	}

	public void setDefaultPropsTableName(String defaultPropsTableName) {
		this.defaultPropsTableName = defaultPropsTableName;
	}

}
