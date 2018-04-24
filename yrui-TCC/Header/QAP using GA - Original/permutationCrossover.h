vector<Caminho> PermutationCrossoverO(vector<Caminho> caminhosCrossover, vector<Caminho> caminhos, int individuoCross1, int individuoCross2) {
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

	int corte = rand() % quantidadeVertices;

	int *primeiraParteInd1 = new int[corte];
	int *segundaParteInd1 = new int[quantidadeVertices - corte];
	int *primeiraParteInd2 = new int[corte];
	int *segundaParteInd2 = new int[quantidadeVertices - corte];
	int *repeteInicioInd1noFimInd2 = new int[corte];
	int *repeteInicioInd2noFimInd1 = new int[corte];

	int i, j, k;

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
	

	//printf("Corte %d", c);

	//printf("\n Caminho 1 antes :");
	//for (i = 0; i < quantidadeVertices; i++) {
	//	printf(" %d", caminho1[i]);
	//}
	//printf("\n");

	//printf("\n Caminho 2 antes :");
	//for (i = 0; i < quantidadeVertices; i++) {
	//	printf(" %d", caminho2[i]);
	//}
	//printf("\n");




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

	//printf("\n x11 :");
	//for (i = 0; i < corte; i++) {
	//	printf(" %d", primeiraParteInd1[i]);
	//}
	//printf("\n");

	//printf("\n x21 :");
	//for (i = 0; i < corte; i++) {
	//	printf(" %d", primeiraParteInd2[i]);
	//}
	//printf("\n");

	//printf("\n x12 :");
	//for (i = 0; i < quantidadeVertices - corte; i++) {
	//	printf(" %d", segundaParteInd1[i]);
	//}
	//printf("\n");

	//printf("\n x22 :");
	//for (i = 0; i < quantidadeVertices - corte; i++) {
	//	printf(" %d", segundaParteInd2[i]);
	//}
	//printf("\n");


	j = 0;
	k = 0;

	int * testeExiste;

	

	for (i = 0; i < corte; i++) {
		 testeExiste = find(segundaParteInd2, segundaParteInd2+quantidadeVertices - corte, primeiraParteInd1[i]);
		 if (testeExiste != segundaParteInd2 + quantidadeVertices - corte) {
			 repeteInicioInd1noFimInd2[j] = primeiraParteInd1[i];
			 j++;
		 }

		//if (existe(x22,x11[i], quantidadeVertices - c)) {
		//	r1[j] = x11[i];
		//	j++;
		//}

		 testeExiste = find(segundaParteInd1, segundaParteInd1 + quantidadeVertices - corte, primeiraParteInd2[i]);
		 if (testeExiste != segundaParteInd1 + quantidadeVertices - corte) {
			 repeteInicioInd2noFimInd1[k] = primeiraParteInd2[i];
			 k++;
		 }

		/*if (existe(x12, x21[i], quantidadeVertices - c)) {
			r2[k] = x21[i];
			k++;
		}*/
	}

	

	if(j > 0)
		sort(repeteInicioInd1noFimInd2, repeteInicioInd1noFimInd2+ j);

	if(k > 0)
		sort(repeteInicioInd2noFimInd1, repeteInicioInd2noFimInd1 + k);

	

	//printf("\n r1 :");
	//for (i = 0; i < j; i++) {
	//	printf(" %d", repeteInicioInd1noFimInd2[i]);
	//}
	//printf("\n");

	//printf("\n r2 :");
	//for (i = 0; i < k; i++) {
	//	printf(" %d", repeteInicioInd2noFimInd1[i]);
	//}
	//printf("\n");

	int j1 = 0;
	int k1 = 0;
	int p = 0;
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

			/*if (existe(repeteInicioInd1noFimInd2, primeiraParteInd1[i], j)) {
				caminho1Aux[i] = repeteInicioInd2noFimInd1[k1];
				k1++;
			}
			else {
				caminho1Aux[i] = primeiraParteInd1[i];
			}*/

			testeExiste = find(repeteInicioInd2noFimInd1, repeteInicioInd2noFimInd1+k, primeiraParteInd2[i]);

			if(testeExiste != repeteInicioInd2noFimInd1 + k){
				caminho2Aux[i] = repeteInicioInd1noFimInd2[j1];
				j1++;
			}
			else {
				caminho2Aux[i] = primeiraParteInd2[i];
			}

			//if (existe(repeteInicioInd2noFimInd1, primeiraParteInd2[i], k)) {
			//	caminho2Aux[i] = repeteInicioInd1noFimInd2[j1];
			//	j1++;
			//}
			//else {
			//	caminho2Aux[i] = primeiraParteInd2[i];
			//}
		}
		else {
			caminho1Aux[i] = segundaParteInd2[p];
			caminho2Aux[i] = segundaParteInd1[p];
			p++;
		}

	}

	//printf("\n Caminho1Aux :");
	//for (i = 0; i < quantidadeVertices; i++) {
	//	printf(" %d", caminho1Aux[i]);
	//}
	//printf("\n");

	//printf("\n Caminho2Aux :");
	//for (i = 0; i < quantidadeVertices; i++) {
	//	printf(" %d", caminho2Aux[i]);
	//}
	//printf("\n");

	strcpy(caminho.caminho,caminhoParaString(caminho1Aux));
	caminho.custoCaminho = myCost(caminho1Aux,0);

	caminhosCrossover.push_back(caminho);

	strcpy(caminho.caminho, caminhoParaString(caminho2Aux));
	caminho.custoCaminho = myCost(caminho2Aux,0);

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