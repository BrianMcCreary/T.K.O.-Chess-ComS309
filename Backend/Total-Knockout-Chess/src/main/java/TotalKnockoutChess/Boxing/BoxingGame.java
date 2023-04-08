package TotalKnockoutChess.Boxing;

import TotalKnockoutChess.Users.User;

import javax.persistence.*;

@Entity
public class BoxingGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player1Id")
    private User player1;

    //Amount of lives player1 has left, starts with 3
    private int p1Lives;

    //Move that player1 is doing
    private String p1Move;

    @OneToOne
    @JoinColumn(name = "player2Id")
    private User player2;

    //Amount of lives player2 has left, starts with 3
    private int p2Lives;

    //Move that player2 is doing
    private String p2Move;

    public BoxingGame(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        p1Lives = 3;
        p2Lives = 3;
        p1Move = "";
        p2Move = "";
    }

    public BoxingGame() {
    }

    //Method used to remove a life from the round losing player
    public void dockLife(String player) {
        if (player.equals("p1")) {
            p1Lives--;
        }
        else if (player.equals("p2")) {
            p2Lives--;
        }
    }

    public int getP1Lives() {
        return p1Lives;
    }

    public int getP2Lives() {
        return p2Lives;
    }

    public void setP1Lives(int lives) {
        p1Lives = lives;
    }

    public void setP2Lives(int lives) {
        p2Lives = lives;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setP1Move(String move) {
        p1Move = move;
    }

    public String getP1Move() {
        return p1Move;
    }

    public void setP2Move(String move) {
        p2Move = move;
    }

    public String getP2Move() {
        return p2Move;
    }

    //Method used to get the username of the round winner
    public String getRoundWinner() {
        //If player1's move is kick, look at player2's move and determine who won
        if (p1Move.equals("kick")) {
            if (p2Move.equals("kick")) {
                clearMoves();
                return "tie";
            }
            else if (p2Move.equals("block")) {
                clearMoves();
                return player1.getUsername();
            }
            else if (p2Move.equals("jab")) {
                clearMoves();
                return player2.getUsername();
            }
        }
        //If player1's move is block, look at player2's move and determine who won
        else if (p1Move.equals("block")) {
            if (p2Move.equals("kick")) {
                clearMoves();
                return player2.getUsername();
            }
            else if (p2Move.equals("block")) {
                clearMoves();
                return "tie";
            }
            else if (p2Move.equals("jab")) {
                clearMoves();
                return player1.getUsername();
            }
        }
        //If player1's move is jab, look at player2's move and determine who won
        else if (p1Move.equals("jab")) {
            if (p2Move.equals("kick")) {
                clearMoves();
                return player1.getUsername();
            }
            else if (p2Move.equals("block")) {
                clearMoves();
                return player2.getUsername();
            }
            else if (p2Move.equals("jab")) {
                clearMoves();
                return "tie";
            }
        }
        return null;
    }

    public void clearMoves() {
        p1Move = "";
        p2Move = "";
    }

    //Method for determining if one of the players has won
    public boolean isGameOver() {
        return p1Lives == 0 || p2Lives == 0;
    }

    //Method for determining who won the game
    public String getGameWinner() {
        if (p1Lives == 0) {
            return player2.getUsername();
        }
        else {
            return player1.getUsername();
        }
    }
}
