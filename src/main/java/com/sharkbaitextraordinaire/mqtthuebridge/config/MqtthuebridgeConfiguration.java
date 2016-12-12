package com.sharkbaitextraordinaire.mqtthuebridge.config;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqtthuebridgeConfiguration {
	
	/**
	 * 
	 * @return The configuration for Philips Hue integration
	 */
	@Bean
	public HueConfiguration hueConfiguration() {
		return new HueConfiguration();
	}
	
	/**
	 * 
	 * @return The configuration for our MQTT broker configuration
	 */
	@Bean
	public MqttConfiguration mqttConfiguration() {
		return new MqttConfiguration();
	}
	
	/**
	 * This is a queue for communication going from the MQTT broker
	 * integration to the Hue lamps (ie, commands for lights)
	 * @return
	 */
	@Bean(name="mqttToHue")
	public LinkedBlockingQueue<Object> mqttToHue() {
		return new LinkedBlockingQueue<Object>();
	}
	
	/**
	 * This is a queue for communication going from the Hue lamp
	 * integration to the MQTT broker (ie, status from lamps)
	 * @return
	 */
	@Bean(name="hueToMqtt")
	public LinkedBlockingQueue<Object> hueToMqtt() {
		return new LinkedBlockingQueue<Object>();
	}
}
