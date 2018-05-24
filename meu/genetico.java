class Genetico{
    private int nRodadas;               // Nº maximo de interações
    private int nRodSemAlt;             // Nº de interações sem fazer alteração
    private int populacao;              // Tamanho da população
    private double pc;                  // Porcentagem do Crossover
    private int nPais;                  // Nº de pais
    private double pm;                  // Porcentagem de sobreviventes apos mutação
    private int nMutacoes;              // Nº de mutações
    private int beta;                   // Selection Pressure Utilizado para Roleta
    // private double pIndivSobrevive;  // Porcentagem de individuos sobreviventes apos o nRodadas
    private double nIndivSobrevive;     // Nº de individuos sobreviventes apos o nRodadas
    public double pMaiorDist;          //Porcentagem de links de um individuo que pode exceder a distancia maxima permitida

    public Genetico(int a, int b, int c, double d, double e, int f, double j){
        this.nRodadas = a;
        this.nRodSemAlt = b;
        this.populacao = c;
        this.pc = d;
        this.nPais = (int) (2 * Math.round(this.pc * this.populacao / 2));
        this.pm = e;
        this.nMutacoes = (int) Math.round(this.pm * this.populacao);
        this.beta = f;
        this.nIndivSobrevive = (int) (2 * Math.round(0.1 * this.populacao / 2));
        this.pMaiorDist = j;
    }

}
