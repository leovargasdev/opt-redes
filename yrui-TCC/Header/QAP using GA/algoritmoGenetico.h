/*
	Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapecó - SC
	Trabalho de Conclusão de Curso:
		- Ciência da Computação
		- Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações
*/

//Verifica se ja existe um individuo igual na população
int comparaCaminhos(vector<Caminho> caminhosNaoEquivalentes, Caminho caminho, int totalEquivalentePossivel) {
	int i,j, posicoesIguais;
	int *vetor1 = new int[quantidadeVertices];
	int *vetor2 = new int[quantidadeVertices];
	int numeroCaminhosEquivalentes = caminhosNaoEquivalentes.size();
	vetor2 = caminhoParaVetor(vetor2,caminho.caminho);

	for (i = 0; i < numeroCaminhosEquivalentes; i++) {
		vetor1 = caminhoParaVetor(vetor1,caminhosNaoEquivalentes[i].caminho);
		posicoesIguais = 0;
		//Verifica se todas as posições são iguais, do caminho novo e dos caminhos da população
		for (j = 0; j < quantidadeVertices; j++) {
			if (vetor1[j] == vetor2[j]) {
				posicoesIguais ++;
			}
		}

		if (posicoesIguais > totalEquivalentePossivel) {
			free(vetor1);
			free(vetor2);
			return 1;
		}
	}
	free(vetor1);
	free(vetor2);
	return 0;
}

//Insere na população somente caminhos que já não existem na população
vector<Caminho>  buscaCaminhosNaoEquivalentes(vector<Caminho> caminhosNaoEquivalentes,vector<Caminho> caminhos, int totalEquivalentePossivel) {
	caminhosNaoEquivalentes.push_back(caminhos[0]);
	int i, temIgual = 0;
	int numeroCaminhos = caminhos.size();
	for (i = 1; i < numeroCaminhos ; i++) {
		temIgual = comparaCaminhos(caminhosNaoEquivalentes, caminhos[i], totalEquivalentePossivel);
		//Se não existir um caminho igual ele insere na população
		if (temIgual == 0) {
			caminhosNaoEquivalentes.push_back(caminhos[i]);
		}
		
		if (caminhosNaoEquivalentes.size() == nPop) {
			return caminhosNaoEquivalentes;
		}
	}
	return caminhosNaoEquivalentes;
}



//Função que gera individuos aleatórios
vector<Caminho> inicializaPopulacao(vector<Caminho> caminhos, int numeroPopulacao, char *caminhoInicial) {
	Caminho caminho;
	int i, j;
	double custoCaminho = 0.0;
	srand(time(NULL));

	int *caminhoVet = new int[quantidadeVertices];
	int v;

	//A cada duas renovações adiciona o caminho original a população
	if (numeroDeRenovacoes == 2) {
		for (int t = 0; t < quantidadeVertices; t++) {
			caminhoVet[t] = t;
		}
		caminho.custoCaminho = calculaCusto(caminhoVet, 0);
		strcpy(caminho.caminho, caminhoParaString(caminhoVet));
		caminhos.push_back(caminho);
	}

	//Adiciona o caminho que vem por parametro na população
	if (caminhoInicial != ""){
		caminhoVet = caminhoParaVetor(caminhoVet, caminhoInicial);
		caminho.custoCaminho = calculaCusto(caminhoVet, 0);
		strcpy(caminho.caminho, caminhoParaString(caminhoVet));
		caminhos.push_back(caminho);
	}

	for (int t = 0; t < quantidadeVertices; t++) {
		caminhoVet[t] = -1;
	}

	//Gera numeroPopulacao caminhos
	for (i = 0; i < numeroPopulacao; i++) {
		//Vai adicionando os vertices que não se repetem no caminho
		for (j = 0; j < quantidadeVertices; j++) {

			v = rand() % quantidadeVertices;
			while(find(caminhoVet, caminhoVet + quantidadeVertices, v) != caminhoVet + quantidadeVertices)
			{
				v = rand() % quantidadeVertices;
			}
			caminhoVet[j] = v;
		}

		//No calculo de custo é controlado o numero de links que permitem exceder a distancia total permitida
		caminho.custoCaminho = calculaCusto(caminhoVet,1);
		//Caminho com custo 0 quando não é um caminho valido, ou seja, excede o numero de links com distancia total permitida
		if (caminho.custoCaminho != 0) {
			strcpy(caminho.caminho, caminhoParaString(caminhoVet));
			caminhos.push_back(caminho);
		}
		else {
			i--;
		}

		for (int t = 0; t < quantidadeVertices; t++) {
			caminhoVet[t] = -1;
		}
	}
	free(caminhoVet);
	return caminhos;
}

