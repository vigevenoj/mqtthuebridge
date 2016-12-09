This is a small Java application designed to provide a bidirectional bridge between Philips Hue light bulbs and an MQTT broker. It was inspired by https://github.com/owagner/hue2mqtt but had different design goals to achieve the same behavior. Specifically, the long-term goal for this application is to fit into an architecture consisting of mutally-authenticated microservices. See http://developer.okta.com/blog/2015/12/02/tls-client-authentication-for-services and https://codurance.com/2016/03/28/Mutual-Problems/ for details about the design goals.

Requirements
---
* An MQTT broker
* A Philips Hue bridge and some lights to control

Configuration
---
See src/main/resources/default.properties for the properties that can be configured at statup. Currently, these are
 * hue.bridgeAddress
 * hue.userName
 * hue.appName
 * hue.deviceName
 * mqtt.brokerUrl
 * mqtt.clientId
 * mqtt.userName
 * mqtt.password
 * mqtt.sslProtocol=tlsv1
 * mqtt.trustStore
 * mqtt.trustStorePassword
 * mqtt.topicPrefix
