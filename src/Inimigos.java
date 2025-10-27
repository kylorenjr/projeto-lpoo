import java.awt.Graphics;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.util.List;

public abstract class Inimigos {
    protected int vida;
    protected int maxVida;
    protected int dano;
    protected int recompensa;
    private int waypointIndex;
    private long lastMoveTime;

    public Inimigos(int vida, int dano, int recompensa) {
        this.vida = vida;
        this.maxVida = vida;
        this.dano = dano;
        this.recompensa = recompensa;
        this.waypointIndex = 0;
        this.lastMoveTime = System.currentTimeMillis();
    }

    public void mover(List<int[]> caminho) {
        long now = System.currentTimeMillis();
        if (now - lastMoveTime > 1000) {
            if (waypointIndex < caminho.size() - 1) {
                this.waypointIndex++;
                this.lastMoveTime = now;
            }
        }
    }

    public int getPixelX(Mapa mapa) {
        int[] posGrid = mapa.getCaminho().get(this.waypointIndex);
        return posGrid[0] * GamePanel.TILE_SIZE + GamePanel.TILE_SIZE / 2;
    }

    public int getPixelY(Mapa mapa) {
        int[] posGrid = mapa.getCaminho().get(this.waypointIndex);
        return posGrid[1] * GamePanel.TILE_SIZE + GamePanel.TILE_SIZE / 2;
    }

    public void draw(Graphics g, int offsetX, int offsetY, Mapa mapa) {
        Graphics2D g2d = (Graphics2D) g;

        int[] pos = mapa.getCaminho().get(this.waypointIndex);
        int x = offsetX + pos[0] * GamePanel.TILE_SIZE;
        int y = offsetY + pos[1] * GamePanel.TILE_SIZE;

        // Corpo principal (Hog Rider)
        g2d.setColor(new Color(160, 120, 80)); // Marrom mais escuro
        g2d.fillRoundRect(x + 15, y + 25, 34, 18, 10, 10);

        // Cabeça
        g2d.setColor(new Color(240, 200, 160)); // Pele
        g2d.fillOval(x + 20, y + 15, 24, 24);

        // Detalhes do capacete
        g2d.setColor(new Color(60, 60, 60)); // Capacete
        g2d.fillArc(x + 18, y + 12, 28, 18, 0, 180);

        // Olhos
        g2d.setColor(Color.RED);
        g2d.fillOval(x + 26, y + 22, 4, 4);
        g2d.fillOval(x + 34, y + 22, 4, 4);

        // Chifres
        g2d.setColor(new Color(150, 150, 150));
        g2d.fillOval(x + 15, y + 18, 8, 5);
        g2d.fillOval(x + 41, y + 18, 8, 5);

        // Barra de vida
        if (this.vida > 0) {
            int barraVidaLargura = 40;
            int barraVidaAltura = 6;
            int barraVidaX = x + (GamePanel.TILE_SIZE - barraVidaLargura) / 2;
            int barraVidaY = y - 12;

            // Fundo da barra
            g2d.setColor(new Color(60, 0, 0));
            g2d.fillRect(barraVidaX, barraVidaY, barraVidaLargura, barraVidaAltura);

            // Vida atual
            int vidaAtualLargura = (int) ((double) this.vida / this.maxVida * barraVidaLargura);
            GradientPaint vidaGradient = new GradientPaint(
                    barraVidaX, barraVidaY, Color.RED,
                    barraVidaX + vidaAtualLargura, barraVidaY, Color.GREEN
            );
            g2d.setPaint(vidaGradient);
            g2d.fillRect(barraVidaX, barraVidaY, vidaAtualLargura, barraVidaAltura);

            // Borda
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(barraVidaX, barraVidaY, barraVidaLargura, barraVidaAltura);
        }
    }

    public void levarDano(int danoRecebido) {
        this.vida -= danoRecebido;
        if (this.vida < 0) this.vida = 0;
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public int getDano() {
        return this.dano;
    }

    public int getWaypointIndex() {
        return this.waypointIndex;
    }

    public int getRecompensa() {
        return this.recompensa;
    }
}