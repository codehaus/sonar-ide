package org.sonar.ide.ui;

import javax.swing.*;

/**
 * @author Evgeny Mandrikov
 */
public class TabbetPaneBuilder {
    private final JTabbedPane pane;

    public TabbetPaneBuilder() {
        pane = new JTabbedPane();
    }

    public TabbetPaneBuilder addTab(AbstractViewer page) {
        pane.addTab(page.getTitle(), page);
        return this;
    }

    public JTabbedPane build() {
        return pane;
    }
}
