// GA Parametros
int MaxItO = 5000;          //% Número máximo de iterações
int nPopO = 70;           //% Tamanho da População

double pcO = 0.8;				  //% Porcentagem de Crossover
int ncO = 2 * round(pcO*nPopO / 2);  //% Número de pais

double pmO = 0.8;                 //% Porcentagem que sofrerá mutação
int nmO = round(pmO*nPopO);         //% Número de mutações

int betaO = 1;                    //% Selection Pressure Utilizado para Roleta


//Função
//bool existe(int *caminhoVet, int valor, int quantidade) {
//	int i;
//	for (i = 0; i < quantidade; i++) {
//		if (caminhoVet[i] == valor)
//			return true;
//	}
//	return false;
//}

//Converte vetor em string separando por ','
char* caminhoParaStringO(int *caminhoVet) {
	int i;
	char cstr[300] = "";
	string caminhoChar;
	caminhoChar.clear();
	for (i = 0; i < quantidadeVertices; i++) {
		caminhoChar += to_string(caminhoVet[i]);

		if (i < quantidadeVertices - 1)
			caminhoChar += ',';
	}
	strcpy(cstr, caminhoChar.c_str());
	return cstr;
}

//Le o caminho separado por ',' e gera um vetor
int *caminhoParaVetorO(int *caminhoVet, char caminho[tamanhoString]) {

	char caminhoChar[tamanhoBloco];

	strcpy(caminhoChar, caminho);
	int posicao = 0;
	char *token;

	token = strtok(caminhoChar, ",");

	while (posicao < quantidadeVertices) {
		caminhoVet[posicao] = atoi(token);
		posicao++;
		token = strtok(NULL, ",");
	}

	return caminhoVet;
}

char* buscaParametrosGAO() {
	char parametros[tamanhoString] = "";

	sprintf(parametros, "\nParametros do GA:\nN° Iteracoes = %d\nTamanho População = %d\nPorc. de Crossover = %f\nPorc. Mutação = %f\nPressão de Seleção = %d", MaxItO, nPopO, pcO, pmO, betaO);
	return parametros;
}