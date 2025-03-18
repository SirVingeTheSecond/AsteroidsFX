package dk.sdu.mmmi.cbse.common.components;

import javafx.scene.paint.Color;

/**
 * Component that defines how an entity should be rendered.
 * Following Unity's approach where each entity controls its own visual representation.
 */
public class RendererComponent implements IComponent {
    private Color strokeColor = Color.WHITE;
    private Color fillColor = Color.TRANSPARENT;
    private double strokeWidth = 1.5;
    private int renderLayer = 0;
    private boolean visible = true;
    private boolean castsShadow = false;
    private double opacity = 1.0;

    /**
     * Create a new renderer component with default values
     */
    public RendererComponent() {
        // Default constructor with reasonable defaults
    }

    /**
     * Create a renderer with specified colors and layer
     */
    public RendererComponent(Color strokeColor, Color fillColor, int renderLayer) {
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.renderLayer = renderLayer;
    }

    // Getters and setters
    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getRenderLayer() {
        return renderLayer;
    }

    public void setRenderLayer(int renderLayer) {
        this.renderLayer = renderLayer;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isCastsShadow() {
        return castsShadow;
    }

    public void setCastsShadow(boolean castsShadow) {
        this.castsShadow = castsShadow;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }
}