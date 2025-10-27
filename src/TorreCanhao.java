import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.util.List;

public class TorreCanhao extends Torre {

    public TorreCanhao(int x, int y) {
        super(x, y);
        this.custo = 100;
        this.alcance = (int)(GamePanel.TILE_SIZE * 2.5);
        this.dano = 25;
        this.cadencia = 2000;
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
                return new Projetil(torrePixelX, torrePixelY, this.dano, 7, Color.ORANGE, inimigo, mapa);
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
                xPos, yPos, new Color(180, 60, 60),
                xPos + tileSize, yPos + tileSize, new Color(120, 40, 40)
        );
        g2d.setPaint(baseGradient);
        g2d.fillRoundRect(xPos + 5, yPos + 5, tileSize - 10, tileSize - 10, 15, 15);

        // Cano da torre
        g2d.setColor(new Color(80, 80, 80));
        g2d.fillRect(xPos + tileSize/2 - 3, yPos + 10, 6, tileSize/2);

        // Detalhes
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillOval(xPos + tileSize/2 - 8, yPos + tileSize/2 - 8, 16, 16);

        // Borda
        g2d.setColor(new Color(40, 40, 40));
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawRoundRect(xPos + 5, yPos + 5, tileSize - 10, tileSize - 10, 15, 15);
    }
}