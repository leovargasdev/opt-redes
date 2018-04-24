//Calcula o custo dos individuos, verificando se existe conexão entre os mesmo, caso existir (valor 1), soma a distancia entre os mesmos
double myCost(int *caminho, int validaCusto) {
	int i, j, totalExecedeMaxima = 0; 
	double custo = 0.0;
	for (i = 0; i < quantidadeVertices; i++) {
		for (j = 0; j < quantidadeVertices; j++) {
			if(j > i)
				custo += matrizF[i * quantidadeVertices + j] * matrizD[caminho[i] * quantidadeVertices + caminho[j]];
		}
	}
	return custo;
}