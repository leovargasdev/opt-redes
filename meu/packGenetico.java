import java.io.*;
import java.nio.file.*;
import java.util.*;
class PackGenetico{
    public int nNodos = 0;
    public int nLinks = 0;
    public byte[][] conexoes;
    public double[][] custos;
    public double dTotal;
    public int nNosExeDistMax;
    public int maiorGrau;
    public double maiorDist;

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
            this.nLinks++;
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
            if (aux > this.maiorGrau) this.maiorGrau = aux;
            System.out.println();
        }
        this.maiorGrau++;
    }

    public void geraDistTotal(){
        this.dTotal = 0.0;
        for(int k = 0; k < this.nNodos; k++)
            for (int i = k+1; i < this.nNodos; i++)
                if(this.conexoes[k][i] == 1)
                    this.dTotal += this.custos[k][i];
    }

    public double buscaMaiorDistancia(int p){
        double[] abc = this.custos[p].clone();
        Arrays.sort(abc);
        return abc[this.maiorGrau+2];
    }

    public void geraMaiorDistPermitida(){
        System.out.println("Maior grau: " + this.maiorGrau);
        this.maiorDist = 0.0;
        double aux = 0;
        for (int i = 0; i < nNodos; i++) {
            aux = 0;
            aux = buscaMaiorDistancia(i);
            if (aux > this.maiorDist)
                this.maiorDist = aux;
        }
    }

}
