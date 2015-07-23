package com.mhe.bundle.domain;


public class  ServerInfo {
	
	private String serverIp;
	private String serverPort;
	private String serverName;
	private String machineName;
	private String protocol = "http://";
	private String manageServerUrl;
	/**
	 * @return the serverIp
	 */
	public String getServerIp() {
		return serverIp;
	}
	/**
	 * @return the machineName
	 */
	public String getMachineName() {
		return machineName;
	}
	/**
	 * @param machineName the machineName to set
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	/**
	 * @param serverIp the serverIp to set
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	/**
	 * @return the serverPort
	 */
	public String getServerPort() {
		return serverPort;
	}
	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * @return the manageServerUrl
	 */
	public String getManageServerUrl() {
		return this.getProtocol()+this.getServerIp()+":"+this.getServerPort();
	}
	/**
	 * @param manageServerUrl the manageServerUrl to set
	 */
	public void setManageServerUrl(String manageServerUrl) {
		this.manageServerUrl = manageServerUrl;
	}
	
	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}
	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServerInfo [machineName=" + machineName + ", manageServerUrl="
				+ manageServerUrl + ", protocol=" + protocol + ", serverIp="
				+ serverIp + ", serverName=" + serverName + ", serverPort="
				+ serverPort + "]";
	}
	
	
}
