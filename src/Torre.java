import java.awt.Graphics;
import java.util.List;

public abstract class Torre {
    protected int x, y;
    protected int custo;
    protected int alcance;
    protected int dano;
    protected long cadencia;
    protected long ultimoAtaque;

    public Torre(int x, int y) {
        this.x = x;
        this.y = y;
        this.ultimoAtaque = System.currentTimeMillis();
    }

    public abstract Projetil atacar(List<Inimigos> inimigos, Mapa mapa);

    public int getValorVenda() {
        return (int)(this.custo * 0.7);
    }

    public abstract void draw(Graphics g, int tileSize, int offsetX, int offsetY);

    public int getX() { return x; }
    public int getY() { return y; }
    public int getCusto() { return custo; }
    public int getAlcance() { return alcance; }
    public int getDano() { return dano; }
}