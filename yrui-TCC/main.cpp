/*
	Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapecó - SC
	Trabalho de Conclusão de Curso:
	 - Ciência da Computação
	 - Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações
*/

#include "Header/bibliotecas.h"
#include "Header/caminho.h"
#include "Header/nos.h"
#include "Header/matrizFluxo.h"
#include "Header/calculoHaversine.h"
#include "Header/calculaComprimentoCaminho.h"
#include "Header/buscaMaiorDistancia.h"
#include "Header/QAP using GA/main.h"
#include "Header/QAP using GA - Original/main.h"
#include "Header/Força Bruta/mainForcaBruta.h"
#include "Header/Grafo/grafo.h"
#include "Header/saida.h"
#include "Header/Suurballe/matrizAdjacencias.h"
#include "Header/Suurballe/dijkstra.h"
#include "Header/RSA/mainRSA.h"

int main()
{
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

	//Quantidade de vertices é o tamanho do vetor de pontos
	quantidadeVertices = pontos.size();

	//As matrizes são trabalhadas como vetores, para ser possível gerar uma matriz conforme o tamanho da entrada		

	//Matriz de Distâncias, distância entre i e j na posição ij
	matrizD = new double[quantidadeVertices * quantidadeVertices];

	//Matriz de Fluxo, quando ij = 1 existe conexão, quando 0 não existe conexão
	matrizF = new int[quantidadeVertices * quantidadeVertices];
	
	//Vetor que contem o caminho
	int *caminhoOriginal = new int[quantidadeVertices];

	//Zerar vetores
	for (i = 0; i < quantidadeVertices * quantidadeVertices; i++) {
		matrizF[i] = 0;
		matrizD[i] = 0;
	}

	//Somente para teste, inicialização
	int *vetorTeste = new int[quantidadeVertices];
	int *vetorTeste2 = new int[quantidadeVertices];

	//Caminho original é sempre a sequência de valores 0,1,2,...,quantidadeVertices
	for (i = 0; i < quantidadeVertices; i++) {
		caminhoOriginal[i] = i;
	}

	//Seta os valores das distância entre os pontos
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
	printf("\n|  1 - Algoritmo Genético                         |");
	printf("\n|  2 - Força Bruta                                |");
	printf("\n|  3 - RSA                                        |");
	printf("\n|_________________________________________________|\n");
	scanf("%d", &opcao);

	distanciaTotalOriginal = calculaComprimentoTotal(caminhoOriginal);

	switch (opcao)
	{
	case 1:
		melhorCaminho = algoritmoGenetico();
		montaSaidaComprimento("GA", pontos, caminhoOriginal, distanciaTotalOriginal, melhorCaminho, tempo_gasto);

		printf("\n distancia Total %f KM\n", distanciaTotalOriginal);
		printf("\n distancia Total Do GA %f KM\n", melhorCaminho.custoCaminho);

		printf("\n distancia Total %f KM\n", distanciaTotalOriginal);
		printf("\n distancia Total Do GA %f KM\n", melhorCaminho.custoCaminho);

		break;
	case 2:
		melhorCaminho = mainForcaBruta(caminhoOriginal);
		tf = clock();
		tempo_gasto = ((double)(tf - t0)) / CLOCKS_PER_SEC;
		montaSaidaComprimento("ForçaBruta",pontos, caminhoOriginal, distanciaTotalOriginal, melhorCaminho, tempo_gasto);
		break;
	case 3:
		mainRSA(melhorCaminhoRSA, pontos);
		break;
	}
	
	system("pause");

	free(matrizD);
	free(caminhoOriginal);
	pontos.clear();
	return 0;
}
