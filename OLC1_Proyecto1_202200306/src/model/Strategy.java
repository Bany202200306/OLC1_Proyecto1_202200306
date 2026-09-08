
package model;

import expr.EvalContext;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author baqzc
 */
public class Strategy {
    private final String name;
    private final CharacterType type;
    private final Action initialAction;
    private final List<Rule> rules;
    private final Action defaultAction;
    
    public Strategy(String name, CharacterType type, Action initialAction, List<Rule> rules, Action defaultAction){
        this.name = name;
        this.type = type;
        this.initialAction = initialAction;
        this.rules = new ArrayList<>(rules);
        this.defaultAction = defaultAction;
    }
    
    public Action choose(EvalContext context){
        for (Rule rule: rules){
            Object result = rule.getCondition().eval(context);
            
            if(!(result instanceof Boolean)){
                throw new RuntimeException("La condicion de la regla no hizo un booleano :D");
            }
            if((Boolean)result){
                return rule.getAction();
            }
        }
        return defaultAction;
    }

    public String getName() {
        return name;
    }

    public CharacterType getType() {
        return type;
    }

    public Action getInitialAction() {
        return initialAction;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Action getDefaultAction() {
        return defaultAction;
    }
    
    
}
