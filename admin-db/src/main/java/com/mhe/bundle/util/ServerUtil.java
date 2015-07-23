package com.mhe.bundle.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

import com.google.common.base.Splitter;
import com.mhe.bundle.common.Constants;
import com.mhe.bundle.common.Logger;
import com.mhe.bundle.domain.ClusterInfo;


@Service("serverUtil")
public class ServerUtil {
	
	
	private static Logger logger = Logger.getInstance(ServerUtil.class);
	
	@Autowired
	private LogbackConfigHandler logbackConfigHandler;
	
	/**
	 * Return all the information like cluster information, associated manage server list across the AZ
	 * @return List of ClusterInfo
	 * @throws Exception
	 */
	public List<ClusterInfo> appServers(String applicationName) throws Exception {
		logger.debug("****************Inside appServers Entry******************");
		ClusterProcessor clusterProcessor = new ClusterProcessor ();
		/*
		 * admin.servers -- 10.221.9.141:7003|openapi_devx3_cluster,10.221.9.141:7003|openapi_devx4_cluster
		 * The information is pipe delimited. First part is the hostName:port, & second part is the cluster Name
		 */
		//String adminServersInfo = OpenAPIConfiguration.getAllDBValues().get("admin.servers");
		//hard coded
		String adminServersInfo = "10.221.10.166:7005,10.221.17.4:7005";
		String horizonClusterName = null;
		String adminUrl = null;
		Splitter commaSplitter =Splitter.on( Constants.DELIMITER_COMMA).omitEmptyStrings().trimResults();
		logger.debug("adminServersInfo :: {}" , adminServersInfo);
		logger.debug(commaSplitter.split(adminServersInfo));
		for ( String str : commaSplitter.split(adminServersInfo) ) {
			logger.debug("str :: {}", str);
			if( isHorizontalCluster(adminServersInfo) ){
				adminUrl = getAdminserversForHorCluster(adminServersInfo);
				horizonClusterName = getHorizantalClusterName(adminServersInfo);
			}
			else{
				adminUrl = str;
				horizonClusterName = null;
			}
			logger.debug("adminUrl :: {},  horizonClusterName :: {}", new Object[]{adminUrl,horizonClusterName});
			clusterProcessor.initConnection(adminUrl,horizonClusterName);
		}
		logger.debug("clusterProcessor.getClusterInfoList() :: " + clusterProcessor.getClusterInfoList());
		logger.debug("****************Inside appServers Exit******************");
		return clusterProcessor.getClusterInfoList();
	}
	
	/**
	 * Retrieve cluster information, associated manage server list for admin server.
	 * @param adminServerUrl
	 * @return ClusterInfo
	 * @throws Exception
	 */
	
	public ClusterInfo appServerDetail(String adminServerUrl) throws Exception {
		logger.debug("****************Inside appServers Entry******************");
		//String adminServersInfo = OpenAPIConfiguration.getAllDBValues().get("admin.servers");
		//hard coded
		String adminServersInfo = "10.221.10.166:7005,10.221.17.4:7005";
		String horizonClusterName = null;
		if ( isHorizontalCluster(adminServersInfo) ){
			horizonClusterName = getHorizantalClusterName(adminServersInfo);
		}
		ClusterProcessor clusterProcessor = new ClusterProcessor ();
		ClusterInfo clusterInfo = null;
		logger.debug("adminServerUrl :: " + adminServerUrl);
		clusterProcessor.initConnection(adminServerUrl,horizonClusterName);
		logger.debug("clusterProcessor.getClusterInfoList() :: " + clusterProcessor.getClusterInfoList());
		if(CollectionUtils.isNotEmpty(clusterProcessor.getClusterInfoList())){
			clusterInfo = clusterProcessor.getClusterInfoList().get(0);
		}
		logger.debug("****************Inside appServers Exit******************");
		return clusterInfo;
	}
	
	/**
	 * Server information
	 * @return
	 * @throws Exception
	 */
	public String getServerName() throws Exception {
		InitialContext ctx = null;
		String thisServer = null;
		String machineName = null;
		ObjectName service = null;
		MBeanServer server = null;
		ObjectName rt = null;
		Integer ListenAddress = null;
		try {
			machineName = java.net.InetAddress.getLocalHost().getHostName();
			service = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
			ctx = new InitialContext();
			server = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");
			rt = (ObjectName) server.getAttribute(service, "ServerRuntime");
			ListenAddress = (Integer) server.getAttribute(rt, "ListenPort");
			thisServer = machineName + ":" + ListenAddress;
		} finally {
			if (ctx != null) {
				ctx.close();
			}
		}
		return thisServer;
	}
	
	/**
	 * getLoggerList return the List of Logger loaded in the JVM
	 * 
	 * @return List<ch.qos.logback.classic.Logger>
	 * @throws Exception
	 */
	public Map<String,Level> getLoggerList() throws Exception{
		logger.debug("****************Inside getLoggerList Entry******************");
		Map<String,Level> loggerMap = new LinkedHashMap<String, Level>();
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		List<ch.qos.logback.classic.Logger> loggerList = context.getLoggerList();
		for(ch.qos.logback.classic.Logger logger : loggerList){
			loggerMap.put(logger.getName(), logger.getEffectiveLevel());
		}
		logger.debug("loggerMap   ::  "+loggerMap);
		logger.debug("****************Inside getLoggerList Exit******************");
		return loggerMap;
	}
	
	
	/**
	 * changeLogLevel set the log Level for the Logger Name
	 * 
	 * @param loggerName
	 * @param logLevel
	 * @return String
	 */
	public String changeLogLevel(String loggerName, String logLevel) {
		logger.debug("****************Inside changeLogLevel Entry******************");
		String updateStatus = null;
		try {
			if (StringUtils.isNotBlank(loggerName)
					&& StringUtils.isNotBlank(logLevel)) {
				logbackConfigHandler.changeLevel(loggerName, logLevel);
				updateStatus = "Log level changed successfully";
			}
		} catch (Exception ex) {
			updateStatus = "Exception while changing log levels - "
					+ ex.getMessage() + " . Look at log for more details ";
			logger.error(updateStatus, ex);
		}
		logger.debug("****************Inside changeLogLevel Exit******************");
		return updateStatus;
	}
	
	public String enableErrorLogLevelForAll() {
		logger.debug("****************Inside enableErrorLogLevelForAll Entry******************");
		String updateStatus = null;
		try {
			LogbackConfigHandler.setErrorLogLevel();
			updateStatus = "Log level changed successfully";
		} catch (Exception ex) {
			updateStatus = "Exception while changing log levels - "
					+ ex.getMessage() + " . Look at log for more details ";
			logger.error(updateStatus, ex);
		}
		logger
				.debug("****************Inside enableErrorLogLevelForAll Exit******************");
		return updateStatus;
	}
	
	/**
	 * Check wheather horizontal cluster is present or not
	 * @param adminServerInfo
	 * @return boolean
	 */
	
	private boolean isHorizontalCluster ( String adminServerInfo ) {
		
		return adminServerInfo.contains( Constants.DELIMITER_PIPE )? Boolean.TRUE : Boolean.FALSE;
	}
	
	/**
	 * Admin server for horizontal cluster
	 * @param adminServerInfo
	 * @return String
	 */
	private String getAdminserversForHorCluster ( String adminServerInfo ) {
		return adminServerInfo.split("\\|")[0];
	}
	
	/**
	 * Cluster name for horizontal cluster
	 * @param adminServerInfo
	 * @return String
	 */
	private String getHorizantalClusterName ( String adminServerInfo ) {
		return adminServerInfo.split("\\|")[1];
	}
	
}
