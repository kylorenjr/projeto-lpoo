import java.util.List;

public abstract class Inimigos {
    protected double vida;
    protected int dano;
    private int waypointIndex;
    private long lastMoveTime; // Para controlar a velocidade de movimento

    public Inimigos(double vida, int dano) {
        this.vida = vida;
        this.dano = dano;
        this.waypointIndex = 0;
        this.lastMoveTime = System.currentTimeMillis();
    }

    public void mover(List<int[]> caminho) {
        long now = System.currentTimeMillis();
        // Move a cada 1 segundo (1000 milissegundos)
        if (now - lastMoveTime > 1000) {
            if (waypointIndex < caminho.size() - 1) {
                this.waypointIndex++;
                this.lastMoveTime = now;
            }
        }
    }
    // ... O resto dos métodos (levarDano, estaVivo, getDano, etc.) permanece igual
    public void levarDano(int danoRecebido) {
        this.vida -= danoRecebido;
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
}