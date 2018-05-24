import java.io.*;
import java.nio.file.*;
import java.util.*;
class main{
    private static int nNodos = 0;
    private static int numeroLinks = 0;
    public static byte[][] conexoes;
    public static double[][] custos;

    public static int getMaiorGrau(){
        int maiorGrau = 0, aux = -1;
        for (int i = 0; i < nNodos; i++) {
            aux = 0;
            for (int j = 0; j < nNodos; j++)
                if (conexoes[i][j] == 1)
                    aux++;
            if (aux > maiorGrau)
                maiorGrau = aux;
        }
        return (maiorGrau + 1);
    }

    // public bool myfunction(int i, int j) { return (i<j); }

    // public double buscaMaiorDistancia(int p, int grau) {
    //     vector<double> vetorDistancias(distancias, distancias + nNodos - 1);
    //
    //     sort(vetorDistancias.begin(), vetorDistancias.end());
    //
    //     double maiorDistanciaRetorno = vetorDistancias[grau + 1];
    //     vetorDistancias.clear();
    //     return maiorDistanciaRetorno;
    // }

    public static double maiorDistPermitida(){
        int maiorGrau = getMaiorGrau();
        System.out.println("maior grau:" + maiorGrau);
        double maiorDistancia, aux = 0;

        for (i = 0; i < nNodos; i++) {
            aux = 0;
            // Passa o indice do vetor de distancias e o maior grau encontrado do grafo
            aux = buscaMaiorDistancia(i, maiorGrau);
            if (aux > maiorDistancia)
                maiorDistancia = aux;
        }
        return maiorDistancia;
    }

    public static void leEnlaces(List<String> enlaces){
        conexoes = new byte[nNodos][nNodos];
        for (int k = 0; k < nNodos; k++)
            for (int p = 0; p < nNodos; p++)
                conexoes[k][p] = 0;
        for(String l : enlaces){
            l = l.replaceAll("\t", " ");
            String[] enlace = l.split(" ");
            int v1 = (Integer.parseInt(enlace[0]) - 1), v2 = (Integer.parseInt(enlace[1]) - 1);
            conexoes[v1][v2] = conexoes[v2][v1] = 1;
            numeroLinks++;
        }
        // PRINT DA MATRIZ
        System.out.println("\nMATRIZ DE FLUXO:");
        for (int k = 0; k < nNodos; k++){
            for (int p = 0; p < nNodos; p++)
                System.out.print(conexoes[k][p] + " ");
            System.out.println();
        }
    }

    public static void main(String[] input) throws IOException{
        // CRIAÇÃO NODOS:
        List<Nodo> nodos = new ArrayList<Nodo>();
        for(String l : Files.readAllLines(FileSystems.getDefault().getPath(input[0])))
            if(!l.startsWith("Id	"))
                nodos.add(new Nodo(l));
        // Imprime lista
        // for(Nodo a : nodos) a.getCampos();

        nNodos = nodos.size();

        // CRIAÇÃO MATRIZ DE DISTANCIAS:
        Haversine c = new Haversine();
        custos = c.calculo(nodos, nNodos);


        // CRIAÇÃO MATRIZ DE CONEXÕES:
        leEnlaces(Files.readAllLines(FileSystems.getDefault().getPath(input[1])));

        Calculo yeap = new Calculo();
        double dTotal = yeap.dTotal(custos, conexoes, nNodos);
        System.out.println("Distacia total: " + dTotal);

        Genetico alg;
        if (nNodos <= 20)
    		alg = new Genetico(5000, 100, 80, 0.7, 0.7, 2, 0.2);
    	else if (nNodos > 20 && nNodos <= 40)
            alg = new Genetico(1000, 5000, 90, 0.7, 0.7, 2, 0.4);
    	else if (nNodos > 40 && nNodos <= 60)
            alg = new Genetico(2000, 10000, 100, 0.8, 0.8, 3, 0.4);
    	else
            alg = new Genetico(2000, 20000, 100, 0.9, 0.9, 4, 0.55);
        int numeroNosExcedeDistanciaMaxima = (int) Math.round(alg.pMaiorDist * numeroLinks);
        System.out.println("numeroNosExcedeDistanciaMaxima: " + numeroNosExcedeDistanciaMaxima);
        double maiorDist = maiorDistPermitida();
    }
}
