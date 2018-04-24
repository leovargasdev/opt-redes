### Simulador de Redes Ópticas Elásticas

Essa ferramenta foi desenvolvida para um trabalho de conclusão de curso (TCC2) do curso de Ciência da Computação na Universidade Federal da Fronteira Sul apresentado no dia 21/07/2016. 

Essa ferramenta recebe uma topologia como entrada e devolve as informações de taxa de bloqueio apresentada pelos algoritmos de roteamento e alocação de espectro desenvolvidos no trabalho. Mais informações podem ser encontradas no arquivo do projeto TCC2 que se encontra nesse repositório.

### Parâmetros

Para iniciar o simulador, é necessário informar a topologia como parâmetro. Um exemplo de arquivo de topologia pode ser visto a seguir:

1 2 260

1 3 252

1 4 324

2 3 380

2 8 868

3 6 416

4 5 248

4 11 1140

5 6 272

5 7 292

6 10 704

Cada linha representa um enlace, a primeira coluna de cada linha é o nó de origem, a segunda o nó de destino e a terceira a distância entre cada nó.

Durante a execução, o simulador irá utilizar os algoritmos de roteamento e alocação de espectro definidos pelo programador para alocar as demandas.

### Prerequisites

JAVA jdk


### Authors

Winder Hipólito

Tailan Bonassi

