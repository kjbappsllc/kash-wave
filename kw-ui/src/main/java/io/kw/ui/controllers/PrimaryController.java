package io.kw.ui.controllers;

import java.io.IOException;

import io.kw.ui.bootstrap.UIBootstrap;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        System.out.println("Secondary Button Clicked");
    }
}
