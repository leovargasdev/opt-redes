import java.io.*;
import java.nio.file.*;
import java.util.*;
class main{
    public static PackGenetico pg;

    public static void main(String[] input) throws IOException{
        // CRIAÇÃO NODOS:
        Haversine c = new Haversine();
        List<Nodo> nodos = new ArrayList<Nodo>();
        Genetico ga;
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

        double tes = 0.0;

        for (int t = 0; t < pg.nNodos; t++){
            for (int w = t+1; w < pg.nNodos; w++){
                if(pg.conexoes[t][w] == 1)
                    tes += pg.custos[t][w];
            }
        }
        System.out.println("Distancia Maxima 2: " + tes);


        List<Caminho> caminhos = new ArrayList<Caminho>();
        ga.inicializaPopulacao(caminhos, 0, "", pg);

        for(Caminho a : caminhos){
            String[] abc = a.caminho.split(" ");
            for(String g : abc)
                System.out.print(g + "\t");
            System.out.println("  -> custo: " + a.getCusto());
        }
    }
}
