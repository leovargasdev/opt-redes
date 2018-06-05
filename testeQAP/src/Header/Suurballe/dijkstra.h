/*
	Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapecó - SC
	Trabalho de Conclusão de Curso:
		- Ciência da Computação
		- Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações
*/

//Encontra o menor vertice visinho ainda não visitado, e não esta na arvore de caminho mais curto
int verticeMenorDistancia(double dist[], bool visitado[])
{
	// Initialize min value
	double min = INT_MAX;
	int vertice;

	for (int v = 0; v < quantidadeVertices; v++) {
		if (visitado[v] == false && dist[v] <= min) {
			min = dist[v];
			vertice = v;
		}
	}

	return vertice;
}

//Função que calcula a distância dó caminho de uma unica origem
int* dijkstra(int *caminho, double *matrizAdjacencia, int noOrigem, int noDestino)
{
	double *dist = new double[quantidadeVertices];     //Vetor que contem a distancia da origem para o Vertice index
	bool *visitado = new bool[quantidadeVertices];; //Vetor que contem se o nó indice ja foi visitado
	int *caminhoAux = new int[quantidadeVertices];
	int *anterior = new int[quantidadeVertices];


	//Todas as distancias não são visitadas, e nenhum vertice esta visitado
	for (int i = 0; i < quantidadeVertices; i++) {
		dist[i] = INT_MAX;
		visitado[i] = false;
		anterior[i] = -1;
	}
	//Distancia do vertice para ele mesmo é 0
	dist[noOrigem] = 0.0;

	//Encontra o menor caminho do vertice origem para os demais
	for (int count = 0; count < quantidadeVertices - 1; count++)
	{
		//Pega o vertice de distancia minima não visitado, onde na primeira vez é sempre o vertice de origem
		int u = verticeMenorDistancia(dist, visitado);

		//O vertice com a distancia minima é marcado como visitado
		visitado[u] = true;

		// Atualize o valor dist dos vértices adjacentes do vértice escolhido.
		for (int v = 0; v < quantidadeVertices; v++) {

			//Atualiza a distancia do vertice vizinho caso o vertice não foi visitado, existe conexão entre os vertices
			//e a distancia do caminho é menor que a distancia do vizinho. Atualizando o vetor dos vértices anteriores
			if (!visitado[v] && matrizAdjacencia[u * quantidadeVertices + v] && dist[u] != INT_MAX	&& dist[u] + matrizAdjacencia[u * quantidadeVertices + v] < dist[v]) {
				dist[v] = dist[u] + matrizAdjacencia[u * quantidadeVertices + v];
				anterior[v] = u;
			}
		}
	}
	//printf("Caminho: ");
	int posicao = 0;
	int v, h = 0;
	for (v = noDestino; v != -1; v = anterior[v], posicao++) {
		caminhoAux[posicao] = v;

	}

	for (v = posicao - 1; v >= 0; v--, h++) {
		caminho[h] = caminhoAux[v];
	}

	free(dist);
	free(visitado);
	free(caminhoAux);
	free(anterior);

	return caminho;
}
