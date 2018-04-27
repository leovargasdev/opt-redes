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
        for(Nodo a : nodos)
            a.getCampos();
        int quant = nodos.size();
        byte[][] conexoes = new byte[quant][quant];
        for (int k = 0; k < quant; k++)
            for (int p = 0; p < quant; p++)
                conexoes[k][p] = 0;
        Haversine c = new Haversine();
        double[][] custos = c.calculo(nodos, quant);
        System.out.println("matrix:");
        for(int g = 0; g < quant; g++){
            for(int f = 0; f < quant; f++)
                System.out.printf("%.2f \t", custos[g][f]);
            System.out.println();
        }
    }
}
