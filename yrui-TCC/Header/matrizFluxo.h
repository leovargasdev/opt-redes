/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

//Le o arquivo de fluxo e monta a matriz F de Fluxo.
void leFluxo() {
	FILE *arquivo;
	char linha[tamanhoString];
	char *linhas[tamanhoString];
	int numArestas = 0;
	int i = 0;
	int j = 0;

	arquivo = fopen(caminhoArestas, "rt");
	if (arquivo == NULL)  // Se houve erro na abertura
	{
		printf("Problemas na abertura do arquivo\n");
	}
	else {
		while (fgets(linha, sizeof linha, arquivo) != NULL)
		{
			//Adiciona linha ao vetor
			linhas[i] = strdup(linha);
			i++;
			//Conta a quantidade de arestas
			numArestas++;
		}
	}


	int posicao;
	int posicaoI = 0, posicaoJ = 0;
	char *token;
	//Le todas as palavras separando por \t
	for (j = 0; j < numArestas; j++) {
		token = strtok(linhas[j], "\t");
		posicao = 0;
		while (token != NULL) {

			switch (posicao) {
			case 0:
				//Posicao -1 pois o arquivo come�a no vertice 1
				posicaoI = atoi(token) - 1;
				break;
			case 1:
				posicaoJ = atoi(token) - 1;
				break;

			}
			token = strtok(NULL, "\t");
			posicao++;
		}
		//Adiciona na matriz na posi��o que contem conex�o entre ij, tanto na parte superior quanto inferior
		matrizF[posicaoI * quantidadeVertices + posicaoJ] = 1;
		matrizF[posicaoJ * quantidadeVertices + posicaoI] = 1;

		numeroLinks++;
	};
	
	fclose(arquivo);
}
