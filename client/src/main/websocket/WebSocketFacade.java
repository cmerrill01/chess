package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deserializers.ChessGameAdapter;
import server.ResponseException;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = null;
                    if (message.toLowerCase().contains("load_game")) {
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
                        notification = builder.create().fromJson(message, LoadGameMessage.class);
                    } else if (message.toLowerCase().contains("error")) {
                        notification = new Gson().fromJson(message, ErrorMessage.class);
                    } else if (message.toLowerCase().contains("notification")) {
                        notification = new Gson().fromJson(message, NotificationMessage.class);
                    }
                    notificationHandler.notify(notification);
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void joinPlayer(int gameID, ChessGame.TeamColor playerColor) {

    }

    public void joinObserver(int gameID) {

    }

    public void help() throws ResponseException {

    }

    public void redrawChessBoard() throws ResponseException {

    }

    public void leave(int gameID) throws ResponseException {

    }

    public void makeMove(int gameID, String move) throws ResponseException {

    }

    public void resign(int gameID) throws ResponseException {

    }

    public void highlightLegalMoves(String position) throws ResponseException {

    }

}
