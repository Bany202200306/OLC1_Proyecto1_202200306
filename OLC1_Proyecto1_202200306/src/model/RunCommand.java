
package model;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author baqzc
 */
public class RunCommand {
    private final List<String> matches;
    private final int seed;
    
    public RunCommand(List<String> matches, int seed){
        this.matches = new ArrayList<>(matches);
        this.seed = seed;
    }

    public List<String> getMatches() {
        return matches;
    }

    public int getSeed() {
        return seed;
    }
    
}
