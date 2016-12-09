package com.sharkbaitextraordinaire.mqtthuebridge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="mqtt")
public class MqttConfiguration {
	private String brokerUrl;
	private String clientID;
	private String username;
	private String password;
	private String sslProtocol;
	private String trustStore;
	private String trustStorePassword;
	private String topicPrefix;
	
	public String getBrokerUrl() {
		return brokerUrl;
	}
	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSslProtocol() {
		return sslProtocol;
	}
	public void setSslProtocol(String sslProtocol) {
		this.sslProtocol = sslProtocol;
	}
	public String getTrustStore() {
		return trustStore;
	}
	public void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}
	public String getTrustStorePassword() {
		return trustStorePassword;
	}
	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}
	public String getTopicPrefix() {
		return topicPrefix;
	}
	public void setTopicPrefix(String topicPrefix) {
		this.topicPrefix = topicPrefix;
	}
}
