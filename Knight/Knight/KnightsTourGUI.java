import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KnightsTourGUI extends JFrame {
    private static final int TILE_SIZE = 80;
    private int boardSize = 8;
    private int[][] board;
    private boolean[][] visited;
    private int moveCount = 0;
    private int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    private int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};

    private JPanel boardPanel;
    private JTextField sizeField, startXField, startYField;
    private JButton startButton;
    private JLabel infoLabel;

    public KnightsTourGUI() {
        setTitle("Knight's Tour Visualizer");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel controlPanel = new JPanel();
        sizeField = new JTextField("8", 3);
        startXField = new JTextField("0", 3);
        startYField = new JTextField("0", 3);
        startButton = new JButton("Start Tour");
        infoLabel = new JLabel("Board Size: 8");

        controlPanel.add(new JLabel("Board Size:"));
        controlPanel.add(sizeField);
        controlPanel.add(new JLabel("Start X:"));
        controlPanel.add(startXField);
        controlPanel.add(new JLabel("Start Y:"));
        controlPanel.add(startYField);
        controlPanel.add(startButton);
        controlPanel.add(infoLabel);

        add(controlPanel, BorderLayout.NORTH);

        // Board Panel
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (board != null) {
                    for (int i = 0; i < boardSize; i++) {
                        for (int j = 0; j < boardSize; j++) {
                            boolean isLight = (i + j) % 2 == 0;
                            g.setColor(isLight ? Color.WHITE : Color.GRAY);
                            g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                            g.setColor(Color.BLACK);
                            g.drawRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                            if (board[i][j] > 0) {
                                g.setColor(Color.BLUE);
                                g.drawString(String.valueOf(board[i][j]), j * TILE_SIZE + 35, i * TILE_SIZE + 45);
                            }
                            if (board[i][j] == moveCount) {
                                g.setColor(Color.RED);
                                g.fillOval(j * TILE_SIZE + 25, i * TILE_SIZE + 25, 30, 30);
                            }
                        }
                    }
                }
            }
        };
        boardPanel.setPreferredSize(new Dimension(TILE_SIZE * boardSize, TILE_SIZE * boardSize));
        add(boardPanel, BorderLayout.CENTER);

        // Button listener
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardSize = Integer.parseInt(sizeField.getText());
                int startX = Integer.parseInt(startXField.getText());
                int startY = Integer.parseInt(startYField.getText());

                board = new int[boardSize][boardSize];
                visited = new boolean[boardSize][boardSize];
                moveCount = 1;

                new Thread(() -> {
                    boolean success = solve(startX, startY, 1);
                    if (!success) {
                        JOptionPane.showMessageDialog(null, "No solution found!");
                    }
                }).start();
            }
        });

        setVisible(true);
    }

    private boolean solve(int x, int y, int move) {
        if (x < 0 || y < 0 || x >= boardSize || y >= boardSize || visited[x][y]) return false;

        board[x][y] = move;
        visited[x][y] = true;
        moveCount = move;

        boardPanel.repaint();
        try {
            Thread.sleep(100); // Animasi cepat
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (move == boardSize * boardSize) return true;

        for (int i = 0; i < 8; i++) {
            if (solve(x + dx[i], y + dy[i], move + 1)) return true;
        }

        board[x][y] = 0;
        visited[x][y] = false;
        moveCount = move - 1;
        boardPanel.repaint();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(KnightsTourGUI::new);
    }
}
