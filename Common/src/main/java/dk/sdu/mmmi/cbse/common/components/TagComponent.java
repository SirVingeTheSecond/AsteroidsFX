package dk.sdu.mmmi.cbse.common.components;

import java.util.HashSet;
import java.util.Set;

/**
 * Component that stores tags for identifying entity types and properties.
 * Allows for flexible entity categorization without using inheritance.
 */
public class TagComponent implements Component {

    // Tags should probably be stored as a separate Enum?
    public static final String TAG_PLAYER = "player";
    public static final String TAG_ENEMY = "enemy";
    public static final String TAG_ASTEROID = "asteroid";
    public static final String TAG_BULLET = "bullet";
    public static final String TAG_POWERUP = "powerup";

    private final Set<String> tags = new HashSet<>();

    /**
     * Create a TagComponent with no initial tags.
     */
    public TagComponent() {
        // Empty constructor
    }

    /**
     * Create a TagComponent with initial tags.
     * @param initialTags Initial tags to add
     */
    public TagComponent(String... initialTags) {
        if (initialTags != null) {
            for (String tag : initialTags) {
                addTag(tag);
            }
        }
    }

    /**
     * Add a tag to this entity.
     * @param tag Tag to add
     */
    public void addTag(String tag) {
        if (tag != null && !tag.isEmpty()) {
            tags.add(tag.toLowerCase());
        }
    }

    /**
     * Remove a tag from this entity.
     * @param tag Tag to remove
     */
    public void removeTag(String tag) {
        if (tag != null) {
            tags.remove(tag.toLowerCase());
        }
    }

    /**
     * Check if entity has a specific tag.
     * @param tag Tag to check
     * @return true if entity has the tag, false otherwise
     */
    public boolean hasTag(String tag) {
        return tag != null && tags.contains(tag.toLowerCase());
    }

    /**
     * Check if entity has all the specified tags.
     * @param tagList Tags to check
     * @return true if entity has all tags, false otherwise
     */
    public boolean hasAllTags(String... tagList) {
        if (tagList == null) {
            return true;
        }

        for (String tag : tagList) {
            if (!hasTag(tag)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if entity has any of the specified tags.
     * @param tagList Tags to check
     * @return true if entity has at least one tag, false otherwise
     */
    public boolean hasAnyTag(String... tagList) {
        if (tagList == null) {
            return false;
        }

        for (String tag : tagList) {
            if (hasTag(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all tags for this entity.
     * @return Unmodifiable set of tags
     */
    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    /**
     * Clear all tags from this entity.
     */
    public void clearTags() {
        tags.clear();
    }
}
