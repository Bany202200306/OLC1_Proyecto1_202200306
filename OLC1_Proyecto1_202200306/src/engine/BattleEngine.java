
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

public class BattleEngine {

    public String execute(ProgramModel program) {

        program.validateForExecution();

        StringBuilder output = new StringBuilder();

        for (RunCommand run : program.getRuns()) {

            for (String matchName : run.getMatches()) {

                MatchConfig match = program.findMatch(matchName);

                output.append("========================================\n");
                output.append("PARTIDA: ").append(match.getName()).append("\n");
                output.append("SEED: ").append(run.getSeed()).append("\n");
                output.append("========================================\n");

                executeMatch(
                        program,
                        match,
                        run.getSeed(),
                        output
                );

                output.append("\n");
            }
        }

        return output.toString();
    }

    private void executeMatch(
            ProgramModel program,
            MatchConfig match,
            int seed,
            StringBuilder output
    ) {

        Strategy strategy1 =
                program.findStrategy(match.getPlayer1());

        Strategy strategy2 =
                program.findStrategy(match.getPlayer2());

        Combatant player1 = new Combatant(strategy1);
        Combatant player2 = new Combatant(strategy2);

        Random random1 = new Random(seed);
        Random random2 = new Random((long) seed + 1L);

        boolean finished = false;

        for (int round = 0;
             round < match.getRounds();
             round++) {

            player1.defending = false;
            player2.defending = false;

            double randomValue1 = 0.0;
            double randomValue2 = 0.0;

            if (round > 0) {
                randomValue1 = random1.nextDouble();
                randomValue2 = random2.nextDouble();
            }

            Action action1;
            Action action2;

            if (round == 0) {

                action1 = strategy1.getInitialAction();
                action2 = strategy2.getInitialAction();

            } else {

                EvalContext context1 = createContext(
                        round,
                        match.getRounds(),
                        player1,
                        player2,
                        randomValue1
                );

                EvalContext context2 = createContext(
                        round,
                        match.getRounds(),
                        player2,
                        player1,
                        randomValue2
                );

                action1 = strategy1.choose(context1);
                action2 = strategy2.choose(context2);
            }

            output.append("\n");
            output.append("------------- RONDA ")
                    .append(round)
                    .append(" -------------\n");

            if (round > 0) {

                output.append("Random ")
                        .append(player1.name)
                        .append(": ")
                        .append(randomValue1)
                        .append("\n");

                output.append("Random ")
                        .append(player2.name)
                        .append(": ")
                        .append(randomValue2)
                        .append("\n");
            }

            output.append(player1.name)
                    .append(" selecciona ")
                    .append(action1)
                    .append("\n");

            output.append(player2.name)
                    .append(" selecciona ")
                    .append(action2)
                    .append("\n");

            boolean player1First = goesFirst(
                    player1,
                    action1,
                    player2,
                    action2
            );

            if (player1First) {

                executeAction(
                        player1,
                        player2,
                        action1,
                        match,
                        output
                );

                if (player2.health <= 0) {

                    finished = true;

                } else {

                    executeAction(
                            player2,
                            player1,
                            action2,
                            match,
                            output
                    );

                    if (player1.health <= 0) {
                        finished = true;
                    }
                }

            } else {

                executeAction(
                        player2,
                        player1,
                        action2,
                        match,
                        output
                );

                if (player1.health <= 0) {

                    finished = true;

                } else {

                    executeAction(
                            player1,
                            player2,
                            action1,
                            match,
                            output
                    );

                    if (player2.health <= 0) {
                        finished = true;
                    }
                }
            }

            output.append("Estado post-ronda -> ")
                    .append(player1.name)
                    .append(" [HP: ")
                    .append(player1.health)
                    .append(", Recurso: ")
                    .append(player1.resource)
                    .append(", Puntos: ")
                    .append(player1.score)
                    .append("] | ")
                    .append(player2.name)
                    .append(" [HP: ")
                    .append(player2.health)
                    .append(", Recurso: ")
                    .append(player2.resource)
                    .append(", Puntos: ")
                    .append(player2.score)
                    .append("]\n");

            if (finished) {

                output.append("Combate finalizado por KO en la ronda ")
                        .append(round)
                        .append(".\n");

                break;
            }
        }

        Combatant winner =
                determineWinner(
                        player1,
                        player2
                );

        if (winner == null) {

            output.append("\n");
            output.append("RESULTADO: EMPATE\n");

        } else {

            int victoryBonus =
                    match.getScore()
                            .getVictoryBonus();

            winner.score += victoryBonus;

            output.append("\n");
            output.append("GANADOR: ")
                    .append(winner.name)
                    .append(" (+")
                    .append(victoryBonus)
                    .append(" puntos de victoria)")
                    .append("\n");

            if (winner.health * 4
                    <= winner.maxHealth) {

                int lowHealthBonus =
                        match.getBonus()
                                .getLowHealthVictory();

                winner.score += lowHealthBonus;

                output.append(
                                "BONUS POR VICTORIA CON POCA VIDA: +"
                        )
                        .append(lowHealthBonus)
                        .append(" puntos\n");
            }
        }

        output.append("PUNTAJE FINAL: ")
                .append(player1.name)
                .append(" = ")
                .append(player1.score)
                .append(" | ")
                .append(player2.name)
                .append(" = ")
                .append(player2.score)
                .append("\n");
    }

