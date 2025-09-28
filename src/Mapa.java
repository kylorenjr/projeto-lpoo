import java.util.ArrayList;
import java.util.List;

public class Mapa {

    private final List<int[]> caminho;

    public Mapa() {
        this.caminho = new ArrayList<>();
        // Define um caminho fixo e simples. Cada {x, y} é um ponto no grid.
        caminho.add(new int[]{0, 5}); // Ponto inicial (Spawn)
        caminho.add(new int[]{1, 5});
        caminho.add(new int[]{2, 5});
        caminho.add(new int[]{3, 5});
        caminho.add(new int[]{4, 5});
        caminho.add(new int[]{5, 5}); // Ponto final (Base)
    }

    public List<int[]> getCaminho() {
        return this.caminho;
    }

    public boolean chegouAoFim(Inimigos inimigo) {
        return inimigo.getWaypointIndex() >= this.caminho.size() - 1;
    }
}