#include "bibliotecas.h"
#include "caminho.h"
#include "nos.h"
#include "matrizFluxo.h"
#include "calculoHaversine.h"
#include "calculaComprimentoCaminho.h"
#include "buscaMaiorDistancia.h"
#include "QAP/main.h"
#include "saida.h"

int main(){
	//Medir tempo
	t0 = clock();
	//----------
	int opcao;
	Vertice ponto;
	//Vetor que contem todo os pontos com os dados de Lat, Long, nome e ID. Lendo do arquivo de vertices
	vector<Vertice> pontos = ponto.leVertices();
	Caminho melhorCaminho;

	int i, j;
	double distanciaTotalOriginal;

	//Quantidade de vertices � o tamanho do vetor de pontos
	quantidadeVertices = pontos.size();

	//As matrizes s�o trabalhadas como vetores, para ser poss�vel gerar uma matriz conforme o tamanho da entrada

	//Matriz de Dist�ncias, dist�ncia entre i e j na posi��o ij
	matrizD = new double[quantidadeVertices * quantidadeVertices];

	//Matriz de Fluxo, quando ij = 1 existe conex�o, quando 0 n�o existe conex�o
	matrizF = new int[quantidadeVertices * quantidadeVertices];

	//Vetor que contem o caminho
	int *caminhoOriginal = new int[quantidadeVertices];

	//Zerar vetores
	for (i = 0; i < quantidadeVertices * quantidadeVertices; i++) {
		matrizF[i] = 0;
		matrizD[i] = 0;
	}
	//Caminho original � sempre a sequ�ncia de valores 0,1,2,...,quantidadeVertices
	for (i = 0; i < quantidadeVertices; i++) {
		caminhoOriginal[i] = i;
	}
	//Seta os valores das dist�ncia entre os pontos
	leFluxo();
	//Seta a matriz de Fluxo com as arestas, lendo do caminho do arquivo de arestas
	calculoHaversine(pontos);
	printf("\n MATRIZ F %d\n", quantidadeVertices);
	for (i = 0; i < quantidadeVertices; i++) {
		for (j = 0; j < quantidadeVertices; j++) {
			printf("   %d", matrizF[i * quantidadeVertices + j]);
		}
		printf("\n");
	}
	printf("\n MATRIZ D\n");
	for (i = 0; i < quantidadeVertices; i++) {
		for (j = 0; j < quantidadeVertices; j++) {
			printf(" %f,", matrizD[i * quantidadeVertices + j]);
		}
		printf(";\n");
	}
	printf("\n Links = %d, NumeroVertices = %d \n", numeroLinks, quantidadeVertices);
	//Calcula a distancia total do grafo a a partir do melhor caminho
	printf("\n _________________________________________________\n");
	printf("\n|                      Menu                       |\n");
	printf("\n|_________________________________________________|\n");
	printf("\n|  1 - Algoritmo Genetico                         |");
	printf("\n|_________________________________________________|\n");
	scanf("%d", &opcao);

	distanciaTotalOriginal = calculaComprimentoTotal(caminhoOriginal);
	printf("\n distancia Total %f KM\n", distanciaTotalOriginal);
	switch (opcao){
		case 1:
			melhorCaminho = algoritmoGenetico();
			// montaSaidaComprimento("GA", pontos, caminhoOriginal, distanciaTotalOriginal, melhorCaminho, tempo_gasto);
			// printf("\n distancia Total Do GA %f KM\n", melhorCaminho.custoCaminho);
			// printf("\n distancia Total %f KM\n", distanciaTotalOriginal);
			// printf("\n distancia Total Do GA %f KM\n", melhorCaminho.custoCaminho);
			break;
	}
	free(matrizD);
	free(caminhoOriginal);
	pontos.clear();
	return 0;
}
