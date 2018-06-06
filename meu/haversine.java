import java.io.*;
import java.nio.file.*;
import java.util.*;
class Haversine{
    private double pi = 3.14159265358979323846;
    private double radius = 6373.0;

    private double converte(double angulo){
        return angulo * (this.pi / 180);
    }
    
    private double distancia(Nodo n1, Nodo n2){
        double latD = converte(n2.getLat() - n1.getLat());
    	double lonD = converte(n2.getLong() - n1.getLong());

    	double cLat1 = converte(n1.getLat());
    	double cLat2 = converte(n2.getLat());

        double a = Math.pow(Math.sin(latD / 2), 2);
        a+= Math.cos(cLat1) * Math.cos(cLat2) * Math.pow(Math.sin(lonD / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (this.radius * c);
    }

    public double[][] calculo(List<Nodo> nodos, int tam){
        double[][] mat = new double[tam][tam];
        for(int g = 0; g < tam; g++){
            for(int f = 0; f < tam; f++){
                if(g == f)
                    mat[g][f] = 0;
                else
                    mat[g][f] = mat[f][g] = this.distancia(nodos.get(g), nodos.get(f));
            }
        }
        System.out.println("\nMATRIZ DE DISTANCIAS:");
         for (int k = 0; k < tam; k++){
            for (int p = 0; p < tam; p++)
                System.out.printf("%.1f\t", mat[k][p]);
            System.out.println();
        }
        System.out.println();
        return mat;
    }

}
