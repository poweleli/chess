//package client;
//
//import com.google.gson.Gson;
//import exception.ResponseException;
//import websocket.messages.ServerMessage;
//
//import javax.websocket.*;
//import java.net.URI;
//
//public class WebSocketFacade extends Endpoint {
//    private final Gson gson = new Gson();
//    private final NotificationHandler notificationHandler = new NotificationHandler();
//    Session session;
//    String serverUrl;
//
//    public WebSocketFacade(String url) throws ResponseException {
//        try {
//            serverUrl = url.replace("http", "ws");
//            URI socketURI = new URI(url + "/ws");
//
//            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//            this.session = container.connectToServer(this, socketURI);
//
//            //set message handler
//            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
//                @Override
//                public void onMessage(String message) {
//                    ServerMessage notification = gson.fromJson(message, ServerMessage.class);
//                    switch (notification.getServerMessageType()) {
//                        case NOTIFICATION -> notificationHandler.notify(gson.fromJson());
//                    }
//                    notificationHandler.notify(notification);
//                }
//            });
//
//        } catch (Exception e) {
//            throw new ResponseException(500, e.getMessage());
//        }
//
//
//    }
//
//
//    @Override
//    public void onOpen(Session session, EndpointConfig endpointConfig) {
//
//    }
//}
