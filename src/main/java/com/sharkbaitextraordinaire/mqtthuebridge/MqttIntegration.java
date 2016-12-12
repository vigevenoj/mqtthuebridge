package com.sharkbaitextraordinaire.mqtthuebridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.*;

public class MqttIntegration implements MqttCallback {

	private final Logger logger = LoggerFactory.getLogger(MqttIntegration.class);
	@Autowired
	@Qualifier("hueToMqtt")
	private LinkedBlockingQueue<Object> incomingFromHue;
	@Autowired
	@Qualifier("mqttToQueue")
	private LinkedBlockingQueue<Object> outgoingToHue;
	
	public MqttIntegration() {
		
	}
	
	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
}
