package io.manjo.chat.controller;

import io.manjo.chat.domain.ChatMessage;
import io.manjo.chat.session.WebSocketSessions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpUserRegistry registry;

    @Autowired
    private WebSocketSessions sessions;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/queue/public")
    public ChatMessage sendMessage(@Payload ChatMessage message) {

        for (String session : sessions) {
            System.out.println("session: " + session);
        }

        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/queue/public")
    public ChatMessage addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor accessor) {
        accessor.getSessionAttributes().put("username", message.getSender());


        return message;
    }


}