    private EvalContext createContext(
            int round,
            int totalRounds,
            Combatant self,
            Combatant opponent,
            double random
    ) {

        return new EvalContext(
                round,
                totalRounds,
                self.health,
                opponent.health,
                self.resource,
                opponent.resource,
                self.score,
                opponent.score,
                new ArrayList<>(self.history),
                new ArrayList<>(opponent.history),
                random
        );
    }

    private boolean goesFirst(
            Combatant player1,
            Action action1,
            Combatant player2,
            Action action2
    ) {

        if (action1.getPriority()
                != action2.getPriority()) {

            return action1.getPriority()
                    > action2.getPriority();
        }

        if (player1.speed
                != player2.speed) {

            return player1.speed
                    > player2.speed;
        }

        return true;
    }

    private void executeAction(
            Combatant actor,
            Combatant defender,
            Action action,
            MatchConfig match,
            StringBuilder output
    ) {

        Score score =
                match.getScore();

        if (actor.resource
                < action.getCost()) {

            actor.score =
                    Math.max(
                            0,
                            actor.score
                            - score
                                    .getFailedActionPenalty()
                    );

            output.append(actor.name)
                    .append(" falla ")
                    .append(action)
                    .append(
                            " por recurso insuficiente. Penalización: -"
                    )
                    .append(
                            score
                                    .getFailedActionPenalty()
                    )
                    .append(" puntos\n");

            return;
        }

        actor.resource -=
                action.getCost();

        switch (action.getKind()) {

            case DEFENSE -> {

                actor.defending = true;

                output.append(actor.name)
                        .append(" ejecuta ")
                        .append(action)
                        .append(". Activa defensa del 50%.\n");
            }

            case HEAL -> {

                int missingHealth =
                        actor.maxHealth
                        - actor.health;

                int healed =
                        Math.min(
                                action.getPower(),
                                missingHealth
                        );

                actor.health += healed;

                actor.score +=
                        healed
                        * score
                                .getHealingPoint();

                output.append(actor.name)
                        .append(" ejecuta ")
                        .append(action)
                        .append(". Recupera ")
                        .append(healed)
                        .append(" HP.\n");
            }

            case RECOVERY -> {

                int missingResource =
                        actor.maxResource
                        - actor.resource;

                int recovered =
                        Math.min(
                                action.getPower(),
                                missingResource
                        );

                actor.resource += recovered;

                output.append(actor.name)
                        .append(" ejecuta ")
                        .append(action)
                        .append(". Recupera ")
                        .append(recovered)
                        .append(" de recurso.\n");
            }

            case BUFF -> {

                actor.warCryActive = true;

                output.append(actor.name)
                        .append(" ejecuta WAR_CRY.")
                        .append(" El próximo ataque físico tendrá +10.\n");
            }

            case PHYSICAL_ATTACK -> {

                physicalAttack(
                        actor,
                        defender,
                        action,
                        score,
                        output
                );
            }

            case MAGICAL_ATTACK -> {

                magicalAttack(
                        actor,
                        defender,
                        action,
                        score,
                        output
                );
            }
        }

        actor.history.add(action);

        checkCombo(
                actor,
                match.getBonus(),
                output
        );
    }

    private void physicalAttack(
            Combatant actor,
            Combatant defender,
            Action action,
            Score score,
            StringBuilder output
    ) {

        int warCryBonus =
                actor.warCryActive
                        ? 10
                        : 0;

        int damage =
                action.getPower()
                + actor.physicalAttack
                + warCryBonus
                - defender.armor;

        damage =
                Math.max(
                        1,
                        damage
                );

        if (actor.warCryActive) {

            actor.warCryActive = false;

            output.append(actor.name)
                    .append(" consume el efecto de WAR_CRY.\n");
        }

        output.append(actor.name)
                .append(" ejecuta ")
                .append(action)
                .append(".\n");

        applyDamage(
                actor,
                defender,
                damage,
                score,
                output
        );
    }

