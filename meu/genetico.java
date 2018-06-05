import java.util.*;
class Genetico{
    private int nRodadas;               // Nº maximo de interações
    private int nRodSemAlt;             // Nº de interações sem fazer alteração
    public int populacao;              // Tamanho da população
    private double pc;                  // Porcentagem do Crossover
    private int nPais;                  // Nº de pais
    private double pm;                  // Porcentagem de sobreviventes apos mutação
    private int nMutacoes;              // Nº de mutações
    private int beta;                   // Selection Pressure Utilizado para Roleta
    // private double pIndivSobrevive;  // Porcentagem de individuos sobreviventes apos o nRodadas
    private double nIndivSobrevive;     // Nº de individuos sobreviventes apos o nRodadas
    public double pMaiorDist;          //Porcentagem de links de um individuo que pode exceder a distancia maxima permitida
    private int nRenovacoes;
    private int nn;
    private byte[][] conexoes;
    private double[][] custos;
    private Random hugo = new Random();

    public Genetico(int a, int b, int c, double d, double e, int f, double j, int nn, double[][] cu, byte[][] co){
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
        this.nRenovacoes = 0;
        this.nn = nn;
        this.custos = cu.clone();
        this.conexoes = co.clone();
    }

    public String vConverteInt(int[] vetor){
        String result = "";
        for(int d : vetor)
            result += String.valueOf(d) + " ";
        return result;
    }

    public int[] vConverteString(String[] vetor){
        int[] result = new int[this.nn];
        for(int t = 0; t < this.nn; t++){
            result[t] = Integer.parseInt(vetor[t]);
            System.out.print(result[t] + " ");
        }
        System.out.println();
        return result;
    }

    public double calculaCusto(int[] caminho, int validaCusto, int nExceMax, double maiorDist){
    	int totalExecedeMaxima = 0;
    	double custo = 0.0;
    	for (int i = 0; i < this.nn; i++){
    		for (int j = i+1; j < this.nn; j++){
                if(this.conexoes[i][j] == 1){
                    double aux = this.custos[caminho[i]][caminho[j]];
        			if (validaCusto == 1 && aux > maiorDist)
        				totalExecedeMaxima++;

        			if (totalExecedeMaxima > nExceMax)
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

    public void inicializaPopulacao(List<Caminho> caminhos, int nPopu, String ci, int nExceMax, double maiorDist){
        double custoC = 0.0;
        int[] caminhoV = new int[this.nn];
        int vGerado = 0;
        if(this.nRenovacoes == 2){
            for (int g = 0; g < this.nn; g++)
                caminhoV[g] = g;
            custoC = calculaCusto(caminhoV, 0, nExceMax, maiorDist);
            caminhos.add(new Caminho(custoC, vConverteInt(caminhoV)));
        }

        if (ci != ""){
            caminhoV = vConverteString(ci.split(" "));
            custoC = calculaCusto(caminhoV, 0, nExceMax, maiorDist);
            caminhos.add(new Caminho(custoC, vConverteInt(caminhoV)));
        }

        for (int i = 0; i < nPopu; i++){

            for (int j = 0; j < this.nn; j++){
                do{
                    vGerado = hugo.nextInt(this.nn);
                }while(testaValor(caminhoV, vGerado, j));
                caminhoV[j] = vGerado;
            }

            custoC = calculaCusto(caminhoV, 1, nExceMax, maiorDist);

            if(custoC != 0) caminhos.add(new Caminho(custoC, vConverteInt(caminhoV)));
            else i--; // Para não avançar no for

            for (int t = 0; t < this.nn; t++)
    			caminhoV[t] = -1;
        }
    }

}
