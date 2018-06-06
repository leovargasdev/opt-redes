/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

// GA Parametros
int MaxIt;          //% N�mero m�ximo de itera��es
int maxItSemAlteracao;
int nPop;           //% Tamanho da Popula��o

double pc;				  //% Porcentagem de Crossover
int nc;  //% N�mero de pais

double pm;                 //% Porcentagem que sofrer� muta��o
int nm;         //% N�mero de muta��es

int beta;    //% Selection Pressure Utilizado para Roleta

double pocentagemIndividuosSobrevivem; //Parametro para controlar quantos individuos se manteram depois das MaxIt
int numeroIndividuosSobrevivem;

int numeroDeRenovacoes = 0;


void inicializaConfiguracao1() {
	MaxIt = 5000;          //% N�mero m�ximo de itera��es
	maxItSemAlteracao = 100;
	nPop = 80;           //% Tamanho da Popula��o

	pc = 0.7;				  //% Porcentagem de Crossover
	nc = 2 * round(pc*nPop / 2);  //% N�mero de pais

	pm = 0.7;                 //% Porcentagem que sofrer� muta��o
	nm = round(pm*nPop);         //% N�mero de muta��es

	beta = 2;                    //% Selection Pressure Utilizado para Roleta

	pocentagemIndividuosSobrevivem = 0.1; //Parametro para controlar quantos individuos se manteram depois das MaxIt
	numeroIndividuosSobrevivem = 2 * round(pocentagemIndividuosSobrevivem * nPop / 2);

	porcentagemMaiorDistancia = 0.2; //Porcentagem de links de um individuo que pode exceder a distancia maxima permitida

}



void inicializaConfiguracao2() {
	MaxIt = 1000;          //% N�mero m�ximo de itera��es
	maxItSemAlteracao = 5000;
	nPop = 90;           //% Tamanho da Popula��o

	pc = 0.7;				  //% Porcentagem de Crossover
	nc = 2 * round(pc*nPop / 2);  //% N�mero de pais

	pm = 0.7;                 //% Porcentagem que sofrer� muta��o
	nm = round(pm*nPop);         //% N�mero de muta��es

	beta = 2;                    //% Selection Pressure Utilizado para Roleta

	pocentagemIndividuosSobrevivem = 0.1; //Parametro para controlar quantos individuos se manteram depois das MaxIt
	numeroIndividuosSobrevivem = 2 * round(pocentagemIndividuosSobrevivem * nPop / 2);

	porcentagemMaiorDistancia = 0.40;//Porcentagem de links de um individuo que pode exceder a distancia maxima permitida
}

void inicializaConfiguracao3() {
	MaxIt = 2000;          //% N�mero m�ximo de itera��es
	maxItSemAlteracao = 10000;
	nPop = 100;           //% Tamanho da Popula��o

	pc = 0.8;				  //% Porcentagem de Crossover
	nc = 2 * round(pc*nPop / 2);  //% N�mero de pais

	pm = 0.8;                 //% Porcentagem que sofrer� muta��o
	nm = round(pm*nPop);         //% N�mero de muta��es

	beta = 3;                    //% Selection Pressure Utilizado para Roleta

	pocentagemIndividuosSobrevivem = 0.1; //Parametro para controlar quantos individuos se manteram depois das MaxIt
	numeroIndividuosSobrevivem = 2 * round(pocentagemIndividuosSobrevivem * nPop / 2);

	porcentagemMaiorDistancia =0.40;//Porcentagem de links de um individuo que pode exceder a distancia maxima permitida
}

void inicializaConfiguracao4() {
	MaxIt = 2000;          //% N�mero m�ximo de itera��es
	maxItSemAlteracao = 20000;
	nPop = 100;           //% Tamanho da Popula��o

	pc = 0.9;				  //% Porcentagem de Crossover
	nc = 2 * round(pc*nPop / 2);  //% N�mero de pais

	pm = 0.9;                 //% Porcentagem que sofrer� muta��o
	nm = round(pm*nPop);         //% N�mero de muta��es

	beta = 4;                    //% Selection Pressure Utilizado para Roleta

	pocentagemIndividuosSobrevivem = 0.1; //Parametro para controlar quantos individuos se manteram depois das MaxIt
	numeroIndividuosSobrevivem = 2 * round(pocentagemIndividuosSobrevivem * nPop / 2);

	porcentagemMaiorDistancia = 0.55;//Porcentagem de links de um individuo que pode exceder a distancia maxima permitida
}


double porcentagemEquivalenciaPermitida = 0.0; //Parametro usado para permitir a equivalencia entre os N�o selecionados
int numeroEquivalentesPermitidos = 0;

//1 Troca
//2 Insercao
//3 Inversao
//4 Todos
int tipoMutacao = 4;

int factorial(int n)
{
	return (n == 1 || n == 0) ? 1 : factorial(n - 1) * n;
}

//Converte vetor em string separando por ','
// char* caminhoParaString(int *caminhoVet){
// 	int i;
// 	char cstr[300] = "";
// 	string caminhoChar;
// 	caminhoChar.clear();
// 	for (i = 0; i < quantidadeVertices; i++) {
// 		caminhoChar += to_string(caminhoVet[i]);
//
// 		if (i < quantidadeVertices - 1)
// 			caminhoChar += ',';
// 	}
// 	strcpy(cstr, caminhoChar.c_str());
// 	printf("\n\n\nPERDI [4.1] %s\n\n\n", cstr);
// 	return cstr;
// }

string caminhoParaString2(int *caminhoVet){
	int i;
	string caminhoChar;
	caminhoChar.clear();
	for (i = 0; i < quantidadeVertices; i++) {
		caminhoChar += to_string(caminhoVet[i]);
		if (i < quantidadeVertices - 1)
			caminhoChar += ',';
	}
	return caminhoChar;
}

//Le o caminho separado por ',' e gera um vetor
int *caminhoParaVetor(int *caminhoVet, char caminho[tamanhoString]) {

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

char* buscaParametrosGA() {
	char parametros[tamanhoString] = "";

	sprintf(parametros, "\nParametros do GA:\nN� Iteracoes = %d\nTamanho Popula��o = %d\nPorc. de Crossover = %f\nPorc. Muta��o = %f\nPress�o de Sele��o = %d", MaxIt, nPop, pc, pm, beta);

	return parametros;
}
