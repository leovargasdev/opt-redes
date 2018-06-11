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
    private int[] resultCS;
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
        for(int t = 0; t < pg.nNodos; t++)
            resultCS[t] = Integer.parseInt(vetor[t]);
        return resultCS;
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
        caminhoV = null;
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
        // else
            // System.out.println("\n**Discartado**\ncusto: " + custo + "\t caminho: " + vConverteInt(caminhoF));

        custo = calculaCusto(caminhoC, 1);
        if(custo < piorCusto)
            caminhos.add(new Caminho(custo, vConverteInt(caminhoC)));
        // else
            // System.out.println("\n**Discartado**\ncusto: " + custo + "\t caminho: " + vConverteInt(caminhoC));

        caminhoF = null;
        caminhoC = null;
    }

    public void executaCrossover(List<Caminho> caminhos){
        int ps, ss, tam = caminhos.size();
        for (int u = 0; u < nPais; u+=2){
            ps = selecionaIndividuo(u, tam); //Primeiro Selecionado
            do{
                ss = selecionaIndividuo(u+1, tam); //Segundo Selecionado
                // if(ss == ps) System.out.println("ENTROOOOOOOOOOOOOU");
            }while(ss == ps);
            crossover(caminhos, caminhos.get(ps), caminhos.get(ss));
        }
    }

    public void executaMutacoes(List<Caminho> caminhos){
        int tam = caminhos.size(), tMutacao, individuo, aux;
        int[] novoCaminho;
        String tipoM = "";
        double custo, menorc = caminhos.get(0).getCusto();
        for (int m = 0; m < nMutacoes; m++){
            individuo = hugo.nextInt(tam);
            tMutacao = hugo.nextInt(3);
            novoCaminho = vConverteString(caminhos.get(individuo).caminho.split(" "));
            if(tMutacao == 0) { //Somatorio
                // tipoM = "Somatorio";
                do{
                    aux = hugo.nextInt(pg.nNodos-1); // soma não pode ser 0,1 e o ultimo numero disponivel
                }while(aux < 2);
                for (int k = 0; k < pg.nNodos; k++)
                    novoCaminho[k] = (novoCaminho[k] + aux) % pg.nNodos;
            } else if(tMutacao == 1) { //Troca
                // tipoM = "Troca";
                aux = hugo.nextInt(pg.nNodos);
                int aux2 = hugo.nextInt(pg.nNodos-1);
                int aux3 = novoCaminho[aux];
                novoCaminho[aux] = novoCaminho[aux2];
                novoCaminho[aux2] = aux3;
            } else { //Inversão
                // tipoM = "Inversão";
                aux = hugo.nextInt(pg.nNodos);
                int[] aux2 = novoCaminho.clone();
                int aux3 = pg.nNodos - 1;
                for(int y = aux; y < pg.nNodos; y++, aux3--)
                    novoCaminho[y] = aux2[aux3];
                aux2 = null;
            }
            custo = calculaCusto(novoCaminho, 1);

            if(custo < piorCusto)
                caminhos.add(new Caminho(custo, vConverteInt(novoCaminho)));
            // else
                // System.out.println("\n**[" + tipoM + "]Discartado**\ncusto: " + custo + "\t caminho: " + vConverteInt(novoCaminho));
            // System.out.println("[selecionado] caminho: " + caminhos.get(individuo).caminho + "\tcusto: " + caminhos.get(individuo).getCusto());
            // System.out.println("[mutacionado] caminho: " + caminhos.get(caminhos.size()-1).caminho + "\tcusto: " + caminhos.get(caminhos.size()-1).getCusto() + "\n\n");
        }
        novoCaminho = null;
    }

    public Caminho executaAG(PackGenetico pga){
        this.pg = pga;
        resultCS = new int[pg.nNodos];
        List<Caminho> caminhos = new ArrayList<Caminho>();
        inicializaPopulacao(caminhos, 0, "");

        if (this.nIndivSobrevive == 0) this.nIndivSobrevive = 1;

        double pEquivalenciaPermitida = 0.0; //Parametro usado para permitir a equivalencia entre os N�o selecionados
        int nEquivalentesPermitidos = (int) Math.round((pEquivalenciaPermitida * pg.nNodos));
        System.out.println("numeroEquivalentesPermitidos: " + nEquivalentesPermitidos);

        Collections.sort(caminhos, new Comparator(){
            public int compare(Object o1, Object o2){
                Caminho c1 = (Caminho) o1;
                Caminho c2 = (Caminho) o2;
                return c1.getCusto() < c2.getCusto() ? -1 : (c1.getCusto() > c2.getCusto() ? +1 : 0);
            }
        });

        System.out.println("[melhor inicial] caminho: " + caminhos.get(0).caminho + "\tcusto: " + caminhos.get(0).getCusto());

        int repeticoesSemAlteracao = 0, iTotais = 0;

        // while(repeticoesSemAlteracao < nRodSemAlt){
            for(int rodadas = 0; rodadas < 10000; rodadas++){
                // if (mCusto <= caminhosget(0).getCusto()) repeticoesSemAlteracao++; // significa que melhorou
    			// else repeticoesSemAlteracao = 0;

                // if (iTotais % 10 == 0) System.out.println(caminhos.get(0).getCusto() + ", It(" + iTotais + ")");
                //
                // iTotais++;

                executaCrossover(caminhos);
                executaMutacoes(caminhos);

                if(rodadas%50 == 0){
                    Collections.sort(caminhos, new Comparator(){
                        public int compare(Object o1, Object o2){
                            Caminho c1 = (Caminho) o1;
                            Caminho c2 = (Caminho) o2;
                            return c1.getCusto() < c2.getCusto() ? -1 : (c1.getCusto() > c2.getCusto() ? +1 : 0);
                        }
                    });
                    if(caminhos.size() > 300)
                        while(300 != caminhos.size())
                            caminhos.remove(300);
                    piorCusto = caminhos.get(caminhos.size() - 1).getCusto();
                }
            }
        // }
        System.out.println("[melhor final] caminho: " + caminhos.get(0).caminho + "\tcusto: " + caminhos.get(0).getCusto());

        return caminhos.get(0);
    }
}
