public class Canhao {
    private Projetil projetil;

    public Canhao() {
        this.projetil = new Projetil();
    }

    public void atirar(Inimigos alvo) {
        if (alvo != null && alvo.estaVivo()) {
            System.out.println("Canhão atirando em " + alvo.getClass().getSimpleName() + "!");
            alvo.levarDano(this.projetil.getDano());
        }
    }

    public Projetil getProjetil() {
        return this.projetil;
    }

    public void exibirInfo() {
        System.out.println("--- Status do Canhão ---");
        this.projetil.exibirInfo();
        System.out.println("------------------------");
    }
}