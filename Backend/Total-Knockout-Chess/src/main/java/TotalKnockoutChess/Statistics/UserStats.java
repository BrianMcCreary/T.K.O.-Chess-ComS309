package TotalKnockoutChess.Statistics;

import TotalKnockoutChess.Users.User;

import javax.persistence.*;

@Entity
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "UserStats")
    User user;

    private int chessWins;

    private int chessLosses;

    private double chessWinRate;

    private int boxingWins;

    private int boxingLosses;

    private double boxingWinRate;

    private int chessBoxingWins;

    private int chessBoxingLosses;

    private double chessBoxingWinRate;

    public UserStats(User user) {
        this.user = user;
        chessWins = 0;
        chessLosses = 0;
        chessWinRate = 0.0;
        boxingWins = 0;
        boxingLosses = 0;
        boxingWinRate = 0.0;
        chessBoxingWins = 0;
        chessBoxingLosses = 0;
        chessBoxingWinRate = 0.0;
    }

    public UserStats() {
    }

    public User getUser() {
        return user;
    }

    public void chessWin() {
        chessWins++;
        chessWinRate = (((double) chessWins) / ( (double) (chessWins + chessLosses))) * 100.0;
    }

    public void chessLoss() {
        chessLosses++;
        if (chessWins > 0) {
            chessWinRate = (((double) chessWins) / ( (double) (chessWins + chessLosses))) * 100.0;
        }
        else {
            chessWinRate = 0.0;
        }
    }

    public void boxingWin() {
        boxingWins++;
        boxingWinRate = (((double) boxingWins) / ( (double) (boxingWins + boxingLosses))) * 100.0;
    }

    public void boxingLoss() {
        boxingLosses++;
        if (boxingWins > 0) {
            boxingWinRate = (((double) boxingWins) / ( (double) (boxingWins + boxingLosses))) * 100.0;
        }
        else {
            boxingWinRate = 0.0;
        }
    }

    public void chessBoxingWin() {
        chessBoxingWins++;
        chessBoxingWinRate = (((double) chessBoxingWins) / ( (double) (chessBoxingWins + chessBoxingLosses))) * 100.0;
    }

    public void chessBoxingLoss() {
        chessBoxingLosses++;
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

    public int getBoxingWins() {
        return boxingWins;
    }

    public int getBoxingLosses() {
        return boxingLosses;
    }

    public int getChessBoxingWins() {
        return chessBoxingWins;
    }

    public int getChessBoxingLosses() {
        return chessBoxingLosses;
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
