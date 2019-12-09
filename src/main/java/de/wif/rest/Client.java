package de.wif.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Client extends RESTCall implements ChatClient {

    /**
     * Creates an instance of RESTCall.
     *
     * @param base Base URL of the endpoint
     */
    public Client(URL base) {
        super(base);
    }

    @Override
    public String getHello() {
        // todo: Implementieren Sie hier den Aufruf, um den /hello Endpunkt abzurufen
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

    public static void main(String[] args) throws MalformedURLException {
        Client  c = new Client(new URL("http://localhost:8080"));
        System.out.println(c.getHello());

        System.out.println(c.sendMessage("Test", "Hallo Welt!"));

        System.out.println(c.getMessages());
    }
}
