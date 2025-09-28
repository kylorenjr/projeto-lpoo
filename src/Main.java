import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Garante que a criação da GUI seja feita na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tower Defense - Versão Swing");

            // Adiciona o painel do jogo à janela
            frame.add(new GamePanel());

            // Configurações da janela
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa ao fechar a janela
            frame.setResizable(false); // Impede que o usuário redimensione a janela
            frame.pack(); // Ajusta o tamanho da janela ao tamanho do GamePanel
            frame.setLocationRelativeTo(null); // Centraliza a janela na tela
            frame.setVisible(true); // Torna a janela visível
        });
    }
}