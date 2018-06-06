/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

//Crossover � de corte simples
vector<Caminho> crossover(vector<Caminho> caminhosCrossover, vector<Caminho> caminhos, int individuoCross1, int individuoCross2) {
	Caminho caminho;

	int *caminho1 = new int[quantidadeVertices];
	int *caminho2 = new int[quantidadeVertices];
	int *caminho1Aux = new int[quantidadeVertices];
	int *caminho2Aux = new int[quantidadeVertices];
	char caminhoChar1[tamanhoString];
	char caminhoChar2[tamanhoString];

	strcpy(caminhoChar1, caminhos[individuoCross1].caminho);
	strcpy(caminhoChar2, caminhos[individuoCross2].caminho);

	caminho1 = caminhoParaVetor(caminho1,caminhoChar1);
	caminho2 = caminhoParaVetor(caminho2,caminhoChar2);

	int corte = rand() % (quantidadeVertices -1);

	if (corte == 0) {
		corte = 1;
	}

	int *primeiraParteInd1 = new int[corte];
	int *segundaParteInd1 = new int[quantidadeVertices - corte];
	int *primeiraParteInd2 = new int[corte];
	int *segundaParteInd2 = new int[quantidadeVertices - corte];
	int *repeteInicioInd1noFimInd2 = new int[corte];
	int *repeteInicioInd2noFimInd1 = new int[corte];

	int i, j, k;
	//Divide o individuo em duas partes, e seta o individuo comforme o tamanho da parte
	for (i = 0; i < corte; i++) {
		repeteInicioInd1noFimInd2[i] = -1;
		repeteInicioInd2noFimInd1[i] = -1;
		primeiraParteInd1[i] = -1;
		primeiraParteInd2[i] = -1;
	}

	for (i = 0; i < quantidadeVertices - corte; i++) {
		segundaParteInd1[i] = -1;
		segundaParteInd2[i] = -1;
	}

	for (i = 0; i < quantidadeVertices; i++) {
		if (i < corte) {
			primeiraParteInd1[i] = caminho1[i];
			primeiraParteInd2[i] = caminho2[i];
		}
		else {
			segundaParteInd1[i - corte] = caminho1[i];
			segundaParteInd2[i - corte] = caminho2[i];
		}
	}


	j = 0;
	k = 0;

	int * testeExiste;

	//Separa os indiv�duos, juntando as primeiras e as segundas partes de cada parte
	for (i = 0; i < corte; i++) {
		 testeExiste = find(segundaParteInd2, segundaParteInd2+quantidadeVertices - corte, primeiraParteInd1[i]);
		 if (testeExiste != segundaParteInd2 + quantidadeVertices - corte) {
			 repeteInicioInd1noFimInd2[j] = primeiraParteInd1[i];
			 j++;
		 }


		 testeExiste = find(segundaParteInd1, segundaParteInd1 + quantidadeVertices - corte, primeiraParteInd2[i]);
		 if (testeExiste != segundaParteInd1 + quantidadeVertices - corte) {
			 repeteInicioInd2noFimInd1[k] = primeiraParteInd2[i];
			 k++;
		 }

	}


	if(j > 0)
		sort(repeteInicioInd1noFimInd2, repeteInicioInd1noFimInd2+ j);

	if(k > 0)
		sort(repeteInicioInd2noFimInd1, repeteInicioInd2noFimInd1 + k);



	int j1 = 0;
	int k1 = 0;
	int p = 0;
	//Verifica os vertices que se repetem no ind�viduo e substitui pelos vertices que n�o se repetem de forma ordenad, 0,1,2,3...
	for (i = 0; i < quantidadeVertices; i++) {
		if (i < corte) {
			testeExiste = find(repeteInicioInd1noFimInd2, repeteInicioInd1noFimInd2+j, primeiraParteInd1[i]);
			if(testeExiste != repeteInicioInd1noFimInd2 + j){
				caminho1Aux[i] = repeteInicioInd2noFimInd1[k1];
				k1++;
			}
			else {
				caminho1Aux[i] = primeiraParteInd1[i];
			}


			testeExiste = find(repeteInicioInd2noFimInd1, repeteInicioInd2noFimInd1+k, primeiraParteInd2[i]);

			if(testeExiste != repeteInicioInd2noFimInd1 + k){
				caminho2Aux[i] = repeteInicioInd1noFimInd2[j1];
				j1++;
			}
			else {
				caminho2Aux[i] = primeiraParteInd2[i];
			}

		}
		else {
			caminho1Aux[i] = segundaParteInd2[p];
			caminho2Aux[i] = segundaParteInd1[p];
			p++;
		}

	}
	//adiciona os indiv�duos na popula��o
	string temp = caminhoParaString2(caminho1Aux);
	strcpy(caminho.caminho, temp.c_str());
	caminho.custoCaminho = calculaCusto(caminho1Aux,0);

	caminhosCrossover.push_back(caminho);

	string temp2 = caminhoParaString2(caminho2Aux);
	strcpy(caminho.caminho, temp2.c_str());
	caminho.custoCaminho = calculaCusto(caminho2Aux,0);

	caminhosCrossover.push_back(caminho);

	free(caminho1);
	free(caminho2);
	free(caminho1Aux);
	free(caminho2Aux);
	free(primeiraParteInd1);
	free(segundaParteInd1);
	free(primeiraParteInd2);
	free(segundaParteInd2);
	free(repeteInicioInd1noFimInd2);
	free(repeteInicioInd2noFimInd1);

	return caminhosCrossover;
}
