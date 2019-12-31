package com.googlecode.lanterna.gui2.menu;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TerminalTextUtils;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.AbstractInteractableComponent;
import com.googlecode.lanterna.gui2.InteractableRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class MenuItem extends AbstractInteractableComponent<MenuItem> {
    private String label;
    private final Runnable action;

    public MenuItem(String label) {
        this(label, new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    public MenuItem(String label, Runnable action) {
        this.action = action;
        if (label == null || label.trim().isEmpty()) {
            throw new IllegalArgumentException("Menu label is not allowed to be null or empty");
        }
        this.label = label.trim();
    }

    public String getLabel() {
        return label;
    }

    @Override
    protected InteractableRenderer<MenuItem> createDefaultRenderer() {
        return new DefaultMenuItemRenderer();
    }

    /**
     * Helper interface that doesn't add any new methods but makes coding new menu renderers a little bit more clear
     */
    public static abstract class MenuItemRenderer implements InteractableRenderer<MenuItem> {
    }

    public static class DefaultMenuItemRenderer extends MenuItemRenderer {
        @Override
        public TerminalPosition getCursorLocation(MenuItem component) {
            return null;
        }

        @Override
        public TerminalSize getPreferredSize(MenuItem component) {
            return TerminalSize.ONE.withColumns(TerminalTextUtils.getColumnWidth(component.getLabel()));
        }

        @Override
        public void drawComponent(TextGUIGraphics graphics, MenuItem menuItem) {
            ThemeDefinition themeDefinition = menuItem.getThemeDefinition();
            if (menuItem.isFocused()) {
                graphics.applyThemeStyle(themeDefinition.getSelected());
            }
            else {
                graphics.applyThemeStyle(themeDefinition.getNormal());
            }
            String label = menuItem.getLabel();
            graphics.putString(0, 0, label);
            if (!label.isEmpty()) {
                if (menuItem.isFocused()) {
                    graphics.applyThemeStyle(themeDefinition.getActive());
                }
                else {
                    graphics.applyThemeStyle(themeDefinition.getPreLight());
                }
                graphics.putString(0, 0, label.substring(0, 1));
            }
        }
    }
}
