package TotalKnockoutChess.Boxing;

import TotalKnockoutChess.Statistics.UserStats;
import TotalKnockoutChess.Statistics.UserStatsRepository;
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

@ServerEndpoint(value = "/websocket/boxing/{username}")
@Component
public class BoxingGameSocket {

    private static BoxingGameRepository boxingGameRepository;

    private static UserStatsRepository userStatsRepository;

    @Autowired
    public void setBoxingGameRepository(BoxingGameRepository boxingGameRepository) {
        this.boxingGameRepository = boxingGameRepository;
    }

    @Autowired
    public void setUserStatsRepository(UserStatsRepository userStatsRepository) {
        this.userStatsRepository = userStatsRepository;
    }

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(BoxingGameSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        logger.info("Entered into Open");
        System.out.println("Opened connection");

        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Entered into Message. Got Message: " + message);

        //Username of the user in this session
        String username = sessionUsernameMap.get(session);

        //Boxing game that the user in this session is in
        BoxingGame bg = findBoxingGame(boxingGameRepository.findAll(), username);

        String messages[] = message.split(" ");

        //If the received message is a move, update game accordingly
        if (message.equals("kick") || message.equals("block") || message.equals("jab")) {
            if (bg != null) {
                //If this session is with player 1 or player 2, update their move accordingly
                if (bg.getPlayer1().equals(username)) {
                    bg.setP1Move(message);
                } else if (bg.getPlayer2().equals(username)) {
                    bg.setP2Move(message);
                }

                String roundWinner = "";

                //If both of the users have a confirmed move
                if (!bg.getP1Move().equals("") && !bg.getP2Move().equals("")) {

                    //roundWinner is username of the round winner (possibly tie)
                    roundWinner = bg.getRoundWinner();

                    //Find the round winner, send information to the client, and update the boxing game accordingly
                    if (roundWinner.equals(bg.getPlayer1())) {
                        bg.dockLife(bg.getPlayer2());
                        usernameSessionMap.get(bg.getPlayer1()).getBasicRemote().sendText("RoundWin " + bg.getP2Move());
                        usernameSessionMap.get(bg.getPlayer2()).getBasicRemote().sendText("RoundLoss " + bg.getP1Move());
                        sendSpectatorsMessage(bg.getPlayer1(), "RoundOver Player1 " + bg.getP1Move() + " Player2 " + bg.getP2Move());
                    } else if (roundWinner.equals(bg.getPlayer2())) {
                        bg.dockLife(bg.getPlayer1());
                        usernameSessionMap.get(bg.getPlayer1()).getBasicRemote().sendText("RoundLoss " + bg.getP2Move());
                        usernameSessionMap.get(bg.getPlayer2()).getBasicRemote().sendText("RoundWin " + bg.getP1Move());
                        sendSpectatorsMessage(bg.getPlayer1(), "RoundOver Player2 " + bg.getP2Move() + " Player1 " + bg.getP1Move());
                    } else if (roundWinner.equals("tie")) {
                        usernameSessionMap.get(bg.getPlayer1()).getBasicRemote().sendText("Tie " + bg.getP2Move());
                        usernameSessionMap.get(bg.getPlayer2()).getBasicRemote().sendText("Tie " + bg.getP1Move());
                        sendSpectatorsMessage(bg.getPlayer1(), "RoundTie " + bg.getP1Move());
                    }
                    bg.clearMoves();
                }

                //If one of the players is out of lives, send information to the client and delete the boxing game from the repository
                if (bg.isGameOver()) {
                    String gameWinner = bg.getGameWinner();
                    if (gameWinner.equals(bg.getPlayer1())) {
                        usernameSessionMap.get(bg.getPlayer1()).getBasicRemote().sendText("GameWin");
                        usernameSessionMap.get(bg.getPlayer2()).getBasicRemote().sendText("GameLoss");
                        sendSpectatorsMessage(bg.getPlayer1(), "GameOver Player1");
                    } else if (gameWinner.equals(bg.getPlayer2())) {
                        usernameSessionMap.get(bg.getPlayer1()).getBasicRemote().sendText("GameLoss");
                        usernameSessionMap.get(bg.getPlayer2()).getBasicRemote().sendText("GameWin");
                        sendSpectatorsMessage(bg.getPlayer1(), "GameOver Player2");
                    }
                    boxingGameRepository.delete(bg);
                } else {
                    boxingGameRepository.save(bg);
                }
            }
        }
        //If the user leaves, send information to their opponent's client and delete the boxing game from the repository
        else if (message.equals("leave")) {
            if (bg != null) {
                if (bg.getPlayer1().equals(username)) {
                    usernameSessionMap.get(bg.getPlayer2()).getBasicRemote().sendText("OpponentLeft");
                } else if (bg.getPlayer2().equals(username)) {
                    usernameSessionMap.get(bg.getPlayer1()).getBasicRemote().sendText("OpponentLeft");
                }
                sendSpectatorsMessage(username, "PlayerLeft");
                boxingGameRepository.delete(bg);
            }
        }
        //If a user has won, update their statistics
        else if (messages[0].equals("GameType")) {
            UserStats us = getUserStats(username);
            if (us != null) {
                if (messages[1].equals("Boxing")) {
                    if (messages[2].equals("win")) {
                        us.boxingWin();
                    } else if (messages[2].equals("loss")) {
                        us.boxingLoss();
                    }
                } else if (messages[1].equals("ChessBoxing")) {
                    if (messages[2].equals("win")) {
                        us.chessBoxingWin();
                    } else if (messages[2].equals("loss")) {
                        us.chessBoxingLoss();
                    }
                }
                userStatsRepository.save(us);
                userStatsRepository.flush();
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into close");

        //Remove the session and username from the Maps
        String username = sessionUsernameMap.get(session);
        BoxingGame bg = findBoxingGame(boxingGameRepository.findAll(), username);
        if (bg != null) {
            if (bg.getSpectators().contains(username)) {
                bg.removeFromSpectators(username);
                boxingGameRepository.save(bg);
            }
            else if (bg.getPlayer1().equals(username) || bg.getPlayer2().equals(username)) {
                boxingGameRepository.delete(bg);
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

    //Helper method used to find the boxing game that the given user is in
    private BoxingGame findBoxingGame(List<BoxingGame> boxingGameList, String username) {
        for (BoxingGame bg : boxingGameList) {
            if (bg.contains(username)) {
                return bg;
            }
        }
        return null;
    }

    //Helper method used to send spectators a message
    private void sendSpectatorsMessage(String username, String message) throws IOException {
        BoxingGame bg = findBoxingGame(boxingGameRepository.findAll(), username);
        List<String> spectators = bg.getSpectators();
        for (String spec : spectators) {
            usernameSessionMap.get(spec).getBasicRemote().sendText(message);
        }
    }

    //Helper method used to get a UserStats object from their username
    private UserStats getUserStats(String username) {
        for (UserStats us : userStatsRepository.findAll()) {
            if (us.getUsername().equals(username)) {
                return us;
            }
        }
        return null;
    }
}