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
        caminho.add(new int[]{5, 5});
        caminho.add(new int[]{6, 5}); // Aumentei o caminho
        caminho.add(new int[]{7, 5});
        caminho.add(new int[]{8, 5}); // Ponto final (Base)
    }

    public List<int[]> getCaminho() {
        return this.caminho;
    }

    public boolean chegouAoFim(Inimigos inimigo) {
        return inimigo.getWaypointIndex() >= this.caminho.size() - 1;
    }

    /**
     * NOVO: Verifica se uma coordenada (x, y) do grid faz parte do caminho.
     */
    public boolean isLocalNoCaminho(int x, int y) {
        for (int[] ponto : caminho) {
            if (ponto[0] == x && ponto[1] == y) {
                return true;
            }
        }
        return false;
    }
}