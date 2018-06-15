import java.io.*;
import java.nio.file.*;
import java.util.*;
class PackGenetico{
    public int nNodos = 0;
    public byte[][] conexoes;
    public double[][] custos;

    public PackGenetico(int a, String arquivoLinks)throws IOException{
        this.nNodos = a;
        leEnlaces(Files.readAllLines(FileSystems.getDefault().getPath(arquivoLinks)));
    }

    public void leEnlaces(List<String> enlaces){
        this.conexoes = new byte[nNodos][nNodos];
        for (int k = 0; k < nNodos; k++)
            for (int p = 0; p < nNodos; p++)
                this.conexoes[k][p] = 0;
        for(String l : enlaces){
            l = l.replaceAll("\t", " ");
            String[] enlace = l.split(" ");
            int v1 = (Integer.parseInt(enlace[0]) - 1), v2 = (Integer.parseInt(enlace[1]) - 1);
            this.conexoes[v1][v2] = this.conexoes[v2][v1] = 1;
        }
        // PRINT DA MATRIZ
        int aux;
        System.out.println("\nMATRIZ DE FLUXO:");
        for (int k = 0; k < this.nNodos; k++){
            aux = 0;
            for (int p = 0; p < this.nNodos; p++){
                System.out.print(this.conexoes[k][p] + " ");
                if (this.conexoes[k][p] == 1)
                    aux++;
            }
            System.out.println();
        }
    }

}
