package com.mhe.bundle.service.dbRefresh;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.mhe.bundle.common.Logger;
import com.mhe.bundle.domain.ClusterInfo;
import com.mhe.bundle.exception.BundleException;
import com.mhe.bundle.service.dbRefresh.api.IDBRefresh;
import com.mhe.bundle.util.ServerUtil;
//import com.mhe.bundle.common.Configuration;



public class DBRefreshImpl  implements IDBRefresh{
	
	private static Logger logger = Logger.getInstance(DBRefreshImpl.class);
	
	@Autowired
	private ServerUtil serverUtil;
	
	/**
	 * Method is used to show Connect properties values
	 * @return Map -- key is DB key and value is corresponding value
	 * @throws BundleException
	 */
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/showConnectSysValues")
	public String showConnectSysValues () throws BundleException {
		
		
			return "Hello World";
		
	}
	
	/**
	 * Return server list now from DB but future from AWS/Weblogic cluster
	 * @return map--------
	 * @throws BundleException
	 */
	public Map<String, List<ClusterInfo>> serverList(String applicationName) throws BundleException {
		Map<String, List<ClusterInfo>> map = null;
		logger.debug("****************Inside serverList Entry******************");
		try {
			List<ClusterInfo> clusterList = serverUtil.appServers(applicationName);
			map = new HashMap<String, List<ClusterInfo>>();
			map.put("app.clusters", clusterList);
			logger.debug("map :: " + map);
		} catch (Exception e) {
			throw new BundleException(e);
		}
		logger.debug("****************Inside serverList Exit******************");
		return map;
	}
	
	
	
	



	
}
