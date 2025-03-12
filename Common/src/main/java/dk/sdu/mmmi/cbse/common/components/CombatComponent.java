package dk.sdu.mmmi.cbse.common.components;

/**
 * Component that contains combat-related properties.
 * This can be used by any entity capable of combat, both players and enemies.
 */
public class CombatComponent implements IComponent {
    private float damage;
    private float attackRange;
    private float attackSpeed;
    private int scoreValue; // Points awarded when entity is destroyed

    public CombatComponent() {
        this.damage = 10.0f;
        this.attackRange = 50.0f;
        this.attackSpeed = 1.0f;
        this.scoreValue = 0;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }
}
