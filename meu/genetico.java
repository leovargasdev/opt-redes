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
    private int nIndivSobrevive;      // Nº de individuos sobreviventes apos o nRodadas
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
        this.nIndivSobrevive = (int) (2 * Math.round(0.2 * this.populacao / 2));
        if(this.nIndivSobrevive == 0)
            this.nIndivSobrevive = 1;
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
        return resultCS; // Essa variavel é global
    }

    public double calculaCusto(int[] caminho, int validaCusto){
    	double custo = 0.0;
    	for (int i = 0; i < pg.nNodos; i++){
    		for (int j = i+1; j < pg.nNodos; j++){
                if(pg.conexoes[i][j] == 1){
                    custo += pg.custos[caminho[i]][caminho[j]];
                }
    		}
    	}
    	return custo;
    }

    public Boolean testaValor(int[] vetor, int valor, int p){
        int aux = 0;
        for(int teste : vetor){
            if(aux == p) break;
            if(teste == valor) return true;
            aux++;
        }
        return false;
    }

    public void inicializaPopulacao(List<Caminho> caminhos){
        int vGerado = 0, nPopu = this.populacao - caminhos.size();
        double custo = 0.0;
        int[] caminhoV = new int[pg.nNodos];
        if(this.nRenovacoes == 2){
            for (int g = 0; g < pg.nNodos; g++)
                caminhoV[g] = g;
            custo = calculaCusto(caminhoV, 0);
            caminhos.add(new Caminho(custo, vConverteInt(caminhoV)));
        }
        for (int i = 0; i < nPopu; i++){
            for (int j = 0; j < pg.nNodos; j++){
                do{
                    vGerado = hugo.nextInt(pg.nNodos);
                }while(testaValor(caminhoV, vGerado, j));
                caminhoV[j] = vGerado;
            }
            custo = calculaCusto(caminhoV, 1);
            if(custo > 0)
                caminhos.add(new Caminho(custo, vConverteInt(caminhoV)));
            else
                i--; // Para não avançar no loop
        }
        caminhoV = null;
    }

    public void ajustaPopulacao(List<Caminho> caminhos){
        int tamanhoP = caminhos.size();
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
            if(a > populacao){ // Estorou o limite populacional
                break;
            }
        }
        int sobrevive = (int) Math.round(0.7 * this.populacao);
        while(sobrevive != caminhos.size())
            caminhos.remove(sobrevive);
    }

    public int selecionaIndividuo(int num, int tam){
        int count = 0, result = 12345678;
        if(num < nPais*0.2){ // 40% = são os primeiros da lista
            return num;
        } else if (num < nPais*0.7){  // 30% = um individuo aleatorio entre os 70% melhores da população
            tam *= 0.7;
            return hugo.nextInt(tam);
        } else {
            while(count < 2){ // 30% = um individuo aleatorio entre toda população, mas deve sobreviver a dois round'
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
        int[] caminhoF = vConverteString(fulano.caminho.split(" ")), caminhoC = vConverteString(ciclano.caminho.split(" "));
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

        custo = calculaCusto(caminhoC, 1);
        if(custo < piorCusto)
            caminhos.add(new Caminho(custo, vConverteInt(caminhoC)));

        caminhoF = null;
        caminhoC = null;
    }

    public void executaCrossover(List<Caminho> caminhos){
        int ps, ss, tam = caminhos.size();
        for (int u = 0; u < nPais; u+=2){
            ps = selecionaIndividuo(u, tam); //Primeiro Selecionado
            do{
                ss = selecionaIndividuo(u+1, tam); //Segundo Selecionado
            }while(ss == ps); // ss não pode ser igual ao ps, porque é o mesmo indice, sendo assim o mesmo caminho
            crossover(caminhos, caminhos.get(ps), caminhos.get(ss));
        }
    }

    public void executaMutacoes(List<Caminho> caminhos){
        int tMutacao, individuo, aux;
        int[] novoCaminho;
        double custo, menorc = caminhos.get(0).getCusto();
        for (int m = 0; m < nMutacoes; m++){
            individuo = hugo.nextInt(caminhos.size());
            tMutacao = hugo.nextInt(3);
            novoCaminho = vConverteString(caminhos.get(individuo).caminho.split(" "));
            if(tMutacao == 0) { // SOMATORIO
                do{
                    aux = hugo.nextInt(pg.nNodos-1); // soma não pode ser 0,1 e o ultimo numero disponivel
                }while(aux < 2);
                for (int k = 0; k < pg.nNodos; k++)
                    novoCaminho[k] = (novoCaminho[k] + aux) % pg.nNodos;
            } else if(tMutacao == 1) { // TROCA
                aux = hugo.nextInt(pg.nNodos);
                int aux2 = hugo.nextInt(pg.nNodos-1);
                int aux3 = novoCaminho[aux];
                novoCaminho[aux] = novoCaminho[aux2];
                novoCaminho[aux2] = aux3;
            } else { // INVERSÃO
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
        }
        novoCaminho = null;
    }

    public Caminho executaAG(PackGenetico pga){
        this.pg = pga;
        resultCS = new int[pg.nNodos];
        List<Caminho> caminhos = new ArrayList<Caminho>();
        inicializaPopulacao(caminhos);

        Collections.sort(caminhos, new Comparator(){
            public int compare(Object o1, Object o2){
                Caminho c1 = (Caminho) o1;
                Caminho c2 = (Caminho) o2;
                return c1.getCusto() < c2.getCusto() ? -1 : (c1.getCusto() > c2.getCusto() ? +1 : 0);
            }
        });

        System.out.println("\n[MELHOR INICIAL]\ncusto: " + caminhos.get(0).getCusto() + "\ncaminho: " + caminhos.get(0).caminho + "\n");
        int repeticoesSemAlteracao = 0, iTotais = 0, contador = 0;
        double melhorCusto = 0.0, auxCusto = 123456789.22;
        while(repeticoesSemAlteracao < nRodSemAlt){
            for(int rodadas = 0; rodadas < nRodadas; rodadas++){

                if (melhorCusto <= caminhos.get(0).getCusto())
                    repeticoesSemAlteracao++;
    			else
                    repeticoesSemAlteracao = 0;

                if (iTotais % 100 == 0) System.out.println(caminhos.get(0).getCusto() + "\t\tIt(" + iTotais + ")");
                iTotais++;
                if(caminhos.size() >= populacao)
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
                if(auxCusto == caminhos.get(0).getCusto()){
                    contador++;
                } else {
                    auxCusto = caminhos.get(0).getCusto();
                    if(contador > 0)
                        contador--;
                }
                while(nIndivSobrevive != caminhos.size())
                    caminhos.remove(nIndivSobrevive);

                System.out.println("\nPopulação Renovada!!!\t\tEncerra em: " + (4 - contador) + "\n");
    			inicializaPopulacao(caminhos);
    			this.nRenovacoes++;
    		}

            if(contador == 4) break;
        }
        System.out.println("\n[MELHOR FINAL]\ncusto: " + caminhos.get(0).getCusto() + "\ncaminho: " + caminhos.get(0).caminho + "\n");
        return caminhos.get(0);
    }
}
