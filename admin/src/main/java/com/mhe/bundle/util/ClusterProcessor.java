/**
 * 
 */
package com.mhe.bundle.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

import org.apache.commons.lang.StringUtils;

import com.mhe.bundle.common.Logger;
import com.mhe.bundle.domain.ClusterInfo;
import com.mhe.bundle.domain.ServerInfo;
import com.mhe.bundle.exception.BundleException;

/**
 * @author 854059
 *
 */
public class ClusterProcessor {

	private static Logger logger = Logger.getInstance(ServerInfo.class);
	private String adminUrl;
	private String port;
	private List<ClusterInfo> clusterInfoList = new ArrayList <ClusterInfo> ();
	private MBeanServerConnection connection;
	private JMXConnector connector;
	private ObjectName service;
	{
		try {
			service = new ObjectName(
					"com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
		} catch (MalformedObjectNameException e) {
			throw new AssertionError(e);
		}
	}
	/**
	 * @return the connection
	 */
	public MBeanServerConnection getConnection() {
		return connection;
	}
	/**
	 * @param connection the connection to set
	 */
	public void setConnection(MBeanServerConnection connection) {
		this.connection = connection;
	}
	/**
	 * @return the connector
	 */
	public JMXConnector getConnector() {
		return connector;
	}
	/**
	 * @param connector the connector to set
	 */
	public void setConnector(JMXConnector connector) {
		this.connector = connector;
	}
	
	/**
	 * @return the adminUrl
	 */
	public String getAdminUrl() {
		return adminUrl;
	}
	/**
	 * @param adminUrl the adminUrl to set
	 */
	public void setAdminUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		//return OpenAPIConfiguration.getAllDBValues().get("admin.user");
		//hard coded
		return "Weblogic";
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		//return OpenAPIConfiguration.getAllDBValues().get("admin.pwd");
		//hard coded
		return "weblogic123";
	}
	
	/**
	 * @return the port
	 */
	public String getPort() {
		return this.port;
	}
	
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	
	/**
	 * @return the clusterInfoList
	 */
	public List<ClusterInfo> getClusterInfoList() {
		return clusterInfoList;
	}
	
	
	/**
	 * This method is used to initialize weblogic connection for retrieving cluster and alive node information
	 * @param hostname --- Admin server url
	 * @param horizonClusterName --- Horizontal Cluster Name to pick details for only the cluster
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	
	public void initConnection(String hostname, String horizonClusterName) throws  BundleException {
		logger.debug("****************Inside initConnection Entry******************");
		String protocol = "t3";
		String jndiroot = "/jndi/";
		String mserver = "weblogic.management.mbeanservers.domainruntime";
		try {
			if (StringUtils.isBlank(this.getUserName()) || StringUtils.isBlank(this.getPassword())) {
				throw new BundleException("OAPI_20001");
			}
			if (hostname != null && hostname.contains(":")) {
				this.adminUrl = hostname.split(":")[0];
				this.port = hostname.split(":")[1];
			} else {
				throw new BundleException("OAPI_20002");
			}
			JMXServiceURL serviceURL = new JMXServiceURL(protocol, this.getAdminUrl(), Integer.valueOf(this.getPort()), jndiroot + mserver);
			logger.debug("serviceURL :: " + serviceURL);
			Map<String, String> h = new HashMap<String, String>();
			h.put(Context.SECURITY_PRINCIPAL, this.getUserName());
			h.put(Context.SECURITY_CREDENTIALS, this.getPassword());
			h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
			connector = JMXConnectorFactory.connect(serviceURL, h);
			logger.debug("connector :: {} " , connector);
			connection = connector.getMBeanServerConnection();
			logger.debug("connection :: {}" , connection);
			populateServerInfo(horizonClusterName);
		} catch (Exception e) {
			throw new BundleException(e);
		}
		logger.debug("****************Inside initConnection Exit******************");
	}
	
	/**
	 * This method is used to populate server info by using cluster
	 * @param horizonClusterName --- Horizontal Cluster Name to pick details for only the cluster
	 * @throws Exception
	 */
	private void populateServerInfo(String horizonClusterName) throws BundleException {
		logger.debug("****************Inside printClusterInfo Entry******************");
		List<ServerInfo> serverInfos = new ArrayList<ServerInfo>();
		ClusterInfo clusterInfo = null;
		String clusterName = null;
		String serverName = null;
		String listenAddress = null;
		InetAddress address = null;
		Integer listenPort = null;
		try {
			ObjectName domain1 = (ObjectName) connection.getAttribute(service,
					"DomainConfiguration");
			logger.debug("domain1 :: {}, horizonClusterName :: {}" , new Object[]{domain1,horizonClusterName});
			ObjectName[] clusterList = (ObjectName[]) connection.getAttribute(
					domain1, "Clusters");
			logger.debug("cluster_list :: " + clusterList);
			for (ObjectName cl : clusterList) {
				clusterName = (String) connection.getAttribute(cl, "Name");
				if(StringUtils.isBlank(horizonClusterName) || 
						(StringUtils.isNotBlank(horizonClusterName) && horizonClusterName.equalsIgnoreCase(clusterName))){
					logger.debug("######################## \n Cluster Name              : {}" , clusterName);
					logger.debug("######################## \n cl : {}" , cl);
					clusterInfo = new ClusterInfo();
					clusterInfo.setClusterName(clusterName);
					clusterInfo.setAdminUrl(this.getAdminUrl() + ":" + this.getPort());
					ObjectName[] servers = (ObjectName[]) connection.getAttribute(cl, "Servers");
					logger.debug("servers :: " + servers);
					for (ObjectName ser : servers) {
						serverName = (String) connection.getAttribute(ser, "Name");
						listenAddress = (String) connection.getAttribute(ser, "ListenAddress");
						address = InetAddress.getByName(listenAddress);
						listenPort = (Integer) connection.getAttribute(ser, "ListenPort");
						ServerInfo serverInfo = new ServerInfo();
						serverInfo.setServerIp(address.getHostAddress());
						serverInfo.setServerName(serverName);
						serverInfo.setServerPort(listenPort.toString());
						logger.debug("serverInfo :: {}" , serverInfo);
						serverInfos.add(serverInfo);
					}
					logger.debug("serverInfos :: " + serverInfos);
					clusterInfo.setServerInfos(serverInfos);
					clusterInfoList.add(clusterInfo);
					logger.debug("######################## \n");
				}
			}
			logger.debug("clusterInfoList :: " + clusterInfoList);
		} catch (Exception e) {
			throw new BundleException(e);
		}

		logger.debug("****************Inside printClusterInfo Exit******************");
	}
	
}
