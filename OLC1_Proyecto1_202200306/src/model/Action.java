
package model;

/**
 *
 * @author baqzc
 */
public enum Action {
    
    ARCANE_BOLT(CharacterType.MAGE, Kind.MAGICAL_ATTACK, 12, 10, 4),
    FIREBALL(CharacterType.MAGE, Kind.MAGICAL_ATTACK, 25, 30, 2),
    MAGIC_BARRIER(CharacterType.MAGE, Kind.DEFENSE, 50, 20, 7),
    HEALING_RUNE(CharacterType.MAGE, Kind.HEAL, 25, 30, 5),
    MEDITATE(CharacterType.MAGE, Kind.RECOVERY, 25, 0, 1),

    SLASH(CharacterType.WARRIOR, Kind.PHYSICAL_ATTACK, 12, 10, 4),
    HEAVY_STRIKE(CharacterType.WARRIOR, Kind.PHYSICAL_ATTACK, 25, 25, 2),
    SHIELD_BLOCK(CharacterType.WARRIOR, Kind.DEFENSE, 50, 15, 7),
    WAR_CRY(CharacterType.WARRIOR, Kind.BUFF, 10, 20, 6),
    REST(CharacterType.WARRIOR, Kind.RECOVERY, 25, 0, 1);
    
    public enum Kind{
        PHYSICAL_ATTACK,
        MAGICAL_ATTACK,
        DEFENSE,
        HEAL,
        RECOVERY,
        BUFF
    }
    
    private final CharacterType owner;
    private final Kind kind;
    private final int power;
    private final int cost;
    private final int priority;
    
    Action(CharacterType owner, Kind kind, int power, int cost, int priority){
        this.owner = owner;
        this.kind = kind;
        this.power = power;
        this.cost = cost;
        this.priority = priority;
    }

    public CharacterType getOwner() {
        return owner;
    }

    public Kind getKind() {
        return kind;
    }

    public int getPower() {
        return power;
    }

    public int getCost() {
        return cost;
    }

    public int getPriority() {
        return priority;
    }
    
    public boolean allowedFor(CharacterType type){
        return owner == type;
    }
    
    public boolean isOffensive(){
        return kind  == Kind.PHYSICAL_ATTACK || kind == Kind.MAGICAL_ATTACK;
    }
    
}
