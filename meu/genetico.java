import java.util.*;
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
    public double nIndivSobrevive;      // Nº de individuos sobreviventes apos o nRodadas
    public double pLinksIndiv;          //Porcentagem de links de um individuo que pode exceder a distancia maxima permitida
    private int nRenovacoes;
    private Random hugo = new Random();

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
        this.pLinksIndiv = j;
        this.nRenovacoes = 1;
    }

    public String vConverteInt(int[] vetor){
        String result = "";
        for(int d : vetor)
            result += String.valueOf(d) + " ";
        return result;
    }

    public int[] vConverteString(String[] vetor, int tam){
        int[] result = new int[tam];
        for(int t = 0; t < tam; t++){
            result[t] = Integer.parseInt(vetor[t]);
            System.out.print(result[t] + " ");
        }
        System.out.println();
        return result;
    }

    public double calculaCusto(int[] caminho, int validaCusto, PackGenetico pg){
    	int totalExecedeMaxima = 0;
    	double custo = 0.0;
    	for (int i = 0; i < pg.nNodos; i++){
    		for (int j = i+1; j < pg.nNodos; j++){
                if(pg.conexoes[i][j] == 1){
                    double aux = pg.custos[caminho[i]][caminho[j]];
        			if (validaCusto == 1 && aux > pg.maiorDist)
        				totalExecedeMaxima++;

        			if (totalExecedeMaxima > pg.nNosExeDistMax)
        				return 0;

        			custo += aux;
                }
    		}
    	}
    	return custo;
    }

    public Boolean testaValor(int[] vetor, int valor, int p){
        int aux = 0;
        for(int teste : vetor){
            if(aux == p) return false;
            if(teste == valor) return true;
            aux++;
        }
        return false;
    }

    public void inicializaPopulacao(List<Caminho> caminhos, int subPopulacao, String ci, PackGenetico pg){
        int nPopu = this.populacao - subPopulacao;
        // int nPopu = 10;
        double custoC = 0.0;
        int[] caminhoV = new int[pg.nNodos];
        int vGerado = 0;
        if(this.nRenovacoes == 2){
            for (int g = 0; g < pg.nNodos; g++)
                caminhoV[g] = g;
            custoC = calculaCusto(caminhoV, 0, pg);
            caminhos.add(new Caminho(custoC, vConverteInt(caminhoV)));
        }

        if (ci != ""){
            caminhoV = vConverteString(ci.split(" "), pg.nNodos);
            custoC = calculaCusto(caminhoV, 0, pg);
            caminhos.add(new Caminho(custoC, vConverteInt(caminhoV)));
        }

        for (int i = 0; i < nPopu; i++){

            for (int j = 0; j < pg.nNodos; j++){
                do{
                    vGerado = hugo.nextInt(pg.nNodos);
                }while(testaValor(caminhoV, vGerado, j));
                caminhoV[j] = vGerado;
            }

            custoC = calculaCusto(caminhoV, 1, pg);

            if(custoC != 0) caminhos.add(new Caminho(custoC, vConverteInt(caminhoV)));
            else i--; // Para não avançar no loop

            for (int t = 0; t < pg.nNodos; t++)
    			caminhoV[t] = -1;
        }
    }

    // public int selecionaIndividuo(int num, int tam){
    //     int count, result = 0;
    //     if(num < (nPais/2)*0.4)
    //         return num;
    //     else if(num < (nPais/2)*0.7)
    //         return hugo.nextInt(tam*0.7);
    //     else
    //         while()
    //     return result;
    // }

    public List<Caminho> executaCrossover(List<Caminho> caminhos){
        List<Caminho> aux = new ArrayList<Caminho>();
        int ps, ss, tam = caminhos.size();
        for (int u = 0; u < nPais / 2; u++){
            ps = selecionaIndividuo(u, tam); //Primeiro Selecionado
            ss = selecionaIndividuo(u, tam); //Segundo Selecionado
            // aux.add(crossover(caminhos.get(ps), caminhos.get(ss)));
        }
            // caminhosCrossover = crossover(caminhosCrossover, caminhos, individuoCross1, individuoCross2);


        return aux;
    }

    public Caminho executaAG(PackGenetico pg){

        List<Caminho> caminhos = new ArrayList<Caminho>();
        inicializaPopulacao(caminhos, 0, "", pg);

        List<Caminho> caminhosCrossover, caminhosMutacao, caminhosNaoSelecionados, caminhosEquivalencia, caminhosMutacionados;
        Caminho CaminhoMutacionado;

        if (this.nIndivSobrevive == 0) this.nIndivSobrevive = 1;

        double pEquivalenciaPermitida = 0.0; //Parametro usado para permitir a equivalencia entre os N�o selecionados
        int nEquivalentesPermitidos = (int) Math.round((pEquivalenciaPermitida * pg.nNodos));
        System.out.println("numeroEquivalentesPermitidos: " + nEquivalentesPermitidos);

        Collections.sort(caminhos, new Comparator(){
            public int compare(Object o1, Object o2){
                Caminho p1 = (Caminho) o1;
                Caminho p2 = (Caminho) o2;
                return p1.getCusto() < p2.getCusto() ? -1 : (p1.getCusto() > p2.getCusto() ? +1 : 0);
            }
        });

        double mCusto = 0.0, pCusto = 0.0;
        System.out.println("\n\nnPais: " + nPais + "\n\n");
    	int repeticoesSemAlteracao = 0, iTotais = 0;

        // caminhosCrossover = executaCrossover(caminhos);

        // while(repeticoesSemAlteracao < nRodSemAlt){
        //     for(int rodadas = 0; rodadas < nRodadas; rodadas++){
        //         if (mCusto <= caminhosget(0).getCusto()) repeticoesSemAlteracao++; // significa que melhorou
    	// 		else repeticoesSemAlteracao = 0;
        //
        //         if (iTotais % 10 == 0) System.out.println(caminhos.get(0).getCusto() + ", It(" + iTotais + ")");
        //
        //         iTotais++;
        //         mCusto = caminhos.get(0).custoCaminho;
        //         pCusto = caminhos.get(caminhos.size() - 1).getCusto();
        //         // Esse metodo gera uma nova população 'boa' e já faz o crossover nos individuos.
        //         caminhosCrossover = executaCrossover(caminhos);
        //     }
        // }

        return caminhos.get(0);
    }
}
