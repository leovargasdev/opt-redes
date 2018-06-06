/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/


//Muta��o de Troca, apenas troca um vertice de posi��o com outro
void swap(int individuo1, int individuo2, int* caminhoOriginal) {
	int individuoAux;

	individuoAux = caminhoOriginal[individuo1];
	caminhoOriginal[individuo1] = caminhoOriginal[individuo2];
	caminhoOriginal[individuo2] = individuoAux;

}

//Reverte os v�rtices de um conjunto continuo dentro do vetor
void reversao(int individuo1, int individuo2, int* caminhoOriginal) {
	int min;
	int max;

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

	int j = max;
	for (i = min; i <= max; i++) {
		caminhoOriginal[i] = caminhoAux[j];
		j--;
	}


	free(caminhoAux);
}

//Retira um vertice de uma posi��o e coloca em outra posi��o deslocando para o lado
void insercao(int individuo1, int individuo2, int* caminhoOriginal) {
	int indAux1;
	int indAux2;

	int *caminhoAux = new int[quantidadeVertices];
	int i;
	for (i = 0; i < quantidadeVertices; i++) {
		caminhoAux[i] = caminhoOriginal[i];
	}

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

	free(caminhoAux);
}

vector<Caminho> mutacao(vector<Caminho> caminhosMutacionados,Caminho caminho) {
	Caminho caminhoMutancionado;
	int *caminhoVetor = new int[quantidadeVertices];

	//Sempre sortei os dois valores que s�o utilizados como parametro nas fun��es de muta��o
	int individuo1 = rand() % quantidadeVertices;
	int individuo2 = rand() % quantidadeVertices;
	//printf("\n%d , %d", individuo1, individuo2);

	if (tipoMutacao == 1 || tipoMutacao == 4) {
		caminhoVetor = caminhoParaVetor(caminhoVetor, caminho.caminho);
		swap(individuo1, individuo2, caminhoVetor);

		string temp = caminhoParaString2(caminhoVetor);
		strcpy(caminhoMutancionado.caminho, temp.c_str());

		caminhoMutancionado.custoCaminho = calculaCusto(caminhoVetor, 0);

		caminhosMutacionados.push_back(caminhoMutancionado);
	}

	if (tipoMutacao == 2 || tipoMutacao == 4) {
		caminhoVetor = caminhoParaVetor(caminhoVetor, caminho.caminho);
		insercao(individuo1, individuo2, caminhoVetor);

		string temp2 = caminhoParaString2(caminhoVetor);
		strcpy(caminhoMutancionado.caminho, temp2.c_str());
		caminhoMutancionado.custoCaminho = calculaCusto(caminhoVetor, 0);

		caminhosMutacionados.push_back(caminhoMutancionado);
	}

	if (tipoMutacao == 3 || tipoMutacao == 4) {
		caminhoVetor = caminhoParaVetor(caminhoVetor, caminho.caminho);
		reversao(individuo1, individuo2, caminhoVetor);

		string temp3 = caminhoParaString2(caminhoVetor);
		strcpy(caminhoMutancionado.caminho, temp3.c_str());
		caminhoMutancionado.custoCaminho = calculaCusto(caminhoVetor, 0);

		caminhosMutacionados.push_back(caminhoMutancionado);
	}


	free(caminhoVetor);
	return caminhosMutacionados;

}
