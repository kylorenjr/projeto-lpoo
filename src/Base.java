public class Base {
    private int vida;

    public Base() {
        this.vida = 100; // Vida inicial da base
    }

    public void levarDano(int dano) {
        this.vida -= dano;
        if (this.vida < 0) {
            this.vida = 0;
        }
    }

    public int getVida() {
        return this.vida;
    }

    public boolean estaDestruida() {
        return this.vida <= 0;
    }
}