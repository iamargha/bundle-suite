/**
 * 
 */
package com.mhe.bundle.service.dbRefresh.api;

import java.util.List;
import java.util.Map;






import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mhe.bundle.domain.ClusterInfo;
import com.mhe.bundle.exception.BundleException;

/**
 * This Interface is used to refresh and show DB,System and ouath values
 *
 */
@Path("/dbRefresh")
public interface IDBRefresh {
	
	
	/**
	 * Return server list now from DB but future from AWS/Weblogic cluster
	 * 
	 * @param applicationName
	 * @return map--------
	 * @throws BundleException
	 */
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/servers/{applicationName}")
	public Map<String, List<ClusterInfo>> serverList(@PathParam("applicationName") String applicationName) throws BundleException;

}
