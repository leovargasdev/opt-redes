/*
	Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapecó - SC
	Trabalho de Conclusão de Curso:
		- Ciência da Computação
		- Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações
*/

void calculaMatrizAdjacencias(double *matrizAdjacencias, int *caminho) {
	int i, j;
	for (i = 0; i < quantidadeVertices; i++) {
		for (j = 0; j < quantidadeVertices; j++) {
			matrizAdjacencias[caminho[i] * quantidadeVertices + caminho[j]] = matrizF[i * quantidadeVertices + j] * matrizD[caminho[i] * quantidadeVertices + caminho[j]];
		}
	}
}