package coms309.demo1gamestate.game_experiments;

public class ChessGamestate {
    private String Gamestate;
    private String playerB;
    private String playerW;

    public ChessGamestate() {}

    public ChessGamestate(String Gamestate, String playerB, String playerW) {
        this.Gamestate = Gamestate;
        this.playerB = playerB;
        this.playerW = playerW;
    }

    public String getPlayerB() {
        return playerB;
    }

    public void setPlayerB(String playerB) {
        this.playerB = playerB;
    }

    public String getPlayerW() {
        return playerW;
    }

    public void setPlayerW(String playerW) {
        this.playerW = playerW;
    }

    public String getGamestate() {
        return Gamestate;
    }

    public void setGamestate(String Gamestate) {
        this.Gamestate = Gamestate;
    }

    @Override
    public String toString() {
        return "PlayerW: " + playerW + "\nPlayerB: " + playerB + "\nGamestate: " + Gamestate;
    }
}
