package com.seeyon.chat.ui;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Shaozz
 */
public class RoundRectPanel extends JPanel {

    private int cornerRadius = 16;

    private RoundLineBorder roundLineBorder;

    public RoundRectPanel(LayoutManager layout) {
        super(layout);
    }

    public RoundRectPanel() {
    }

    protected void paintComponent(Graphics g) {
        Dimension arcs = new Dimension(this.cornerRadius, this.cornerRadius);
        int width = this.getWidth();
        int height = this.getHeight();
        Graphics2D graphics = (Graphics2D)g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setPaint(this.getBackground());
        graphics.fillRoundRect(0, 0, width, height, arcs.width, arcs.height);
        this.paintChildren(graphics);
        if (this.roundLineBorder != null) {
            this.roundLineBorder.paintBorder(this, graphics, 0, 0, width, height);
        }

        graphics.dispose();
    }

    public int getCornerRadius() {
        return this.cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void setBorderColor(Color borderColor, int thickness) {
        if (borderColor != null) {
            this.roundLineBorder = new RoundLineBorder(borderColor, thickness, this.cornerRadius);
        } else {
            this.roundLineBorder = null;
        }

    }

    public void refreshColor(EditorColorsScheme scheme, Color themeColor) {
        if (this.getBackground() instanceof JBColor) {
            JBColor.setDark(themeColor == null);
        }
    }

    static class RoundLineBorder extends LineBorder {
        private final int radius;

        public RoundLineBorder(@NotNull Color color, int thickness, int radius) {
            super(color, thickness, true);
            this.radius = radius;
        }

        public void paintBorder(@Nullable Component component, @Nullable Graphics g, int x, int y, int width, int height) {
            if (this.thickness > 0 && g instanceof Graphics2D graphics) {
                Color oldColor = graphics.getColor();
                graphics.setColor(this.lineColor);
                int offs = this.thickness;
                int size = offs + offs;
                float arc = (float)this.radius;
                Shape outer = new RoundRectangle2D.Float((float)x, (float)y, (float)width, (float)height, arc, arc);
                Shape inner = new RoundRectangle2D.Float((float)(x + offs), (float)(y + offs), (float)(width - size), (float)(height - size), arc, arc);
                Path2D path = new Path2D.Float(0);
                path.append(outer, false);
                path.append(inner, false);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.fill(path);
                graphics.setColor(oldColor);
            }

        }
    }
}
