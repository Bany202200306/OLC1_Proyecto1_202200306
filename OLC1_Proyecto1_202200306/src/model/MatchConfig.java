
package model;

/**
 *
 * @author baqzc
 */
public class MatchConfig {
    private final String name;
    private final String player1;
    private final String player2;
    private final int rounds;
    private final Score score;
    private final Bonus bonus;
    
    public MatchConfig(String name, String player1, String player2, int rounds, Score score, Bonus bonus){
        this.name = name;
        this.player1 = player1;
        this.player2 = player2;
        this.rounds = rounds;
        this.score = score;
        this.bonus = bonus;
    }

    public String getName() {
        return name;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public int getRounds() {
        return rounds;
    }

    public Score getScore() {
        return score;
    }

    public Bonus getBonus() {
        return bonus;
    }
    
}
