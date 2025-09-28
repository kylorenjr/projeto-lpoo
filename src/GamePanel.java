import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {

    private static final int TILE_SIZE = 64;
    private static final int GRID_WIDTH = 12;
    private static final int GRID_HEIGHT = 8;

    private final Game game;
    private final Timer timer;

    public GamePanel() {
        this.game = new Game();
        setPreferredSize(new Dimension(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE));
        this.timer = new Timer(16, this);
        this.timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.update();
        repaint();
        if (game.getBase().estaDestruida()) {
            timer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g; // Usar Graphics2D para mais opções

        // Padrão de Tabuleiro (Checkerboard)
        Color color1 = new Color(34, 139, 34); // Verde floresta
        Color color2 = new Color(0, 100, 0);   // Verde escuro
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                if ((row + col) % 2 == 0) {
                    g2d.setColor(color1);
                } else {
                    g2d.setColor(color2);
                }
                g2d.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Linhas do Grid
        g2d.setColor(new Color(0, 0, 0, 50)); // Preto com transparência
        for (int x = 0; x <= GRID_WIDTH * TILE_SIZE; x += TILE_SIZE) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y <= GRID_HEIGHT * TILE_SIZE; y += TILE_SIZE) {
            g2d.drawLine(0, y, getWidth(), y);
        }

        // Caminho
        g2d.setColor(new Color(189, 154, 122)); // Cor de terra/areia
        for (int[] point : game.getMapa().getCaminho()) {
            g2d.fillRect(point[0] * TILE_SIZE, point[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Base
        List<int[]> caminho = game.getMapa().getCaminho();
        int[] endPoint = caminho.get(caminho.size() - 1);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(endPoint[0] * TILE_SIZE, endPoint[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);


        for (Inimigos inimigo : game.getInimigosEmCampo()) {
            int[] pos = game.getMapa().getCaminho().get(inimigo.getWaypointIndex());
            int x = pos[0] * TILE_SIZE;
            int y = pos[1] * TILE_SIZE;

            // Corpo do "Hog" (Porco)
            g2d.setColor(new Color(139, 69, 19)); // Marrom
            g2d.fillOval(x + 8, y + 20, 48, 24);

            // "Rider" (Montador)
            g2d.setColor(new Color(255, 228, 196)); // Cor de pele
            g2d.fillOval(x + 22, y + 8, 20, 20);

            // Contorno para dar definição
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2)); // Linha mais grossa
            g2d.drawOval(x + 8, y + 20, 48, 24);
            g2d.drawOval(x + 22, y + 8, 20, 20);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Vida da Base: " + game.getBase().getVida(), 20, 30);
        g2d.drawString("Onda: " + game.getOnda(), 20, 60);

        if (game.getBase().estaDestruida()) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 50));
            g2d.drawString("GAME OVER", getWidth() / 4, getHeight() / 2);
        }
    }
}