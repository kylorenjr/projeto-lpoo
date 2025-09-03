public class Projetil {
    private int dano;
    private int nivel;
    private int custoEvolucao;

    public Projetil() {
        this.dano = 10;
        this.nivel = 1;
        this.custoEvolucao = 50;
    }

    public int getDano() {
        return this.dano;
    }

    public int getCustoEvolucao() {
        return this.custoEvolucao;
    }

    public void evoluir() {
        this.nivel++;
        this.dano += 15;
        this.custoEvolucao *= 2;
        System.out.println("Projétil evoluiu para o nível " + this.nivel + "! Dano atual: " + this.dano);
    }

    public void exibirInfo() {
        System.out.println("Projétil Nível " + this.nivel + " | Dano: " + this.dano + " | Custo para Próxima Evolução: " + this.custoEvolucao + " moedas");
    }
}