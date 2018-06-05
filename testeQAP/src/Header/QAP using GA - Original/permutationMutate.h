void DoSwapO(int* caminhoOriginal) {
	int individuoAux;
	int individuo1 = rand() %  quantidadeVertices;
	int individuo2 = rand() % quantidadeVertices;
	//printf("\nSawp entre %d %d\n", individuo1, individuo2);

	/*printf("\n CaminhoOriginal :");
	for (int i = 0; i < quantidadeVertices; i++) {
		printf(" %d", caminhoOriginal[i]);
	}
	printf("\n");*/

	individuoAux = caminhoOriginal[individuo1];

	caminhoOriginal[individuo1] = caminhoOriginal[individuo2];
	caminhoOriginal[individuo2] = individuoAux;

	//printf("\n CaminhoNovo :");
	//for (int i = 0; i < quantidadeVertices; i++) {
	//	printf(" %d", caminhoOriginal[i]);
	//}
	//printf("\n");

	//return caminhoOriginal;
}

void DoReversionO(int* caminhoOriginal) {
	int individuo1 = rand() % quantidadeVertices;
	int individuo2 = rand() % quantidadeVertices;
	int min;
	int max;
	//printf("\nreversion entre %d %d\n", individuo1, individuo2);

	int *caminhoAux = new int[quantidadeVertices];
	int i;
	for (i = 0; i < quantidadeVertices; i++) {
		caminhoAux[i] = caminhoOriginal[i];
	}

	if (individuo1 > individuo2) {
		min = individuo2;
		max = individuo1;
	}
	else {
		min = individuo1;
		max = individuo2;
	}

	/*printf("\n CaminhoOriginal :");
	for (int i = 0; i < quantidadeVertices; i++) {
		printf(" %d", caminhoOriginal[i]);
	}
	printf("\n");*/

	int j = max;
	for (i = min; i <= max; i++) {
		caminhoOriginal[i] = caminhoAux[j];
		j--;
	}

	/*printf("\n CaminhoNovo :");
	for (int i = 0; i < quantidadeVertices; i++) {
		printf(" %d", caminhoOriginal[i]);
	}
	printf("\n");
*/

	free(caminhoAux);
}

void DoInsertionO(int* caminhoOriginal) {
	int individuo1 = rand() % quantidadeVertices;
	int individuo2 = rand() % quantidadeVertices;
	int indAux1;
	int indAux2;
	//printf("\nreversion entre %d %d\n", individuo1, individuo2);
	int *caminhoAux = new int[quantidadeVertices];
	int i;
	for (i = 0; i < quantidadeVertices; i++) {
		caminhoAux[i] = caminhoOriginal[i];
	}

	/*printf("\n CaminhoOriginal :");
	for (int i = 0; i < quantidadeVertices; i++) {
		printf(" %d", caminhoOriginal[i]);
	}
	printf("\n");*/

	indAux1 = caminhoOriginal[individuo1];
	indAux2 = caminhoOriginal[individuo2];
	
	if (individuo1 < individuo2) {
		for (i = individuo1; i < quantidadeVertices; i++) {
			if (i < individuo2)
				caminhoOriginal[i] = caminhoOriginal[i + 1];

			if (i == individuo2)
				caminhoOriginal[i] = indAux1;

			if (i > individuo2)
				break;
		}
	}
	else {
		for (i = individuo2 + 1; i < quantidadeVertices; i++) {
			if (i > individuo1)
				break;

			if (i == individuo2 + 1)
				caminhoOriginal[i] = indAux1;
			else
				caminhoOriginal[i] = caminhoAux[i - 1];
			
			
		}
	}

	/*printf("\n CaminhoNovo :");
	for (int i = 0; i < quantidadeVertices; i++) {
		printf(" %d", caminhoOriginal[i]);
	}
	printf("\n");
*/
	free(caminhoAux);
}

Caminho PermutationMutateO(Caminho caminho) {
	Caminho caminhoMutancionado;
	int *caminhoVetor = new int[quantidadeVertices];

	caminhoVetor = caminhoParaVetor(caminhoVetor,caminho.caminho);

	int opcao = rand() % 3;

	switch (opcao)
	{
	case 0:
		DoSwapO(caminhoVetor);
		break;
	case 1:
		DoReversionO(caminhoVetor);
		break;
	case 2:
		DoInsertionO(caminhoVetor);
		break;
	default:
		break;
	}

	strcpy(caminhoMutancionado.caminho, caminhoParaStringO(caminhoVetor));
 	caminhoMutancionado.custoCaminho = calculaCusto(caminhoVetor,0);


	free(caminhoVetor);
	return caminhoMutancionado;

}