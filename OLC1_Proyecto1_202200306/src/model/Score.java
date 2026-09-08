
package model;

/**
 *
 * @author baqzc
 */
public class Score {
    private final int damagePoint;
    private final int healingPoint;
    private final int successfulDefense;
    private final int victoryBonus;
    private final int failedActionPenalty;
    
    public Score(int damagePoint, int healingPoint, int successfulDefense, int victoryBonus, int failedActionPenalty){
        this.damagePoint = damagePoint;
        this.healingPoint = healingPoint;
        this.successfulDefense = successfulDefense;
        this.victoryBonus = victoryBonus;
        this.failedActionPenalty = failedActionPenalty;
    }

    public int getDamagePoint() {
        return damagePoint;
    }

    public int getHealingPoint() {
        return healingPoint;
    }

    public int getSuccessfulDefense() {
        return successfulDefense;
    }

    public int getVictoryBonus() {
        return victoryBonus;
    }

    public int getFailedActionPenalty() {
        return failedActionPenalty;
    }
    
}
