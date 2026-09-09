
package model;
import engine.BattleScriptExecutionException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author baqzc
 */
public class ProgramModel {
    private final List<Strategy> strategies;
    private final List<MatchConfig> matches;
    private final List<RunCommand> runs;

    public ProgramModel(List<Strategy> strategies, List<MatchConfig> matches, List<RunCommand> runs){
        this.strategies = new ArrayList<>(strategies);
        this.matches = new ArrayList<>(matches);
        this.runs = new ArrayList<>(runs);
    }
    
    public void validateForExecution(){
        Set<String> strategyNames = new HashSet<>();
        
        for (Strategy strategy : strategies){
            if(!strategyNames.add(strategy.getName())){
                throw new BattleScriptExecutionException("Partida duplicada :D" + strategy.getName());
            }
            
            Set<String> matchNames = new HashSet<>();
            
            for (MatchConfig match : matches) {

            if (!matchNames.add(match.getName())) {
                throw new BattleScriptExecutionException(
                        "Partida duplicada: " + match.getName()
                );
            }

            if (match.getRounds() <= 0) {
                throw new BattleScriptExecutionException(
                        "Las rondas de " + match.getName()
                        + " deben ser mayores que cero."
                );
            }

            if (findStrategy(match.getPlayer1()) == null) {
                throw new BattleScriptExecutionException(
                        "No existe la estrategia: "
                        + match.getPlayer1()
                );
            }

            if (findStrategy(match.getPlayer2()) == null) {
                throw new BattleScriptExecutionException(
                        "No existe la estrategia: "
                        + match.getPlayer2()
                );
            }

            validateScore(match.getScore());
            validateBonus(match.getBonus());
        }
        for(RunCommand run: runs){
            if(run.getSeed() <= 0){
                throw new BattleScriptExecutionException("Debe ser entero mayor que 0 y no debe ser negativa :D");
            }
            if(run.getMatches().isEmpty()){
                throw new BattleScriptExecutionException("Run debe tener una partida para iniciar");
            }
            
            for(String name : run.getMatches()){
                if(findMatch(name) == null){
                   throw new BattleScriptExecutionException("La partida indicada en main no exite D:" + name); 
                }
            }
        }
    }
}
    
    private void validateScore(Score score){
        if(score.getDamagePoint() <= 0){
           throw new BattleScriptExecutionException("damage_point debe ser mayor a cero"); 
        }
        if(score.getHealingPoint() < 0 || score.getSuccessfulDefense() <0 || score.getVictoryBonus() < 0
           || score.getFailedActionPenalty() < 0){
           throw new BattleScriptExecutionException("Los valores de score no puede tomar valores negativos :DD:");
        }
        
    }
    private void validateBonus(Bonus bonus){
        if(bonus.getMageCombo().isEmpty()){
           throw new BattleScriptExecutionException("mage_combo no puede estar vacio :D"); 
        }
        if(bonus.getWarriorCombo().isEmpty()){
           throw new BattleScriptExecutionException("warrior_combo no puede estar vacio :D"); 
        }
        for(Action action : bonus.getMageCombo()){
            if(!action.allowedFor(CharacterType.MAGE)){
               throw new BattleScriptExecutionException("El mage contiene una accion ilegal"); 
            }
        }
        for(Action action: bonus.getWarriorCombo()){
            if(!action.allowedFor(CharacterType.WARRIOR)){
               throw new BattleScriptExecutionException("Warrior contiene acciones ilegales"); 
            }
        }
        if(bonus.getMageComboPoints() < 0 || bonus.getWarriorComboPoints() < 0 || bonus.getLowHealthVictory() < 0){
           throw new BattleScriptExecutionException("No pueden ser valores negativos :D"); 
        }
    }
    public Strategy findStrategy(String name){
        for(Strategy strategy : strategies){
            if(strategy.getName().equals(name)){
                return strategy;
            }
        }
        return null;
    }
    public MatchConfig findMatch(String name){
        for(MatchConfig match : matches){
            if(match.getName().equals(name)){
                return match;
            }
        }
        return null;
    }
    public List<Strategy> getStrategies(){
        return new ArrayList<>(strategies);
    }
    public List<MatchConfig> getMatches(){
        return new ArrayList<>(matches);
    }
    public List<RunCommand> getRuns(){
        return new ArrayList<>(runs);
    }
}

