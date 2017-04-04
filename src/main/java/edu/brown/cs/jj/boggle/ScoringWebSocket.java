package edu.brown.cs.jj.boggle;

import java.io.IOException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

@WebSocket
public class ScoringWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;

  private static enum MESSAGE_TYPE {
    CONNECT,
    SCORE,
    UPDATE
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    // TODO Add the session to the queue
    // TODO Build the CONNECT message
    // TODO Send the CONNECT message
    session.getRemote().sendString("TODO");
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    // TODO Remove the session from the queue
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.SCORE.ordinal();

    // TODO Compute the player's score
    // TODO Send an UPDATE message to all users
  }
}
