package com.coms309.demo1.Players;


public class Player {
    String username;
    public int wins, losses, winPercent;

    protected int id;

    public Player(String username, int id) {

        this.username = username;
        wins = 0;
        losses = 0;
        winPercent = 0;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    protected String getStats() {
        return "Stats:\nWins: " + wins + "\nlosses: " + losses + "\nwinPercent: " + winPercent;
    }
}
