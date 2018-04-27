import java.io.*;
import java.nio.file.*;
import java.util.*;
// import nodo.*;
public class main{
    public static void main(String[] input) throws IOException{
        List<Nodo> nodos = new ArrayList<Nodo>();
        for(String l : Files.readAllLines(FileSystems.getDefault().getPath(input[0])))
            if(!l.startsWith("Id	"))
                nodos.add(new Nodo(l));
        // Imprime lista
        // for(Nodo a : nodos) a.getCampos();

        int quant = nodos.size();

        // Cria matriz de distancias e calcula os custos
        Haversine c = new Haversine();
        double[][] custos = c.calculo(nodos, quant);
        // System.out.println("matrix:");
        // for(int g = 0; g < quant; g++){
        //     for(int f = 0; f < quant; f++)
        //         System.out.printf("%.1f \t", custos[g][f]);
        //     System.out.println();
        // }

        // Cria matriz de enlaces e seta as conexões
        byte[][] conexoes = new byte[quant][quant];
        for (int k = 0; k < quant; k++)
            for (int p = 0; p < quant; p++)
                conexoes[k][p] = 0;
        for(String l : Files.readAllLines(FileSystems.getDefault().getPath(input[1]))){
            l = l.replaceAll("\t", " ");
            String[] enlace = l.split(" ");
            int v1 = (Integer.parseInt(enlace[0]) - 1), v2 = (Integer.parseInt(enlace[1]) - 1);
            conexoes[v1][v2] = conexoes[v2][v1] = 1;
        }
        for (int k = 0; k < quant; k++){
            for (int p = 0; p < quant; p++){
                System.out.print(conexoes[k][p] + " ");
            }
            System.out.println();
        }

    }
}
