package TotalKnockoutChess.Statistics;

import TotalKnockoutChess.Users.User;

import javax.persistence.*;

@Entity
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "User")
    User user;

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

    public User getUser() {
        return user;
    }

    public void chessWin() {
        chessWins++;
        chessGames++;
        chessWinRate = (((double) chessWins) / ( (double) (chessWins + chessLosses))) * 100.0;
    }

    public void chessLoss() {
        chessLosses++;
        chessGames++;
        if (chessWins > 0) {
            chessWinRate = (((double) chessWins) / ( (double) (chessWins + chessLosses))) * 100.0;
        }
        else {
            chessWinRate = 0.0;
        }
    }

    public void boxingWin() {
        boxingWins++;
        boxingGames++;
        boxingWinRate = (((double) boxingWins) / ( (double) (boxingWins + boxingLosses))) * 100.0;
    }

    public void boxingLoss() {
        boxingLosses++;
        boxingGames++;
        if (boxingWins > 0) {
            boxingWinRate = (((double) boxingWins) / ( (double) (boxingWins + boxingLosses))) * 100.0;
        }
        else {
            boxingWinRate = 0.0;
        }
    }

    public void chessBoxingWin() {
        chessBoxingWins++;
        chessBoxingGames++;
        chessBoxingWinRate = (((double) chessBoxingWins) / ( (double) (chessBoxingWins + chessBoxingLosses))) * 100.0;
    }

    public void chessBoxingLoss() {
        chessBoxingLosses++;
        chessBoxingGames++;
        if (chessBoxingWins > 0) {
            chessBoxingWinRate = (((double) chessBoxingWins) / ( (double) (chessBoxingWins + chessBoxingLosses))) * 100.0;
        }
        else {
            chessBoxingWinRate = 0.0;
        }
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
