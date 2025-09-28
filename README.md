# projeto-lpoo
Projeto referente a disciplina de programação Orientada a Objetos da UPE
````mermaid
classDiagram
    class Main {
        +main(String[] args)
    }
    class GamePanel {
        -Game game
        -Timer timer
        +paintComponent(Graphics g)
        +actionPerformed(ActionEvent e)
    }
    class Game {
        -Mapa mapa
        -Base base
        -List~Inimigos~ inimigosEmCampo
        +update()
    }
    class Inimigos {
        <<Abstract>>
        #double vida
        #int dano
        +mover()
        +levarDano(int)
        +estaVivo()
    }
    class HogRider {
    }
    class Canhao {
        -Projetil projetil
        +atirar(Inimigos alvo)
    }
    class Projetil {
        -int dano
        +evoluir()
    }
    class Mapa {
        -List~int[]~ caminho
    }
    class Base {
        -int vida
        +levarDano(int)
        +estaDestruida()
    }

    Main ..> GamePanel : cria
    GamePanel --|> JPanel
    GamePanel *-- Game : controla
    Game "1" o-- "0..*" Inimigos : gerencia
    Game *-- Mapa : tem
    Game *-- Base : tem
    HogRider --|> Inimigos
    Canhao *-- "1" Projetil : tem
    Canhao ..> Inimigos : atira em
