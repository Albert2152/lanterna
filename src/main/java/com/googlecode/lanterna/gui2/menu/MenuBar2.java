package com.googlecode.lanterna.gui2.menu;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBar2 extends AbstractComponent<MenuBar2> implements Container {
    private static final int EXTRA_PADDING = 1;
    private final List<Menu2> menus;

    public MenuBar2() {
        this.menus = new CopyOnWriteArrayList<Menu2>();
    }

    public MenuBar2 add(Menu2 menu) {
        menus.add(menu);
        menu.onAdded(this);
        return this;
    }

    @Override
    public int getChildCount() {
        return getMenuCount();
    }

    @Override
    public Collection<Component> getChildren() {
        return new ArrayList<Component>(menus);
    }

    @Override
    public boolean containsComponent(Component component) {
        return menus.contains(component);
    }

    @Override
    public synchronized boolean removeComponent(Component component) {
        boolean hadMenu = menus.remove(component);
        if (hadMenu) {
            component.onRemoved(this);
        }
        return hadMenu;
    }

    @Override
    public synchronized Interactable nextFocus(Interactable fromThis) {
        if (menus.isEmpty()) {
            return null;
        }
        else if (fromThis == null) {
            return menus.get(0);
        }
        else if (!menus.contains(fromThis) || menus.indexOf(fromThis) == menus.size() - 1) {
            return null;
        }
        else {
            return menus.get(menus.indexOf(fromThis) + 1);
        }
    }

    @Override
    public Interactable previousFocus(Interactable fromThis) {
        if (menus.isEmpty()) {
            return null;
        }
        else if (fromThis == null) {
            return menus.get(menus.size() - 1);
        }
        else if (!menus.contains(fromThis) || menus.indexOf(fromThis) == 0) {
            return null;
        }
        else {
            return menus.get(menus.indexOf(fromThis) - 1);
        }
    }

    @Override
    public boolean handleInput(KeyStroke key) {
        return false;
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

    @Override
    public synchronized void updateLookupMap(InteractableLookupMap interactableLookupMap) {
        for (Menu2 menu: menus) {
            interactableLookupMap.add(menu);
        }
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
            for (int i = 0; i < menuBar.getMenuCount() && remainingSpace > 0; i++) {
                Menu2 menu = menuBar.getMenu(i);
                TerminalSize preferredSize = menu.getPreferredSize();
                menu.setPosition(menu.getPosition()
                        .withColumn(leftPosition)
                        .withRow(0));
                int finalWidth = Math.min(preferredSize.getColumns(), remainingSpace);
                menu.setSize(menu.getSize()
                                .withColumns(finalWidth)
                                .withRows(graphics.getSize().getRows()));
                remainingSpace -= finalWidth + EXTRA_PADDING;
                leftPosition += finalWidth + EXTRA_PADDING;
                TextGUIGraphics componentGraphics = graphics.newTextGraphics(menu.getPosition(), menu.getSize());
                menu.draw(componentGraphics);
            }
        }
    }
}
