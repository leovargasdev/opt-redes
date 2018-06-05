/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/



int teste = 0;


//Essa fun��o busca todas as possibilidades, sempre armazenando a com menor custo como melhor
Caminho comb(Caminho melhorCaminho, int *vet, int n, int index) {
	int i;
	double custoCaminho;
	if (index == n) {
		teste++;
		custoCaminho = calculaCusto(vet,0);
		if (custoCaminho < melhorCaminho.custoCaminho) {
			strcpy(melhorCaminho.caminho, caminhoParaString(vet));
			melhorCaminho.custoCaminho = custoCaminho;

			tf = clock();
			tempo_gasto = ((double)(tf - t0)) / clock();

			printf("\nMelhor Caminho %s Custo = %f, TEMPO(%lf)\n", melhorCaminho.caminho, custoCaminho,tempo_gasto);
		}

		if (teste >= 1000000000) {
			teste = 0;
			for (i = 0; i < n; i++) printf(" %d", vet[i]);
			puts("");
		}
		return melhorCaminho;
	}
	for (i = index; i < n; i++) {
		int tmp;

		tmp = vet[i];
		vet[i] = vet[index];
		vet[index] = tmp;


		melhorCaminho = comb(melhorCaminho, vet, n, index + 1);

		tmp = vet[i];
		vet[i] = vet[index];
		vet[index] = tmp;
	}
	return melhorCaminho;
}

Caminho mainForcaBruta(int *caminho) {
	Caminho melhorCaminho;
	strcpy(melhorCaminho.caminho, "");
	melhorCaminho.custoCaminho = 999999;
	melhorCaminho = comb(melhorCaminho, caminho, quantidadeVertices, 0);
	return melhorCaminho;
}
