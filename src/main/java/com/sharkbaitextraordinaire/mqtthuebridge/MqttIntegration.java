package com.sharkbaitextraordinaire.mqtthuebridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.*;

public class MqttIntegration {

	private final Logger logger = LoggerFactory.getLogger(MqttIntegration.class);
	// TODO need to wire this up
	private LinkedBlockingQueue<Object> incomingFromHue;
	// TODO need to wire this up
	private LinkedBlockingQueue<Object> outgoingToHue;
	
	
}
