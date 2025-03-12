package dk.sdu.mmmi.cbse.common.components;

/**
 * Component that identifies an entity as a player.
 * This is a tag component to mark player entities.
 */
public class PlayerComponent implements IComponent {
    private int lives = 3;
    private int score = 0;
    private boolean isInvulnerable = false;
    private int invulnerabilityTimer = 0;

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.isInvulnerable = invulnerable;
    }

    public int getInvulnerabilityTimer() {
        return invulnerabilityTimer;
    }

    public void setInvulnerabilityTimer(int timer) {
        this.invulnerabilityTimer = timer;
    }
}