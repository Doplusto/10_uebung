package de.wif.rest;

import com.google.gson.Gson;

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

    /**
     * Creates a server instance
     */
    Server(boolean useNotifications) {
        this.messages = new HashMap<>();
        this.gson = new Gson();
        this.useNotifications = useNotifications;
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
                if (this.messages.containsKey(user)) {
                    List<Message> msgs = this.messages.get(user);
                    this.messages.remove(user);
                    return gson.toJson(msgs);
                }
                return gson.toJson("No new messages!");
            }
            catch (Exception ex) {
                return gson.toJson("An error occured: " + ex.getMessage());
            }
        });

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

    }

    public static void main(String[] args) {
        Server s = new Server(false);
        s.run();
    }
}
