package de.wif.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Client extends RESTCall implements ChatClient, MqttCallback {

    // username
    private String user;

    // add MQTT
    private MqttClient mqttClient;

    /**
     * Creates an instance of RESTCall.
     *
     * @param base Base URL of the endpoint
     */
    public Client(String user, URL base) {
        super(base);
        this.user = user;
        initMqtt();
    }

    @Override
    public String getHello() {
        // todo: Implementieren Sie hier den Aufruf, um den /hello Endpunkt abzurufen
        return null;
    }

    @Override
    public String getMessageCount() {
        // todo: Implementieren Sie hier den Aufruf, um den /messages/:name Endpunkt abzurufen
        return null;
    }

    @Override
    public List<Message> getMessages() {
        // todo: Implementieren Sie hier den Aufruf, um den /messages/:name Endpunkt abzurufen
        return null;
    }

    @Override
    public String sendMessage(String toUser, String messageText) {
        // todo: Implementieren Sie hier den Aufruf, um ein Nachricht an den /messages/:toName Endpunkt zu schicken
        return null;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        if ("New message".equals(msg)) {
            // todo: new message should be handled here
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // ignore
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println(cause.getLocalizedMessage());
    }

    private void initMqtt() {
        try {
            mqttClient = new MqttClient("tcp://51.124.8.126:1883", "WIF", new MemoryPersistence());
            mqttClient.connect();
            mqttClient.setCallback(this);
            mqttClient.subscribe(this.user);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MalformedURLException {
        Client  c = new Client("test", new URL("http://localhost:8080"));
        System.out.println(c.getHello());

        System.out.println(c.sendMessage("Test2", "Hallo Welt!"));

        System.out.println(c.getMessageCount());

        System.out.println(c.getMessages());

        System.out.println(c.sendMessage("test", "Hallo Welt!"));

        System.out.println(c.getMessageCount());

        System.out.println(c.getMessages());
    }

}
