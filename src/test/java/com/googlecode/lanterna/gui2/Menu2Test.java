package com.googlecode.lanterna.gui2;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.dialogs.FileDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.menu.Menu2;
import com.googlecode.lanterna.gui2.menu.MenuBar2;
import com.googlecode.lanterna.gui2.menu.MenuItem;

import java.io.File;
import java.io.IOException;

public class Menu2Test extends TestBase {
    public static void main(String[] args) throws IOException, InterruptedException {
        new Menu2Test().run(args);
    }

    @Override
    public void init(final WindowBasedTextGUI textGUI) {
        // Create window to hold the menu
        final BasicWindow window = new BasicWindow();
        Panel contentPane = new Panel(new BorderLayout());
        contentPane.addComponent(new EmptySpace(TextColor.ANSI.YELLOW, new TerminalSize(40, 15)));
        window.setComponent(contentPane);

        MenuBar2 menubar = new MenuBar2();
        window.setMenuBar(menubar);

        // "File" menu
        Menu2 menuFile = new Menu2("File");
        menubar.add(menuFile);
        menuFile.add(new MenuItem("Open...", new Runnable() {
            public void run() {
                File file = new FileDialogBuilder().build().showDialog(textGUI);
                if (file != null)
                    MessageDialog.showMessageDialog(
                            textGUI, "Open", "Selected file:\n" + file, MessageDialogButton.OK);
            }
        }));
        menuFile.add(new MenuItem("Exit", new Runnable() {
            public void run() {
                window.close();
            }
        }));

        Menu2 countryMenu = new Menu2("Country");
        menubar.add(countryMenu);

        Menu2 germanySubMenu = new Menu2("Germany");
        countryMenu.add(germanySubMenu);
        for (String state: GERMANY_STATES) {
            germanySubMenu.add(new MenuItem(state, DO_NOTHING));
        }
        Menu2 japanSubMenu = new Menu2("Japan");
        countryMenu.add(japanSubMenu);
        for (String prefecture: JAPAN_PREFECTURES) {
            japanSubMenu.add(new MenuItem(prefecture, DO_NOTHING));
        }

        // "Help" menu
        Menu2 menuHelp = new Menu2("Help");
        menubar.add(menuHelp);
        menuHelp.add(new MenuItem("Homepage", new Runnable() {
            public void run() {
                MessageDialog.showMessageDialog(
                        textGUI, "Homepage", "https://github.com/mabe02/lanterna", MessageDialogButton.OK);
            }
        }));
        menuHelp.add(new MenuItem("About", new Runnable() {
            public void run() {
                MessageDialog.showMessageDialog(
                        textGUI, "About", "Lanterna drop-down menu", MessageDialogButton.OK);
            }
        }));

        // Create textGUI and start textGUI
        textGUI.addWindow(window);
    }

    private static final Runnable DO_NOTHING = new Runnable() {
        @Override
        public void run() {
        }
    };

    private static final String[] GERMANY_STATES = new String[]{
            "Baden-Württemberg","Bayern","Berlin","Brandenburg","Bremen","Hamburg","Hessen","Mecklenburg-Vorpommern",
            "Niedersachsen","Nordrhein-Westfalen","Rheinland-Pfalz","Saarland","Sachsen","Sachsen-Anhalt",
            "Schleswig-Holstein","Thüringen",
    };

    private static final String[] JAPAN_PREFECTURES = new String[]{
            "Aichi","Akita","Aomori","Chiba","Ehime","Fukui","Fukuoka","Fukushima","Gifu","Gunma","Hiroshima","Hokkaido",
            "Hyōgo","Ibaraki","Ishikawa","Iwate","Kagawa","Kagoshima","Kanagawa","Kōchi","Kumamoto","Kyoto","Mie",
            "Miyagi","Miyazaki","Nagano","Nagasaki","Nara","Niigata","Ōita","Okayama","Okinawa","Osaka","Saga","Saitama",
            "Shiga","Shimane","Shizuoka","Tochigi","Tokushima","Tokyo","Tottori","Toyama","Wakayama","Yamagata",
            "Yamaguchi","Yamanashi",
    };
}
