
package expr;
import java.util.List;
import model.Action;

/**
 *
 * @author baqzc
 */
public class EvalContext {
    
    private final int roundNumber;
    private final int totalRounds;
    private final int selfHealth;
    private final int opponentHealth;
    private final int selfResource;
    private final int opponentResource;
    private final int selfScore;
    private final int opponentScore;
    private final List<Action> selfHistory;
    private final List<Action> opponentHistory;
    private final double random;
    
    
    public EvalContext(int roundNumber, int totalRounds,int selfHealth, int opponentHealth, int selfResource, int opponentResource, int selfScore, 
        int opponentScore, List<Action> selfHistory, List<Action> opponentHistory, double random){
        
        this.roundNumber = roundNumber;
        this.totalRounds = totalRounds;
        this.selfHealth = selfHealth;
        this.opponentHealth = opponentHealth;
        this.selfResource = selfResource;
        this.opponentResource = opponentResource;
        this.selfScore = selfScore;
        this.opponentScore = opponentScore;
        this.selfHistory = selfHistory;
        this.opponentHistory = opponentHistory;
        this.random = random;
    }
    
    public Object get (String name){
        switch(name){
            case "round _number":
                return roundNumber;
                
            case "total_rounds":
                return totalRounds;
                
            case "self_health":
                return selfHealth;
                
            case "opponent_health":
                return opponentHealth;
                
            case "self_resource":
                return selfResource;
                
            case "opponent_resource":
                return opponentResource;
                
            case "self_score":
                return selfScore;
                
            case "opponent_score":
                return opponentScore;
                
            case "self_history":
                return selfHistory;
                
            case "opponent_history":
                return opponentHistory;
                
            case "random":
                return random;
                
            default:
                throw new RuntimeException("No se reconocio la variable :D " + name);
        }
    }
    
    
    
}
