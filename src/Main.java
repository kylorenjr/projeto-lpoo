import javax.swing.*;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tower Defense - Versão Swing");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setUndecorated(true);

            // Pega as dimensões da tela
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            // Passa as dimensões da tela para o GamePanel para que ele calcule o TILE_SIZE
            GamePanel gamePanel = new GamePanel(screenWidth, screenHeight);

            frame.add(gamePanel);

            // Garante que o frame tem o tamanho do GamePanel, que agora ocupa a tela toda
            frame.pack();

            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}