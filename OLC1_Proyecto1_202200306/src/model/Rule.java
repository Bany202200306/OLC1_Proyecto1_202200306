
package model;

import expr.Expre;

/**
 *
 * @author baqzc
 */
public class Rule {
    private final Expre condition;
    private final Action action;
    
    public Rule(Expre condition, Action action){
        this.condition = condition;
        this.action = action;
    }

    public Expre getCondition() {
        return condition;
    }

    public Action getAction() {
        return action;
    }
    
}
