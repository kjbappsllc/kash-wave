package io.kw.ui.cli.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.kw.model.Currency;
import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;
import io.kw.service.TickStreamer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@ApplicationScoped
public class TerminalView {

    @Inject
    TickStreamer priceStreamingEngine;

    public void initView() {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = null;
        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();
            screen.setCursorPosition(null);

            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            final Window window = createWindow();
            Panel contentPanel = createMainPanel();

            contentPanel.addComponent(new Label("Text Box (aligned)"));
            contentPanel.addComponent(
                    new TextBox()
                            .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER)));

            contentPanel.addComponent(new Label("Password Box (right aligned)"));
            contentPanel.addComponent(
                    new TextBox()
                            .setMask('*')
                            .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));

            contentPanel.addComponent(new Label("Read-only Combo Box (forced size)"));
            List<String> timezonesAsStrings = new ArrayList<>(Arrays.asList(TimeZone.getAvailableIDs()));
            ComboBox<String> readOnlyComboBox = new ComboBox<>(timezonesAsStrings);
            readOnlyComboBox.setReadOnly(true);
            readOnlyComboBox.setPreferredSize(new TerminalSize(20, 1));
            contentPanel.addComponent(readOnlyComboBox);

            contentPanel.addComponent(new Label("Editable Combo Box (filled)"));
            contentPanel.addComponent(
                    new ComboBox<>("Item #1", "Item #2", "Item #3", "Item #4")
                            .setReadOnly(false)
                            .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));

            contentPanel.addComponent(new Label("Button (centered)"));
            contentPanel.addComponent(new Button("Button", () -> {
                priceStreamingEngine.startStream(
                        new CurrencyPair(
                                Currency.EUR,
                                Currency.USD,
                                new BigDecimal("0.2")
                        ),
                        Timeframe.M5
                );
            }));

            contentPanel.addComponent(
                    new EmptySpace()
                            .setLayoutData(
                                    GridLayout.createHorizontallyFilledLayoutData(2)));
            contentPanel.addComponent(
                    new Separator(Direction.HORIZONTAL)
                            .setLayoutData(
                                    GridLayout.createHorizontallyFilledLayoutData(2)));
            contentPanel.addComponent(
                    new Button("Close", () -> {
                        window.close();
                        System.exit(0);
                    }).setLayoutData(
                            GridLayout.createHorizontallyEndAlignedLayoutData(2)));

            window.setComponent(contentPanel.withBorder(Borders.singleLine("Kash Wave")));

            textGUI.addWindowAndWait(window);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(screen != null) {
                try {
                    screen.stopScreen();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Panel createMainPanel() {
        Panel contentPanel = new Panel(new GridLayout(2));
        GridLayout gridLayout = (GridLayout)contentPanel.getLayoutManager();
        gridLayout.setHorizontalSpacing(2);
        return contentPanel;
    }

    private Window createWindow() {
        final Window window = new BasicWindow();
        window.setHints(Collections.singletonList(Window.Hint.FULL_SCREEN));
        return window;
    }
}
