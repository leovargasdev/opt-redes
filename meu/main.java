import java.io.*;
import java.nio.file.*;
import java.util.*;
class main{
    public static PackGenetico pg;

    //PRINT CAMINHOS:
    // for(Caminho a : caminhos){
    //     String[] abc = a.caminho.split(" ");
    //     for(String g : abc)
    //         System.out.print(g + ",");
    //     System.out.println("  -> custo: " + a.getCusto());
    // }

    public static void main(String[] input) throws IOException{
        // CRIAÇÃO NODOS:
        Haversine c = new Haversine();
        List<Nodo> nodos = new ArrayList<Nodo>();
        Genetico ga;
        double pEquivalenciaPermitida = 0.0; //Parametro usado para permitir a equivalencia entre os N�o selecionados
        int nEquivalentesPermitidos = 0;
        for(String l : Files.readAllLines(FileSystems.getDefault().getPath(input[0])))
            if(!l.startsWith("Id	"))
                nodos.add(new Nodo(l));

        pg = new PackGenetico(nodos.size(), input[1]);
        pg.custos = c.calculo(nodos, pg.nNodos);
        pg.geraDistTotal();
        System.out.println("Distacia total: " + pg.dTotal);

        if (pg.nNodos <= 20)
    		ga = new Genetico(5000, 100, 80, 0.7, 0.7, 2, 0.2);
    	else if (pg.nNodos > 20 && pg.nNodos <= 40)
            ga = new Genetico(1000, 5000, 90, 0.7, 0.7, 2, 0.4);
    	else if (pg.nNodos > 40 && pg.nNodos <= 60)
            ga = new Genetico(2000, 10000, 100, 0.8, 0.8, 3, 0.4);
    	else
            ga = new Genetico(2000, 20000, 100, 0.9, 0.9, 4, 0.55);

        pg.nNosExeDistMax = (int) Math.round(ga.pLinksIndiv * pg.nLinks);
        System.out.println("numeroNosExcedeDistanciaMaxima: " + pg.nNosExeDistMax);

        pg.geraMaiorDistPermitida();
        System.out.println("Distancia Maxima: " + pg.maiorDist);

        List<Caminho> caminhos = new ArrayList<Caminho>();
        ga.inicializaPopulacao(caminhos, 0, "", pg);

        List<Caminho> caminhosCrossover, caminhosMutacao, caminhosNaoSelecionados, caminhosEquivalencia, caminhosMutacionados;
        Caminho CaminhoMutacionado;
    	int iCrossover, iMutacao, iTruncate, iCaminhosMutacionados;
    	int numeroCaminhos;

        if (ga.nIndivSobrevive == 0) ga.nIndivSobrevive = 1;

        nEquivalentesPermitidos = (int) Math.round((pEquivalenciaPermitida * pg.nNodos));
        System.out.println("numeroEquivalentesPermitidos: " + nEquivalentesPermitidos);

        Collections.sort(caminhos, new Comparator(){
            public int compare(Object o1, Object o2){
                Caminho p1 = (Caminho) o1;
                Caminho p2 = (Caminho) o2;
                return p1.getCusto() < p2.getCusto() ? -1 : (p1.getCusto() > p2.getCusto() ? +1 : 0);
            }
        });


        double mCusto = 0.0, pCusto = 0.0, somaCustos;
    	int individuoCross1 = 0, individuoCross2 = 0;
    	double *P = new double[nPop]; ????????????????
        System.out.println("numero: " + pg.hugo.nextInt(pg.nNodos));
    	int repeticoesSemAlteracao = 0;
    	int iTotais = 0;
        while(repeticoesSemAlteracao < pg.nRodSemAlt){
            for(int rodadas = 0; rodadas < nRodadas; rodadas++){
                if (mCusto <= caminhosget(0).getCusto())
    				repeticoesSemAlteracao++; // significa que melhorou
    			else
    				repeticoesSemAlteracao = 0;
                // Printa uma amostra da evolução do algoritmo
                if (iTotais % 10 == 0) System.out.println(caminhos.get(0).getCusto() + ", It(" + iTotais + ")");
                iTotais++;
                mCusto = caminhos.get(0).custoCaminho;
                pCusto = caminhos.get(caminhos.size() - 1).getCusto();
                // Esse metodo gera uma nova população 'boa' e já faz o crossover nos individuos.
                // ga.executaCrossover();
            }
        }

    }
}
