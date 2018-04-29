import java.io.*;
import java.nio.file.*;
import java.util.*;
class main{
    private static int quant = 0;

    public static byte[][] leEnlaces(List<String> enlaces){
        byte[][] conex = new byte[quant][quant];
        for (int k = 0; k < quant; k++)
            for (int p = 0; p < quant; p++)
                conex[k][p] = 0;
        for(String l : enlaces){
            l = l.replaceAll("\t", " ");
            String[] enlace = l.split(" ");
            int v1 = (Integer.parseInt(enlace[0]) - 1), v2 = (Integer.parseInt(enlace[1]) - 1);
            conex[v1][v2] = conex[v2][v1] = 1;
        }
        // PRINT DA MATRIZ
        // for (int k = 0; k < quant; k++){
        //     for (int p = 0; p < quant; p++)
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

        quant = nodos.size();

        // CRIAÇÃO MATRIZ DE DISTANCIAS:
        Haversine c = new Haversine();
        double[][] custos = c.calculo(nodos, quant);


        // CRIAÇÃO MATRIZ DE CONEXÕES:
        byte[][] conexoes = leEnlaces(Files.readAllLines(FileSystems.getDefault().getPath(input[1])));

        Calculo yeap = new Calculo();
        double dTotal = yeap.dTotal(custos, conexoes, quant);
        System.out.println("Distacia total: " + dTotal);
    }
}
