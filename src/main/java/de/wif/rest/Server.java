package de.wif.rest;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Server {

    final static private int PORT = 8080;
    final static private int maxThreads = 8;
    final static private int minThreads = 2;
    final static private int timeOutMillis = 30000;

    // messages for users to be handled
    private Map<String, List<Message>> messages;

    //de-/serialization for JSON
    private Gson gson;

    // with notifications
    private boolean useNotifications;

    // add MQTT
    private MqttClient mqttClient;

    /**
     * Creates a server instance
     */
    Server(boolean useNotifications) {
        this.messages = new HashMap<>();
        this.gson = new Gson();
        this.useNotifications = useNotifications;
        if (this.useNotifications) {
            try {
                mqttClient = new MqttClient("tcp://51.124.8.126:1883", "WIF", new MemoryPersistence());
                mqttClient.connect();
            } catch (MqttException e) {
                this.useNotifications = false;
                e.printStackTrace();
            }
        }
    }

    /**
     * Runs the server
     */
    public void run() {
        threadPool(maxThreads, minThreads, timeOutMillis);
        port(PORT); // Spark will run on port 8080

        init();
    }

    /**
     * Initializes pathes
     */
    private void init() {

        get("/hello", (request, response) -> "Hello World", src -> gson.toJson(src));

        get("/messages/:name", (request, response) -> {
            try {
                String user = request.params(":name");
                List<Message> msgs = this.messages.getOrDefault(user, new ArrayList<Message>());
                this.messages.remove(user);
                return gson.toJson(msgs);
            }
            catch (Exception ex) {
                return gson.toJson("An error occured: " + ex.getMessage());
            }
        });

        // todo: get Message Count /messages/:name/count

        post("/messages/:toName", (request, response) -> {
            try {
                Message message = gson.fromJson(request.body(), Message.class);
                if (message != null) {
                    String user = request.params(":toName");
                    List<Message> msgs = this.messages.getOrDefault(user, new ArrayList<Message>());
                    msgs.add(message);
                    this.messages.put(user, msgs);

                    if (this.useNotifications)
                        this.sendNotifications(user);

                    return gson.toJson("Message successfully sent!");
                }
                return gson.toJson("Message was empty!");
            }
            catch (Exception ex) {
                return gson.toJson("An error occured: " + ex.getMessage());
            }
        });
    }

    /**
     * Sends notifications to MQTT Broker
     * @param user
     */
    private void sendNotifications(String user) {
        System.out.println("Connected");
        MqttMessage message = new MqttMessage("New message".getBytes());
        try {
            mqttClient.publish(user, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server s = new Server(false);
        s.run();
    }
}
