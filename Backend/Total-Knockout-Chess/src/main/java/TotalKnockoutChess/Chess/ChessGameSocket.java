package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.ChessPiece;
import TotalKnockoutChess.Chess.Pieces.Coordinate;
import TotalKnockoutChess.Chess.Pieces.King;
import TotalKnockoutChess.Chess.Pieces.Pawn;
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
    private final boolean BACKEND_BOARD = true;

    private final String admin = "admin";

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
        boolean whiteCheckMated = cg.isWhiteCheckMated();
        boolean blackCheckMated = cg.isBlackCheckMated();

        String whitePlayer = cg.getWhitePlayer();
        String blackPlayer = cg.getBlackPlayer();

        boolean userIsBlackPlayer = false, userIsWhitePlayer = false;
        // Update booleans as appropriate
        if (username.equals(whitePlayer)) {
            userIsWhitePlayer = true;
        }
        if (username.equals(blackPlayer)) {
            userIsBlackPlayer = true;
        }

        // If message is a coordinate and the game is running
        if (message.length() == 2 && (!whiteCheckMated && !blackCheckMated)) {

            String whoseMove = cg.getWhoseMove();

            // FOR BACKEND TESTING
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
            sendUserMessage(username, getBoard(cg));
        }
        //If a user has won, update their statistics
        else if (messages[0].equals("GameType")) {
            UserStats us = getUserStats(username);

            if (us != null) {
                switch(messages[1]){
                    case "Chess":
                        // If user won Chess
                        if (messages[2].equals("win")) {
                            us.chessWin();
                            sendAllMessage(cg, "GameWonBy " + username);
                        }
                        // If user lost Chess
                        else if (messages[2].equals("loss")) {
                            us.chessLoss();
                        }
                        break;
                    case "ChessBoxing":
                        // If user won ChessBoxing
                        if (messages[2].equals("win")) {
                            us.chessBoxingWin();
                            sendAllMessage(cg, "GameWonBy " + username);
                        }
                        // If user lost ChessBoxing
                        else if (messages[2].equals("loss")) {
                            us.chessBoxingLoss();
                        }
                        break;
                }
                userStatsRepository.save(us);
                userStatsRepository.flush();
            }
        }
        // If user hit the "I Lost" button
        else if(message.equals("Lost")){
            sendUserMessage(username, "GameLoss");

            if(userIsWhitePlayer && blackPlayer != null){
                sendUserMessage(blackPlayer, "GameWon");
            }
            else if(userIsBlackPlayer && whitePlayer != null){
                sendUserMessage(whitePlayer, "GameWon");
            }
        }
        // If a pawn reached
        else if(messages[0].equals("Promote")){
            String whoseMove = cg.getWhoseMove();

            if ((whoseMove.equals("white") && username.equals(whitePlayer) || (whoseMove.equals("black") && username.equals(blackPlayer)))) {

                // FOR BACKEND TESTING
                if(BACKEND_BOARD){
                    cg.displayBoard();
                }

                Coordinate coordinate = Coordinate.fromString(messages[1]);
                ChessPiece promotionPiece = cg.getPromotionPiece(messages[2]);

                // Updates the tile at coordinate to promotionPiece
                cg.setPiece(coordinate, promotionPiece);

                // Update the turn
                if(userIsWhitePlayer){
                    cg.setWhoseMove("black");
                    sendAllMessage(cg,  "Player1Moved " + messages[2] + " " + messages[1] + " " +  messages[1]);
                }
                else if(userIsBlackPlayer){
                    cg.setWhoseMove("white");
                    sendAllMessage(cg,  "Player2Moved " + messages[2] + " " + messages[1] + " " +  messages[1]);

                }

                // FOR BACKEND TESTING
                if(BACKEND_BOARD){
                    cg.displayBoard();
                }

                // Update the database
                chessGameRepository.save(cg);
                chessGameRepository.flush();
            }
        }
        // Admin object to clear tiles in a game
        else if(messages[0].equals("Clear")){
            if(username.equals(admin)){
                cg.clearPiece(Coordinate.fromString(messages[1]));

                // Update the database
                chessGameRepository.save(cg);
                chessGameRepository.flush();

                // FOR BACKEND TESTING
                if(BACKEND_BOARD){
                    cg.displayBoard();
                }

                // Inform game participants that the tile was cleared
                sendAllMessage(cg, "An admin has cleared the tile on " + Coordinate.fromString(messages[1]));
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        // Find username and related chess game
        String username = sessionUsernameMap.get(session);
        ChessGame cg = findChessGame(chessGameRepository.findAll(), username);

        // remove the user connection information
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // If game with username was not found
        if(cg == null){
            return;
        }

        // If user that left was one of the players, delete the game from the database
        if ((cg.getWhitePlayer() != null && cg.getWhitePlayer().equals(username))
                || (cg.getBlackPlayer() != null && cg.getBlackPlayer().equals(username))) {

            // Send opponent that this player left the game
            if (cg.getWhitePlayer().equals(username)) {
                cg.setWhitePlayer(null);
                sendUserMessage(cg.getBlackPlayer(), "OpponentLeft");
            } else if (cg.getBlackPlayer().equals(username)) {
                cg.setBlackPlayer(null);
                sendUserMessage(cg.getWhitePlayer(), "OpponentLeft");
            }

            sendAllMessage(cg, "PlayerLeft " + username);


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
            if ((g.getWhitePlayer() != null && g.getWhitePlayer().equals(username))
                    || (g.getBlackPlayer() != null && g.getBlackPlayer().equals(username))
                    || g.getSpectators().contains(username)) {
                game = g;
            }
        }

        return game;
    }

    private void sendUserMessage(String username, String message) throws IOException {
        System.out.println("Attempt to send " + username + ": " + message);
        Session s = usernameSessionMap.get(username);

        // Ensure the user is in the session
        if(s != null){
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        }
    }

    private void sendAllPlayersMessage(ChessGame game, String message) throws IOException {
        String whitePlayer = game.getWhitePlayer();
        String blackPlayer = game.getBlackPlayer();

        // If whitePlayer is in lobby
        if(whitePlayer != null) {
            sendUserMessage(whitePlayer, message);
        }

        // If blackPlayer is in lobby
        if(blackPlayer != null) {
            sendUserMessage(blackPlayer, message);
        }
    }

    private void sendAllSpectators(ChessGame game, String message) throws IOException {
        List<String> spectators = game.getSpectators();

        // Iterate over all spectators in the game and send them the message
        for(String spectator : spectators){
            sendUserMessage(spectator, message);
        }
    }

    private void sendAllMessage(ChessGame game, String message) throws IOException{
        sendAllPlayersMessage(game, message);
        sendAllSpectators(game, message);
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

            // FOR BACKEND TESTING
            if(BACKEND_BOARD){
                cg.displayBoard();
            }

            // Sends this side's player the piece type on sent tile
            sendUserMessage(username, pieceOnSentTile.toString());

            // Sends this side's player the tile that is selected
            sendUserMessage(username, "TileSelected " + Coordinate.fromString(message));
        }
        // If this side's player has clicked on a one of their piece's previously and clicked on either an empty tile or an opponent's piece
        else {
            // Make sure game is updated
            chessGameRepository.save(cg);
            chessGameRepository.flush();

            boolean success = cg.makeMove(fromCoord, Coordinate.fromString(message));
            if (success) {

                // Check for promotion, then update whose move it is
                switch (sideColor) {
                    case "white":
                        // If the piece moved was a white pawn, and it moved to the promotion rank
                        if(cg.getTile(message).piece instanceof Pawn && Coordinate.fromString(message).y == 7) {
                            sendUserMessage(username, "Promotion " + message);
                            return;
                        }

                        cg.setWhoseMove("black");
                        cg.setWhitePreviousMove(cg.getTile(message).piece + " " + message);
                        break;
                    case "black":
                        // If the piece moved was a black pawn, and it moved to the promotion rank
                        if(cg.getTile(message).piece instanceof Pawn && Coordinate.fromString(message).y == 0){
                            sendUserMessage(username, "Promotion " + message);
                            return;
                        }

                        cg.setWhoseMove("white");
                        cg.setBlackPreviousMove(cg.getTile(message).piece + " " + message);
                        break;
                }
                cg.setWhiteFromSquare("");
                cg.setBlackFromSquare("");

                // Get chess game status
                boolean whiteCheckMated = cg.isWhiteCheckMated();
                boolean blackCheckMated = cg.isBlackCheckMated();

                // If white was checkMated
                if(whiteCheckMated){
                    sendAllMessage(cg, "GameWonBy " + cg.getBlackPlayer()); // Black won
                    // Ensure the database is updated
                    chessGameRepository.save(cg);
                    chessGameRepository.flush();
                    return;
                }
                // If black was checkMated
                else if(blackCheckMated){
                    sendAllMessage(cg, "GameWonBy " + cg.getWhitePlayer()); // White won
                    // Ensure the database is updated
                    chessGameRepository.save(cg);
                    chessGameRepository.flush();
                    return;
                }

                // Ensure the database is updated
                chessGameRepository.save(cg);
                chessGameRepository.flush();

                // FOR BACKEND TESTING
                if(BACKEND_BOARD){
                    cg.displayBoard();
                }

                // Tell players that a move has been made
                sendUserMessage(username, "userMoved");
                sendUserMessage(oppositePlayer, "opponentMoved " + fromCoord + " "
                        + message + " " + cg.getTile(message).piece);

                // Alternative way to tell players that a move has been made
                switch(sideColor){
                    case "white":
                        sendAllMessage(cg, "Player1Moved " + cg.getTile(message).piece + " "
                            + fromCoord + " " + Coordinate.fromString(message));
                        break;
                    case "black":
                        sendAllMessage(cg, "Player2Moved " + cg.getTile(message).piece + " "
                                + fromCoord + " " + Coordinate.fromString(message));
                        break;
                }
            } else {
                sendUserMessage(username, "invalidMove");
            }
        }
    }

    private String getBoard(ChessGame game) {
        String encodedBoard = "GameBoard ";
        ChessGameTile[][] board = game.getBoard();

        for (int row = 0; row < board.length; row++) {
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
