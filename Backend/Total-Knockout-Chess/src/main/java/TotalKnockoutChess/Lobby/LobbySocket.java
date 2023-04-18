package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value = "/websocket/lobby/{username}/{joinOrHost}/{lobbyCode}")
@Component
public class LobbySocket {

    private static LobbyRepository lobbyRepository;
    private static UserRepository userRepository;

    @Autowired
    public void setLobbyRepository(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("joinOrHost") String joinOrHost, @PathParam("lobbyCode") Long lobbyCode) throws IOException {
        logger.info("Entered into Open");
        System.out.println("Opened connection");        //Don't need to know who the owner is, just put users into spectators
                                                        //when they join the lobby
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        if (joinOrHost.equals("host")) {        //If hosting, create lobby and generate code.
            Lobby lobby = new Lobby(username);
            lobby.setCode(lobby.generateLobbyCode(lobbyRepository.findAll()));
            lobbyRepository.save(lobby);
        }
        else if (joinOrHost.equals("join")) {
            Lobby lobby = findLobbyWithCode(lobbyCode);
            if (lobby != null) {
//                User user = getUser(username);
                if (lobby.getPlayer1() == null) {
                    lobby.setPlayer1(username);
                    lobby.incrementUserCount();
                    lobbyRepository.save(lobby);
                    lobbyRepository.flush();
                    sendOtherUsersMessage(username, "Player1 " + username);
                }
                else if (lobby.getPlayer2() == null) {
                    lobby.setPlayer2(username);
                    lobby.incrementUserCount();
                    lobbyRepository.save(lobby);
                    lobbyRepository.flush();
                    sendOtherUsersMessage(username, "Player2 " + username);
                }
                else {
                    lobby.addToSpectators(username);
                    lobby.incrementUserCount();
                    lobbyRepository.save(lobby);
                    lobbyRepository.flush();
                    sendOtherUsersMessage(username, "Spectator " + username);
                }
            }
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Entered into Message. Got Message: " + message);
        //Username of the user in this session
        String username = sessionUsernameMap.get(session);
        String[] messages = message.split(" ");

        if (message.equals("Ready")) {
            Lobby l = findLobbyWithUsername(lobbyRepository.findAll(), username);
            if (l.getPlayer1() != null) {
                if (l.getPlayer1().equals(username)) {
                    l.setPlayer1Ready(true);
                }
            }
            else if (l.getPlayer2() != null) {
                if (l.getPlayer2().equals(username)) {
                    l.setPlayer2Ready(true);
                }
            }
            //TODO Start game if both players ready

        }
        else if (message.equals("UnReady")) {
            Lobby l = findLobbyWithUsername(lobbyRepository.findAll(), username);
            if (l.getPlayer1() != null) {
                if (l.getPlayer1().equals(username)) {
                    l.setPlayer1Ready(false);
                }
            }
            else if (l.getPlayer2() != null) {
                if (l.getPlayer2().equals(username)) {
                    l.setPlayer2Ready(false);
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into close");

        //Remove the session and username from the Maps
        String username = sessionUsernameMap.get(session);

        Lobby lobby = findLobbyWithUsername(lobbyRepository.findAll(), username);

        if (lobby != null) {
            if (lobby.getOwner().equals(username)) {
                sendOtherUsersMessage(username, "HostLeft");
                lobbyRepository.delete(lobby);
            }
            else {
                String who = null;
                if (lobby.getPlayer1().equals(username)) {
                    who = "Player1 ";
                }
                else if (lobby.getPlayer2().equals(username)) {
                    who = "Player2 ";
                }
                else {
                    who = "Spectator ";
                }
                sendOtherUsersMessage(username, "PlayerLeft " + who + username);

                if (lobby.getPlayer1().equals(username)) {
                    lobby.setPlayer1(null);
                }
                else if (lobby.getPlayer2().equals(username)) {
                    lobby.setPlayer2(null);
                }
                else if (lobby.getSpectators().contains(username)) {
                    lobby.removeSpectator(username);
                }
                lobbyRepository.save(lobby);
                lobbyRepository.flush();
            }
        }

        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }

    //Helper method used to find the lobby that the given user is in
    private Lobby findLobbyWithUsername(List<Lobby> lobbies, String username) {
        for (Lobby l : lobbies) {
            if (l.contains(username)) {
                return l;
            }
        }
        return null;
    }

    //Helper method used to find the lobby that the given code correlates to
    private Lobby findLobbyWithCode(Long code) {
        for (Lobby l : lobbyRepository.findAll()) {
            if (l.getCode().equals(code)) {
                return l;
            }
        }
        return null;
    }

    //Helper method used to find a User given their username
    private User getUser(String username) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    //Helper method to send all other users a message
    private void sendOtherUsersMessage(String username, String message) throws IOException {
        Lobby lobby = findLobbyWithUsername(lobbyRepository.findAll(), username);
        if (lobby != null) {
            if (lobby.getPlayer1() != null) {
                if (!lobby.getPlayer1().equals(username)) {
                    usernameSessionMap.get(lobby.getPlayer1()).getBasicRemote().sendText(message);
                }
            }
            if (lobby.getPlayer2() != null) {
                if (!lobby.getPlayer2().equals(username)) {
                    usernameSessionMap.get(lobby.getPlayer2()).getBasicRemote().sendText(message);
                }
            }
            List<String> spectators = lobby.getSpectators();
            for (String u : spectators) {
                if (!u.equals(username)) {
                    usernameSessionMap.get(u).getBasicRemote().sendText(message);
                }
            }
        }
    }

    private void sendAllUsersMessage(String username, String message) throws IOException {
        Lobby lobby = findLobbyWithUsername(lobbyRepository.findAll(), username);
        if (lobby != null) {
            if (lobby.getPlayer1() != null) {
                usernameSessionMap.get(lobby.getPlayer1()).getBasicRemote().sendText(message);
            }
            if (lobby.getPlayer2() != null) {
                usernameSessionMap.get(lobby.getPlayer2()).getBasicRemote().sendText(message);
            }
            for (String user : lobby.getSpectators()) {
                usernameSessionMap.get(user).getBasicRemote().sendText(message);
            }
        }
    }
}