    private void magicalAttack(
            Combatant actor,
            Combatant defender,
            Action action,
            Score score,
            StringBuilder output
    ) {

        int damage =
                action.getPower()
                + actor.magicPower
                - defender.magicResistance;

        damage =
                Math.max(
                        1,
                        damage
                );

        output.append(actor.name)
                .append(" ejecuta ")
                .append(action)
                .append(".\n");

        applyDamage(
                actor,
                defender,
                damage,
                score,
                output
        );
    }

    private void applyDamage(
            Combatant actor,
            Combatant defender,
            int originalDamage,
            Score score,
            StringBuilder output
    ) {

        int finalDamage =
                originalDamage;

        if (defender.defending) {

            finalDamage =
                    (int) Math.floor(
                            originalDamage
                            * 0.50
                    );

            int reduced =
                    originalDamage
                    - finalDamage;

            if (reduced >= 1) {

                defender.score +=
                        score
                                .getSuccessfulDefense();

                output.append(defender.name)
                        .append(
                                " reduce el daño con defensa."
                        )
                        .append(" Defensa exitosa: +")
                        .append(
                                score
                                        .getSuccessfulDefense()
                        )
                        .append(" puntos.\n");
            }
        }

        int actualDamage =
                Math.min(
                        finalDamage,
                        defender.health
                );

        defender.health -=
                actualDamage;

        actor.score +=
                actualDamage
                * score.getDamagePoint();

        output.append("-> ")
                .append(actor.name)
                .append(" causa ")
                .append(actualDamage)
                .append(" de daño a ")
                .append(defender.name)
                .append(".\n");
    }

    private void checkCombo(
            Combatant actor,
            Bonus bonus,
            StringBuilder output
    ) {

        List<Action> combo;
        int points;

        if (actor.type
                == CharacterType.MAGE) {

            combo =
                    bonus.getMageCombo();

            points =
                    bonus
                            .getMageComboPoints();

        } else {

            combo =
                    bonus
                            .getWarriorCombo();

            points =
                    bonus
                            .getWarriorComboPoints();
        }

        if (actor.history.size()
                < combo.size()) {

            return;
        }

        int endIndex =
                actor.history.size()
                - 1;

        if (actor.lastComboEndIndex
                == endIndex) {

            return;
        }

        int start =
                actor.history.size()
                - combo.size();

        for (int i = 0;
             i < combo.size();
             i++) {

            if (actor.history.get(
                    start + i
            ) != combo.get(i)) {

                return;
            }
        }

        actor.score += points;

        actor.lastComboEndIndex =
                endIndex;

        output.append(actor.name)
                .append(
                        " completa combo: +"
                )
                .append(points)
                .append(" puntos.\n");
    }

    private Combatant determineWinner(
            Combatant player1,
            Combatant player2
    ) {

        if (player1.health <= 0
                && player2.health > 0) {

            return player2;
        }

        if (player2.health <= 0
                && player1.health > 0) {

            return player1;
        }

        if (player1.score
                != player2.score) {

            return player1.score
                    > player2.score
                    ? player1
                    : player2;
        }

        if (player1.health
                != player2.health) {

            return player1.health
                    > player2.health
                    ? player1
                    : player2;
        }

        if (player1.resource
                != player2.resource) {

            return player1.resource
                    > player2.resource
                    ? player1
                    : player2;
        }

        return null;
    }

    private static class Combatant {

        private final String name;
        private final CharacterType type;

        private final int maxHealth;
        private final int maxResource;

        private final int physicalAttack;
        private final int magicPower;

        private final int armor;
        private final int magicResistance;

        private final int speed;

        private int health;
        private int resource;
        private int score;

        private boolean defending;
        private boolean warCryActive;

        private int lastComboEndIndex;

        private final List<Action> history;

        Combatant(
                Strategy strategy
        ) {

            CharacterType character =
                    strategy.getType();

            this.name =
                    strategy.getName();

            this.type =
                    character;

            this.maxHealth =
                    character.getMaxHealth();

            this.maxResource =
                    character.getMaxResource();

            this.physicalAttack =
                    character.getPhysicalAttack();

            this.magicPower =
                    character.getMagicPower();

            this.armor =
                    character.getArmor();

            this.magicResistance =
                    character.getMagicResistance();

            this.speed =
                    character.getSpeed();

            this.health =
                    maxHealth;

            this.resource =
                    maxResource;

            this.score = 0;

            this.defending = false;

            this.warCryActive = false;

            this.lastComboEndIndex =
                    -1;

            this.history =
                    new ArrayList<>();
        }
    }
}