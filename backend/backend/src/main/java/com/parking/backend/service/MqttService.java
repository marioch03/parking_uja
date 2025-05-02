package com.parking.backend.service;

import java.util.Optional;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.parking.backend.model.Estado;
import com.parking.backend.model.Plaza;

@Component
public class MqttService implements MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id.subscriber}")
    private String clientId;

    private MqttClient mqttClient;

    @Autowired
    private PlazaService plazaService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private EstadoService estadoService;

    public void connect() {
        try {
            mqttClient = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(600);
            mqttClient.setCallback(this); // Establecer el callback para recibir mensajes

            logger.info("Conectando al broker MQTT: {}", brokerUrl);
            mqttClient.connect(connOpts);
            logger.info("Conectado al broker MQTT");

            // Suscribirse a los topics al iniciar
            String[] topics = {
                    "parking/Plaza_1", "parking/Plaza_2", "parking/Plaza_3",
                    "parking/Matricula_1", "parking/Matricula_2", "parking/Matricula_3",
                    "parking/Led_1", "parking/Led_2", "parking/Led_3"
            };
            // Estableceer la calidad de servicio
            int[] qos = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
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
            case "parking/Plaza_1":
                procesarTopicPlaza(1, payload);
                break;
            case "parking/Plaza_2":
                procesarTopicPlaza(2, payload);
                break;
            case "parking/Plaza_3":
                procesarTopicPlaza(3, payload);
                break;
            case "parking/Matricula_1":
                procesarTopicMatricula(1, payload);
                break;
            case "parking/Matricula_2":
                procesarTopicMatricula(2, payload);
                break;
            case "parking/Matricula_3":
                procesarTopicMatricula(3, payload);
                break;
            case "parking/Led_1":
                logger.warn("Led 1: ", payload);
                break;
            case "parking/Led_2":
                logger.warn("Led 2: ", payload);
                break;
            case "parking/Led_3":
                logger.warn("Led 3: ", payload);
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

    public void procesarTopicPlaza(int plaza, String payload) {
        System.out.println("Procesando mensaje de la plaza: "+plaza);
        int idEstado = Integer.parseInt(payload);
        // Solo me interesa el caso en el que un coche abandona una plaza, en el otro caso dependo de la matrícula y se gestiona en otro lado.
        if(idEstado == 0){
            Optional<Estado> estadoLibre = estadoService.obtenerEstadoPorId(1);
            plazaService.actualizarPlaza(plaza, estadoLibre.get());
            //Publicar qué led hay que encender
            String topicLed = "Led_"+plaza;
            publishMessage(topicLed, String.valueOf(estadoLibre));
        } 
    }

    public void procesarTopicMatricula(int idPlaza, String payload) {
        System.out.println("Procesando matricula de la plaza "+ idPlaza+": "+payload);
        // Pensar la lógica
        String topicLed = "Led_"+idPlaza;
        Optional<Plaza> plaza = plazaService.obtenerPlazaPorId(idPlaza);
        if(plaza.isPresent()){
            Optional<Estado> estadoPlaza = estadoService.obtenerEstadoPorId(plaza.get().getEstado().getId());
            Optional<Estado> estadoOcupado = estadoService.obtenerEstadoPorId(2);
            Optional<Estado> estadoIlegal = estadoService.obtenerEstadoPorId(4);
            System.out.println("PLAZA "+ plaza);
            //Comprobamos si está reservada
            if(estadoPlaza.isPresent()){
                System.out.println("Estado plaza: "+ estadoPlaza.get().getNombre());
                if(estadoPlaza.get().getId() == 3){
                    boolean ocupacionLegal = reservaService.comprobarPlazaReservada(idPlaza, payload);
                    if(ocupacionLegal){
                        //El estado de la plaza y el led pasn a ocupado
                        System.out.println("OCUPACION LEGAL");
                        plazaService.actualizarPlaza(idPlaza, estadoOcupado.get());
                        publishMessage(topicLed, String.valueOf(estadoOcupado));
                    } else{
                        //El estado de la plaza y led pasa a ilegal
                        System.out.println("OCUPACION ILEGAL");
                        plazaService.actualizarPlaza(idPlaza, estadoIlegal.get());
                        publishMessage(topicLed, String.valueOf(estadoIlegal));
                    }
                } else if(estadoPlaza.get().getId() == 1){
                    //Plaza y led pasan a ocupado
                    System.out.println("OCUPACION LEGAL");
                    plazaService.actualizarPlaza(idPlaza, estadoOcupado.get());
                    publishMessage(topicLed, String.valueOf(estadoOcupado));
                }
            }
        }
    }

    @PostConstruct
    public void init() {
        connect();
    }
}
