package TotalKnockoutChess.Boxing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

@Controller
@ServerEndpoint(value = "/boxingGame/{username}")
public class BoxingGameSocket {

    private static BoxingGameRepository boxingGameRepository;

    @Autowired
    public void setBoxingGameRepository(BoxingGameRepository boxingGameRepository) {
        this.boxingGameRepository = boxingGameRepository;
    }

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(BoxingGameSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        logger.info("Entered into Open");

        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Entered into Message. Got Message: " + message);

        //Username of the user in this session
        String username = sessionUsernameMap.get(session);

        //Boxing game that the user in this sesssion is in
        BoxingGame bg = findBoxingGame(boxingGameRepository.findAll(), username);

        //If the received message is a move, update game accordingly
        if (message.equals("kick") || message.equals("block") || message.equals("jab")) {

            //If this session is with player 1 or player 2, update their move accordingly
            if (bg.getPlayer1().getUsername().equals(username)) {
                bg.setP1Move(message);
            }
            else if (bg.getPlayer2().getUsername().equals(username)) {
                bg.setP2Move(message);
            }

            String roundWinner = "";

            //If both of the users have a confirmed move
            if (!bg.getP1Move().equals("") && !bg.getP2Move().equals("")) {

                //roundWinner is username of the round winner (possibly tie)
                roundWinner = bg.getRoundWinner();

                //Find the round winner, send information to the client, and update the boxing game accordingly
                if (roundWinner.equals(bg.getPlayer1().getUsername())) {
                    bg.dockLife(bg.getPlayer2().getUsername());
                    usernameSessionMap.get(bg.getPlayer1().getUsername()).getBasicRemote().sendText("RoundWin " + bg.getP2Move());
                    usernameSessionMap.get(bg.getPlayer2().getUsername()).getBasicRemote().sendText("RoundLoss " + bg.getP1Move());
                }
                else if (roundWinner.equals(bg.getPlayer2().getUsername())) {
                    bg.dockLife(bg.getPlayer1().getUsername());
                    usernameSessionMap.get(bg.getPlayer1().getUsername()).getBasicRemote().sendText("RoundLoss " + bg.getP2Move());
                    usernameSessionMap.get(bg.getPlayer2().getUsername()).getBasicRemote().sendText("RoundWin " + bg.getP1Move());
                }
                else if (roundWinner.equals("tie")) {
                    usernameSessionMap.get(bg.getPlayer1().getUsername()).getBasicRemote().sendText("Tie" + bg.getP2Move());
                    usernameSessionMap.get(bg.getPlayer2().getUsername()).getBasicRemote().sendText("Tie" + bg.getP1Move());
                }
            }
            boxingGameRepository.flush();

            //If one of the players is out of lives, send information to the client and delete the boxing game from the repository
            if (bg.isGameOver()) {
                String gameWinner = bg.getGameWinner();
                if (gameWinner.equals(bg.getPlayer1().getUsername())) {
                    usernameSessionMap.get(bg.getPlayer1().getUsername()).getBasicRemote().sendText("GameWin");
                    usernameSessionMap.get(bg.getPlayer2().getUsername()).getBasicRemote().sendText("GameLoss");
                }
                else if (gameWinner.equals(bg.getPlayer2().getUsername())) {
                    usernameSessionMap.get(bg.getPlayer1().getUsername()).getBasicRemote().sendText("GameLoss");
                    usernameSessionMap.get(bg.getPlayer2().getUsername()).getBasicRemote().sendText("GameWin");
                }
                boxingGameRepository.delete(bg);
            }
        }
        //If the user leaves, send information to their opponent's client and delete the boxing game from the repository
        else if (message.equals("leave")) {
            if (bg.getPlayer1().getUsername().equals(username)) {
                usernameSessionMap.get(bg.getPlayer2().getUsername()).getBasicRemote().sendText("OpponentLeft");
            }
            else if (bg.getPlayer2().getUsername().equals(username)) {
                usernameSessionMap.get(bg.getPlayer1().getUsername()).getBasicRemote().sendText("OpponentLeft");
            }
            boxingGameRepository.delete(bg);
        }
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

    //Helper method used to find the boxing game that the given user is in
    private BoxingGame findBoxingGame(List<BoxingGame> boxingGameList, String username) {
        for (BoxingGame bg : boxingGameList) {
            if (bg.getPlayer1().getUsername().equals(username) || bg.getPlayer2().getUsername().equals(username)) {
                return bg;
            }
        }
        return null;
    }
}