package TotalKnockoutChess.Boxing;

import TotalKnockoutChess.Users.User;

import javax.persistence.*;

@Entity
public class BoxingGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player1")
    private User player1;

    private int p1Lives;

    @OneToOne
    @JoinColumn(name = "player2")
    private User player2;

    private int p2Lives;

    public BoxingGame(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        p1Lives = 3;
        p2Lives = 3;
    }

    public BoxingGame() {
    }

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
}
