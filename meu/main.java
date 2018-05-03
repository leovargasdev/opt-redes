import java.io.*;
import java.nio.file.*;
import java.util.*;
class main{
    private static int nNodos = 0;

    public static byte[][] leEnlaces(List<String> enlaces){
        byte[][] conex = new byte[nNodos][nNodos];
        for (int k = 0; k < nNodos; k++)
            for (int p = 0; p < nNodos; p++)
                conex[k][p] = 0;
        for(String l : enlaces){
            l = l.replaceAll("\t", " ");
            String[] enlace = l.split(" ");
            int v1 = (Integer.parseInt(enlace[0]) - 1), v2 = (Integer.parseInt(enlace[1]) - 1);
            conex[v1][v2] = conex[v2][v1] = 1;
        }
        // PRINT DA MATRIZ
        // for (int k = 0; k < nNodos; k++){
        //     for (int p = 0; p < nNodos; p++)
        //         System.out.print(conex[k][p] + " ");
        //     System.out.println();
        // }
        return conex;
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
        double[][] custos = c.calculo(nodos, nNodos);


        // CRIAÇÃO MATRIZ DE CONEXÕES:
        byte[][] conexoes = leEnlaces(Files.readAllLines(FileSystems.getDefault().getPath(input[1])));

        Calculo yeap = new Calculo();
        double dTotal = yeap.dTotal(custos, conexoes, nNodos);
        System.out.println("Distacia total: " + dTotal);

        Genetico algoTOPTOP;
        if (nNodos <= 20)
    		algoTOPTOP = new Genetico(500, 2000, 80, 0.7, 0.6, 100);
    	else if (nNodos > 20 && nNodos <= 40)
            algoTOPTOP = new Genetico(2000, 10000, 90, 0.8, 0.8, 2);
    	else if (nNodos > 40 && nNodos <= 60)
            algoTOPTOP = new Genetico(4000, 40000, 100, 0.9, 0.9, 4);
    	else
            System.out.println("Perdi!");
    }
}
