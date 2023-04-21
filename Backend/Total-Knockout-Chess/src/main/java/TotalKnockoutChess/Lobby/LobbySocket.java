package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Boxing.BoxingGame;
import TotalKnockoutChess.Boxing.BoxingGameRepository;
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

    private static BoxingGameRepository boxingGameRepository;

    @Autowired
    public void setLobbyRepository(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setBoxingGameRepository(BoxingGameRepository boxingGameRepository) {
        this.boxingGameRepository = boxingGameRepository;
    }

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("joinOrHost") String joinOrHost, @PathParam("lobbyCode") Long lobbyCode) throws IOException {
        logger.info("Entered into Open");
        System.out.println("Opened connection");

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
                lobby.addToSpectators(username);
                lobby.incrementUserCount();
                lobbyRepository.save(lobby);
                lobbyRepository.flush();
                sendOtherUsersMessage(username, "Spectator " + username);
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
            boolean wasNotP1 = true;
            if (l != null) {
                if (l.getPlayer1() != null) {
                    if (l.getPlayer1().equals(username)) {
                        l.setPlayer1Ready(true);
                        wasNotP1 = false;
                        sendAllUsersMessage(username, "Ready " + username);
                    }
                }
                if (l.getPlayer2() != null && wasNotP1) {
                    if (l.getPlayer2().equals(username)) {
                        l.setPlayer2Ready(true);
                        sendAllUsersMessage(username, "Ready " + username);
                    }
                }
                if (l.getPlayer1Ready() && l.getPlayer2Ready()) {
                    usernameSessionMap.get(l.getOwner()).getBasicRemote().sendText("CanStart");
                }
                lobbyRepository.save(l);
                lobbyRepository.flush();
            }
        }
        else if (message.equals("UnReady")) {
            Lobby l = findLobbyWithUsername(lobbyRepository.findAll(), username);
            if (l != null) {
                boolean sendCantStart = l.getPlayer1Ready() && l.getPlayer2Ready();
                if (l.getPlayer1() != null) {
                    if (l.getPlayer1().equals(username)) {
                        l.setPlayer1Ready(false);
                        sendAllUsersMessage(username, "UnReady " + username);
                    }
                } else if (l.getPlayer2() != null) {
                    if (l.getPlayer2().equals(username)) {
                        l.setPlayer2Ready(false);
                        sendAllUsersMessage(username, "UnReady " + username);
                    }
                }
                if (sendCantStart) {
                    usernameSessionMap.get(l.getOwner()).getBasicRemote().sendText("CannotStart");
                }
                lobbyRepository.save(l);
                lobbyRepository.flush();
            }
        }
        else if (messages[0].equals("Start")) {
            Lobby l = findLobbyWithUsername(lobbyRepository.findAll(), username);
            if (l != null) {
                if (messages[1].equals("Boxing")) {
                    sendAllUsersMessage(username, "StartGame Player1 " + l.getPlayer1() + " Player2 " + l.getPlayer2());
                    BoxingGame bg = new BoxingGame(l.getPlayer1(), l.getPlayer2(), l.getSpectators());
                    boxingGameRepository.save(bg);
                    lobbyRepository.delete(l);
                } else if (messages[1].equals("Chess")) {

                } else if (messages[1].equals("ChessBoxing")) {

                }
            }
        }
        else if (message.equals("SwitchToP1")) {
            Lobby l = findLobbyWithUsername(lobbyRepository.findAll(), username);
            if (l != null) {
                String prev = "";
                if (l.getPlayer1() == null) {
                    if (l.getPlayer2() != null) {
                        if (l.getPlayer2().equals(username)) {
                            prev = "Player2 ";
                            l.setPlayer2(null);
                            sendAllUsersMessage(username, "UnReady " + username);
                            l.setPlayer2Ready(false);
                            l.setPlayer1(username);
                            sendAllUsersMessage(username, "Switch " + prev + "Player1 " + username);
                        }
                    }

                    if (l.getSpectators().contains(username)) {
                        prev = "Spectator ";
                        l.removeSpectator(username);
                        l.setPlayer1(username);
                        sendAllUsersMessage(username, "Switch " + prev + "Player1 " + username);
                    }
                }
                lobbyRepository.save(l);
                lobbyRepository.flush();
            }
        }
        else if (message.equals("SwitchToP2")) {
            Lobby l = findLobbyWithUsername(lobbyRepository.findAll(), username);
            if (l != null) {
                String prev = "";
                if (l.getPlayer2() == null) {
                    if (l.getPlayer1() != null) {
                        if (l.getPlayer1().equals(username)) {
                            prev = "Player1 ";
                            l.setPlayer1(null);
                            sendAllUsersMessage(username, "UnReady " + username);
                            l.setPlayer1Ready(false);
                            l.setPlayer2(username);
                            sendAllUsersMessage(username, "Switch " + prev + "Player2 " + username);
                        }
                    }
                    if (l.getSpectators().contains(username)) {
                        prev = "Spectator ";
                        l.removeSpectator(username);
                        l.setPlayer2(username);
                        sendAllUsersMessage(username, "Switch " + prev + "Player2 " + username);
                    }
                }
                lobbyRepository.save(l);
                lobbyRepository.flush();
            }
        }
        else if (message.equals("SwitchToSpectate")) {
            Lobby l = findLobbyWithUsername(lobbyRepository.findAll(), username);
            if (l != null) {
                String prev = "";
                boolean wasNotP1 = true;
                if (l.getPlayer1() != null) {
                    if (l.getPlayer1().equals(username)) {
                        wasNotP1 = false;
                        prev = "Player1 ";
                        l.setPlayer1(null);
                        if (l.getPlayer1Ready() && l.getPlayer2Ready()) {
                            usernameSessionMap.get(l.getOwner()).getBasicRemote().sendText("CannotStart");
                        }
                        sendAllUsersMessage(username, "UnReady " + username);
                        sendAllUsersMessage(username, "Switch " + prev + "Spectator " + username);
                        l.setPlayer1Ready(false);
                        l.addToSpectators(username);
                    }
                }
                if (wasNotP1) {
                    if (l.getPlayer2() != null) {
                        if (l.getPlayer2().equals(username)) {
                            prev = "Player2 ";
                            l.setPlayer2(null);
                            if (l.getPlayer1Ready() && l.getPlayer2Ready()) {
                                usernameSessionMap.get(l.getOwner()).getBasicRemote().sendText("CannotStart");
                            }
                            sendAllUsersMessage(username, "UnReady " + username);
                            sendAllUsersMessage(username, "Switch " + prev + "Spectator " + username);
                            l.setPlayer2Ready(false);
                            l.addToSpectators(username);
                        }
                    }
                }
                lobbyRepository.save(l);
                lobbyRepository.flush();
            }
        }
        else if (messages[0].equals("Kick")) {
            String usernameKicked = messages[1];
            usernameSessionMap.get(usernameKicked).getBasicRemote().sendText("Kicked");
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
                String who = "";
                if (lobby.getPlayer1() != null) {
                    if (lobby.getPlayer1().equals(username)) {
                        who = "Player1 ";
                        lobby.setPlayer1(null);
                        if (lobby.getPlayer1Ready() && lobby.getPlayer2Ready()) {
                            usernameSessionMap.get(lobby.getOwner()).getBasicRemote().sendText("CannotStart");
                        }
                        if (lobby.getPlayer1Ready()) {
                            lobby.setPlayer1Ready(false);
                            sendOtherUsersMessage(username, "Unready " + username);
                        }
                    }
                }
                if (lobby.getPlayer2() != null) {
                    if (lobby.getPlayer2().equals(username)) {
                        who = "Player2 ";
                        lobby.setPlayer2(null);
                        if (lobby.getPlayer1Ready() && lobby.getPlayer2Ready()) {
                            usernameSessionMap.get(lobby.getOwner()).getBasicRemote().sendText("CannotStart");
                        }
                        if (lobby.getPlayer2Ready()) {
                            lobby.setPlayer2Ready(false);
                            sendOtherUsersMessage(username, "Unready " + username);
                        }
                    }
                }
                if (lobby.getSpectators().contains(username)) {
                    who = "Spectator ";
                    lobby.removeSpectator(username);
                }
                sendOtherUsersMessage(username, "PlayerLeft " + who + username);
                lobby.decrementUserCount();
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
            List<String> spectators = lobby.getSpectators();
            for (String u : spectators) {
                usernameSessionMap.get(u).getBasicRemote().sendText(message);
            }
        }
    }

    private void sendSpectatorsMessage(String username, String message) throws IOException {
        Lobby lobby = findLobbyWithUsername(lobbyRepository.findAll(), username);
        if (lobby != null) {
            for (String u : lobby.getSpectators()) {
                usernameSessionMap.get(u).getBasicRemote().sendText(message);
            }
        }
    }

    private void sendPlayersMessage(String username, String message) throws IOException {
        Lobby lobby = findLobbyWithUsername(lobbyRepository.findAll(), username);
        if (lobby != null) {
            if (lobby.getPlayer1() != null) {
                usernameSessionMap.get(lobby.getPlayer1()).getBasicRemote().sendText(message);
            }
            if (lobby.getPlayer2() != null) {
                usernameSessionMap.get(lobby.getPlayer2()).getBasicRemote().sendText(message);
            }
        }
    }
}