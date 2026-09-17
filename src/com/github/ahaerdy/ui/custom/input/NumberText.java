package com.github.ahaerdy.ui.custom.input;

import com.github.ahaerdy.model.Space;
import com.github.ahaerdy.service.EventEnum;
import com.github.ahaerdy.service.EventListener;


import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class NumberText extends JTextField implements EventListener {

    private final Space space;

    public NumberText(final Space space) {
        this.space = space;
        var dimension = new Dimension(50, 50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setHorizontalAlignment(CENTER);
        this.setDocument(new NumberTextLimit());
        this.setEditable(!space.isFixed());
        if (space.isFixed()) {
            this.setText(space.getActual().toString());
            this.setFont(new Font("Arial", Font.BOLD, 20));
            this.setBackground(new Color(224, 224, 224));
            this.setForeground(Color.BLACK);
        } else {
            this.setFont(new Font("Arial", Font.PLAIN, 20));
        }
        this.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(final DocumentEvent e) {
                changeSpace();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                changeSpace();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                changeSpace();
            }

            private void changeSpace() {
                if (getText().isEmpty()) {
                    space.clearSpace();
                    return;
                }
                space.setActual(Integer.parseInt(getText()));
            }

        });
    }

    @Override
    public void update(final EventEnum eventType) {
        if (eventType.equals(EventEnum.CLEAR_SPACE) && this.isEditable()) {
            this.setText("");
        }
    }

}