Caminho algoritmoGenetico() {
	//Conforme o tamanho do grafo é alterada a configuração, o tamanho é dado pela quantidade de vertices
	if (quantidadeVertices <= 20)
		inicializaConfiguracao1();
	else if (quantidadeVertices > 20 && quantidadeVertices <= 40)
		inicializaConfiguracao2();
	else if (quantidadeVertices > 40 && quantidadeVertices <= 60)
		inicializaConfiguracao3();
	else
		inicializaConfiguracao4();

	//Numero de links que podem exceder a distancia maxima permitida para um link
	numeroNosExcedeDistanciaMaxima = round(porcentagemMaiorDistancia * numeroLinks);

	//busca qual é a maior distância permitida para um Link
	distanciaMaxima = buscaMaiorDistanciaPermitida();
	printf("\nDistancia Maxima = %f\n", distanciaMaxima);

	int i, iteracoes;
	int iteracoesCrossover, iteracoesMutacao, iteracoesTruncate,iteradorCaminhosMutacionados;
	int numeroCaminhos;

	vector<Caminho> caminhos = inicializaPopulacao(caminhos, nPop, "");
	vector<Caminho> caminhosCrossover;
	vector<Caminho> caminhosMutacao;
	vector<Caminho> caminhosNaoSelecionados;
	vector<Caminho> caminhosEquivalencia;
	vector<Caminho> caminhosMutacionados;
	Caminho caminhoRetorno;

	Caminho CaminhoMutacionado;

	if (numeroIndividuosSobrevivem == 0)
		numeroIndividuosSobrevivem = 1;

	sort(caminhos.begin(), caminhos.end(), comparaCusto);

	numeroEquivalentesPermitidos = round(porcentagemEquivalenciaPermitida * quantidadeVertices);

	double melhorCusto = 0;
	double piorCusto;
	int individuoCross1 = 0;
	int individuoCross2 = 0;
	double *P = new double[nPop];
	double somaCustos = 0.0;
	int repeticoesSemAlteracao = 0;
	int iteracoesTotais = 0;

	while (repeticoesSemAlteracao < maxItSemAlteracao)
	{
		for (iteracoes = 0; iteracoes < MaxIt; iteracoes++) {

			if (repeticoesSemAlteracao > maxItSemAlteracao)
				return caminhos[0];

			somaCustos = 0.0;
			if (melhorCusto <= caminhos[0].custoCaminho) {
				repeticoesSemAlteracao++;
			}
			else {
				//printf("\nPosicoes sem mudar %d, = %f", repeticoesSemAlteracao, caminhos[0].custoCaminho);

				tf = clock();
				tempo_gasto = ((double)(tf - t0)) / CLOCKS_PER_SEC;

				repeticoesSemAlteracao = 0;
			}

			if (iteracoesTotais % 10 == 0) {
				//printf("\nMelhor caminho %s, = %f. It(%d). Tempo(%f)\n", caminhos[0].caminho, caminhos[0].custoCaminho, iteracoesTotais, tempo_gasto);
				printf("\n%f,It(%d)\n", caminhos[0].custoCaminho, iteracoesTotais);
			}


			iteracoesTotais++;

			melhorCusto = caminhos[0].custoCaminho;
			piorCusto = caminhos[caminhos.size() - 1].custoCaminho;
			//Atualiza matriz que divide os bilhetes entre os caminhos, onde cada um recebe um peso relacionado ao seu custo
			// e o pior custo da população
			for (i = 0; i < nPop; i++) {
				P[i] = exp(-beta*caminhos[i].custoCaminho / piorCusto);
				somaCustos += exp(-beta*caminhos[i].custoCaminho / piorCusto);
			}

			//Divide de forma corrente os valores da matriz para cada individuo de 0 até 1
			//Os melhores ganham mais bilhetes
			for (i = 0; i < nPop; i++) {
				P[i] = P[i] / somaCustos;
			}

			//Seleciona dois individuos utilizando a roleta, e faz o crossover entre os individuos
			for (iteracoesCrossover = 0; iteracoesCrossover < nc / 2; iteracoesCrossover++) {
				individuoCross1 = roletaSeleciona(P);
				individuoCross2 = roletaSeleciona(P);

				caminhosCrossover = crossover(caminhosCrossover, caminhos, individuoCross1, individuoCross2);
			}
			int individuoAleatorioMutacao;

			//Faz a mutação de um individuo, gerando um novo individuo
			for (iteracoesMutacao = 0; iteracoesMutacao < nm; iteracoesMutacao++) {
				individuoAleatorioMutacao = rand() % nPop;

				caminhosMutacionados = mutacao(caminhosMutacionados,caminhos[individuoAleatorioMutacao]);

				for(iteradorCaminhosMutacionados = 0; iteradorCaminhosMutacionados < caminhosMutacionados.size(); iteradorCaminhosMutacionados++)
					caminhosMutacao.push_back(caminhosMutacionados[iteradorCaminhosMutacionados]);

				caminhosMutacionados.clear();
			}

			//Insere os novos individuos na População
			caminhos.insert(caminhos.end(), caminhosCrossover.begin(), caminhosCrossover.end());
			caminhos.insert(caminhos.end(), caminhosMutacao.begin(), caminhosMutacao.end());

			//Ordena a população conforme o seu custo
			sort(caminhos.begin(), caminhos.end(), comparaCusto);

			numeroCaminhos = caminhos.size();
			
			caminhosEquivalencia = buscaCaminhosNaoEquivalentes(caminhosEquivalencia, caminhos, quantidadeVertices - 1);

			if (caminhosEquivalencia.size() < nPop)
				caminhosEquivalencia = inicializaPopulacao(caminhosEquivalencia, nPop - caminhosEquivalencia.size(), "");

			copy(caminhosEquivalencia.begin(), caminhosEquivalencia.end(), caminhos.begin());
			//Faz um truncate, pegando somente os nPop Melhores
			for (iteracoesTruncate = nPop; iteracoesTruncate < numeroCaminhos; iteracoesTruncate++) {
				caminhosNaoSelecionados.push_back(caminhos[nPop]);
				caminhos.erase(caminhos.begin() + nPop);
				
			}

			caminhosEquivalencia.clear();
			caminhosMutacao.clear();
			caminhosCrossover.clear();

			sort(caminhosNaoSelecionados.begin(), caminhosNaoSelecionados.end(), comparaCusto);
			caminhosEquivalencia = buscaCaminhosNaoEquivalentes(caminhosEquivalencia, caminhosNaoSelecionados, numeroEquivalentesPermitidos);

			copy(caminhosEquivalencia.begin(), caminhosEquivalencia.end(), caminhosNaoSelecionados.begin());
			numeroCaminhos = caminhosNaoSelecionados.size();

			for (iteracoesTruncate = nPop; iteracoesTruncate < numeroCaminhos; iteracoesTruncate++) {
				caminhosNaoSelecionados.erase(caminhosNaoSelecionados.begin() + nPop);
			}

			sort(caminhosNaoSelecionados.begin(), caminhosNaoSelecionados.end(), comparaCusto);
			caminhosEquivalencia.clear();

		}

		if (repeticoesSemAlteracao < maxItSemAlteracao) {
			//Faz um truncate, pegando somente os nPop Melhores]
			printf("\nMuda População\n");
			for (iteracoesTruncate = numeroIndividuosSobrevivem; iteracoesTruncate < nPop; iteracoesTruncate++)
				caminhos.erase(caminhos.begin() + numeroIndividuosSobrevivem);

			//for (iteracoesNaoSelecionados = 0; iteracoesNaoSelecionados < nPop - numeroIndividuosSobrevivem; iteracoesNaoSelecionados++) 
				//caminhos.push_back(caminhosNaoSelecionados[iteracoesNaoSelecionados]);

			caminhos = inicializaPopulacao(caminhos, nPop - caminhos.size(), "");
			numeroDeRenovacoes++;
			caminhosNaoSelecionados.clear();
		}

	}
	free(P);
	caminhoRetorno = caminhos[0];
	return caminhoRetorno;
}
