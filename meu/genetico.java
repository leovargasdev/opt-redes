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
    private PackGenetico pg;
    private double piorCusto = 0.0;

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

    public int[] vConverteString(String[] vetor){
        int[] result = new int[pg.nNodos];
        for(int t = 0; t < pg.nNodos; t++){
            result[t] = Integer.parseInt(vetor[t]);
            // System.out.print(result[t] + " ");
        }
        System.out.println();
        return result;
    }

    public double calculaCusto(int[] caminho, int validaCusto){
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

    public void inicializaPopulacao(List<Caminho> caminhos, int subPopulacao, String ci){
        int nPopu = this.populacao - subPopulacao;
        // int nPopu = 10;
        double custoC = 0.0;
        int[] caminhoV = new int[pg.nNodos];
        int vGerado = 0;
        if(this.nRenovacoes == 2){
            for (int g = 0; g < pg.nNodos; g++)
                caminhoV[g] = g;
            custoC = calculaCusto(caminhoV, 0);
            caminhos.add(new Caminho(custoC, vConverteInt(caminhoV)));
        }

        if (ci != ""){
            caminhoV = vConverteString(ci.split(" "));
            custoC = calculaCusto(caminhoV, 0);
            caminhos.add(new Caminho(custoC, vConverteInt(caminhoV)));
        }

        for (int i = 0; i < nPopu; i++){

            for (int j = 0; j < pg.nNodos; j++){
                do{
                    vGerado = hugo.nextInt(pg.nNodos);
                }while(testaValor(caminhoV, vGerado, j));
                caminhoV[j] = vGerado;
            }

            custoC = calculaCusto(caminhoV, 1);

            if(custoC != 0) caminhos.add(new Caminho(custoC, vConverteInt(caminhoV)));
            else i--; // Para não avançar no loop

            for (int t = 0; t < pg.nNodos; t++)
    			caminhoV[t] = -1;
        }
    }

    public int selecionaIndividuo(int num, int tam){
        int count = 0, result = 12345678;
        if(num < nPais*0.4){ // 40% = são os primeiros da lista
            return num;
        } else if (num < nPais*0.7){  // 30% = um individuo aleatorio entre os 70% melhores da população
            tam *= 0.7;
            return hugo.nextInt(tam);
        } else {
            while(count < 2){ // 30% = um individuo aleatorio entre toda população
                int aux = hugo.nextInt(tam);
                if(result > aux){
                    result = aux;
                }
                count++;
            }
        }
        return result;
    }

    public void crossover(List<Caminho> caminhos, Caminho fulano, Caminho ciclano){
        int[] caminhoF = vConverteString(fulano.caminho.split(" "));
        int[] caminhoC = vConverteString(ciclano.caminho.split(" "));
        int corte = 0, aux = 0;
        double custo = 0;
        do{
            corte = hugo.nextInt(pg.nNodos);
        }while(corte < 2);

        for(int k = corte; k < pg.nNodos; k++){
            aux = caminhoC[k];
            caminhoC[k] = caminhoF[k];
            caminhoF[k] = aux;
        }

        custo = calculaCusto(caminhoF, 1);
        if(custo < piorCusto)
            caminhos.add(new Caminho(custo, vConverteInt(caminhoF)));
        else
            System.out.println("\n**Discartado**\ncusto: " + custo + "\t caminho: " + vConverteInt(caminhoF));

        custo = calculaCusto(caminhoC, 1);
        if(custo < piorCusto)
            caminhos.add(new Caminho(custo, vConverteInt(caminhoC)));
        else
            System.out.println("\n**Discartado**\ncusto: " + custo + "\t caminho: " + vConverteInt(caminhoF));


        // int ff = caminhosCrossover.size() - 1;
        // System.out.println("\ncorte: " + corte);
        // System.out.println("[fulano] caminho: " + fulano.caminho + "\t[ciclano] caminho: " + ciclano.caminho);
        // System.out.println("[filho1] caminho: " + caminhosCrossover.get(ff - 1).caminho + "\t[filho2] caminho : " + caminhosCrossover.get(ff).caminho);
        //
        // System.out.println("\n[fulano] custo: " + fulano.getCusto() + "\t[ciclano] custo: " + ciclano.getCusto());
        // System.out.println("[filho1] custo: " + caminhosCrossover.get(ff - 1).getCusto() + "\t[filho2] custo : " + caminhosCrossover.get(ff).getCusto());
        //
        // System.out.println("\n**\n");
    }

    public void executaCrossover(List<Caminho> caminhos){
        int ps, ss, tam = caminhos.size();
        for (int u = 0; u < nPais; u+=2){
            ps = selecionaIndividuo(u, tam); //Primeiro Selecionado
            do{
                ss = selecionaIndividuo(u+1, tam); //Segundo Selecionado
                if(ss == ps) System.out.println("\n\n\n\nENTROOOOOOOOOOOOOU\n\n\n\n\n");
            }while(ss == ps);
            crossover(caminhos, caminhos.get(ps), caminhos.get(ss));
        }
    }

    public Caminho executaAG(PackGenetico pga){
        this.pg = pga;
        List<Caminho> caminhos = new ArrayList<Caminho>();
        inicializaPopulacao(caminhos, 0, "");

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

        piorCusto = caminhos.get(caminhos.size() - 1).getCusto();

        int repeticoesSemAlteracao = 0, iTotais = 0;

        for (int g = 0; g < 15; g++){
            executaCrossover(caminhos);
            System.out.println("Nº caminhos: " + caminhos.size());
        }

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
