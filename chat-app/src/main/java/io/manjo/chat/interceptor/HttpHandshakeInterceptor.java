package io.manjo.chat.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    private static final String SESSION_ID = "simpSessionId";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler handler, Map<String, Object> attributes
    ) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession();
            System.out.println(session.getId() + "!!!!!!!!!");
            attributes.put(SESSION_ID, session.getId());
//            UUID
        }

        handler = new ExceptionWebSocketHandlerDecorator(((WebSocketHandlerDecorator)handler).getDelegate()) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                System.out.println("ololo!");
                super.afterConnectionEstablished(session);
            }
        };

        System.out.println(handler.getClass().getName());

        attributes.forEach((s, o) -> {
            System.out.println(s+ " - " +o);
        });

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception
    ) {

    }

}
