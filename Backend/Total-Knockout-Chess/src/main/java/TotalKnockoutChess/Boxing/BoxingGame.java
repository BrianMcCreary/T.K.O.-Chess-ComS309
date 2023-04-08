package TotalKnockoutChess.Boxing;

import TotalKnockoutChess.Users.User;

import javax.persistence.*;

@Entity
public class BoxingGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne
//    @JoinColumn(name = "player1")
    @Column(columnDefinition = "TEXT")
    private String player1;

    //Amount of lives player1 has left, starts with 3
    private int p1Lives;

    //Move that player1 is doing
    @Column(columnDefinition = "TEXT")
    private String p1Move;

//    @OneToOne
//    @JoinColumn(name = "player2")
    @Column(columnDefinition = "TEXT")
    private String player2;

    //Amount of lives player2 has left, starts with 3
    private int p2Lives;

    //Move that player2 is doing
    @Column(columnDefinition = "TEXT")
    private String p2Move;

    public BoxingGame(String player1, String player2) {
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
        if (player.equals(player1)) {
            p1Lives--;
        }
        else if (player.equals(player2)) {
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

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
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
                return "tie";
            }
            else if (p2Move.equals("block")) {
<<<<<<< HEAD
                return player1;
            }
            else if (p2Move.equals("jab")) {
=======
                clearMoves();
                return player1;
            }
            else if (p2Move.equals("jab")) {
                clearMoves();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
                return player2;
            }
        }
        //If player1's move is block, look at player2's move and determine who won
        else if (p1Move.equals("block")) {
            if (p2Move.equals("kick")) {
<<<<<<< HEAD
=======
                clearMoves();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
                return player2;
            }
            else if (p2Move.equals("block")) {
                return "tie";
            }
            else if (p2Move.equals("jab")) {
<<<<<<< HEAD
=======
                clearMoves();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
                return player1;
            }
        }
        //If player1's move is jab, look at player2's move and determine who won
        else if (p1Move.equals("jab")) {
            if (p2Move.equals("kick")) {
<<<<<<< HEAD
                return player1;
            }
            else if (p2Move.equals("block")) {
=======
                clearMoves();
                return player1;
            }
            else if (p2Move.equals("block")) {
                clearMoves();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
=======
>>>>>>> 5dbea53058508ad775a524fad450efb369315058
                return player2;
            }
            else if (p2Move.equals("jab")) {
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
            return player2;
        }
        else {
            return player1;
        }
    }
}
