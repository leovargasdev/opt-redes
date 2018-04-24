/*
	Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapecó - SC
	Trabalho de Conclusão de Curso:
		- Ciência da Computação
		- Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações
*/

//Busca qual é o maior grau, ou seja qual a linha da matriz F que possui maior somatório de suas colunas
int retornaMaiorGrau() {
	int i, j;
	int maiorGrau = 0;
	int maiorGrauRetorno = -1;

	for (i = 0; i < quantidadeVertices; i++) {
		maiorGrau = 0;
		for (j = 0; j < quantidadeVertices; j++) {
			if (matrizF[i * quantidadeVertices + j] == 1) {
				maiorGrau++;
			}
		}
		if (maiorGrau > maiorGrauRetorno) {
			maiorGrauRetorno = maiorGrau;
		}
	}
	//Retorna mais 1 devido ao valor 0 que é a distancia do proprio vertice
	return maiorGrauRetorno + 1;
}

bool myfunction(int i, int j) { return (i<j); }

double buscaMaiorDistancia(double *distancias,int grau) {
	vector<double> vetorDistancias(distancias, distancias + quantidadeVertices - 1);

	sort(vetorDistancias.begin(), vetorDistancias.end());

	double maiorDistanciaRetorno = vetorDistancias[grau + 1];
	vetorDistancias.clear();
	return maiorDistanciaRetorno;
}

double buscaMaiorDistanciaPermitida() {

	int maiorGrau;
	int i, j, k;
	double maiorDistancia;
	double maiorDistanciaRetorno = 0;
	double *distancias = new double[quantidadeVertices];

	//Busca o maior grau
	maiorGrau = retornaMaiorGrau();

	//Com o maior grau ele retorna qual é a linha que possui o maior valor dos n éssimo menores valores da linha da matriz D
	for (i = 0; i < quantidadeVertices; i++) {
		maiorDistancia = 0;
		for (k = 0; k < quantidadeVertices; k++)
			distancias[k] = 0;

		for (j = 0; j < quantidadeVertices; j++) {
			distancias[j] = matrizD[i * quantidadeVertices + j];
		}
		//busca qual é n° éssima maior distancia
		maiorDistancia = buscaMaiorDistancia(distancias, maiorGrau);
		if (maiorDistancia > maiorDistanciaRetorno)
			maiorDistanciaRetorno = maiorDistancia;
	}

	free(distancias);
	return maiorDistanciaRetorno;
}