package io.manjo.chat.session;

import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class WebSocketSessions extends CopyOnWriteArraySet<String> {

}
