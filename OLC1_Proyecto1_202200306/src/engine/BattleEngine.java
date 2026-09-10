
package engine;
import expr.EvalContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Action;
import model.Bonus;
import model.CharacterType;
import model.MatchConfig;
import model.ProgramModel;
import model.RunCommand;
import model.Score;
import model.Strategy;
/**
 *
 * @author baqzc
 */
public class BattleEngine {
    public String excute (ProgramModel program){
        program.validateForExecution();
        
        StringBuilder output = new StringBuilder();
        
        for(RunCommand run: program.getRuns()){
            for(String matchName : run.getMatches()){
                
                MatchConfig match = program.findMatch(matchName);
                output.append("=========================================== ");
                output.append("PARTIDA: ").append(match.getName()).append("\n");
                output.append("SEED: ").append(run.getSeed()).append("\n");
                output.append("===========================================");
                
                executeMatch(program, match, run.getSeed(), output);
                
                output.append("\n");
            }
        }
        return output.toString();
    }
    
    private void executeMatch(ProgramModel program, MatchConfig match, int seed, StringBuilder output){
        
        Strategy strategy1 = program.findStrategy(match.getPlayer1());
        Strategy strategy2 = program.findStrategy(match.getPlayer2());
        
        Combatant player1 = new Combatant(strategy1);
        Combatant player2 = new Combatant(strategy2);
        
        Random random1 = new Random(seed);
        Random random2 = new Random((long) seed + 1L);
        
        boolean finished = false;
        
        for(int round = 0; round < match.getRounds(); round++){
            
            player1.defending = false;
            player2.defending = false;
            
            double randomValue1 = 0.0;
            double randomValue2 = 0.0;
            
            if(round > 0){
                randomValue1 = random1.nextDouble();
                randomValue2 = random2.nextDouble();
            }
            
            Action action1;
            Action action2;
            
            if(round == 0){
                action1 = strategy1.getInitialAction();
                action2 = strategy2.getInitialAction();
            }else{
                EvalContext context1 = createContext(round, match.getRounds(), player1, player2, randomValue1);
                EvalContext context2 = createContext(round, match.getRounds(), player2, player1, randomValue2);
                
                action1 = strategy1.choose(context1);
                action2 = strategy2.choose(context2);
            }
            
            output.append("\n Ronda").append(round).append("\n");
            
            if(round > 0){
                output.append("Random ").append(player1.name).append(": ").append(randomValue1).append("\n");

                output.append("Random ").append(player2.name).append(": ").append(randomValue2).append("\n");
            }
            output.append(player1.name).append(" selecciona ").append(action1).append("\n");

            output.append(player2.name).append(" selecciona ").append(action2).append("\n");
            
            boolean player1First = goesFirst(player1, action1, player2, action2);
            
            if(player1First){
                executeAction(player1, player2, action1, match, output);
                
                if(player2.health <= 0){
                    finished = true;
                }else{
                    executeAction(player2, player1, action2, match, output);
                    
                    if(player1.health <= 0){
                        finished = true;
                    }
                }
            } else{
                executeAction(player2, player1, action2, match, output);
                
                if(player1.health <= 0){
                    finished = true;
                } else{
                    executeAction(player1, player2, action1, match, output);
                    
                    if(player2.health <= 0){
                        finished = true;
                    }
                }
            }
            
            output.append("Estado -> ").append(player1.name).append(": HP=").append(player1.health).append(", recurso=").append(player1.resource).append(", puntos=").append(player1.score)
            .append(" | ").append(player2.name).append(": HP=").append(player2.health).append(", recurso=").append(player2.resource).append(", puntos=").append(player2.score).append("\n");
            
            if(finished){
                break;
            }
         }
        
        Combatant winner = determineWinner(player1, player2);
        
        if(winner = null){
            output.append("\n EMPATE");
        } else{
            winner.score += match.getScore().getVictoryBonus();
            
            output.append("LA VICTORIA ES PARA EL GANADOR :D").append(winner.name).append(": + ").append(match.getScore().getVictoryBonus()).append("\n");
            
            if(winner.health * 4 <= winner.maxHealth){
                winner.score += match.getBonus().getLowHealthVictory();
                output.append("Bonus por la Victoria con la vida baja :D ").append(match.getBonus().getLowHealthVictory()).append("\n");
            }
            output.append("Ganador: ").append(winner.name).append("\n");
        }
        output.append("Punteo final ").append(player1.name).append(":").append(player1.score).append("\n");
        output.append("Punteo final ").append(player2.name).append(":").append(player2.score).append("\n");
    }
    
    private EvalContext createContext(int round,int totalRounds,Combatant self,Combatant opponent,double random) {

        return new EvalContext(round,totalRounds,self.health,opponent.health,self.resource, opponent.resource,self.score,opponent.score,new ArrayList<>(self.history),new ArrayList<>(opponent.history),
                random);
    }
    
    private boolean goesFirst(Combatant player1,Action action1,Combatant player2,Action action2) {

        if(action1.getPriority()!= action2.getPriority()){
            return action1.getPriority()
            > action2.getPriority();
        }

        if (player1.speed != player2.speed) {
            return player1.speed
            > player2.speed;
        }
        return true;
    }
    
    
    
}
