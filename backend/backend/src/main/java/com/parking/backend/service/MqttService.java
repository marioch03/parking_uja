package com.parking.backend.service;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttService implements MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id.subscriber}")
    private String clientId;

    private MqttClient mqttClient;

    public void connect() {
        try {
            mqttClient = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            mqttClient.setCallback(this); // Establecer el callback para recibir mensajes

            logger.info("Conectando al broker MQTT: {}", brokerUrl);
            mqttClient.connect(connOpts);
            logger.info("Conectado al broker MQTT");

            // Suscribirse a los topics al iniciar
            String[] topics = { "parking/plaza1", "parking/plaza2", "parking/plaza3", "parking/plaza4",
                    "parking/plaza5" };
            int[] qos = { 0, 0, 0, 0, 0 };
            mqttClient.subscribe(topics, qos);
            logger.info("Suscrito a los topics: {}", String.join(", ", topics));

        } catch (MqttException e) {
            logger.error("Error al conectar o suscribirse al broker MQTT: {}", e.getMessage(), e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        logger.warn("Conexión perdida con el broker MQTT", cause);
        // Aquí podrías intentar reconectar automáticamente
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        logger.info("Mensaje recibido en el topic '{}': {}", topic, payload);
        processMessage(topic, payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Se puede dejar vacío si no necesitas confirmación de entrega
    }

    private void processMessage(String topic, String payload) {
        switch (topic) {
            case "parking/plaza1":
                logger.info("Procesando mensaje de la plaza 1: {}", payload);
                break;
            case "parking/plaza2":
                logger.info("Procesando mensaje de la plaza 2: {}", payload);
                break;
            case "parking/plaza3":
                logger.info("Procesando mensaje de la plaza 3: {}", payload);
                break;
            case "parking/plaza4":
                logger.info("Procesando mensaje de la plaza 4: {}", payload);
                break;
            case "parking/plaza5":
                logger.info("Procesando mensaje de la plaza 5: {}", payload);
                break;
            default:
                logger.warn("Mensaje recibido en un topic no esperado: {}", topic);
                break;
        }
    }

    public void publishMessage(String topic, String payload) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(0);
            mqttClient.publish(topic, message);
            logger.info("Mensaje publicado en el topic '{}': {}", topic, payload);
        } catch (MqttException e) {
            logger.error("Error al publicar el mensaje en el topic '{}': {}", topic, e.getMessage(), e);
        }
    }

    public void disconnect() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                logger.info("Desconectado del broker MQTT");
            }
        } catch (MqttException e) {
            logger.error("Error al desconectar del broker MQTT: {}", e.getMessage(), e);
        }
    }

    @PostConstruct
    public void init() {
        connect();
    }
}
