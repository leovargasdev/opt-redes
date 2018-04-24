/*
	Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapecó - SC
	Trabalho de Conclusão de Curso:
		- Ciência da Computação
		- Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações
*/

void gravaSaida(char *nomeArquivo, char *conteudo) {

	char hora[50] = "";

	time_t tempo; //variável que guarda um tempo
	struct tm *sTempo;  //estrutura que guarda um tempo

	tempo = time(NULL); //recebe tempo
	sTempo = localtime(&tempo);

	ofstream out; // out é uma variavel.
	char saida[tamanhoString];

	strcpy(saida, caminhoSaida);
	strcat(saida, nomeArquivo);

	strcat(saida, __DATE__);
	sprintf(hora, " Hora %d-%d-%d", sTempo->tm_hour, sTempo->tm_min, sTempo->tm_sec);
	strcat(saida, hora);

	strcat(saida, ".txt");

	out.open(saida); // o arquivo que será criado;
	out <<  conteudo;
	out.close(); // nã oesqueça de fechar...

}

void montaSaidaComprimento(char tipo[10],vector<Vertice> pontos, int caminhoOriginal[tamanhoString], double distanciaTotalOriginal, Caminho melhorCaminhoGA, double tempo_gasto) {
	char tempoSaida[tamanhoString] = "";
	char arquivoSaida[tamanhoString] = "";
	char conteudoSaida[10000] = "";
	char distanciaSaida[tamanhoString] = "";
	char infoVertices[100] = "";
	int countVertices;

	strcat(arquivoSaida, tipo);

	strcat(arquivoSaida, "ArquivoComprimento");

	sprintf(distanciaSaida, " %f ", melhorCaminhoGA.custoCaminho);
	strcat(arquivoSaida, distanciaSaida);


	strcat(conteudoSaida, "Mapeamento:");
	for (countVertices = 0; countVertices < quantidadeVertices; countVertices++) {
		sprintf(infoVertices, "\n%d -> %s", pontos[countVertices].id, pontos[countVertices].nome);
		strcat(conteudoSaida, infoVertices);
	}

	strcat(conteudoSaida, "\n\nCaminho Original:");
	strcat(conteudoSaida, caminhoParaString(caminhoOriginal));

	strcat(conteudoSaida, "\nComprimento Total Original ");
	sprintf(distanciaSaida, "%f", distanciaTotalOriginal);
	strcat(conteudoSaida, distanciaSaida);
	strcat(conteudoSaida, "\n");

	strcat(conteudoSaida, "\n\nCaminho do GA:");
	strcat(conteudoSaida, melhorCaminhoGA.caminho);

	strcat(conteudoSaida, "\nComprimento Total do GA ");
	sprintf(distanciaSaida, "%f", melhorCaminhoGA.custoCaminho);
	strcat(conteudoSaida, distanciaSaida);
	strcat(conteudoSaida, "\n");

	strcat(conteudoSaida, buscaParametrosGA());



	sprintf(tempoSaida, "\n\nTempo Gasto Para melhor Caminho = %lf segundos", tempo_gasto);
	strcat(conteudoSaida, tempoSaida);

	tf = clock();
	tempo_gasto = ((double)(tf - t0)) / CLOCKS_PER_SEC;

	sprintf(tempoSaida, "\n\nTempo Gasto Total = %lf segundos", tempo_gasto);
	strcat(conteudoSaida, tempoSaida);


	gravaSaida(arquivoSaida, conteudoSaida);
}
void montaSaidaRSA(char rede[10], vector<Vertice> pontos, int *caminho, double fragmentacao, double totalRequisicoes, double totalBloqueios, int noCentral) {
	char tempoSaida[tamanhoString] = "";
	char arquivoSaida[tamanhoString] = "";
	char conteudoSaida[10000] = "";
	char fragmentacaoChar[tamanhoString] = "";
	char bloqueioChar[tamanhoString] = "";
	char noCentralChar[20] = "";
	char taxaBloqueioChar[tamanhoString] = "";
	char totalRequisicoesChar[tamanhoString] = "";
	char infoVertices[100] = "";
	int countVertices;

	strcat(arquivoSaida, "ArquivoRSA");

	strcat(arquivoSaida, rede);


	strcat(conteudoSaida, "Mapeamento:");
	for (countVertices = 0; countVertices < quantidadeVertices; countVertices++) {
		sprintf(infoVertices, "\n%d -> %s", pontos[countVertices].id, pontos[countVertices].nome);

		if (pontos[countVertices].id == noCentral)
			strcpy(noCentralChar, pontos[countVertices].nome);

		strcat(conteudoSaida, infoVertices);
	}

	strcat(conteudoSaida, "\nCaminho:");
	strcat(conteudoSaida, caminhoParaString(caminho));

	strcat(conteudoSaida, "\nFragmentação Total:");
	sprintf(fragmentacaoChar, "%f", fragmentacao);
	strcat(conteudoSaida, fragmentacaoChar);

	strcat(conteudoSaida, "\nTotal Requisicoes:");
	sprintf(totalRequisicoesChar, "%f", totalRequisicoes);
	strcat(conteudoSaida, totalRequisicoesChar);

	strcat(conteudoSaida, "\nBloqueios:");
	sprintf(bloqueioChar, "%f", totalBloqueios);
	strcat(conteudoSaida, bloqueioChar);

	strcat(conteudoSaida, "\nTaxa de Bloqueio:");
	sprintf(taxaBloqueioChar, "%f", 1 - ((totalRequisicoes - totalBloqueios) / totalRequisicoes));
	strcat(conteudoSaida, taxaBloqueioChar);

	strcat(conteudoSaida, "\nNó Central:");
	strcat(conteudoSaida, noCentralChar);

	tf = clock();
	tempo_gasto = ((double)(tf - t0)) / CLOCKS_PER_SEC;

	sprintf(tempoSaida, "\nTempo Gasto = %lf segundos", tempo_gasto);
	strcat(conteudoSaida, tempoSaida);

	gravaSaida(arquivoSaida, conteudoSaida);
}