	package com.example.demo;
	
	import java.io.IOException;
	import java.util.List;
	import java.util.concurrent.CopyOnWriteArrayList;

	import org.springframework.context.annotation.Configuration;
	import org.springframework.web.socket.TextMessage;
	import org.springframework.web.socket.WebSocketSession;
	import org.springframework.web.socket.config.annotation.EnableWebSocket;
	import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
	import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
	import org.springframework.web.socket.handler.TextWebSocketHandler;

	
	@Configuration
	@EnableWebSocket
	public class WebSocketConfig implements WebSocketConfigurer {
	
	    @Override
	    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	        registry.addHandler(new ChatWebSocketHandler(), "/chat").setAllowedOrigins("*");
	    }
	
	    private static class ChatWebSocketHandler extends TextWebSocketHandler {
	        // Thread-safe list to store active sessions
	        private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	
	        @Override
	        public void afterConnectionEstablished(WebSocketSession session) {
	            // Add session to the list when a new connection is established
	            sessions.add(session);
	        }
	
	        @Override
	        public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
	            System.out.println("Received message: " + message.getPayload());
	            for (WebSocketSession s : sessions) {
	                if (s.isOpen()) {
	                    s.sendMessage(message);
	                }
	            }
	        }

	
	        @Override
	        public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
	            // Remove the session from the list when the connection is closed
	            sessions.remove(session);
	        }
	    }
	}
