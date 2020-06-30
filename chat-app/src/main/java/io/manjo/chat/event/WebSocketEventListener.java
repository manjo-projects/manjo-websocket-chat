package io.manjo.chat.event;

import io.manjo.chat.domain.ChatMessage;
import io.manjo.chat.session.WebSocketSessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static io.manjo.chat.domain.ChatMessage.MessageType.LEAVE;
import static org.springframework.messaging.simp.stomp.StompHeaderAccessor.wrap;

@SuppressWarnings({"unused"})
@Component
public class WebSocketEventListener {

    private static final Logger LOGGER   = LoggerFactory.getLogger(WebSocketEventListener.class);
    private static final String USERNAME = "username";

    @Autowired
    private SimpMessageSendingOperations template;

    @Autowired
    private WebSocketSessions sessions;

    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        sessions.add(getSessionId(event));
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = wrap(event.getMessage());
        String              username = (String) accessor.getSessionAttributes().get(USERNAME);


        if (username != null) {
            ChatMessage message = new ChatMessage();

            message.setType(LEAVE);
            message.setSender(username);

            template.convertAndSend("/queue/public", message);
        }

        sessions.remove(getSessionId(event));
    }

    private String getSessionId(AbstractSubProtocolEvent event) {
        return wrap(event.getMessage()).getSessionId();
    }

}
