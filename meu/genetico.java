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

        			// if (totalExecedeMaxima > pg.nNosExeDistMax){
                    //     System.out.println("ENTROOOOOOOOOOOOOU");
                    //     return 0;
                    // }

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

    public void inicializaPopulacao(List<Caminho> caminhos, int subPopulacao){
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

    public void ajustaPopulacao(List<Caminho> caminhos){
        int tamanhoP = caminhos.size();
        Boolean limpar = false;
        String cTeste = "", cTestado = "";
        for(int a = 0; a < tamanhoP; a++){
            cTeste = caminhos.get(a).caminho;
            for(int c = a+1; c < tamanhoP; c++){
                cTestado = caminhos.get(c).caminho;
                if(cTeste.equals(cTestado)){
                    caminhos.remove(c);
                    c--;
                    tamanhoP = caminhos.size();
                }

                if(caminhos.get(a).getCusto() < caminhos.get(c).getCusto())
                    break;
            }

            if(a >= populacao){
                limpar = true;
                break;
            }
        }

        if(limpar)
            while(populacao != caminhos.size())
                caminhos.remove(populacao);
        else{
            // System.out.println("vai renovar\n tamanho atual: " + caminhos.size() + "\ttamanho permitido: " + populacao);
            inicializaPopulacao(caminhos, caminhos.size());
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
        String caminho = vConverteInt(caminhoF);
        if(custo < piorCusto)
            caminhos.add(new Caminho(custo, caminho));
        // else
            // System.out.println("\n**Discartado**\ncusto: " + custo + "\t caminho: " + vConverteInt(caminhoF));

        custo = calculaCusto(caminhoC, 1);
        caminho = vConverteInt(caminhoC);
        if(custo < piorCusto)
            caminhos.add(new Caminho(custo, caminho));
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
            String caminho = vConverteInt(novoCaminho);
            if(custo < piorCusto)
                caminhos.add(new Caminho(custo, caminho));
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
        inicializaPopulacao(caminhos, 0);

        if (this.nIndivSobrevive == 0) this.nIndivSobrevive = 1;

        double pEquivalenciaPermitida = 0.0; //Parametro usado para permitir a equivalencia entre os N�o selecionados
        int nEquivalentesPermitidos = (int) Math.round((pEquivalenciaPermitida * pg.nNodos));
        // System.out.println("numeroEquivalentesPermitidos: " + nEquivalentesPermitidos);

        Collections.sort(caminhos, new Comparator(){
            public int compare(Object o1, Object o2){
                Caminho c1 = (Caminho) o1;
                Caminho c2 = (Caminho) o2;
                return c1.getCusto() < c2.getCusto() ? -1 : (c1.getCusto() > c2.getCusto() ? +1 : 0);
            }
        });

        System.out.println("[melhor inicial] caminho: " + caminhos.get(0).caminho + "\tcusto: " + caminhos.get(0).getCusto());

        int repeticoesSemAlteracao = 0, iTotais = 0, nCaminhos;
        double melhorCusto = 0.0;

        while(repeticoesSemAlteracao < nRodSemAlt){
            for(int rodadas = 0; rodadas < nRodadas; rodadas++){

                // if (repeticoesSemAlteracao > nRodSemAlt)
    			// 	return caminhos.get(0);

                if (melhorCusto <= caminhos.get(0).getCusto()) //Significa que melhorou
                    repeticoesSemAlteracao++;
    			else
                    repeticoesSemAlteracao = 0;

                piorCusto = caminhos.get(caminhos.size() - 1).getCusto();
                melhorCusto = caminhos.get(0).getCusto();

                executaCrossover(caminhos);
                executaMutacoes(caminhos);

                Collections.sort(caminhos, new Comparator(){
                    public int compare(Object o1, Object o2){
                        Caminho c1 = (Caminho) o1;
                        Caminho c2 = (Caminho) o2;
                        return c1.getCusto() < c2.getCusto() ? -1 : (c1.getCusto() > c2.getCusto() ? +1 : 0);
                    }
                });

                ajustaPopulacao(caminhos);
            }

            if (repeticoesSemAlteracao < nRodSemAlt){
    			// System.out.print("\nMuda População\n");
    			inicializaPopulacao(caminhos, caminhos.size());
    			this.nRenovacoes++;
    		}

        }
        // for(Caminho a : caminhos){
        //     String[] abc = a.caminho.split(" ");
        //     for(String g : abc)
        //         System.out.print(g + ",");
        //     System.out.println("  -> custo: " + a.getCusto());
        // }
        System.out.println("[melhor final] caminho: " + caminhos.get(0).caminho + "\tcusto: " + caminhos.get(0).getCusto());
        return caminhos.get(0);
    }
}
