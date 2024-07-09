package me.nilsschlegel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import org.jetbrains.annotations.Nullable;

public class GameOfLife extends JFrame {

    private static final int SIZE = 50;
    private static final int CELL_SIZE = 10;
    private boolean[][] grid;
    @Nullable
    private Timer timer;

    public GameOfLife() {
        setTitle("Conway's Game of Life");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(SIZE * CELL_SIZE, SIZE * CELL_SIZE);
        setLocationRelativeTo(null);

        grid = new boolean[SIZE][SIZE];
        initializeGrid();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
            }
        };
        panel.setPreferredSize(new Dimension(SIZE * CELL_SIZE, SIZE * CELL_SIZE));
        add(panel);

        setupButtonPanel();

        pack();
        setVisible(true);
    }

    private void setupButtonPanel() {
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> startSimulation());

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopSimulation());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            resetGrid();
            initializeGrid();
            repaint();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameOfLife::new);
    }

    private void initializeGrid() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = Math.random() > 0.5;
            }
        }
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j]) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void startSimulation() {
        if (timer == null) {
            timer = new Timer(100, e -> {
                updateGrid();
                repaint();
            });
            timer.start();
        }
    }

    private void stopSimulation() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private void updateGrid() {
        boolean[][] newGrid = new boolean[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int neighbors = countNeighbors(i, j);

                if (grid[i][j]) {
                    newGrid[i][j] = neighbors >= 2 && neighbors <= 3;
                } else {
                    if (neighbors == 3) {
                        newGrid[i][j] = true;
                    }
                }
            }
        }

        grid = newGrid;
    }

    private int countNeighbors(int x, int y) {
        int count = 0;
        int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};

        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE && grid[nx][ny]) {
                count++;
            }
        }

        return count;
    }

    private void resetGrid() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = false;
            }
        }
    }
}
