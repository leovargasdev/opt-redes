/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

void gravaSaida(char *nomeArquivo, char *conteudo) {

	char hora[50] = "";

	time_t tempo; //vari�vel que guarda um tempo
	struct tm *sTempo;  //estrutura que guarda um tempo

	tempo = time(NULL); //recebe tempo
	sTempo = localtime(&tempo);

	ofstream out; // out � uma variavel.
	char saida[tamanhoString];

	strcpy(saida, caminhoSaida);
	strcat(saida, nomeArquivo);

	strcat(saida, __DATE__);
	sprintf(hora, " Hora %d-%d-%d", sTempo->tm_hour, sTempo->tm_min, sTempo->tm_sec);
	strcat(saida, hora);

	strcat(saida, ".txt");

	out.open(saida); // o arquivo que ser� criado;
	out <<  conteudo;
	out.close(); // n� oesque�a de fechar...

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
	string temp = caminhoParaString2(caminhoOriginal);
	strcat(conteudoSaida, temp.c_str());

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
	tempo_gasto = ((double)(tf - t0)) / clock();

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
	string temp = caminhoParaString2(caminho);
	strcat(conteudoSaida, temp.c_str());

	strcat(conteudoSaida, "\nFragmenta��o Total:");
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

	strcat(conteudoSaida, "\nN� Central:");
	strcat(conteudoSaida, noCentralChar);

	tf = clock();
	tempo_gasto = ((double)(tf - t0)) / clock();

	sprintf(tempoSaida, "\nTempo Gasto = %lf segundos", tempo_gasto);
	strcat(conteudoSaida, tempoSaida);

	gravaSaida(arquivoSaida, conteudoSaida);
}
