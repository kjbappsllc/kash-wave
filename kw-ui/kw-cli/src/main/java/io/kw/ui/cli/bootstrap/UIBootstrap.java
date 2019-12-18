package io.kw.ui.cli.bootstrap;

import javax.enterprise.inject.se.SeContainerInitializer;

/**
 * JavaFX App
 */
public class UIBootstrap {
    public static void main(String[] args) {
        SeContainerInitializer.newInstance().initialize();
    }
}