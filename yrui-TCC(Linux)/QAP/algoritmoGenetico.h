//Verifica se ja existe um individuo igual na popula��o
int comparaCaminhos(vector<Caminho> caminhosNaoEquivalentes, Caminho caminho, int totalEquivalentePossivel) {
	int i,j, posicoesIguais;
	int *vetor1 = new int[quantidadeVertices];
	int *vetor2 = new int[quantidadeVertices];
	int numeroCaminhosEquivalentes = caminhosNaoEquivalentes.size();
	vetor2 = caminhoParaVetor(vetor2,caminho.caminho);

	for (i = 0; i < numeroCaminhosEquivalentes; i++) {
		vetor1 = caminhoParaVetor(vetor1,caminhosNaoEquivalentes[i].caminho);
		posicoesIguais = 0;
		//Verifica se todas as posi��es s�o iguais, do caminho novo e dos caminhos da popula��o
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

//Insere na popula��o somente caminhos que j� n�o existem na popula��o
vector<Caminho>  buscaCaminhosNaoEquivalentes(vector<Caminho> caminhosNaoEquivalentes,vector<Caminho> caminhos, int totalEquivalentePossivel) {
	caminhosNaoEquivalentes.push_back(caminhos[0]);
	int i, temIgual = 0;
	int numeroCaminhos = caminhos.size();
	for (i = 1; i < numeroCaminhos ; i++) {
		temIgual = comparaCaminhos(caminhosNaoEquivalentes, caminhos[i], totalEquivalentePossivel);
		//Se n�o existir um caminho igual ele insere na popula��o
		if (temIgual == 0) {
			caminhosNaoEquivalentes.push_back(caminhos[i]);
		}
		if (caminhosNaoEquivalentes.size() == nPop) {
			return caminhosNaoEquivalentes;
		}
	}
	return caminhosNaoEquivalentes;
}
//Fun��o que gera individuos aleat�rios
vector<Caminho> inicializaPopulacao(vector<Caminho> caminhos, int numeroPopulacao) {
	Caminho caminho;
	int i, j;
	double custoCaminho = 0.0;
	int *caminhoVet = new int[quantidadeVertices];
	int v;
	//A cada duas renova��es adiciona o caminho original a popula��o
	if (numeroDeRenovacoes == 2) {
		for (int t = 0; t < quantidadeVertices; t++) {
			caminhoVet[t] = t;
		}
		caminho.custoCaminho = calculaCusto(caminhoVet, 0);
		string temp = caminhoParaString2(caminhoVet);
		strcpy(caminho.caminho, temp.c_str());
		caminhos.push_back(caminho);
	}

	for (int t = 0; t < quantidadeVertices; t++) {
		caminhoVet[t] = -1;
	}
	//Gera numeroPopulacao caminhos
	for (i = 0; i < numeroPopulacao; i++) {
		//Vai adicionando os vertices que n�o se repetem no caminho
		for (j = 0; j < quantidadeVertices; j++) {
			v = rand() % quantidadeVertices;
			while(find(caminhoVet, caminhoVet + quantidadeVertices, v) != caminhoVet + quantidadeVertices){
				v = rand() % quantidadeVertices;
			}
			caminhoVet[j] = v;
		}
		//No calculo de custo � controlado o numero de links que permitem exceder a distancia total permitida
		caminho.custoCaminho = calculaCusto(caminhoVet,1);
		//Caminho com custo 0 quando n�o � um caminho valido, ou seja, excede o numero de links com distancia total permitida
		if (caminho.custoCaminho != 0) {
			string temp = caminhoParaString2(caminhoVet);
			strcpy(caminho.caminho, temp.c_str());
			caminhos.push_back(caminho);
		} else {
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
	//Conforme o tamanho do grafo � alterada a configura��o, o tamanho � dado pela quantidade de vertices
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
	printf("\nnumeroNosExcedeDistanciaMaxima: %d\n", numeroNosExcedeDistanciaMaxima);
	distanciaMaxima = buscaMaiorDistanciaPermitida();
	printf("\nDistancia Maxima: %f\n", distanciaMaxima);
	int i, iteracoes;
	int iteracoesCrossover, iteracoesMutacao, iteracoesTruncate,iteradorCaminhosMutacionados;
	int numeroCaminhos;
	vector<Caminho> caminhos;
	caminhos = inicializaPopulacao(caminhos, nPop);
	// for (int i = 0; i < 10; i++)
	// 	cout << "caminho: " << caminhos[i].caminho << ", custo: " << caminhos[i].custoCaminho << "\n";

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
	cout << "numeroEquivalentesPermitidos: " << numeroEquivalentesPermitidos << "\n";
	// return caminhos[0];
	double melhorCusto = 0;
	double piorCusto;
	int individuoCross1 = 0;
	int individuoCross2 = 0;
	double *P = new double[nPop];
	double somaCustos = 0.0;
	int repeticoesSemAlteracao = 0;
	int iteracoesTotais = 0;
	while (repeticoesSemAlteracao < maxItSemAlteracao){
		for (iteracoes = 0; iteracoes < MaxIt; iteracoes++){
			// if (repeticoesSemAlteracao > maxItSemAlteracao)
			// 	return caminhos[0];
			somaCustos = 0.0;
			if (melhorCusto <= caminhos[0].custoCaminho) {
				repeticoesSemAlteracao++;
			} else {
				//printf("\nPosicoes sem mudar %d, = %f", repeticoesSemAlteracao, caminhos[0].custoCaminho);
				tf = clock();
				tempo_gasto = ((double)(tf - t0)) / clock();
				repeticoesSemAlteracao = 0;
			}
			if (iteracoesTotais % 250 == 0) {
				//printf("\nMelhor caminho %s, = %f. It(%d). Tempo(%f)\n", caminhos[0].caminho, caminhos[0].custoCaminho, iteracoesTotais, tempo_gasto);
				printf("\n%f,It(%d)\n", caminhos[0].custoCaminho, iteracoesTotais);
			}
			iteracoesTotais++;
			melhorCusto = caminhos[0].custoCaminho;
			piorCusto = caminhos[caminhos.size() - 1].custoCaminho;
			//Atualiza matriz que divide os bilhetes entre os caminhos, onde cada um recebe um peso relacionado ao seu custo
			// e o pior custo da popula��o
			for (i = 0; i < nPop; i++) {
				P[i] = exp(-beta*caminhos[i].custoCaminho / piorCusto);
				somaCustos += exp(-beta*caminhos[i].custoCaminho / piorCusto);
			}
			//Divide de forma corrente os valores da matriz para cada individuo de 0 at� 1
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
			//Faz a muta��o de um individuo, gerando um novo individuo
			for (iteracoesMutacao = 0; iteracoesMutacao < nm; iteracoesMutacao++) {
				individuoAleatorioMutacao = rand() % nPop;
				caminhosMutacionados = mutacao(caminhosMutacionados,caminhos[individuoAleatorioMutacao]);
				for(iteradorCaminhosMutacionados = 0; iteradorCaminhosMutacionados < caminhosMutacionados.size(); iteradorCaminhosMutacionados++){
					caminhosMutacao.push_back(caminhosMutacionados[iteradorCaminhosMutacionados]);
				}
				caminhosMutacionados.clear();
			}
			//Insere os novos individuos na Popula��o
			caminhos.insert(caminhos.end(), caminhosCrossover.begin(), caminhosCrossover.end());
			caminhos.insert(caminhos.end(), caminhosMutacao.begin(), caminhosMutacao.end());

			//Ordena a popula��o conforme o seu custo
			sort(caminhos.begin(), caminhos.end(), comparaCusto);



			numeroCaminhos = caminhos.size();
			caminhosEquivalencia = buscaCaminhosNaoEquivalentes(caminhosEquivalencia, caminhos, quantidadeVertices - 1);
			if (caminhosEquivalencia.size() < nPop){
				caminhosEquivalencia = inicializaPopulacao(caminhosEquivalencia, nPop - caminhosEquivalencia.size());
			}
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
			printf("\nMuda Popula��o\n");
			std::cout << "numeroIndividuosSobrevivem: " << numeroIndividuosSobrevivem << ", caminhos.size(): " << caminhos.size() << '\n';
			for (iteracoesTruncate = numeroIndividuosSobrevivem; iteracoesTruncate < nPop; iteracoesTruncate++){
				caminhos.erase(caminhos.begin() + numeroIndividuosSobrevivem);
			}
			std::cout << "[depois truncate] caminhos.size(): " << caminhos.size() << '\n';
			//for (iteracoesNaoSelecionados = 0; iteracoesNaoSelecionados < nPop - numeroIndividuosSobrevivem; iteracoesNaoSelecionados++)
				//caminhos.push_back(caminhosNaoSelecionados[iteracoesNaoSelecionados]);
			caminhos = inicializaPopulacao(caminhos, nPop - caminhos.size());
			numeroDeRenovacoes++;
			caminhosNaoSelecionados.clear();
		}
	}
	free(P);
	caminhoRetorno = caminhos[0];
	std::cout << "custo: " << caminhoRetorno.custoCaminho << '\n';
	return caminhoRetorno;
}
