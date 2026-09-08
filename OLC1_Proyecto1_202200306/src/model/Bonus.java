
package model;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author baqzc
 */
public class Bonus {
    private final List<Action> mageCombo;
    private final int mageComboPoints;
    private final List<Action> warriorCombo;
    private final int warriorComboPoints;
    private final int lowHealthVictory;
    
    public Bonus(List<Action> mageCombo, int mageComboPoints, List<Action> warriorCombo, int warriorComboPoints, int lowHealthVictory){
        this.mageCombo = new ArrayList<>(mageCombo);
        this.mageComboPoints = mageComboPoints;
        this.warriorCombo = new ArrayList<>(warriorCombo);
        this.warriorComboPoints = warriorComboPoints;
        this.lowHealthVictory = lowHealthVictory; 
        
    }

    public List<Action> getMageCombo() {
        return mageCombo;
    }

    public int getMageComboPoints() {
        return mageComboPoints;
    }

    public List<Action> getWarriorCombo() {
        return warriorCombo;
    }

    public int getWarriorComboPoints() {
        return warriorComboPoints;
    }

    public int getLowHealthVictory() {
        return lowHealthVictory;
    }
    
}
