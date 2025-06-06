package com.parking.backend.service;

import java.util.Optional;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PreDestroy;
import java.util.Stack;
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

    private final Stack<Estado> historialPlaza1 = new Stack<>();
    private final Stack<Estado> historialPlaza2 = new Stack<>();
    private final Stack<Estado> historialPlaza3 = new Stack<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void connect() {
        try {
            mqttClient = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(600);
            connOpts.setAutomaticReconnect(true); // Habilitar reconexión automática
            connOpts.setMaxReconnectDelay(5000); // Máximo 5 segundos entre intentos de reconexión
            mqttClient.setCallback(this);

            logger.info("Conectando al broker MQTT: {}", brokerUrl);
            mqttClient.connect(connOpts);
            logger.info("Conectado al broker MQTT");

            // Suscribirse a los topics al iniciar
            String[] topics = {
                    "parking/plaza/1", "parking/plaza/2", "parking/plaza/3",
                    "parking/matricula/1", "parking/matricula/2", "parking/matricula/3",
                    "parking/led/1", "parking/led/2", "parking/led/3"
            };
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
        // Intentar reconectar automáticamente
        try {
            Thread.sleep(5000); // Esperar 5 segundos antes de intentar reconectar
            connect();
        } catch (InterruptedException e) {
            logger.error("Error durante la reconexión: {}", e.getMessage(), e);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload());
            logger.info("Mensaje recibido en el topic '{}': {}", topic, payload);

            // Procesar el mensaje de forma asíncrona
            executorService.submit(() -> {
                try {
                    processMessage(topic, payload);
                } catch (Exception e) {
                    logger.error("Error al procesar mensaje del topic '{}': {}", topic, e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            logger.error("Error al procesar mensaje del topic '{}': {}", topic, e.getMessage(), e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Se puede dejar vacío si no necesitas confirmación de entrega
    }

    private void processMessage(String topic, String payload) {
        try {
            switch (topic) {
                case "parking/plaza/1":
                    procesarTopicPlaza(1, payload);
                    break;
                case "parking/plaza/2":
                    procesarTopicPlaza(2, payload);
                    break;
                case "parking/plaza/3":
                    procesarTopicPlaza(3, payload);
                    break;
                case "parking/matricula/1":
                    procesarTopicMatricula(1, payload);
                    break;
                case "parking/matricula/2":
                    procesarTopicMatricula(2, payload);
                    break;
                case "parking/matricula/3":
                    procesarTopicMatricula(3, payload);
                    break;
                case "parking/led/1":
                    logger.warn("Led 1: {}", String.valueOf(payload));
                    break;
                case "parking/led/2":
                    logger.warn("Led 2: {}", String.valueOf(payload));
                    break;
                case "parking/led/3":
                    logger.warn("Led 3: {}", String.valueOf(payload));
                    break;
                default:
                    logger.warn("Mensaje recibido en un topic no esperado: {}", topic);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error al procesar mensaje del topic '{}': {}", topic, e.getMessage(), e);
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
        System.out.println("Procesando mensaje de la plaza: " + plaza);
        int idEstado = Integer.parseInt(payload);
        String topicLed = "parking/led/" + plaza;
        // Solo me interesa el caso en el que un coche abandona una plaza, en el otro
        // caso dependo de la matrícula y se gestiona en otro lado.
        if (idEstado == 0) {
            Estado ultimoEstado = getUltimoEstadoPlaza(plaza);
            if (ultimoEstado != null) {
                if (ultimoEstado.getId() == 4) {
                    Optional<Estado> estadoReservado = estadoService.obtenerEstadoPorId(3);
                    plazaService.actualizarPlaza(plaza, estadoReservado.get());
                    agregarEstadoAHistorial(plaza, estadoReservado.get());
                    publishMessage(topicLed, "3");


                } else {
                    Optional<Estado> estadoLibre = estadoService.obtenerEstadoPorId(1);
                    plazaService.actualizarPlaza(plaza, estadoLibre.get());
                    agregarEstadoAHistorial(plaza, estadoLibre.get());
                    // Publicar qué led hay que encender
                    publishMessage(topicLed, "1");
                }
            } else {
                Optional<Estado> estadoLibre = estadoService.obtenerEstadoPorId(1);
                plazaService.actualizarPlaza(plaza, estadoLibre.get());
                agregarEstadoAHistorial(plaza, estadoLibre.get());
                // Publicar qué led hay que encender
                publishMessage(topicLed, "1");
            }

        }
    }

    public void procesarTopicMatricula(int idPlaza, String payload) {
        System.out.println("Procesando matricula de la plaza " + idPlaza + ": " + payload);
        // Pensar la lógica
        String topicLed = "parking/led/" + idPlaza;
        Optional<Plaza> plaza = plazaService.obtenerPlazaPorId(idPlaza);
        if (plaza.isPresent()) {
            Optional<Estado> estadoPlaza = estadoService.obtenerEstadoPorId(plaza.get().getEstado().getId());
            Optional<Estado> estadoOcupado = estadoService.obtenerEstadoPorId(2);
            Optional<Estado> estadoIlegal = estadoService.obtenerEstadoPorId(4);
            System.out.println("PLAZA " + plaza);
            // Comprobamos si está reservada
            if (estadoPlaza.isPresent()) {
                System.out.println("Estado plaza: " + estadoPlaza.get().getNombre());
                if (estadoPlaza.get().getId() == 3) {
                    boolean ocupacionLegal = reservaService.comprobarPlazaReservada(idPlaza, payload);
                    if (ocupacionLegal) {
                        // El estado de la plaza y el led pasn a ocupado
                        System.out.println("OCUPACION LEGAL");
                        plazaService.actualizarPlaza(idPlaza, estadoOcupado.get());
                        publishMessage(topicLed, "2");
                        agregarEstadoAHistorial(idPlaza, estadoOcupado.get());
                    } else {
                        // El estado de la plaza y led pasa a ilegal
                        System.out.println("OCUPACION ILEGAL");
                        plazaService.actualizarPlaza(idPlaza, estadoIlegal.get());
                        publishMessage(topicLed, "4");
                        agregarEstadoAHistorial(idPlaza, estadoIlegal.get());

                    }
                } else if (estadoPlaza.get().getId() == 1) {
                    // Plaza y led pasan a ocupado
                    System.out.println("OCUPACION LEGAL");
                    plazaService.actualizarPlaza(idPlaza, estadoOcupado.get());
                    publishMessage(topicLed, "2");
                    agregarEstadoAHistorial(idPlaza, estadoOcupado.get());
                }
            }
        }
    }

    @PostConstruct
    public void init() {
        connect();
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (executorService != null) {
                executorService.shutdown();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            }
            disconnect();
        } catch (InterruptedException e) {
            logger.error("Error durante la limpieza: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    public void agregarEstadoAHistorial(int plazaId, Estado estado) {
        switch (plazaId) {
            case 1:
                historialPlaza1.push(estado);
                System.out.println("PILA DE LA PLAZA 1: "+historialPlaza1.toString());
                break;
            case 2:
                historialPlaza2.push(estado);
                break;
            case 3:
                historialPlaza3.push(estado);
                break;
            default:
                logger.warn("Plaza desconocida: {}", plazaId);
        }
    }

    private Estado getUltimoEstadoPlaza(int idPlaza) {
        switch (idPlaza) {
            case 1:
                if (!historialPlaza1.empty()) {
                    return historialPlaza1.peek();
                }
                return null;
            case 2:
                if (!historialPlaza2.empty()) {
                    return historialPlaza1.peek();
                }
                return null;
            case 3:
                if (!historialPlaza3.empty()) {
                    return historialPlaza1.peek();
                }
                return null;
            default:
                logger.warn("Plaza desconocida: {}", idPlaza);
                return null;
        }
    }
}
