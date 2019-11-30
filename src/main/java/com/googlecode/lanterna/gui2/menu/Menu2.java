package com.googlecode.lanterna.gui2.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu2 extends MenuItem {
    private final List<MenuItem> subItems;

    public Menu2(String label) {
        super(label);
        this.subItems = new ArrayList<MenuItem>();
    }

    public void add(MenuItem menuItem) {
        synchronized (subItems) {
            subItems.add(menuItem);
        }
    }
}
