package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.ChessPiece;
import TotalKnockoutChess.Chess.Pieces.Coordinate;
import TotalKnockoutChess.Chess.Pieces.King;
import TotalKnockoutChess.Statistics.UserStats;
import TotalKnockoutChess.Statistics.UserStatsRepository;
import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Controller
@ServerEndpoint("/chess/{username}")
public class ChessGameSocket {

    private static ChessGameRepository chessGameRepository;
    private static UserRepository userRepository;
    private static UserStatsRepository userStatsRepository;

    // Variable to toggle backend output of the board. Used for testing
    private final boolean BACKEND_BOARD = false;

    @Autowired
    public void setChessGameRepository(ChessGameRepository chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    @Autowired
    public void setUserStatsRepository(UserStatsRepository userStatsRepository) {
        this.userStatsRepository = userStatsRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Store all socket session and their corresponding username.
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(ChessGameSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username)
            throws IOException {

        logger.info("Entered into Open");

        // store connecting user information
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        // Handle new messages
        logger.info("Entered into Message: Got Message:" + message);
        String username = sessionUsernameMap.get(session);

        String[] messages = message.split(" ");

        //Chess game that the user in this session is in
        ChessGame cg = findChessGame(chessGameRepository.findAll(), username);

        // Chess game status
        boolean running = cg.isRunning();

        String whitePlayer = cg.getWhitePlayer();
        String blackPlayer = cg.getBlackPlayer();

        boolean userIsBlackPlayer = false, userIsWhitePlayer = false;
        // Update booleans as appropriate
        if (whitePlayer != null && username.equals(whitePlayer)) {
            userIsWhitePlayer = true;
        }
        if (blackPlayer != null && username.equals(blackPlayer)) {
            userIsBlackPlayer = true;
        }


        // If message is a coordinate && and game is running
        if (message.length() == 2 && running) {

            String whoseMove = cg.getWhoseMove();

            // TODO FOR BACKEND TESTING
            if(BACKEND_BOARD){
                cg.displayBoard();
            }
            switch (whoseMove) {
                // If it is white's turn
                case "white":
                    if (userIsWhitePlayer) {
                        executePlayerTurn(cg, username, message, "white", blackPlayer);
                    } else if (userIsBlackPlayer) {
                        // TODO Return players available moves
                    }
                    break;
                // If it is black's turn
                case "black":
                    if (userIsBlackPlayer) {
                        executePlayerTurn(cg, username, message, "black", whitePlayer);
                    } else if (userIsWhitePlayer) {
                        // TODO Return players available moves
                    }
                    break;
            }
        }
        // If message requests the board
        else if (message.equals("GetBoard")) {
            sendPlayerMessage(username, getBoard(cg));
        }
        //If a user has won, update their statistics
        else if (messages[0].equals("GameType") && running) {
            UserStats us = getUserStats(username);

            if (us != null) {
                switch(messages[1]){
                    case "Chess":
                        // If user won Chess
                        if (messages[2].equals("win")) {
                            us.chessWin();
                            cg.setRunning(false);
                        }
                        // If user lost Chess
                        else if (messages[2].equals("loss")) {
                            us.chessLoss();
                            cg.setRunning(false);
                        }
                        break;
                    case "ChessBoxing":
                        // If user won ChessBoxing
                        if (messages[2].equals("win")) {
                            us.chessBoxingWin();
                            cg.setRunning(false);
                        }
                        // If user lost ChessBoxing
                        else if (messages[2].equals("loss")) {
                            us.chessBoxingLoss();
                            cg.setRunning(false);
                        }
                        break;
                }
                userStatsRepository.save(us);
                userStatsRepository.flush();
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        ChessGame cg = findChessGame(chessGameRepository.findAll(), username);

        // If user that left was one of the players, delete the game from the database
        if ((cg.getWhitePlayer() != null && cg.getWhitePlayer().equals(username))
                || (cg.getBlackPlayer() != null && cg.getBlackPlayer().equals(username))) {
            chessGameRepository.delete(cg);
            chessGameRepository.flush();
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }

    private ChessGame findChessGame(List<ChessGame> all, String username) {
        ChessGame game = null;

        // Search through repository for chess game with username in it
        for (ChessGame g : all) {
            if (g.getWhitePlayer() != null && g.getWhitePlayer().equals(username)
                    || g.getBlackPlayer() != null && g.getBlackPlayer().equals(username)
                    || g.getSpectators().contains(username)) {
                game = g;
            }
        }

        return game;
    }

    private void sendPlayerMessage(String username, String message) throws IOException {
        usernameSessionMap.get(username).getBasicRemote().sendText(message);
    }

    private void executePlayerTurn(ChessGame cg, String username, String message, String sideColor, String oppositePlayer) throws IOException {

        // Coordinate of the piece to move
        Coordinate fromCoord = null;
        if (sideColor.equals("white")) {
            fromCoord = Coordinate.fromString(cg.getWhiteFromSquare());
        } else if (sideColor.equals("black")) {
            fromCoord = Coordinate.fromString(cg.getBlackFromSquare());
        }
        chessGameRepository.save(cg);
        chessGameRepository.flush();

        // The piece that is on the tile sent in the message
        ChessPiece pieceOnSentTile = cg.getTile(message).piece;


        // If the user has not selected one of their pieces and do not select one this time, return
        if (fromCoord == null && !pieceOnSentTile.color.equals(sideColor)) {
            return;
        }


        // If this side's player clicked on one of their piece's
        if (pieceOnSentTile.color.equals(sideColor)) {

            // Update this side's player's from square
            switch (sideColor) {
                case "white":
                    cg.setWhiteFromSquare(message);
                    break;
                case "black":
                    cg.setBlackFromSquare(message);
                    break;
            }

            // Update the database
            chessGameRepository.save(cg);
            chessGameRepository.flush();

            // TODO FOR BACKEND TESTING
            if(BACKEND_BOARD){
                cg.displayBoard();
            }

            // Sends this side's player the piece type on sent tile
            sendPlayerMessage(username, pieceOnSentTile.toString());
        }
        // If this side's player has clicked on a one of their piece's previously and clicked on either an empty tile or an opponent's piece
        else {
            boolean success = cg.makeMove(fromCoord, Coordinate.fromString(message));
            if (success) {
                // Update whose move it is
                switch (sideColor) {
                    case "white":
                        cg.setWhoseMove("black");
                        break;
                    case "black":
                        cg.setWhoseMove("white");
                        break;
                }
                cg.setWhiteFromSquare("");
                cg.setBlackFromSquare("");

                // Ensure the database is updated
                chessGameRepository.save(cg);
                chessGameRepository.flush();

                // TODO FOR BACKEND TESTING
                if(BACKEND_BOARD){
                    cg.displayBoard();
                }

                // Tell players that a move has been made
                sendPlayerMessage(username, "userMoved");
                sendPlayerMessage(oppositePlayer, "opponentMoved " + fromCoord.toString() + " "
                        + message + " " + cg.getTile(message).piece.toString());
            } else {
                sendPlayerMessage(username, "invalidMove");
            }
        }
    }

    private String getBoard(ChessGame game) {
        String encodedBoard = "GameBoard ";
        ChessGameTile[][] board = game.getBoard();

        for (int row = board.length - 1; row >= 0; row--) {
            for (int col = 0; col < board[row].length; col++) {
                // Add current piece to the encodedBoard
                ChessPiece piece = board[col][row].piece;

                // '.' represents end of a column
                encodedBoard += piece.toString() + ".";
            }

            // Add # to signify end of row.
            encodedBoard += "#";
        }

        // Returns the chess board with the format "ChessBoard A1Piece.B1Piece...H1Piece#A2Piece.B2Piece.......H8Piece"
        return encodedBoard;
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
