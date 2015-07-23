package com.mhe.bundle.domain;

import java.util.List;

/**
 * @author TCS
 *
 */
public class ClusterInfo {
	private String clusterName;
	private String adminUrl;
	private List<ServerInfo> serverInfos;

	/**
	 * @return the serverInfos
	 */
	public List<ServerInfo> getServerInfos() {
		return serverInfos;
	}

	/**
	 * @param serverInfos the serverInfos to set
	 */
	public void setServerInfos(List<ServerInfo> serverInfos) {
		this.serverInfos = serverInfos;
	}

	/**
	 * @return the clusterName
	 */
	public String getClusterName() {
		return clusterName;
	}

	/**
	 * @param clusterName the clusterName to set
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClusterInfo [adminUrl=" + adminUrl + ", clusterName="
				+ clusterName + ", serverInfos=" + serverInfos + "]";
	}

	
	
}
