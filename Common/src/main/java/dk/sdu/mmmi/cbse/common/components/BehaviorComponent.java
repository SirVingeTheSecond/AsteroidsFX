package dk.sdu.mmmi.cbse.common.components;

/**
 * Generic component that stores entity behavior type.
 * Can be used by any entity that has different behavior states.
 * Separates data from behavior in the ECS pattern.
 */
public class BehaviorComponent implements Component {
    private Enum<?> behavior;

    public BehaviorComponent(Enum<?> defaultBehavior) {
        this.behavior = defaultBehavior;
    }

    public Enum<?> getBehavior() {
        return behavior;
    }

    public void setBehavior(Enum<?> behavior) {
        this.behavior = behavior;
    }

    /**
     * Getter for behavior with expected enum type
     * @param <T> Enum type expected
     * @param enumClass Class of enum expected
     * @return Behavior cast to the expected enum type, or null if type mismatch
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<?>> T getBehaviorAs(Class<T> enumClass) {
        if (behavior != null && enumClass.isAssignableFrom(behavior.getClass())) {
            return (T) behavior;
        }
        return null;
    }

    /**
     * Checks if the current behavior matches the provided enum constant
     * @param enumConstant Enum constant to check against
     * @return true if behavior matches, false otherwise
     */
    public boolean isBehavior(Enum<?> enumConstant) {
        return behavior != null && behavior.equals(enumConstant);
    }
}
