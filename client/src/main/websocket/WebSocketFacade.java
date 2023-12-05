package websocket;

import server.ResponseException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class WebSocketFacade extends Endpoint {

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void help() throws ResponseException {

    }

    public void redrawChessBoard() throws ResponseException {

    }

    public void leave() throws ResponseException {

    }

    public void makeMove(String move) throws ResponseException {

    }

    public void resign() throws ResponseException {

    }

    public void highlightLegalMoves(String position) throws ResponseException {

    }

}
