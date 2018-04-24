
//Função que gera individuos aleatórios
vector<Caminho> initializesPopulation(vector<Caminho> caminhos, int numeroPopulacao, char *caminhoInicial) {
	Caminho caminho;
	int i, j;
	double custoCaminho = 0.0;


	int *caminhoVet = new int[quantidadeVertices];
	int v;

	if (caminhoInicial == "") {
		for (int t = 0; t < quantidadeVertices; t++) {
			caminhoVet[t] = t;
		}
	}
	else{
		caminhoVet = caminhoParaVetorO(caminhoVet, caminhoInicial);
	}

	caminho.custoCaminho = calculaCusto(caminhoVet, 0);

	strcpy(caminho.caminho, caminhoParaStringO(caminhoVet));

	caminhos.push_back(caminho);

	for (int t = 0; t < quantidadeVertices; t++) {
		caminhoVet[t] = -1;
	}

	//Gera numeroPopulacao caminhos
	for (i = 1; i < numeroPopulacao; i++) {
		//Vai adicionando os vertices que não se repetem no caminho
		for (j = 0; j < quantidadeVertices; j++) {

			v = rand() % quantidadeVertices;
			while(find(caminhoVet, caminhoVet + quantidadeVertices, v) != caminhoVet + quantidadeVertices)
			{
				v = rand() % quantidadeVertices;
			}
			/*while (existe(caminhoVet, v, quantidadeVertices)) {
				v = rand() % quantidadeVertices;
			}*/
			caminhoVet[j] = v;
		}

		caminho.custoCaminho = myCost(caminhoVet,1);
		strcpy(caminho.caminho, caminhoParaStringO(caminhoVet));

		caminhos.push_back(caminho);


		for (int t = 0; t < quantidadeVertices; t++) {
			caminhoVet[t] = -1;
		}
	}
	free(caminhoVet);
	return caminhos;
}

Caminho ga() {
	srand(time(NULL));
	int i, iteracoes;
	int iteracoesCrossover, iteracoesMutacao, iteracoesTruncate;
	int numeroCaminhos;
	vector<Caminho> caminhos = initializesPopulation(caminhos, nPopO, "");
	vector<Caminho> caminhosCrossover;
	vector<Caminho> caminhosMutacao;
	vector<Caminho> caminhosNaoSelecionados;
	vector<Caminho> caminhosEquivalencia;
	Caminho caminhoRetorno;

	Caminho CaminhoMutacionado;

	//printf("\nVetor Original\n");
	//for (int i = 0; i < caminhos.size(); i++) {
	//	printf("Caminho %s Custo = %f \n",caminhos[i].caminho,caminhos[i].custoCaminho);
	//}

	
	sort(caminhos.begin(), caminhos.end(), comparaCusto);

	//printf("\nVetor Ordenado\n");
	//for (int i = 0; i < caminhos.size(); i++) {
	//	printf("Caminho %s Custo = %f \n", caminhos[i].caminho, caminhos[i].custoCaminho);
	//}


	double melhorCusto = 0;
	double piorCusto;
	int individuoCross1 = 0;
	int individuoCross2 = 0;
	double *P = new double[nPopO];
	double somaCustos = 0.0;
	int repeticoesSemAlteracao = 0;


		for (iteracoes = 0; iteracoes < MaxItO; iteracoes++) {
			somaCustos = 0.0;
			if (melhorCusto > caminhos[0].custoCaminho) {
				printf("\n MelhorCusto = %f", melhorCusto);
			}


			melhorCusto = caminhos[0].custoCaminho;

		
			piorCusto = caminhos[caminhos.size() - 1].custoCaminho;
			//Atualiza matriz que divide os bilhetes entre os caminhos, onde cada um recebe um peso relacionado ao seu custo
			// e o pior custo da população
			for (i = 0; i < nPopO; i++) {
				P[i] = exp(-betaO*caminhos[i].custoCaminho / piorCusto);
				somaCustos += exp(-betaO*caminhos[i].custoCaminho / piorCusto);
			}

			//Divide de forma corrente os valores da matriz para cada individuo de 0 até 1
			//Os melhores ganham mais bilhetes
			for (i = 0; i < nPopO; i++) {
				P[i] = P[i] / somaCustos;
			}

			//Seleciona dois individuos utilizando a roleta, e faz o crossover entre os individuos
			for (iteracoesCrossover = 0; iteracoesCrossover < ncO / 2; iteracoesCrossover++) {
				individuoCross1 = RoulettteWhellSelectionO(P);
				individuoCross2 = RoulettteWhellSelectionO(P);

				caminhosCrossover = PermutationCrossoverO(caminhosCrossover, caminhos, individuoCross1, individuoCross2);
			}
			int individuoAleatorioMutacao;

			//Faz a mutação de um individuo, gerando um novo individuo
			for (iteracoesMutacao = 0; iteracoesMutacao < nmO; iteracoesMutacao++) {
				individuoAleatorioMutacao = rand() % nPopO;

				CaminhoMutacionado = PermutationMutateO(caminhos[individuoAleatorioMutacao]);

				caminhosMutacao.push_back(CaminhoMutacionado);
			}

			//Insere os novos individuos na População
			caminhos.insert(caminhos.end(), caminhosCrossover.begin(), caminhosCrossover.end());
			caminhos.insert(caminhos.end(), caminhosMutacao.begin(), caminhosMutacao.end());

			//Ordena a população conforme o seu custo
			sort(caminhos.begin(), caminhos.end(), comparaCusto);

			numeroCaminhos = caminhos.size();

			//Faz um truncate, pegando somente os nPop Melhores
			for (iteracoesTruncate = nPopO; iteracoesTruncate < numeroCaminhos; iteracoesTruncate++) {
				caminhos.erase(caminhos.begin() + nPopO);
				
			}

			caminhosEquivalencia.clear();
			caminhosMutacao.clear();
			caminhosCrossover.clear();
		}
	
	free(P);
	caminhoRetorno = caminhos[0];
	return caminhoRetorno;
}
