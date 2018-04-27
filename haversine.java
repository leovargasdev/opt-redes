import java.io.*;
import java.nio.file.*;
import java.util.*;
class Haversine{
    private double pi = 3.14159265358979323846;
    private double radius = 6373.0;

    private double converte(double angulo){
        return angulo * (this.pi / 180);
    }
    // Funções do Math:
    //      --> atan2() converte coordenadas retangulares em coordenadas polares
    //      --> pow() elava um número à potência
    //      --> sqrt() raiz quadrada
    //      --> cos() calcula o coseno
    //      --> sin() calcula o seno
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
        double d = 0.0;
        for(int g = 0; g < tam; g++){
            for(int f = 0; f < tam; f++){
                if(g == f){
                    mat[g][f] = 0;
                }else{
                    d = this.distancia(nodos.get(g), nodos.get(f));
                    mat[g][f] = d;
                    mat[f][g] = d;
                }
            }
        }
        return mat;
    }
}