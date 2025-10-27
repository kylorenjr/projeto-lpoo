import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {
    private final Mapa mapa;
    private final Base base;
    private final List<Inimigos> inimigosEmCampo;
    private final List<Torre> torresEmCampo;
    private final List<Projetil> projeteisEmCampo;
    private int dinheiro;
    private int onda;
    private int inimigosRestantesNaOnda;
    private long lastSpawnTime;
    private boolean waveEmAndamento;
    private long tempoEntreWaves;

    public Game() {
        this.mapa = new Mapa();
        this.base = new Base();
        this.inimigosEmCampo = new ArrayList<>();
        this.torresEmCampo = new ArrayList<>();
        this.projeteisEmCampo = new ArrayList<>();
        this.dinheiro = 200;
        this.onda = 0;
        this.inimigosRestantesNaOnda = 0;
        this.waveEmAndamento = false;
        this.tempoEntreWaves = System.currentTimeMillis() + 3000;
        this.lastSpawnTime = System.currentTimeMillis();
    }

    public void update() {
        // 1. Verificar se precisa iniciar uma nova wave
        if (!waveEmAndamento && inimigosEmCampo.isEmpty() && System.currentTimeMillis() > tempoEntreWaves) {
            iniciarProximaWave();
        }

        // 2. Spawn de inimigos da wave atual
        if (waveEmAndamento && inimigosRestantesNaOnda > 0) {
            long now = System.currentTimeMillis();
            if ((now - lastSpawnTime) > getTempoSpawnWave()) {
                inimigosEmCampo.add(new HogRider());
                lastSpawnTime = now;
                inimigosRestantesNaOnda--;
            }
        }

        // 3. Verificar se a wave terminou
        if (waveEmAndamento && inimigosRestantesNaOnda == 0 && inimigosEmCampo.isEmpty()) {
            waveEmAndamento = false;
            if (onda < 3) {
                tempoEntreWaves = System.currentTimeMillis() + 5000;
            }
        }

        // 4. Torres atacam (criam projéteis)
        List<Projetil> novosProjeteis = new ArrayList<>();
        for (Torre torre : torresEmCampo) {
            Projetil p = torre.atacar(inimigosEmCampo, mapa);
            if (p != null) {
                novosProjeteis.add(p);
            }
        }
        projeteisEmCampo.addAll(novosProjeteis);

        // 5. Atualizar Projéteis
        Iterator<Projetil> projIt = projeteisEmCampo.iterator();
        while (projIt.hasNext()) {
            Projetil p = projIt.next();
            p.update();
            if (!p.estaAtivo()) {
                projIt.remove();
            }
        }

        // 6. Lógica de Movimento, Ataque à Base e Morte
        Iterator<Inimigos> it = inimigosEmCampo.iterator();
        while (it.hasNext()) {
            Inimigos inimigo = it.next();

            if (!inimigo.estaVivo()) {
                this.dinheiro += inimigo.getRecompensa();
                it.remove();
                continue;
            }

            inimigo.mover(mapa.getCaminho());

            if (mapa.chegouAoFim(inimigo)) {
                base.levarDano(inimigo.getDano());
                it.remove();
            }
        }
    }

    private void iniciarProximaWave() {
        if (onda < 3) {
            onda++;
            waveEmAndamento = true;

            switch (onda) {
                case 1:
                    inimigosRestantesNaOnda = 5;
                    break;
                case 2:
                    inimigosRestantesNaOnda = 8;
                    break;
                case 3:
                    inimigosRestantesNaOnda = 12;
                    break;
            }

            lastSpawnTime = System.currentTimeMillis();
        }
    }

    private long getTempoSpawnWave() {
        switch (onda) {
            case 1: return 2000;
            case 2: return 1500;
            case 3: return 1000;
            default: return 2000;
        }
    }

    public boolean jogoTerminou() {
        return base.estaDestruida() || (onda >= 3 && !waveEmAndamento && inimigosEmCampo.isEmpty());
    }

    public boolean todasWavesCompletas() {
        return onda >= 3 && !waveEmAndamento && inimigosEmCampo.isEmpty();
    }

    public long getTempoEntreWaves() {
        return this.tempoEntreWaves;
    }

    // --- Getters ---
    public int getDinheiro() { return this.dinheiro; }
    public List<Torre> getTorresEmCampo() { return this.torresEmCampo; }
    public List<Projetil> getProjeteisEmCampo() { return this.projeteisEmCampo; }
    public Base getBase() { return base; }
    public Mapa getMapa() { return mapa; }
    public List<Inimigos> getInimigosEmCampo() { return inimigosEmCampo; }
    public int getOnda() { return onda; }
    public boolean isWaveEmAndamento() { return waveEmAndamento; }
    public int getInimigosRestantesNaOnda() { return inimigosRestantesNaOnda; }

    // --- Métodos de Compra/Venda ---
    public boolean isTorreNoLocal(int x, int y) {
        for (Torre t : torresEmCampo) {
            if (t.getX() == x && t.getY() == y) return true;
        }
        return false;
    }

    public Torre getTorreEm(int x, int y) {
        for (Torre t : torresEmCampo) {
            if (t.getX() == x && t.getY() == y) return t;
        }
        return null;
    }

    public boolean comprarTorre(String tipo, int x, int y) {
        if (mapa.isLocalNoCaminho(x, y)) return false;
        if (isTorreNoLocal(x, y)) return false;

        Torre novaTorre = null;
        if (tipo.equals("CANHAO")) {
            novaTorre = new TorreCanhao(x, y);
        } else if (tipo.equals("METRALHADORA")) {
            novaTorre = new TorreMetralhadora(x, y);
        }

        if (novaTorre == null) return false;

        if (this.dinheiro >= novaTorre.getCusto()) {
            this.dinheiro -= novaTorre.getCusto();
            this.torresEmCampo.add(novaTorre);
            return true;
        }
        return false;
    }

    public void venderTorre(Torre torre) {
        if (torre != null) {
            this.dinheiro += torre.getValorVenda();
            this.torresEmCampo.remove(torre);
        }
    }
}