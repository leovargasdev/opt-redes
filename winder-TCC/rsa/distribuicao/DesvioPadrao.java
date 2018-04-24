/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa.distribuicao;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Winder Dias
 */
public class DesvioPadrao {

    List<Double> numeros = new ArrayList<Double>();
    double desvioPadrao;

    public DesvioPadrao(List<Double> numeros)
    {
	this.numeros = numeros;

	int size;

	double total = 0.0;

        size = numeros.size();
        for (int i = 0; i < size; i++)
                total += numeros.get(i);
        double mean = total / size;

        List<Double> deviations = new ArrayList<Double>();

        for (int i = 0; i < size; i++)
        {
                double cur = numeros.get(i);
                deviations.add(cur - mean);
        }

        List<Double> squaredDeviations = new ArrayList<Double>();

        for (int i = 0; i < size; i++)
        {
                double cur = deviations.get(i);
                squaredDeviations.add(Math.pow(cur, 2));
        }

        double sum = 0.0;

        for (int i = 0; i < size; i++)
        {
                sum += squaredDeviations.get(i);
        }

        desvioPadrao = Math.sqrt(sum / size);
    }
    
    public double getDesvioPadrao ()
    {
            return desvioPadrao;
    }
	
}
