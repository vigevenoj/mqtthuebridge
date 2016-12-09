package com.sharkbaitextraordinaire.mqtthuebridge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="hue")
public class HueConfiguration {
	
	private String bridgeAddress;
	private String username;
	private String appName;
	private String deviceName;
	
	public String getBridgeAddress() {
		return bridgeAddress;
	}
	public void setBridgeAddress(String bridgeAddress) {
		this.bridgeAddress = bridgeAddress;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}
