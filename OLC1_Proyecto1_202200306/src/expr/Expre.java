
package expr;
import engine.BattleScriptExecutionException;
import java.util.ArrayList;
import java.util.List;
import model.Action;

/**
 *
 * @author baqzc
 */
public interface Expre {
    Object eval(EvalContext context);
    
    class Literal implements Expre{
        private final Object value;
        
        public Literal (Object value){
            this.value = value;    
        }
        @Override
        public Object eval(EvalContext context){
            return value;
        }
    } 
    

    class Variable implements Expre{
        private final String name;
        
        public Variable(String name){
            this.name = name;
        }
        @Override
        public Object eval (EvalContext context){
            return context.get(name);
        }
    }

    class Not implements Expre{
        private final Expre expression;
        
        public Not(Expre expression){
            this.expression = expression;
        }
        @Override
        public Object eval(EvalContext context){
            Object value = expression.eval(context);
            
            if(!(value instanceof Boolean)){
                throw new BattleScriptExecutionException("El operador, necesita un booleano :D");
            }
            return !(Boolean) value;
        }
    }

    class Binary implements Expre{
        private final String operator;
        private final Expre left;
        private final Expre right;
        
        public Binary(String operator, Expre left, Expre right){
            this.operator = operator;
            this.left = left;
            this.right = right;
        }
        @Override
        public Object eval(EvalContext context){
            if("&&".equals(operator)){
                Object leftValue = left.eval(context);
                
                if(!(leftValue instanceof Boolean)){
                    throw new BattleScriptExecutionException("El operador, necesita un booleano :D");
                }
                
                if(!(Boolean) leftValue){
                    return false;
                }
                
                Object rightValue = right.eval(context);
                
                if(!(rightValue instanceof Boolean)){
                    throw new BattleScriptExecutionException("El operador &&, necesita un booleano :D");
                }
                
                return (Boolean) rightValue;
            }
            
            if("||".equals(operator)){
                Object leftValue = left.eval(context);
                
                if(!(leftValue instanceof Boolean)){
                    throw new BattleScriptExecutionException("El operador ||, necesita un booleano :D");
                }
                
                if((Boolean) leftValue){
                    return true;
                }
                
                Object rightValue = right.eval(context);
                
                if(!(rightValue instanceof Boolean)){
                    throw new BattleScriptExecutionException("El operador ||, necesita un booleano :D");
                }
                return(Boolean) rightValue;
            }
            
            Object a = left.eval(context);
            Object b = left.eval(context);
            
            switch(operator){
                case "==":
                    return equalsValue(a,b);
                case"!=":
                    return !equalsValue(a,b);
                case ">":
                    return number(a) > number(b);

                case "<":
                    return number(a) < number(b);

                case ">=":
                    return number(a) >= number(b);

                case "<=":
                    return number(a) <= number(b);
                    
                default:
                    throw new BattleScriptExecutionException("operador desconicido" + operator);
            }
        }
            private boolean equalsValue(Object a, Object b){
                if(a instanceof Number && b instanceof Number){
                    return Double.compare(((Number) a).doubleValue(),((Number) b).doubleValue()) == 0;
                }
                
                if(a == null){
                    return b == null;
                }
                
                return a.equals(b);
            }
            private double number(Object value){
                if(!(value instanceof Number)){
                    throw new BattleScriptExecutionException("La comparacion necesita valor numericos");
                }
                return((Number) value).doubleValue();
            }
        }
        class Function implements Expre{
            private final String name;
            private final List<Expre> arguments;
            
            public Function(String name, List<Expre> arguments){
                this.name = name;
                this.arguments = arguments;
            }
            @Override
            public Object eval(EvalContext context){
                if("get_move".equals(name)){
                    List<Action> history = actionList(arguments.get(0).eval(context));
                    
                    int index = integer(arguments.get(1).eval(context));
                    
                    if(index < 0 || index >= history.size()){
                        throw new BattleScriptExecutionException("get_move fuera del historial" + index);
                    }
                    return history.get(index);
                }
                if("last_move".equals(name)){
                    List<Action> history = actionList(arguments.get(0).eval(context));
                    
                    if(history.isEmpty()){
                        throw new BattleScriptExecutionException("last_move fuera del historial");
                    }
                    return history.get(history.size() -1);
                }
                if("get_moves_count".equals(name)){
                    List<Action> history = actionList(arguments.get(0).eval(context));
                    
                    Object actionValue = arguments.get(1).eval(context);
                    
                    if(!(actionValue instanceof Action)){
                        throw new BattleScriptExecutionException("last_move fuera del historial");
                    }
                    Action action = (Action) actionValue;
                    int count = 0;
                    
                    for(Action current : history){
                        if(current == action){
                            count++;
                        }
                    }
                    return count;
                }
                if ("get_last_n_moves".equals(name)) {

                List<Action> history = actionList(
                        arguments.get(0).eval(context)
                );

                int n = integer(
                        arguments.get(1).eval(context)
                );

                if (n <= 0) {
                    throw new BattleScriptExecutionException(
                            "get_last_n_moves: n debe ser mayor que cero."
                    );
                }

                if (n > history.size()) {
                    throw new BattleScriptExecutionException(
                            "get_last_n_moves: no existen suficientes movimientos."
                    );
                }

                return new ArrayList<>(
                        history.subList(history.size() - n, history.size())
                );
            }

            throw new BattleScriptExecutionException(
                    "Función desconocida: " + name
            );
        }

        @SuppressWarnings("unchecked")
        private List<Action> actionList(Object value) {

            if (!(value instanceof List)) {
                throw new BattleScriptExecutionException(
                        "Se esperaba un historial o lista de acciones."
                );
            }

            return (List<Action>) value;
        }

        private int integer(Object value) {

            if (!(value instanceof Number)) {
                throw new BattleScriptExecutionException(
                        "Se esperaba un entero."
                );
            }

            return ((Number) value).intValue();
        }
    }
    
}
                
    
