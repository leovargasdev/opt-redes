/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

void calculaMatrizAdjacencias(double *matrizAdjacencias, int *caminho) {
	int i, j;
	for (i = 0; i < quantidadeVertices; i++) {
		for (j = 0; j < quantidadeVertices; j++) {
			matrizAdjacencias[caminho[i] * quantidadeVertices + caminho[j]] = matrizF[i * quantidadeVertices + j] * matrizD[caminho[i] * quantidadeVertices + caminho[j]];
		}
		}
}
