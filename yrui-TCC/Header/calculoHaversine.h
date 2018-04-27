/*
Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
Universidade Federal da Fronteira Sul - Chapec� - SC
Trabalho de Conclus�o de Curso:
- Ci�ncia da Computa��o
- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

# define M_PI 3.14159265358979323846  /* numero aproximado de Pi */

// Converte para radianos
double converte(double angle)
{
	return angle * (M_PI / 180);
}

//Calcula a distancia utilizando haversine
double calculaDistancia(double latitude1,double longtitude1,double latitude2,double longtitude2)
{
	// Radius da Terra Aproximadamente
	double radius = 6373.0;
	double latDelta = converte(latitude2 - latitude1);
	double lonDelta = converte(longtitude2 - longtitude1);

	double convertidoLat1 = converte(latitude1);
	double convertidoLat2 = converte(latitude2);

	double a = pow(sin(latDelta / 2), 2) + cos(convertidoLat1) * cos(convertidoLat2) * pow(sin(lonDelta / 2), 2);

	double c = 2 * atan2(sqrt(a), sqrt(1 - a));
	double d = radius * c;

	return d;
}

void calculoHaversine(vector<Vertice> pontos) {
	int i, j;
	double distancia = 0.0;
	int numeroPontos = pontos.size();
	//Calcula a distancia para todo par de v�rtices da Matriz D, que nesse caso � o Vetor
	//Percore a linha calculando para cada coluna
	for(i = 0; i < numeroPontos; i++){
		for (j = 0; j < numeroPontos; j++) {
			if (i == j) {
				matrizD[i * pontos.size() + j] = 0;
			}
			else {
				//Passa os valores de latitude e longitude do ponto, ponto i que � a linha e ponto j q � coluna
				if(i < j){
					distancia = calculaDistancia(pontos[i].latitude, pontos[i].longitude, pontos[j].latitude, pontos[j].longitude);
					matrizD[i * pontos.size() + j] = distancia;
					matrizD[j * pontos.size() + i] = distancia;
				}
			}
		}
	}
}
