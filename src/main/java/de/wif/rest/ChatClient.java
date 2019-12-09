package de.wif.rest;

import java.util.List;

public interface ChatClient {

    String getHello();
    List<Message> getMessages();
    String sendMessage(String toUser, String messageText);

}
