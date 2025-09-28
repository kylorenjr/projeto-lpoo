import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {
    private final Mapa mapa;
    private final Base base;
    private final List<Inimigos> inimigosEmCampo;
    private int onda;
    private long lastSpawnTime;

    public Game() {
        this.mapa = new Mapa();
        this.base = new Base();
        this.inimigosEmCampo = new ArrayList<>();
        this.onda = 1;
        this.lastSpawnTime = System.currentTimeMillis();
    }

    public void update() {
        // Lógica de Spawn: cria um novo inimigo a cada 3 segundos
        long now = System.currentTimeMillis();
        if ((now - lastSpawnTime) > 3000) {
            inimigosEmCampo.add(new HogRider());
            lastSpawnTime = now;
        }

        // Lógica de Movimento e Ataque à Base
        Iterator<Inimigos> it = inimigosEmCampo.iterator();
        while (it.hasNext()) {
            Inimigos inimigo = it.next();
            inimigo.mover(mapa.getCaminho());

            if (mapa.chegouAoFim(inimigo)) {
                base.levarDano(inimigo.getDano());
                it.remove();
            }
        }
    }

    public Base getBase() { return base; }
    public Mapa getMapa() { return mapa; }
    public List<Inimigos> getInimigosEmCampo() { return inimigosEmCampo; }
    public int getOnda() { return onda; }
}