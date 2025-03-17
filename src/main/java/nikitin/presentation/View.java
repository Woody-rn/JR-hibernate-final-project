package nikitin.presentation;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class View {
    private final JTextArea textArea;

    public View() {
        JFrame frame = new JFrame("JMH Benchmark");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        textArea = new JTextArea();
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setText("Please wait...");
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showResult(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String collect = br.lines().
                    collect(Collectors.joining("\n"));
            content.append(collect);
        } catch (IOException e) {
            textArea.setText("Error reading results from file: " + e.getMessage());
            return;
        }
        textArea.setText(content.toString());
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
