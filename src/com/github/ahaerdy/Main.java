package com.github.ahaerdy;

import com.github.ahaerdy.model.Board;
import com.github.ahaerdy.model.Space;
import com.github.ahaerdy.service.EventEnum;
import com.github.ahaerdy.service.NotifierService;
import com.github.ahaerdy.ui.custom.input.NumberText;
import com.github.ahaerdy.ui.custom.panel.SudokuSector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Main {

    public static void main(String[] args) throws IOException {
        UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.PLAIN, 18));
        UIManager.put("OptionPane.buttonFont", new Font("SansSerif", Font.PLAIN, 16));

        var conteudoArquivo = Files.readString(Path.of(args[0]));
        final var gameConfig = Stream.of(conteudoArquivo.trim().split("\\s+"))
                .collect(toMap(k -> k.split(";")[0], v -> v.split(";")[1]));

        var frame = new JFrame("Sudoku");
        frame.setSize(600, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        var boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));

        List<List<Space>> espacos = new ArrayList<>();
        for (int i = 0; i < 9; i++) espacos.add(new ArrayList<>());

        var notifierService = new NotifierService();

        for (int blocoLinha = 0; blocoLinha < 9; blocoLinha += 3) {
            for (int blocoColuna = 0; blocoColuna < 9; blocoColuna += 3) {
                List<NumberText> camposDoBloco = new ArrayList<>();
                for (int linha = blocoLinha; linha < blocoLinha + 3; linha++) {
                    for (int coluna = blocoColuna; coluna < blocoColuna + 3; coluna++) {
                        var positionConfig = gameConfig.get("%s,%s".formatted(linha, coluna));
                        var expected = Integer.parseInt(positionConfig.split(",")[0]);
                        var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                        var space = new Space(expected, fixed);
                        espacos.get(linha).add(space);

                        var campo = new NumberText(space);
                        notifierService.subscribe(EventEnum.CLEAR_SPACE, campo);
                        camposDoBloco.add(campo);
                    }
                }
                boardPanel.add(new SudokuSector(camposDoBloco));
            }
        }

        var board = new Board(espacos);

        var verificarButton = new JButton("Verificar jogo");
        verificarButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        verificarButton.addActionListener(e -> {
            var mensagem = switch (board.getStatus()) {
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            mensagem += board.hasErrors() ? " e contém erros" : " e não contém erros";
            JOptionPane.showMessageDialog(null, mensagem);
        });

        var resetButton = new JButton("Reiniciar jogo");
        resetButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        resetButton.addActionListener(e -> {
            var resposta = JOptionPane.showConfirmDialog(
                    null, "Deseja realmente reiniciar o jogo?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (resposta == 0) {
                board.reset();
                notifierService.notify(EventEnum.CLEAR_SPACE);
            }
        });

        var finalizarButton = new JButton("Concluir");
        finalizarButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        finalizarButton.addActionListener(e -> {
            if (board.gameIsFinished()) {
                JOptionPane.showMessageDialog(null, "Parabéns, você concluiu o jogo!");
            } else {
                JOptionPane.showMessageDialog(null, "Seu jogo ainda não está correto ou completo.");
            }
        });

        var botoesPanel = new JPanel();
        botoesPanel.add(verificarButton);
        botoesPanel.add(resetButton);
        botoesPanel.add(finalizarButton);

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(botoesPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

}