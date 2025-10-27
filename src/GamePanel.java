import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {

    public static int TILE_SIZE;
    public static int GRID_WIDTH = 12;
    public static int GRID_HEIGHT = 8;

    private final Game game;
    private final Timer timer;

    private String modo = "JOGAR";
    private Torre torreSelecionada = null;

    // Botões da UI
    private Rectangle botComprarCanhao;
    private Rectangle botComprarMetralhadora;
    private Rectangle botVender;
    private Rectangle botSair;

    // Armazena os offsets para centralizar o grid
    private int offsetX;
    private int offsetY;

    // Cores modernas
    private final Color COR_GRAMA_1 = new Color(120, 180, 80);
    private final Color COR_GRAMA_2 = new Color(100, 160, 60);
    private final Color COR_CAMINHO = new Color(210, 180, 140);
    private final Color COR_BASE = new Color(80, 80, 100);
    private final Color COR_UI_BG = new Color(30, 30, 40, 200);
    private final Color COR_BOTAO_NORMAL = new Color(70, 130, 180);
    private final Color COR_BOTAO_HOVER = new Color(100, 160, 210);
    private final Color COR_BOTAO_SELECIONADO = new Color(255, 215, 0);

    public GamePanel(int screenWidth, int screenHeight) {
        // 1. Calcula o TILE_SIZE
        TILE_SIZE = Math.min(screenWidth / GRID_WIDTH, screenHeight / GRID_HEIGHT);

        // 2. Define o tamanho do painel para a tela inteira
        setPreferredSize(new Dimension(screenWidth, screenHeight));

        // 3. Calcula os offsets (espaços pretos)
        this.offsetX = (screenWidth - (GRID_WIDTH * TILE_SIZE)) / 2;
        this.offsetY = (screenHeight - (GRID_HEIGHT * TILE_SIZE)) / 2;

        this.game = new Game();
        this.timer = new Timer(16, this); // ~60 FPS
        this.timer.start();

        // 4. Inicializa a UI
        initUIElements(screenWidth, screenHeight);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

        // Mouse motion listener para efeitos hover
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                repaint(); // Redesenha para efeitos hover
            }
        });
    }

    private void initUIElements(int screenWidth, int screenHeight) {
        int buttonWidth = (int)(TILE_SIZE * 2.8);
        int buttonHeight = (int)(TILE_SIZE * 0.7);
        int margin = (int)(TILE_SIZE * 0.4);

        // Posição X no canto direito
        int buttonX = screenWidth - buttonWidth - margin;

        // Posições Y verticais
        int buttonY_1 = margin + 50; // Espaço para o título
        int buttonY_2 = buttonY_1 + buttonHeight + margin;
        int buttonY_3 = buttonY_2 + buttonHeight + margin;
        int buttonY_4 = buttonY_3 + buttonHeight + margin;

        botComprarCanhao = new Rectangle(buttonX, buttonY_1, buttonWidth, buttonHeight);
        botComprarMetralhadora = new Rectangle(buttonX, buttonY_2, buttonWidth, buttonHeight);
        botVender = new Rectangle(buttonX, buttonY_3, buttonWidth, buttonHeight);
        botSair = new Rectangle(buttonX, buttonY_4, buttonWidth, buttonHeight);
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        // Handle UI button clicks
        if (botComprarCanhao.contains(mouseX, mouseY)) {
            modo = "COMPRAR_CANHAO";
            torreSelecionada = null;
            return;
        }
        if (botComprarMetralhadora.contains(mouseX, mouseY)) {
            modo = "COMPRAR_METRALHADORA";
            torreSelecionada = null;
            return;
        }
        if (botVender.contains(mouseX, mouseY)) {
            modo = "VENDER";
            torreSelecionada = null;
            return;
        }
        if (botSair.contains(mouseX, mouseY)) {
            System.exit(0);
            return;
        }

        // Handle grid clicks
        int gridX = (mouseX - offsetX) / TILE_SIZE;
        int gridY = (mouseY - offsetY) / TILE_SIZE;

        if (gridX < 0 || gridX >= GRID_WIDTH || gridY < 0 || gridY >= GRID_HEIGHT) {
            modo = "JOGAR";
            torreSelecionada = null;
            return;
        }

        switch (modo) {
            case "COMPRAR_CANHAO":
                if (game.comprarTorre("CANHAO", gridX, gridY)) {
                    modo = "JOGAR";
                }
                break;
            case "COMPRAR_METRALHADORA":
                if (game.comprarTorre("METRALHADORA", gridX, gridY)) {
                    modo = "JOGAR";
                }
                break;
            case "VENDER":
                Torre t = game.getTorreEm(gridX, gridY);
                if (t != null) game.venderTorre(t);
                modo = "JOGAR";
                break;
            case "JOGAR":
                torreSelecionada = game.getTorreEm(gridX, gridY);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.update();
        repaint();
        if (game.getBase().estaDestruida() || game.todasWavesCompletas()) {
            timer.stop();
        }
    }

    private void drawButton(Graphics2D g2d, Rectangle rect, String text, boolean selected, boolean hover) {
        // Gradiente do botão
        Color topColor, bottomColor;

        if (selected) {
            topColor = COR_BOTAO_SELECIONADO;
            bottomColor = new Color(200, 170, 0);
        } else if (hover) {
            topColor = COR_BOTAO_HOVER;
            bottomColor = new Color(60, 110, 150);
        } else {
            topColor = COR_BOTAO_NORMAL;
            bottomColor = new Color(50, 90, 130);
        }

        GradientPaint gradient = new GradientPaint(
                (float)rect.x, (float)rect.y, topColor,
                (float)rect.x, (float)rect.y + (float)rect.height, bottomColor
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Borda
        g2d.setColor(selected ? Color.YELLOW : new Color(40, 40, 40));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        // Texto
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, (int)(TILE_SIZE * 0.2)));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + (rect.height + fm.getAscent()) / 2 - 2;
        g2d.drawString(text, textX, textY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fundo com gradiente
        GradientPaint backgroundGradient = new GradientPaint(0, 0, new Color(20, 20, 30), 0, getHeight(), new Color(40, 40, 60));
        g2d.setPaint(backgroundGradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 1. Desenha o Tabuleiro (Grid) com sombras
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                int x = offsetX + col * TILE_SIZE;
                int y = offsetY + row * TILE_SIZE;

                // Sombra
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRect(x + 3, y + 3, TILE_SIZE, TILE_SIZE);

                // Tile
                if (game.getMapa().isLocalNoCaminho(col, row)) {
                    g2d.setColor(COR_CAMINHO);
                } else {
                    g2d.setColor((row + col) % 2 == 0 ? COR_GRAMA_1 : COR_GRAMA_2);
                }
                g2d.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                // Borda do tile
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }

        // 2. Destacar base
        List<int[]> caminho = game.getMapa().getCaminho();
        int[] endPoint = caminho.get(caminho.size() - 1);
        int baseX = offsetX + endPoint[0] * TILE_SIZE;
        int baseY = offsetY + endPoint[1] * TILE_SIZE;

        // Base com gradiente
        GradientPaint baseGradient = new GradientPaint(
                baseX, baseY, COR_BASE,
                baseX + TILE_SIZE, baseY + TILE_SIZE, new Color(100, 100, 120)
        );
        g2d.setPaint(baseGradient);
        g2d.fillRect(baseX, baseY, TILE_SIZE, TILE_SIZE);

        // Detalhes da base
        g2d.setColor(new Color(200, 200, 220));
        g2d.fillRect(baseX + TILE_SIZE/4, baseY + TILE_SIZE/4, TILE_SIZE/2, TILE_SIZE/2);
        g2d.setColor(new Color(60, 60, 80));
        g2d.drawRect(baseX, baseY, TILE_SIZE, TILE_SIZE);

        // 3. Desenhar Torres
        for (Torre torre : game.getTorresEmCampo()) {
            torre.draw(g2d, TILE_SIZE, offsetX, offsetY);
            if (torre == torreSelecionada) {
                // Alcance com efeito de pulso
                float alpha = (float)(0.3 + 0.2 * Math.sin(System.currentTimeMillis() * 0.005));
                g2d.setColor(new Color(255, 255, 0, (int)(alpha * 255)));
                int alcance = torre.getAlcance();
                int pixelX = offsetX + torre.getX() * TILE_SIZE + TILE_SIZE / 2;
                int pixelY = offsetY + torre.getY() * TILE_SIZE + TILE_SIZE / 2;
                g2d.fillOval(pixelX - alcance, pixelY - alcance, alcance * 2, alcance * 2);
            }
        }

        // 4. Desenha os Inimigos
        for (Inimigos inimigo : game.getInimigosEmCampo()) {
            inimigo.draw(g2d, offsetX, offsetY, game.getMapa());
        }

        // 5. Desenhar Projéteis
        for (Projetil p : game.getProjeteisEmCampo()) {
            p.draw(g2d, offsetX, offsetY);
        }

        // 6. HUD PRINCIPAL - Vida, Dinheiro e Waves (canto superior esquerdo do GRID)
        g2d.setColor(COR_UI_BG);
        g2d.fillRoundRect(offsetX + 10, offsetY + 10, 200, 90, 15, 15);
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.drawRoundRect(offsetX + 10, offsetY + 10, 200, 90, 15, 15);

        // Informações principais com fonte maior e mais visível
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, (int)(TILE_SIZE * 0.3)));

        int startY = offsetY + 35;
        int lineHeight = 25;

        g2d.drawString("Vida da Base: " + game.getBase().getVida(), offsetX + 20, startY);
        g2d.drawString("Dinheiro: $" + game.getDinheiro(), offsetX + 20, startY + lineHeight);
        g2d.drawString("Wave: " + game.getOnda() + "/3", offsetX + 20, startY + lineHeight * 2);

        // 7. Painel de Informações da Wave (abaixo do HUD principal)
        g2d.setColor(COR_UI_BG);
        g2d.fillRoundRect(offsetX + 10, offsetY + 110, 200, 50, 15, 15);
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.drawRoundRect(offsetX + 10, offsetY + 110, 200, 50, 15, 15);

        g2d.setFont(new Font("Arial", Font.BOLD, (int)(TILE_SIZE * 0.25)));
        if (game.isWaveEmAndamento()) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Inimigos: " + game.getInimigosRestantesNaOnda(), offsetX + 20, offsetY + 135);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Wave em andamento", offsetX + 20, offsetY + 155);
        } else if (game.getOnda() < 3) {
            long tempoRestante = Math.max(0, (game.getTempoEntreWaves() - System.currentTimeMillis()) / 1000 + 1);
            g2d.setColor(Color.GREEN);
            g2d.drawString("Próxima wave: " + tempoRestante + "s", offsetX + 20, offsetY + 140);
        } else if (game.todasWavesCompletas()) {
            g2d.setColor(Color.GREEN);
            g2d.drawString("Todas waves", offsetX + 20, offsetY + 135);
            g2d.drawString("completas!", offsetX + 20, offsetY + 155);
        }

        // 8. UI de Botões (direita)
        Point mousePos = getMousePosition();
        boolean hoverCanhao = mousePos != null && botComprarCanhao.contains(mousePos);
        boolean hoverMetralhadora = mousePos != null && botComprarMetralhadora.contains(mousePos);
        boolean hoverVender = mousePos != null && botVender.contains(mousePos);
        boolean hoverSair = mousePos != null && botSair.contains(mousePos);

        // Título
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, (int)(TILE_SIZE * 0.4)));
        String titulo = "TOWER DEFENSE";
        FontMetrics fmTitulo = g2d.getFontMetrics();
        int tituloX = botComprarCanhao.x + (botComprarCanhao.width - fmTitulo.stringWidth(titulo)) / 2;
        g2d.drawString(titulo, tituloX, offsetY + 30);

        // Botões
        drawButton(g2d, botComprarCanhao, "CANHÃO ($100)", modo.equals("COMPRAR_CANHAO"), hoverCanhao);
        drawButton(g2d, botComprarMetralhadora, "METRALHADORA ($75)", modo.equals("COMPRAR_METRALHADORA"), hoverMetralhadora);
        drawButton(g2d, botVender, "VENDER TORRE", modo.equals("VENDER"), hoverVender);
        drawButton(g2d, botSair, "SAIR", false, hoverSair);

        // Info da Torre Selecionada
        if (torreSelecionada != null) {
            g2d.setColor(COR_UI_BG);
            g2d.fillRoundRect(botSair.x, botSair.y + botSair.height + 10, botSair.width, 60, 15, 15);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, (int)(TILE_SIZE * 0.18)));
            g2d.drawString("Venda: $" + torreSelecionada.getValorVenda(), botSair.x + 10, botSair.y + botSair.height + 35);
            g2d.drawString("Dano: " + torreSelecionada.getDano(), botSair.x + 10, botSair.y + botSair.height + 55);
        }

        // 9. Game Over
        if (game.getBase().estaDestruida()) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, (int)(TILE_SIZE * 1.2)));
            String s = "GAME OVER";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(s, (getWidth() - fm.stringWidth(s)) / 2, getHeight() / 2);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, (int)(TILE_SIZE * 0.4)));
            String sub = "Base destruída!";
            g2d.drawString(sub, (getWidth() - g2d.getFontMetrics().stringWidth(sub)) / 2, getHeight() / 2 + 50);
        }

        // 10. Vitória
        if (game.todasWavesCompletas()) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.GREEN);
            g2d.setFont(new Font("Arial", Font.BOLD, (int)(TILE_SIZE * 1.2)));
            String s = "VITÓRIA!";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(s, (getWidth() - fm.stringWidth(s)) / 2, getHeight() / 2);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, (int)(TILE_SIZE * 0.4)));
            String sub = "Todas as waves completadas!";
            g2d.drawString(sub, (getWidth() - g2d.getFontMetrics().stringWidth(sub)) / 2, getHeight() / 2 + 50);
        }
    }
}