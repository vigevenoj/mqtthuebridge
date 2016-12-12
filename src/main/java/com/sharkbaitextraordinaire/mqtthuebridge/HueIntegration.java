package com.sharkbaitextraordinaire.mqtthuebridge;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class HueIntegration implements PHSDKListener {
	
	private final Logger logger = LoggerFactory.getLogger(HueIntegration.class);
	private PHHueSDK huesdk;
	@Autowired
	@Qualifier("mqttToHue")
	private LinkedBlockingQueue<Object> incomingFromMqtt;
	@Autowired
	@Qualifier("hueToMqtt")
	private LinkedBlockingQueue<Object> outgoingToMqtt;

	@Override
	public void onAccessPointsFound(List<PHAccessPoint> accessPoints) {
		if (accessPoints != null && (accessPoints.size() == 0 || accessPoints.isEmpty())) {
			logger.error("No Hue bridges found");
			System.exit(1);
		} else {
			for (PHAccessPoint accessPoint : accessPoints) {
				logger.warn("Found Hue bridge '" + accessPoint.getBridgeId() + "' at '" + accessPoint.getIpAddress() + "'");
			}
			if (accessPoints.size() > 1) {
				logger.warn("Multiple Hue bridges found");
			}
		}
		
	}

	@Override
	public void onAuthenticationRequired(PHAccessPoint accessPoint) {
		logger.error("Go push the button on the bridge");
		PHHueSDK.getInstance().startPushlinkAuthentication(accessPoint);
	}

	@Override
	public void onBridgeConnected(PHBridge bridge, String username) {
		logger.info("Connected to bridge '" + bridge.toString() + "' as " + username);
		huesdk.setSelectedBridge(bridge);
		// save the username that we've gotten, just in case it's not expected
		logger.error("The bridge at " 
			+ bridge.getResourceCache().getBridgeConfiguration().getIpAddress() 
			+" gave us username '" + username + "'");
		huesdk.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);
		// set up some tasks to schedule reporting the lights to mqtt broker
	}

	@Override
	public void onCacheUpdated(List<Integer> cacheNotificationsList, PHBridge bridge) {
		logger.debug("Cache updated" + cacheNotificationsList);
		if (cacheNotificationsList.contains(PHMessageType.LIGHTS_CACHE_UPDATED)) {
			// the lights cache has been updated
		}
		if (cacheNotificationsList.contains(PHMessageType.GROUPS_CACHE_UPDATED)) {
			// the groups cache has been updated
		}
		if (cacheNotificationsList.contains(PHMessageType.SCENE_CACHE_UPDATED)) {
			// the scenes cache has been updated
		}
	}

	@Override
	public void onConnectionLost(PHAccessPoint accessPoint) {
		logger.warn("Connection lost to bridge '" + accessPoint.getBridgeId() 
			+ "' at " + accessPoint.getIpAddress());
		
	}

	@Override
	public void onConnectionResumed(PHBridge bridge) {
		// This might be called during a heartbeat so don't log it too heavily
		logger.debug("Resumed connection to bridge " 
				+ bridge.getResourceCache().getBridgeConfiguration().getBridgeID());
		
	}

	@Override
	public void onError(int code, String message) {
		if (code == PHHueError.BRIDGE_NOT_RESPONDING) {
			logger.error("Bridge not responding");
			logger.error(message);
		} else if (code == PHMessageType.PUSHLINK_BUTTON_NOT_PRESSED) {
			logger.error("Did you push the pushlink button?");
			logger.error(message);
		} else if (code == PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
			logger.error("Authenticating to bridge failed");
			logger.error(message);
		} else if (code == PHMessageType.BRIDGE_NOT_FOUND) {
			logger.error("Couldn't find any Hue bridges");
			logger.error(message);
		}
		else logger.error(message);
		
	}

	@Override
	public void onParsingErrors(List<PHHueParsingError> parsingErrors) {
		logger.error("There were internal API errors: ");
		for (PHHueParsingError parseError : parsingErrors) {
			logger.error(parseError.getCode() + ": " + parseError.getMessage());
		}
	}

}
