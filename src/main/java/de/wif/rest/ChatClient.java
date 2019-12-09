package de.wif.rest;

import java.util.List;

public interface ChatClient {

    String getHello();
    List<Message> getMessages();
    String getMessageCount();
    String sendMessage(String toUser, String messageText);

}
