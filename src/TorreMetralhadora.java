import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.util.List;

public class TorreMetralhadora extends Torre {

    public TorreMetralhadora(int x, int y) {
        super(x, y);
        this.custo = 75;
        this.alcance = (int)(GamePanel.TILE_SIZE * 2);
        this.dano = 5;
        this.cadencia = 400;
    }

    @Override
    public Projetil atacar(List<Inimigos> inimigos, Mapa mapa) {
        long agora = System.currentTimeMillis();
        if (agora - ultimoAtaque < cadencia) {
            return null;
        }

        int torrePixelX = x * GamePanel.TILE_SIZE + GamePanel.TILE_SIZE / 2;
        int torrePixelY = y * GamePanel.TILE_SIZE + GamePanel.TILE_SIZE / 2;

        for (Inimigos inimigo : inimigos) {
            int inimigoPixelX = inimigo.getPixelX(mapa);
            int inimigoPixelY = inimigo.getPixelY(mapa);

            double dist = Math.sqrt(Math.pow(torrePixelX - inimigoPixelX, 2) + Math.pow(torrePixelY - inimigoPixelY, 2));

            if (dist <= this.alcance) {
                this.ultimoAtaque = agora;
                return new Projetil(torrePixelX, torrePixelY, this.dano, 12, Color.YELLOW, inimigo, mapa);
            }
        }
        return null;
    }

    @Override
    public void draw(Graphics g, int tileSize, int offsetX, int offsetY) {
        Graphics2D g2d = (Graphics2D) g;

        int xPos = offsetX + x * tileSize;
        int yPos = offsetY + y * tileSize;

        // Base da torre
        GradientPaint baseGradient = new GradientPaint(
                xPos, yPos, new Color(60, 100, 180),
                xPos + tileSize, yPos + tileSize, new Color(40, 70, 120)
        );
        g2d.setPaint(baseGradient);
        g2d.fillRoundRect(xPos + 5, yPos + 5, tileSize - 10, tileSize - 10, 20, 20);

        // Canos da metralhadora
        g2d.setColor(new Color(100, 100, 100));
        g2d.fillRect(xPos + tileSize/2 - 8, yPos + 8, 4, tileSize/3);
        g2d.fillRect(xPos + tileSize/2 - 2, yPos + 8, 4, tileSize/3);
        g2d.fillRect(xPos + tileSize/2 + 4, yPos + 8, 4, tileSize/3);

        // Centro
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillOval(xPos + tileSize/2 - 6, yPos + tileSize/2 - 6, 12, 12);

        // Borda
        g2d.setColor(new Color(30, 30, 30));
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawRoundRect(xPos + 5, yPos + 5, tileSize - 10, tileSize - 10, 20, 20);
    }
}