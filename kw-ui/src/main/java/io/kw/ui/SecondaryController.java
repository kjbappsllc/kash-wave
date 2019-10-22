package io.kw.ui;

import java.io.IOException;

import io.kw.ui.bootstrap.UIBootstrap;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        UIBootstrap.setRoot("primary");
    }
}