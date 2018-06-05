/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

#define tamanhoJanelaSpectro 320
#define tamanhoStringRSA 99000

int requisicoesSimultaneas;
//char melhorCaminhoRSA[tamanhoStringRSA];


//Converte vetor em string separando por ','
char* caminhoParaStringRSA(int *caminhoVet, int tamanho) {
	int i;
	char cstr[tamanhoStringRSA] = "";
	string caminhoChar;
	caminhoChar.clear();
	for (i = 0; i < tamanho; i++) {
		caminhoChar += to_string(caminhoVet[i]);

		if (i < tamanho - 1)
			caminhoChar += ',';
	}
	strcpy(cstr, caminhoChar.c_str());
	return cstr;
}

//Le o caminho separado por ',' e gera um vetor
int *caminhoParaVetorRSA(int *caminhoVet, char caminho[tamanhoStringRSA], int quantidade) {

	char caminhoChar[tamanhoStringRSA];

	strcpy(caminhoChar, caminho);
	int posicao = 0;
	char *token;

	token = strtok(caminhoChar, ",");

	while (posicao < quantidade) {
		caminhoVet[posicao] = atoi(token);
		posicao++;
		token = strtok(NULL, ",");
	}

	return caminhoVet;
}



