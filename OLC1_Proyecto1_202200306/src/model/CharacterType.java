
package model;

/**
 *
 * @author baqzc
 */
public enum CharacterType {
    MAGE(100, 120, 5, 25, 8, 18, 14),
    WARRIOR(140, 100, 22, 0, 20, 8, 10);
    
    private final int maxHealth;
    private final int maxResource;
    private final int physicalAttack;
    private final int magicPower;
    private final int armor;
    private final int magicResistance;
    private final int speed;
    
    CharacterType(int maxHealth, int maxResource, int physicalAttack, int magicPower, int armor, int magicResistance, int speed){
        this.maxHealth = maxHealth;
        this.maxResource = maxResource;
        this.physicalAttack = physicalAttack;
        this.magicPower = magicPower;
        this.armor = armor;
        this.magicResistance = magicResistance;
        this.speed = speed;
}

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxResource() {
        return maxResource;
    }

    public int getPhysicalAttack() {
        return physicalAttack;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public int getArmor() {
        return armor;
    }

    public int getMagicResistance() {
        return magicResistance;
    }

    public int getSpeed() {
        return speed;
    }
    
    
}
