package com.thrashplay.saltar.desktop;

import com.thrashplay.luna.desktop.LunaWindow;
import com.thrashplay.saltar.SaltarScreenManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class Main {
    public static void main(String[] args) {
        new LunaWindow("Saltar", new SaltarScreenManager(), 640, 480);
    }
}
