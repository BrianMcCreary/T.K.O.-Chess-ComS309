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
                    lobbyRepository.flush();
                    sendOtherUsersMessage(username, "Player1 " + username);
                }
                else if (lobby.getPlayer2() == null) {
                    lobby.setPlayer2(username);
                    lobby.incrementUserCount();
                    lobbyRepository.flush();
                    sendOtherUsersMessage(username, "Player2 " + username);
                }
                else {
                    lobby.addToSpectators(username);
                    lobby.incrementUserCount();
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


//        LobbyController controller = new LobbyController();

        // If the user is not in a lobby yet
//        if(lobby == null){
        // TODO
        // When a user wants to host a lobby
//        if(message.equals("host")){
//            String lobbyCode = controller.hostLobby(username);

//        }
        // When a user wants to join the lobby
//        else if(message.equals("join")){
//
//        }
//        }



    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into close");

        //Remove the session and username from the Maps
        String username = sessionUsernameMap.get(session);
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
//        User usr = null;
//
//        // Find user in database with the given username
//        for(User u : userRepository.findAll()){
//            if(u.getUsername().equals(username)){
//                usr = u;
//                break;
//            }
//        }
//
//        // If user is not found in database, return null
//        if(usr == null){ return null; }
//
//        // Search for lobby that contains the usr object
//        for (Lobby l : lobbies) {
//            if (l.contains(usr)) {
//                return l;
//            }
//        }
//        return null;

        for (Lobby l : lobbies) {
            if (l.contains(username)) {
                return l;
            }
        }
        return null;

//        for(Lobby l : lobbyRepository.findAll()) {
//            List<String> spectators = l.getSpectators();
//            for (String s : spectators) {
//                if (s.equals(username)) {
//                    return l;
//                }
//            }
//            if (l.getPlayer1() != null) {
//                if (l.getPlayer1().equals(username)) {
//                    return l;
//                }
//            }
//            if (l.getPlayer2() != null) {
//                if (l.getPlayer2().equals(username)) {
//                    return l;
//                }
//            }
//        }
//        return null;
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
}