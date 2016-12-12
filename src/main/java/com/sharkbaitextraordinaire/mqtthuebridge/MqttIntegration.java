package com.sharkbaitextraordinaire.mqtthuebridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import com.sharkbaitextraordinaire.mqtthuebridge.config.MqttConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.*;

public class MqttIntegration implements MqttCallback, CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(MqttIntegration.class);
	@Autowired
	@Qualifier("hueToMqtt")
	private LinkedBlockingQueue<Object> incomingFromHue;
	@Autowired
	@Qualifier("mqttToQueue")
	private LinkedBlockingQueue<Object> outgoingToHue;
	@Autowired
	private MqttConfiguration configuration;
	
	MqttClient client;
	MqttConnectOptions connectionOptions;
	
	public MqttIntegration() {
		
	}
	
	public MqttClient getClient() {
		return client;
	}
	
	@Override
	public void connectionLost(Throwable cause) {
		logger.error("Connection to the MQTT broker was lost");
		logger.info(cause.getMessage());
		try {
			logger.warn("Reconnecting to MQTT broker");
			client.connect(connectionOptions);
		} catch (MqttException e) {
			logger.error("Failed to reconnect to broker");
			logger.info(e.getMessage());
		}
		
	}
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String payload = new String(message.getPayload());
		logger.info("New message from MQTT broker");
		logger.info(payload);
		logger.info("topic: " + topic);
		
		/*
		 *  Message should be a json-serialized PHLightStatus object containing
		 *  the various data needed to interpret the current state of lights 
		 */
		
	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO call this when we pop items off of hueToMqtt and send them
		
	}

	@Override
	public void run(String... arg0) throws Exception {
		String clientID = configuration.getClientID(); 
		String brokerUrl = configuration.getBrokerUrl();
		
		connectionOptions = new MqttConnectOptions();
		connectionOptions.setKeepAliveInterval(120);
		connectionOptions.setUserName(configuration.getUsername());
		connectionOptions.setPassword(configuration.getPassword().toCharArray());
		
		try {
			InputStream trustStoreInput = Thread.currentThread().getContextClassLoader().getResourceAsStream(configuration.getTrustStore());
			if (null == trustStoreInput) {
				logger.error("Couldn't find the configured trust store");
			}
			setSSLFactories(trustStoreInput);
			connectionOptions.setSocketFactory(SSLContext.getDefault().getSocketFactory());
			trustStoreInput.close();
			
		} catch (NoSuchAlgorithmException|KeyManagementException|CertificateException e) {
			logger.error("Had a problem with the configured trust store ", e);
		} catch (IOException e) {
			logger.error("IO exception dealing with mqtt configuration", e);
		}
		
		try {
			client = new MqttClient(brokerUrl, clientID);
			client.setCallback(this);
			client.connect(connectionOptions);
			
			if (client.isConnected()) {
				logger.error("Connected to MQTT broker");
//				MqttTopic topic = client.getTopic(configuration.getTopicPrefix()); // TODO this might not be needed?
				int subQoS = 0; // we don't really need qos
				client.subscribe(configuration.getTopicPrefix(), subQoS);
			} else {
				logger.error("NOT CONNECTED TO MQTT BROKER");
			}
		} catch (MqttException e) {
			logger.error("Exception while subscribing to updates from MQTT", e);
		}
	}
	
	@PreDestroy 		
	public void shutDown() {
		try {
			client.disconnect();
			client.close();
		} catch (MqttException e) {
			logger.info(e.getMessage());
		}
	}
	
	private void setSSLFactories(InputStream trustStream) throws 
	KeyStoreException, NoSuchAlgorithmException, CertificateException, KeyManagementException, IOException {
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		char[] trustStorePassword = null;
		if (null == configuration.getTrustStorePassword()
			|| "".equals(configuration.getTrustStorePassword())
			|| configuration.getTrustStorePassword().isEmpty()) {
			trustStorePassword = null;
		} else {
			trustStorePassword = configuration.getTrustStorePassword().toCharArray();
		}
		trustStore.load(trustStream, trustStorePassword);
		
		TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustFactory.init(trustStore);
		
		TrustManager[] trustManagers = trustFactory.getTrustManagers();
		
		SSLContext sslContext = SSLContext.getInstance("SSL"); // TODO This might need to be TLSv1?
		sslContext.init(null, trustManagers, null);
		SSLContext.setDefault(sslContext);
	}
}
