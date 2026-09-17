package com.github.ahaerdy.ui.custom.panel;

import com.github.ahaerdy.ui.custom.input.NumberText;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.GridLayout;
import java.util.List;

import static java.awt.Color.black;

public class SudokuSector extends JPanel {

    public SudokuSector(final List<NumberText> campos) {
        this.setLayout(new GridLayout(3, 3));
        this.setBorder(new LineBorder(black, 2, true));
        campos.forEach(this::add);
    }

}