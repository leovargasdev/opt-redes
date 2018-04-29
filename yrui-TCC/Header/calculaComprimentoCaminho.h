/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

//Essa fun��o busca os vizinhos do v�rtice
void buscaVizinhos(int vertice, int *vizinhos) {
	int i;
	int j = 0;
	//Le a matriz F, e guarda no vetor vizinhos todos os valores da coluna que possui o n�mero 1
	//1 represente que existe conectividade entre o vertice linha e o vertice coluna
	for (i = 0; i < quantidadeVertices; i++) {
		if (matrizF[vertice * quantidadeVertices + i] == 1 && i > vertice) {
			vizinhos[j] = i;
			printf("conexoes[%d][%d]\n", vertice, i);
			j++;
		}
	}
}

//Essa fun��o le o vertice e ve quais s�o os seus vizinhos,
//encontra qual a posi�ao dos seus vizinhos no caminho e calcula a distancia do vertice
//para os demais posicoes do caminho, � utilizado o indice do caminho e n�o o valor do mesmo
double calculaDistanciaVertice(int posicaoVertice, int *melhorCaminho, int *vizinhos) {
	int i;
	double distancia = 0.0;
	int j;

	for (i = 0; i < quantidadeVertices; i++) {
		//Enquanto tiver vizinhos
		if (vizinhos[i] != -1) {
			for (j = 0; j < quantidadeVertices; j++) {
				if (melhorCaminho[j] == vizinhos[i]) {
					//Adiciona a distancia entre o vertice e o indice o no qual ele se encontra no caminho
					distancia += matrizD[posicaoVertice * quantidadeVertices + j];
					j = quantidadeVertices;
				}
			}
		} else {
			return distancia;
		}
	}

	return distancia;
}

//Le todas as posi��es do caminho e calcula a distancia com os demais vertices que ele tem conectividade
double calculaComprimentoTotal(int *caminho) {

	int i,j;
	int vertice;
	int *vizinhos = new int[quantidadeVertices];
	double distanciaTotal = 0.0;

	for (i = 0; i < quantidadeVertices; i++) {
		vizinhos[i] = -1;
	}

	for (i = 0; i < quantidadeVertices; i++) {

		for (j = 0; j < quantidadeVertices; j++) {
			vizinhos[j] = -1;
		}

		vertice = caminho[i];
		//Busca os vizinhos do vertice do caminho
		buscaVizinhos(vertice, vizinhos);
		//Calcula a distancia total do vertice para seus vizinhos
		distanciaTotal += calculaDistanciaVertice(i, caminho, vizinhos);
	}
	printf("distanciaTotalOriginal: %d\n", distanciaTotal);
	return distanciaTotal;
}
