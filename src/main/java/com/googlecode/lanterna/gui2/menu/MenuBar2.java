package com.googlecode.lanterna.gui2.menu;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBar2 extends AbstractComponent<MenuBar2> {
    private static final int EXTRA_PADDING = 1;
    private final List<Menu2> menus;

    public MenuBar2() {
        this.menus = new CopyOnWriteArrayList<Menu2>();
    }

    public MenuBar2 add(Menu2 menu) {
        menus.add(menu);
        return this;
    }

    public Menu2 getMenu(int index) {
        return menus.get(index);
    }

    public int getMenuCount() {
        return menus.size();
    }

    @Override
    protected ComponentRenderer<MenuBar2> createDefaultRenderer() {
        return new DefaultMenuBarRenderer();
    }

    public class DefaultMenuBarRenderer implements ComponentRenderer<MenuBar2> {
        @Override
        public TerminalSize getPreferredSize(MenuBar2 menuBar) {
            int maxHeight = 1;
            int totalWidth = EXTRA_PADDING;
            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                Menu2 menu = menuBar.getMenu(i);
                TerminalSize preferredSize = menu.getPreferredSize();
                maxHeight = Math.max(maxHeight, preferredSize.getRows());
                totalWidth += preferredSize.getColumns();
            }
            totalWidth += EXTRA_PADDING;
            return new TerminalSize(totalWidth, maxHeight);
        }

        @Override
        public void drawComponent(TextGUIGraphics graphics, MenuBar2 menuBar) {
            // Reset the area
            graphics.applyThemeStyle(getThemeDefinition().getNormal());
            graphics.fill(' ');

            int leftPosition = EXTRA_PADDING;
            int remainingSpace = graphics.getSize().getColumns() - EXTRA_PADDING;
            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                Menu2 menu = menuBar.getMenu(i);
                TerminalSize preferredSize = menu.getPreferredSize();
                menu.setPosition(menu.getPosition()
                        .withColumn(leftPosition)
                        .withRow(0));
                menu.setSize(menu.getSize()
                                .withColumns(Math.min(preferredSize.getColumns(), remainingSpace))
                                .withRows(graphics.getSize().getRows()));
                TextGUIGraphics componentGraphics = graphics.newTextGraphics(menu.getPosition(), menu.getSize());
                menu.draw(componentGraphics);
            }
        }
    }
}
