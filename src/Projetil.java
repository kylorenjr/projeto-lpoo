import java.awt.Color;
import java.awt.Graphics;

public class Projetil {
    private int x, y;
    private int dano;
    private int velocidade;
    private Inimigos alvo;
    private Mapa mapa;
    private boolean ativo;
    private Color cor;

    // Tamanho do projétil para cálculo de colisão
    private static final int PROJETIL_RAIO = 3;

    public Projetil(int startX, int startY, int dano, int velocidade, Color cor, Inimigos alvo, Mapa mapa) {
        this.x = startX;
        this.y = startY;
        this.dano = dano;
        this.velocidade = velocidade;
        this.cor = cor;
        this.alvo = alvo;
        this.mapa = mapa;
        this.ativo = true;
    }

    public void update() {
        if (!ativo) return;

        if (alvo == null || !alvo.estaVivo()) {
            this.ativo = false;
            return;
        }

        //Encontrar a Posição do Alvo (em pixels, centro do inimigo)
        int targetX = alvo.getPixelX(mapa);
        int targetY = alvo.getPixelY(mapa);

        //Calcular a direção
        double dx = targetX - this.x;
        double dy = targetY - this.y;
        double distancia = Math.sqrt(dx * dx + dy * dy);

        // Checar colisão
        if (distancia <= this.velocidade + (GamePanel.TILE_SIZE / 4)) { // Adjust this value for hit accuracy
            alvo.levarDano(this.dano);
            this.ativo = false;
        } else {
            //Mover o projétil
            this.x += (int) (dx / distancia * velocidade);
            this.y += (int) (dy / distancia * velocidade);
        }
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        if (!ativo) return;
        g.setColor(this.cor);
        g.fillOval(offsetX + x - PROJETIL_RAIO, offsetY + y - PROJETIL_RAIO, PROJETIL_RAIO * 2, PROJETIL_RAIO * 2);
    }

    public boolean estaAtivo() {
        return this.ativo;
    }
}