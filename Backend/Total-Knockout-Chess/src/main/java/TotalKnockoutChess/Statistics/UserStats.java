package TotalKnockoutChess.Statistics;

import TotalKnockoutChess.Users.User;

import javax.persistence.*;

@Entity
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "User")
    User user;

    private String username;

    private int chessWins;

    private int chessLosses;

    private int chessGames;

    private double chessWinRate;

    private int boxingWins;

    private int boxingLosses;

    private int boxingGames;

    private double boxingWinRate;

    private int chessBoxingWins;

    private int chessBoxingLosses;

    private int chessBoxingGames;

    private double chessBoxingWinRate;

    public UserStats(User user) {
        this.user = user;
        username = user.getUsername();
        chessWins = 0;
        chessLosses = 0;
        chessGames = 0;
        chessWinRate = 0.0;
        boxingWins = 0;
        boxingLosses = 0;
        boxingGames = 0;
        boxingWinRate = 0.0;
        chessBoxingWins = 0;
        chessBoxingLosses = 0;
        chessBoxingGames = 0;
        chessBoxingWinRate = 0.0;
    }

    public UserStats() {
    }

    public String getUsername() {
        return username;
    }

    public User getUser() {
        return user;
    }

    public void chessWin() {
        chessWins++;
        chessGames++;
        chessWinRate = (((double) chessWins) / ( (double) (chessGames))) * 100.0;
    }

    public void chessLoss() {
        chessLosses++;
        chessGames++;
        chessWinRate = (((double) chessWins) / ( (double) (chessGames))) * 100.0;
    }

    public void boxingWin() {
        boxingWins++;
        boxingGames++;
        boxingWinRate = (((double) boxingWins) / ( (double) (boxingGames))) * 100.0;
    }

    public void boxingLoss() {
        boxingLosses++;
        boxingGames++;
        boxingWinRate = (((double) boxingWins) / ( (double) (boxingGames))) * 100.0;
    }

    public void chessBoxingWin() {
        chessBoxingWins++;
        chessBoxingGames++;
        chessBoxingWinRate = (((double) chessBoxingWins) / ( (double) (chessBoxingGames))) * 100.0;
    }

    public void chessBoxingLoss() {
        chessBoxingLosses++;
        chessBoxingGames++;
        chessBoxingWinRate = (((double) chessBoxingWins) / ( (double) (chessBoxingGames))) * 100.0;

    }

    public int getChessWins() {
        return chessWins;
    }

    public int getChessLosses() {
        return chessLosses;
    }

    public int getChessGames() {
        return chessGames;
    }

    public int getBoxingWins() {
        return boxingWins;
    }

    public int getBoxingLosses() {
        return boxingLosses;
    }

    public int getBoxingGames() {
        return boxingGames;
    }

    public int getChessBoxingWins() {
        return chessBoxingWins;
    }

    public int getChessBoxingLosses() {
        return chessBoxingLosses;
    }

    public int getChessBoxingGames() {
        return chessBoxingGames;
    }

    public double getChessWinRate() {
        return chessWinRate;
    }

    public double getBoxingWinRate() {
        return boxingWinRate;
    }

    public double getChessBoxingWinRate() {
        return chessBoxingWinRate;
    }
}
